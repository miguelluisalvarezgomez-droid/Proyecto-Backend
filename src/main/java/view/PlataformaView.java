package view;

import domain.Administrador;
import domain.Cliente;
import domain.Comerciante;
import domain.Restaurante;
import domain.User;
import domain.enums.RolUsuario;
import exception.NegocioException;
import service.Inputports.GestionPlataformaInputPort;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PlataformaView {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final GestionPlataformaInputPort plataforma;
    private final Consola consola;

    public PlataformaView(GestionPlataformaInputPort plataforma, Consola consola) {
        this.plataforma = plataforma;
        this.consola = consola;
    }

    // ---------- US-28: crear comercio ----------
    public void crearComercio(Administrador admin) {
        String nombre = consola.capturar("Nombre del comerciante");
        String correo = consola.capturar("Correo del comerciante");
        String telefono = consola.capturar("Teléfono");
        String contrasena = consola.capturar("Contraseña inicial");
        String negocio = consola.capturar("Nombre del negocio");
        String categoria = consola.capturar("Categoría (ej. Pizzería)");
        String direccion = consola.capturar("Dirección");
        String horario = consola.capturar("Horario (ej. Lun-Sab 8:00-20:00)");
        String documentacion = consola.capturar("Documentación presentada (ej. RUT, cámara de comercio)");

        Comerciante comerciante = plataforma.crearComercio(admin.getId(), nombre, correo, telefono, contrasena,
                negocio, categoria, direccion, horario, documentacion);
        consola.mostrar("Comercio creado (comerciante #" + comerciante.getId() + ").");
        consola.mostrar("Queda INACTIVO y con afiliación PENDIENTE: actívalo y apruébalo para que pueda operar.");
    }

    // ---------- US-27: revisar solicitudes ----------
    public void revisarSolicitudes(Administrador admin) {
        int id = -1;
        while (id != 0) {
            List<Restaurante> pendientes = plataforma.listarSolicitudesPendientes();
            if (pendientes.isEmpty()) {
                consola.mostrar("\nNo hay solicitudes pendientes.");
                return;
            }
            consola.mostrar("\n=== SOLICITUDES DE AFILIACIÓN ===");
            for (Restaurante r : pendientes) {
                consola.mostrar("[" + r.getId() + "] " + r.getNombre() + " | " + r.getCategoria()
                        + " | " + r.getDireccion() + " | " + r.getHorario()
                        + "\n    Documentación: " + r.getDocumentacion());
            }
            id = consola.leerEntero("ID de la solicitud a revisar (0 para volver)");
            if (id > 0) {
                final long idBuscado = id;
                Optional<Restaurante> elegida = pendientes.stream()
                        .filter(r -> r.getId() == idBuscado)
                        .findFirst();
                if (elegida.isEmpty()) {
                    consola.mostrar("Esa solicitud no está pendiente.");
                    continue;
                }
                try {
                    decidir(admin, elegida.get());
                } catch (NegocioException e) {
                    consola.mostrar("Error: " + e.getMessage());
                }
            }
        }
    }

    private void decidir(Administrador admin, Restaurante restaurante) {
        int decision = consola.leerEntero("1 = Aprobar, 2 = Rechazar");
        if (decision != 1 && decision != 2) {
            consola.mostrar("Opción no válida.");
            return;
        }
        String comentario = consola.capturar("Comentario");
        plataforma.revisarAfiliacion(admin.getId(), restaurante.getId(), decision == 1, comentario);
        consola.mostrar("Solicitud " + (decision == 1 ? "aprobada" : "rechazada")
                + ". El comercio verá el resultado en su perfil.");
    }

    // ---------- US-28 / 29 / 30: gestión de perfiles ----------
    public void gestionarPerfiles(Administrador admin) {
        int opcion = -1;
        while (opcion != 0) {
            consola.mostrar("\n=== GESTIÓN DE PERFILES ===");
            consola.mostrar("1. Ver comercios");
            consola.mostrar("2. Ver clientes");
            consola.mostrar("3. Activar perfil");
            consola.mostrar("4. Desactivar perfil");
            consola.mostrar("0. Volver");
            opcion = consola.leerEntero("Opción");
            try {
                switch (opcion) {
                    case 1 -> listarComercios();
                    case 2 -> listarClientes();
                    case 3 -> cambiarEstado(admin, true);
                    case 4 -> cambiarEstado(admin, false);
                    case 0 -> { }
                    default -> consola.mostrar("Opción no válida.");
                }
            } catch (NegocioException e) {
                consola.mostrar("Error: " + e.getMessage());
            }
        }
    }

    private void cambiarEstado(Administrador admin, boolean activar) {
        int tipo = consola.leerEntero("Tipo de perfil (1 = comercio, 2 = cliente)");
        RolUsuario rol = tipo == 1 ? RolUsuario.COMERCIANTE : tipo == 2 ? RolUsuario.CLIENTE : null;
        if (rol == null) {
            consola.mostrar("Tipo de perfil no válido.");
            return;
        }
        int id = consola.leerEntero("ID del perfil");
        if (id <= 0) {
            consola.mostrar("ID no válido.");
            return;
        }
        if (activar) {
            plataforma.activarPerfil(admin.getId(), (long) id, rol);
            consola.mostrar("Perfil activado.");
        } else {
            String motivo = consola.capturar("Motivo de la desactivación");
            plataforma.desactivarPerfil(admin.getId(), (long) id, rol, motivo);
            consola.mostrar("Perfil desactivado.");
        }
    }

    // ---------- Listados ----------
    private void listarComercios() {
        List<Comerciante> comercios = plataforma.listarComercios();
        if (comercios.isEmpty()) {
            consola.mostrar("Aún no hay comercios registrados.");
            return;
        }
        consola.mostrar("\n--- COMERCIOS ---");
        for (Comerciante c : comercios) {
            consola.mostrar("[" + c.getId() + "] " + c.getNombre() + " | " + c.getCorreo()
                    + " | negocio #" + c.getRestauranteId() + " | " + estado(c));
        }
    }

    private void listarClientes() {
        List<Cliente> clientes = plataforma.listarClientes();
        if (clientes.isEmpty()) {
            consola.mostrar("Aún no hay clientes registrados.");
            return;
        }
        consola.mostrar("\n--- CLIENTES ---");
        for (Cliente c : clientes) {
            consola.mostrar("[" + c.getId() + "] " + c.getNombre() + " | " + c.getCorreo() + " | " + estado(c));
        }
    }

    private String estado(User u) {
        String texto = u.isActivo() ? "ACTIVO" : "INACTIVO";
        if (!u.isActivo() && u.getMotivoDesactivacion() != null) {
            texto += " (motivo: " + u.getMotivoDesactivacion() + ")";
        }
        if (u.getAdminCambioEstadoId() != null) {
            texto += " [admin #" + u.getAdminCambioEstadoId() + ", " + u.getFechaCambioEstado().format(FORMATO) + "]";
        }
        return texto;
    }
}
