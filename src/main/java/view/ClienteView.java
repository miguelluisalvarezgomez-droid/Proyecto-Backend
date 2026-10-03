package view;

import domain.Cliente;
import exception.NegocioException;
import service.Inputports.CuentaClienteInputPort;

public class ClienteView {

    private final CuentaClienteInputPort cuenta;
    private final DescubrimientoView descubrimientoView;
    private final Consola consola;

    public ClienteView(CuentaClienteInputPort cuenta, DescubrimientoView descubrimientoView, Consola consola) {
        this.cuenta = cuenta;
        this.descubrimientoView = descubrimientoView;
        this.consola = consola;
    }

    // ---------- Menú ----------
    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            consola.mostrar("\n=== BIENVENIDO ===");
            consola.mostrar("1. Crear cuenta");
            consola.mostrar("2. Iniciar sesión");
            consola.mostrar("0. Volver");
            opcion = consola.leerEntero("Opción");
            try {
                switch (opcion) {
                    case 1 -> registrar();
                    case 2 -> iniciarSesion();
                    case 0 -> { }
                    default -> consola.mostrar("Opción no válida.");
                }
            } catch (NegocioException e) {
                consola.mostrar("Error: " + e.getMessage());
            }
        }
    }

    // ---------- Captura de datos ----------
    private void registrar() {
        String nombre = consola.capturar("Nombre");
        String correo = consola.capturar("Correo");
        String telefono = consola.capturar("Teléfono");
        String contrasena = consola.capturar("Contraseña");
        Cliente cliente = cuenta.registrar(nombre, correo, telefono, contrasena);
        consola.mostrar("Registro exitoso. Ya puedes iniciar sesión, " + cliente.getNombre() + ".");
    }

    private void iniciarSesion() {
        String correo = consola.capturar("Correo");
        String contrasena = consola.capturar("Contraseña");
        Cliente cliente = cuenta.iniciarSesion(correo, contrasena);
        descubrimientoView.mostrarMenuPrincipal(cliente);
    }
}
