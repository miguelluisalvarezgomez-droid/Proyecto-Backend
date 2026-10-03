package view;

import domain.Administrador;
import exception.NegocioException;
import service.Inputports.AdministradorInputPort;

public class AdministradorView {

    private final AdministradorInputPort cuenta;
    private final PlataformaView plataformaView;
    private final Consola consola;

    public AdministradorView(AdministradorInputPort cuenta, PlataformaView plataformaView, Consola consola) {
        this.cuenta = cuenta;
        this.plataformaView = plataformaView;
        this.consola = consola;
    }

    // ---------- Acceso (US-26) ----------
    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            consola.mostrar("\n=== ACCESO ADMINISTRADOR ===");
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
        Administrador admin = cuenta.iniciarSesion(correo, contrasena);
        mostrarPanel(admin);
    }

    // ---------- Panel del administrador ----------
    private void mostrarPanel(Administrador admin) {
        int opcion = -1;
        while (opcion != 0) {
            consola.mostrar("\n=== PANEL DEL ADMINISTRADOR - Hola, " + admin.getNombre() + " ===");
            consola.mostrar("1. Crear comercio");
            consola.mostrar("2. Solicitudes de afiliación");
            consola.mostrar("3. Gestión de perfiles");
            consola.mostrar("0. Cerrar sesión");
            opcion = consola.leerEntero("Opción");
            try {
                switch (opcion) {
                    case 1 -> plataformaView.crearComercio(admin);
                    case 2 -> plataformaView.revisarSolicitudes(admin);
                    case 3 -> plataformaView.gestionarPerfiles(admin);
                    case 0 -> consola.mostrar("Sesión cerrada.");
                    default -> consola.mostrar("Opción no válida.");
                }
            } catch (NegocioException e) {
                consola.mostrar("Error: " + e.getMessage());
            }
        }
    }
}
