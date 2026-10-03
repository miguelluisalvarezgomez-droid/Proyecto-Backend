package service;

import domain.Cliente;
import domain.Comerciante;
import domain.Restaurante;
import domain.User;
import domain.enums.EstadoAfiliacion;
import domain.enums.RolUsuario;
import exception.NegocioException;
import helpers.PasswordHelper;
import service.Inputports.GestionPlataformaInputPort;
import service.Outputports.AdministradorOutputPort;
import service.Outputports.ClienteOutputPort;
import service.Outputports.ComercianteOutputPort;
import service.Outputports.RestauranteOutputPort;
import validation.Validador;

import java.time.LocalDateTime;
import java.util.List;

public class GestionPlataformaService implements GestionPlataformaInputPort {

    private final AdministradorOutputPort administradorRepository;
    private final ComercianteOutputPort comercianteRepository;
    private final ClienteOutputPort clienteRepository;
    private final RestauranteOutputPort restauranteRepository;

    public GestionPlataformaService(AdministradorOutputPort administradorRepository,
                                    ComercianteOutputPort comercianteRepository,
                                    ClienteOutputPort clienteRepository,
                                    RestauranteOutputPort restauranteRepository) {
        this.administradorRepository = administradorRepository;
        this.comercianteRepository = comercianteRepository;
        this.clienteRepository = clienteRepository;
        this.restauranteRepository = restauranteRepository;
    }

    // US-28
    @Override
    public Comerciante crearComercio(Long adminId, String nombre, String correo, String telefono, String contrasena,
                                     String nombreNegocio, String categoria, String direccion,
                                     String horario, String documentacion) {
        verificarAdministrador(adminId);

        String nombreValido = Validador.requerido(nombre, "nombre del comerciante");
        String correoValido = Validador.correo(correo);
        String telefonoValido = Validador.telefono(telefono);
        Validador.contrasenaSegura(contrasena);
        String negocioValido = Validador.requerido(nombreNegocio, "nombre del negocio");
        String categoriaValida = Validador.requerido(categoria, "categoría");
        String direccionValida = Validador.requerido(direccion, "dirección");
        String horarioValido = Validador.requerido(horario, "horario");
        String documentacionValida = Validador.requerido(documentacion, "documentación");

        // Sin perfiles duplicados
        if (comercianteRepository.existeCorreo(correoValido) || clienteRepository.existeCorreo(correoValido)) {
            throw new NegocioException("Ya existe un perfil con ese correo.");
        }
        boolean negocioRepetido = restauranteRepository.listarTodos().stream()
                .anyMatch(r -> negocioValido.equalsIgnoreCase(r.getNombre()));
        if (negocioRepetido) {
            throw new NegocioException("Ya existe un negocio con ese nombre.");
        }

        Restaurante restaurante = new Restaurante();
        restaurante.setNombre(negocioValido);
        restaurante.setCategoria(categoriaValida);
        restaurante.setDireccion(direccionValida);
        restaurante.setHorario(horarioValido);
        restaurante.setDocumentacion(documentacionValida);
        restaurante.setEstadoAfiliacion(EstadoAfiliacion.PENDIENTE);
        restaurante = restauranteRepository.guardar(restaurante);

        Comerciante comerciante = new Comerciante();
        comerciante.setNombre(nombreValido);
        comerciante.setCorreo(correoValido);
        comerciante.setTelefono(telefonoValido);
        comerciante.setContrasena(PasswordHelper.hash(contrasena));
        comerciante.setRol(RolUsuario.COMERCIANTE);
        comerciante.setActivo(false); // debe activarlo un administrador (US-29)
        comerciante.setFechaCambioEstado(LocalDateTime.now());
        comerciante.setRestauranteId(restaurante.getId());
        comerciante = comercianteRepository.guardar(comerciante);

        restaurante.setComercianteId(comerciante.getId());
        restauranteRepository.guardar(restaurante);
        return comerciante;
    }

