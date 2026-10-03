package view;

import domain.Cliente;
import domain.DetallePedido;
import domain.Pedido;
import exception.NegocioException;
import service.Inputports.CarritoInputPort;

import java.math.BigDecimal;

public class CarritoView {

    private final CarritoInputPort carritoService;
    private final PagoView pagoView;
    private final Consola consola;

    public CarritoView(CarritoInputPort carritoService, PagoView pagoView, Consola consola) {
        this.carritoService = carritoService;
        this.pagoView = pagoView;
        this.consola = consola;
    }

    // Se usa desde el menú de un restaurante
    public void agregarProducto(Cliente cliente, Long restauranteId) {
        int productoId = consola.leerEntero("ID del producto");
        int cantidad = consola.leerEntero("Cantidad");
        if (productoId <= 0) {
            consola.mostrar("ID de producto no válido.");
            return;
        }
        Pedido carrito = carritoService.agregarProducto(cliente.getId(), restauranteId, (long) productoId, cantidad);
        consola.mostrar("Producto agregado. Subtotal del carrito: $" + dinero(carrito.getTotal()));
    }

    public void mostrarMenuCarrito(Cliente cliente) {
        int opcion = -1;
        while (opcion != 0) {
            Pedido carrito = carritoService.verCarrito(cliente.getId());
            mostrarCarrito(carrito);
            if (carrito.getDetalles().isEmpty()) {
                return;
            }
            consola.mostrar("1. Modificar cantidad");
            consola.mostrar("2. Quitar producto");
            consola.mostrar("3. Confirmar pedido");
            consola.mostrar("0. Volver");
            opcion = consola.leerEntero("Opción");
            try {
                switch (opcion) {
                    case 1 -> modificarCantidad(cliente);
                    case 2 -> quitarProducto(cliente);
                    case 3 -> {
                        if (confirmar(cliente, carrito)) {
                            return;
                        }
                    }
                    case 0 -> { }
                    default -> consola.mostrar("Opción no válida.");
                }
            } catch (NegocioException e) {
                consola.mostrar("Error: " + e.getMessage());
            }
        }
    }

    // ---------- Captura ----------
    private void modificarCantidad(Cliente cliente) {
        int productoId = consola.leerEntero("ID del producto");
        int cantidad = consola.leerEntero("Nueva cantidad");
        carritoService.modificarCantidad(cliente.getId(), (long) productoId, cantidad);
    }

    private void quitarProducto(Cliente cliente) {
        int productoId = consola.leerEntero("ID del producto a quitar");
        carritoService.quitarProducto(cliente.getId(), (long) productoId);
    }

    // Devuelve true si el pedido quedó confirmado
    private boolean confirmar(Cliente cliente, Pedido resumen) {
        consola.mostrar("\n--- RESUMEN DEL PEDIDO ---");
        mostrarDetalles(resumen);
        String respuesta = consola.capturar("¿Confirmar pedido? (S/N)");
        if (!respuesta.trim().equalsIgnoreCase("S")) {
            consola.mostrar("Pedido no confirmado.");
            return false;
        }
        Pedido pedido = carritoService.confirmarPedido(cliente.getId());
        consola.mostrar("\nPedido #" + pedido.getId() + " confirmado. Estado: " + pedido.getEstado());
        consola.mostrar("Total: $" + dinero(pedido.getTotal()));
        consola.mostrar("Código QR de tu pedido: " + pedido.getCodigoQr());
        pagoView.seleccionarMetodoPago(pedido);
        return true;
    }

    // ---------- Muestra ----------
    private void mostrarCarrito(Pedido carrito) {
        consola.mostrar("\n=== MI CARRITO ===");
        if (carrito.getDetalles().isEmpty()) {
            consola.mostrar("Tu carrito está vacío.");
            return;
        }
        mostrarDetalles(carrito);
    }

    private void mostrarDetalles(Pedido pedido) {
        for (DetallePedido d : pedido.getDetalles()) {
            BigDecimal subtotal = d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad()));
            consola.mostrar("[" + d.getProductoId() + "] " + d.getNombreProducto()
                    + " x" + d.getCantidad()
                    + " @ $" + dinero(d.getPrecioUnitario())
                    + " = $" + dinero(subtotal));
        }
        consola.mostrar("TOTAL: $" + dinero(pedido.getTotal()));
    }

    private String dinero(BigDecimal valor) {
        return String.format("%.2f", valor);
    }
}
