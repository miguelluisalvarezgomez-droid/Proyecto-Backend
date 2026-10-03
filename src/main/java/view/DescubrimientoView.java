package view;

import domain.Cliente;
import domain.Producto;
import domain.Restaurante;
import exception.NegocioException;
import service.Inputports.DescubrimientoInputPort;

import java.util.List;

public class DescubrimientoView {

    private final DescubrimientoInputPort descubrimiento;
    private final CarritoView carritoView;
    private final SeguimientoView seguimientoView;
    private final PerfilView perfilView;
    private final Consola consola;

    public DescubrimientoView(DescubrimientoInputPort descubrimiento, CarritoView carritoView,
                              SeguimientoView seguimientoView, PerfilView perfilView, Consola consola) {
        this.descubrimiento = descubrimiento;
        this.carritoView = carritoView;
        this.seguimientoView = seguimientoView;
        this.perfilView = perfilView;
        this.consola = consola;
    }

    // Pantalla principal del cliente (se llega tras iniciar sesión)
    public void mostrarMenuPrincipal(Cliente cliente) {
        int opcion = -1;
        while (opcion != 0) {
            consola.mostrar("\n=== PANTALLA PRINCIPAL - Hola, " + cliente.getNombre() + " ===");
            consola.mostrar("1. Ver restaurantes cercanos");
            consola.mostrar("2. Buscar restaurante");
            consola.mostrar("3. Ver mi carrito");
            consola.mostrar("4. Mis pedidos");
            consola.mostrar("5. Mi perfil");
            consola.mostrar("0. Cerrar sesión");
            opcion = consola.leerEntero("Opción");
            try {
                switch (opcion) {
                    case 1 -> verCercanos(cliente);
                    case 2 -> buscar(cliente);
                    case 3 -> carritoView.mostrarMenuCarrito(cliente);
                    case 4 -> seguimientoView.mostrarHistorial(cliente);
                    case 5 -> perfilView.mostrarPerfil(cliente);
                    case 0 -> consola.mostrar("Sesión cerrada.");
                    default -> consola.mostrar("Opción no válida.");
                }
            } catch (NegocioException e) {
                consola.mostrar("Error: " + e.getMessage());
            }
        }
    }

    // ---------- Captura ----------
    private void verCercanos(Cliente cliente) {
        double latitud = consola.leerDecimal("Tu latitud");
        double longitud = consola.leerDecimal("Tu longitud");
        List<Restaurante> restaurantes = descubrimiento.listarCercanos(latitud, longitud);
        mostrarRestaurantes(restaurantes, "No hay restaurantes disponibles por ahora.");
        elegirRestaurante(cliente, restaurantes);
    }

    private void buscar(Cliente cliente) {
        String texto = consola.capturar("Nombre (Enter para omitir)");
        String categoria = consola.capturar("Categoría (Enter para omitir)");
        List<Restaurante> restaurantes = descubrimiento.buscar(texto, categoria);
        mostrarRestaurantes(restaurantes, "No se encontraron restaurantes.");
        elegirRestaurante(cliente, restaurantes);
    }

    private void elegirRestaurante(Cliente cliente, List<Restaurante> restaurantes) {
        if (restaurantes.isEmpty()) {
            return;
        }
        int id = consola.leerEntero("ID del restaurante para ver su menú (0 para volver)");
        if (id > 0) {
            menuRestaurante(cliente, (long) id);
        }
    }

    // Menú de un restaurante: ver productos y armar el carrito
    private void menuRestaurante(Cliente cliente, Long restauranteId) {
        if (!mostrarMenuRestaurante(restauranteId)) {
            return;
        }
        int opcion = -1;
        while (opcion != 0) {
            consola.mostrar("\n1. Agregar producto al carrito");
            consola.mostrar("2. Ver mi carrito");
            consola.mostrar("0. Volver");
            opcion = consola.leerEntero("Opción");
            try {
                switch (opcion) {
                    case 1 -> carritoView.agregarProducto(cliente, restauranteId);
                    case 2 -> carritoView.mostrarMenuCarrito(cliente);
                    case 0 -> { }
                    default -> consola.mostrar("Opción no válida.");
                }
            } catch (NegocioException e) {
                consola.mostrar("Error: " + e.getMessage());
            }
        }
    }

    // ---------- Muestra ----------
    private void mostrarRestaurantes(List<Restaurante> restaurantes, String mensajeVacio) {
        if (restaurantes.isEmpty()) {
            consola.mostrar(mensajeVacio);
            return;
        }
        consola.mostrar("\n--- RESTAURANTES ---");
        for (Restaurante r : restaurantes) {
            String calificacion = r.getCalificacionPromedio() > 0
                    ? String.format("%.1f/5", r.getCalificacionPromedio())
                    : "Sin calificaciones";
            consola.mostrar("[" + r.getId() + "] " + r.getNombre()
                    + " | " + r.getCategoria()
                    + " | " + calificacion
                    + " | Imagen: " + r.getLogoUrl());
        }
    }

    // Devuelve false si el restaurante no tiene productos
    private boolean mostrarMenuRestaurante(Long restauranteId) {
        List<Producto> productos = descubrimiento.verMenu(restauranteId);
        if (productos.isEmpty()) {
            consola.mostrar("Este restaurante aún no tiene productos.");
            return false;
        }
        consola.mostrar("\n--- MENÚ ---");
        String categoriaActual = null;
        for (Producto p : productos) {
            if (categoriaActual == null || !categoriaActual.equals(p.getCategoria())) {
                categoriaActual = p.getCategoria();
                consola.mostrar("\n# " + categoriaActual);
            }
            String estado = p.isDisponible() ? "" : "  [NO DISPONIBLE]";
            consola.mostrar("[" + p.getId() + "] " + p.getNombre() + " | $" + String.format("%.2f", p.getPrecio())
                    + estado + "\n    " + p.getDescripcion());
        }
        return true;
    }
}
