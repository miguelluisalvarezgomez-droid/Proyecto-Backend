package view;

// Punto de entrada: cada rol tiene su propio acceso
public class MenuPrincipalView {

    private final ClienteView clienteView;
    private final ComercianteView comercianteView;
    private final AdministradorView administradorView;
    private final Consola consola;

    public MenuPrincipalView(ClienteView clienteView, ComercianteView comercianteView,
                             AdministradorView administradorView, Consola consola) {
        this.clienteView = clienteView;
        this.comercianteView = comercianteView;
        this.administradorView = administradorView;
        this.consola = consola;
    }

    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            consola.mostrar("\n=== PLATAFORMA DE PEDIDOS ===");
            consola.mostrar("1. Soy cliente");
            consola.mostrar("2. Soy comerciante");
            consola.mostrar("3. Soy administrador");
            consola.mostrar("0. Salir");
            opcion = consola.leerEntero("Opción");
            switch (opcion) {
                case 1 -> clienteView.mostrarMenu();
                case 2 -> comercianteView.mostrarMenu();
                case 3 -> administradorView.mostrarMenu();
                case 0 -> consola.mostrar("Hasta pronto.");
                default -> consola.mostrar("Opción no válida.");
            }
        }
    }
}
