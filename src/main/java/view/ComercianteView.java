package view;

import domain.Comerciante;
import exception.NegocioException;
import service.Inputports.CuentaComercianteInputPort;

public class ComercianteView {

    private final CuentaComercianteInputPort cuenta;
    private final NegocioView negocioView;
    private final Consola consola;

    public ComercianteView(CuentaComercianteInputPort cuenta, NegocioView negocioView, Consola consola) {
        this.cuenta = cuenta;
        this.negocioView = negocioView;
        this.consola = consola;
    }

    // ---------- Acceso (US-15) ----------
    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            consola.mostrar("\n=== ACCESO COMERCIANTE ===");
            consola.mostrar("1. Iniciar sesión");
            consola.mostrar("0. Volver");
            opcion = consola.leerEntero("Opción");
            try {
                switch (opcion) {
                    case 1 -> iniciarSesion();
                    case 0 -> { }
                    default -> consola.mostrar("Opción no válida.");
                }
            } catch (NegocioException e) {
                consola.mostrar("Error: " + e.getMessage());
            }
        }
    }

    private void iniciarSesion() {
        String correo = consola.capturar("Correo");
        String contrasena = consola.capturar("Contraseña");
        Comerciante comerciante = cuenta.iniciarSesion(correo, contrasena);
        mostrarPanel(comerciante);
    }

    // ---------- Panel del comerciante ----------
    private void mostrarPanel(Comerciante comerciante) {
        int opcion = -1;
        while (opcion != 0) {
            consola.mostrar("\n=== PANEL DEL COMERCIANTE - Hola, " + comerciante.getNombre() + " ===");
            consola.mostrar("1. Perfil del negocio");
            consola.mostrar("0. Cerrar sesión");
            opcion = consola.leerEntero("Opción");
            switch (opcion) {
                case 1 -> negocioView.mostrarPerfil(comerciante);
                case 0 -> consola.mostrar("Sesión cerrada.");
                default -> consola.mostrar("Opción no válida.");
            }
        }
    }
}
