package view;

import domain.Comerciante;
import domain.Restaurante;
import exception.NegocioException;
import service.Inputports.NegocioInputPort;

public class NegocioView {

    private final NegocioInputPort negocio;
    private final Consola consola;

    public NegocioView(NegocioInputPort negocio, Consola consola) {
        this.negocio = negocio;
        this.consola = consola;
    }

    // US-16
    public void mostrarPerfil(Comerciante comerciante) {
        int opcion = -1;
        while (opcion != 0) {
            try {
                mostrarDatos(negocio.verPerfil(comerciante.getId()));
            } catch (NegocioException e) {
                consola.mostrar("Error: " + e.getMessage());
                return;
            }
            consola.mostrar("1. Editar perfil del negocio");
            consola.mostrar("0. Volver");
            opcion = consola.leerEntero("Opción");
            try {
                switch (opcion) {
                    case 1 -> editar(comerciante);
                    case 0 -> { }
                    default -> consola.mostrar("Opción no válida.");
                }
            } catch (NegocioException e) {
                consola.mostrar("Error: " + e.getMessage());
            }
        }
    }

    // ---------- Captura (Enter conserva el valor actual) ----------
    private void editar(Comerciante comerciante) {
        Restaurante actual = negocio.verPerfil(comerciante.getId());
        String nombre = capturarConActual("Nombre del negocio", actual.getNombre());
        String direccion = capturarConActual("Dirección", actual.getDireccion());
        double latitud = capturarCoordenada("Latitud", actual.getLatitud());
        double longitud = capturarCoordenada("Longitud", actual.getLongitud());
        String horario = capturarConActual("Horario", actual.getHorario());
        String logo = capturarConActual("Logo (URL)", actual.getLogoUrl());

        negocio.editarPerfil(comerciante.getId(), nombre, direccion, latitud, longitud, horario, logo);
        consola.mostrar("Perfil del negocio actualizado. Los clientes ya ven los cambios.");
    }

    private String capturarConActual(String etiqueta, String actual) {
        String valor = consola.capturar(etiqueta + " [" + (actual == null ? "sin definir" : actual) + "]");
        return valor.isBlank() ? actual : valor;
    }

    private double capturarCoordenada(String etiqueta, double actual) {
        String valor = consola.capturar(etiqueta + " [" + actual + "]");
        if (valor.isBlank()) {
            return actual;
        }
        try {
            return Double.parseDouble(valor.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new NegocioException("La " + etiqueta.toLowerCase() + " no es un número válido.");
        }
    }

    // ---------- Muestra ----------
    private void mostrarDatos(Restaurante r) {
        consola.mostrar("\n=== PERFIL DE MI NEGOCIO ===");
        consola.mostrar("Nombre:       " + r.getNombre());
        consola.mostrar("Categoría:    " + r.getCategoria());
        consola.mostrar("Dirección:    " + (r.getDireccion() == null ? "sin definir" : r.getDireccion()));
        consola.mostrar("Ubicación:    " + r.getLatitud() + ", " + r.getLongitud());
        consola.mostrar("Horario:      " + (r.getHorario() == null ? "sin definir" : r.getHorario()));
        consola.mostrar("Logo:         " + (r.getLogoUrl() == null ? "sin logo" : r.getLogoUrl()));
        consola.mostrar("Afiliación:   " + r.getEstadoAfiliacion());
        if (r.getComentarioAfiliacion() != null) {
            consola.mostrar("Comentario del administrador: " + r.getComentarioAfiliacion());
        }
        consola.mostrar("Calificación: "
                + (r.getCalificacionPromedio() > 0 ? String.format("%.1f/5", r.getCalificacionPromedio()) : "sin calificaciones"));
    }
}
