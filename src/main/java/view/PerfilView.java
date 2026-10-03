package view;

import domain.Cliente;
import exception.NegocioException;
import service.Inputports.CuentaClienteInputPort;

public class PerfilView {

    private final CuentaClienteInputPort cuenta;
    private final Consola consola;

    public PerfilView(CuentaClienteInputPort cuenta, Consola consola) {
        this.cuenta = cuenta;
        this.consola = consola;
    }

    // US-13
    public void mostrarPerfil(Cliente cliente) {
        int opcion = -1;
        while (opcion != 0) {
            mostrarDatos(cliente);
            consola.mostrar("1. Editar datos");
            consola.mostrar("0. Volver");
            opcion = consola.leerEntero("Opción");
            try {
                switch (opcion) {
                    case 1 -> editar(cliente);
                    case 0 -> { }
                    default -> consola.mostrar("Opción no válida.");
                }
            } catch (NegocioException e) {
                consola.mostrar("Error: " + e.getMessage());
            }
        }
    }

    // ---------- Captura (Enter conserva el valor actual) ----------
    private void editar(Cliente cliente) {
        String nombre = capturarConActual("Nombre", cliente.getNombre());
        String telefono = capturarConActual("Teléfono", cliente.getTelefono());
        String direccion = capturarConActual("Dirección", cliente.getDireccion());
        String foto = capturarConActual("Foto (URL)", cliente.getFotoUrl());

        Cliente actualizado = cuenta.editarPerfil(cliente.getId(), nombre, telefono, direccion, foto);
        consola.mostrar("Perfil actualizado correctamente.");
        mostrarDatos(actualizado);
    }

    private String capturarConActual(String etiqueta, String actual) {
        String valor = consola.capturar(etiqueta + " [" + (actual == null ? "sin definir" : actual) + "]");
        return valor.isBlank() ? actual : valor;
    }

    // ---------- Muestra ----------
    private void mostrarDatos(Cliente c) {
        consola.mostrar("\n=== MI PERFIL ===");
        consola.mostrar("Nombre:    " + c.getNombre());
        consola.mostrar("Correo:    " + c.getCorreo());
        consola.mostrar("Teléfono:  " + c.getTelefono());
        consola.mostrar("Dirección: " + (c.getDireccion() == null ? "sin definir" : c.getDireccion()));
        consola.mostrar("Foto:      " + (c.getFotoUrl() == null ? "sin foto" : c.getFotoUrl()));
    }
}