    @Override
    public List<Comerciante> listarComercios() {
        return comercianteRepository.listarTodos();
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.listarTodos();
    }

    // US-27
    @Override
    public List<Restaurante> listarSolicitudesPendientes() {
        return restauranteRepository.listarTodos().stream()
                .filter(r -> r.getEstadoAfiliacion() == EstadoAfiliacion.PENDIENTE)
                .toList();
    }

    // US-27: al rechazar el comentario es obligatorio
    @Override
    public Restaurante revisarAfiliacion(Long adminId, Long restauranteId, boolean aprobar, String comentario) {
        verificarAdministrador(adminId);
        Restaurante restaurante = restauranteRepository.buscarPorId(restauranteId)
                .orElseThrow(() -> new NegocioException("La solicitud no existe."));
        if (restaurante.getEstadoAfiliacion() != EstadoAfiliacion.PENDIENTE) {
            throw new NegocioException("Esta solicitud ya fue revisada.");
        }
        String comentarioLimpio = comentario == null ? "" : comentario.trim();
        if (!aprobar && comentarioLimpio.isEmpty()) {
            throw new NegocioException("Debes indicar un comentario al rechazar una solicitud.");
        }
        restaurante.setEstadoAfiliacion(aprobar ? EstadoAfiliacion.APROBADA : EstadoAfiliacion.RECHAZADA);
        restaurante.setComentarioAfiliacion(comentarioLimpio.isEmpty() ? null : comentarioLimpio);
        return restauranteRepository.guardar(restaurante);
    }

    // US-29
    @Override
    public void activarPerfil(Long adminId, Long usuarioId, RolUsuario rol) {
        verificarAdministrador(adminId);
        User usuario = obtenerUsuario(usuarioId, rol);
        if (usuario.isActivo()) {
            throw new NegocioException("El perfil ya está activo.");
        }
        usuario.setActivo(true);
        usuario.setMotivoDesactivacion(null);
        registrarCambio(usuario, adminId);
        guardar(usuario, rol);
    }

    // US-30
    @Override
    public void desactivarPerfil(Long adminId, Long usuarioId, RolUsuario rol, String motivo) {
        verificarAdministrador(adminId);
        String motivoValido = Validador.requerido(motivo, "motivo");
        User usuario = obtenerUsuario(usuarioId, rol);
        if (!usuario.isActivo()) {
            throw new NegocioException("El perfil ya está inactivo.");
        }
        usuario.setActivo(false);
        usuario.setMotivoDesactivacion(motivoValido);
        registrarCambio(usuario, adminId);
        guardar(usuario, rol);
    }

    // ---------- Métodos privados ----------
    private void verificarAdministrador(Long adminId) {
        administradorRepository.buscarPorId(adminId)
                .filter(User::isActivo)
                .orElseThrow(() -> new NegocioException("No tienes permisos de administrador."));
    }

    private User obtenerUsuario(Long id, RolUsuario rol) {
        return switch (rol) {
            case COMERCIANTE -> comercianteRepository.buscarPorId(id)
                    .orElseThrow(() -> new NegocioException("El perfil no existe."));
            case CLIENTE -> clienteRepository.buscarPorId(id)
                    .orElseThrow(() -> new NegocioException("El perfil no existe."));
            default -> throw new NegocioException("Ese tipo de perfil no se puede gestionar.");
        };
    }

    // Se registra la fecha y el administrador que hizo el cambio
    private void registrarCambio(User usuario, Long adminId) {
        usuario.setFechaCambioEstado(LocalDateTime.now());
        usuario.setAdminCambioEstadoId(adminId);
    }

    private void guardar(User usuario, RolUsuario rol) {
        if (rol == RolUsuario.COMERCIANTE) {
            comercianteRepository.guardar((Comerciante) usuario);
        } else {
            clienteRepository.guardar((Cliente) usuario);
        }
    }
}
