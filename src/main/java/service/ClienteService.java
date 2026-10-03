package service;

import domain.Cliente;
import domain.enums.RolUsuario;
import exception.NegocioException;
import helpers.PasswordHelper;
import service.Inputports.CuentaClienteInputPort;
import service.Outputports.ClienteOutputPort;
import validation.Validador;

import java.time.LocalDateTime;

public class ClienteService implements CuentaClienteInputPort {

    private final ClienteOutputPort clienteRepository;

    // Inversión de dependencias: dependemos de la interfaz, no de la implementación
    public ClienteService(ClienteOutputPort clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // US-01
    @Override
    public Cliente registrar(String nombre, String correo, String telefono, String contrasena) {
        String nombreValido = Validador.requerido(nombre, "nombre");
        String correoValido = Validador.correo(correo);
        String telefonoValido = Validador.telefono(telefono);
        Validador.contrasenaSegura(contrasena);

        if (clienteRepository.existeCorreo(correoValido)) {
            throw new NegocioException("Ya existe una cuenta registrada con ese correo.");
        }

        // El Service crea el objeto de dominio
        Cliente cliente = new Cliente();
        cliente.setNombre(nombreValido);
        cliente.setCorreo(correoValido);
        cliente.setTelefono(telefonoValido);
        cliente.setContrasena(PasswordHelper.hash(contrasena));
        cliente.setRol(RolUsuario.CLIENTE);
        cliente.setActivo(true);
        cliente.setFechaCambioEstado(LocalDateTime.now());
        return clienteRepository.guardar(cliente);
    }

    // US-02
    @Override
    public Cliente iniciarSesion(String correo, String contrasena) {
        String correoBuscado = correo == null ? "" : correo.trim();
        String hash = PasswordHelper.hash(contrasena == null ? "" : contrasena);

        return clienteRepository.buscarPorCorreo(correoBuscado)
                .filter(c -> c.isActivo() && hash.equals(c.getContrasena()))
                .orElseThrow(() -> new NegocioException("Credenciales incorrectas."));
    }

    // US-02 (pendiente)
    @Override
    public void recuperarContrasena(String correo) {
        throw new UnsupportedOperationException("Pendiente: recuperar contraseña.");
    }

    // US-13
    @Override
    public Cliente editarPerfil(Long clienteId, String nombre, String telefono, String direccion, String fotoUrl) {
        Cliente cliente = clienteRepository.buscarPorId(clienteId)
                .orElseThrow(() -> new NegocioException("El cliente no existe."));
        String nombreValido = Validador.requerido(nombre, "nombre");
        String telefonoValido = Validador.telefono(telefono);
        cliente.setNombre(nombreValido);
        cliente.setTelefono(telefonoValido);
        cliente.setDireccion(direccion == null ? null : direccion.trim());
        cliente.setFotoUrl(fotoUrl == null ? null : fotoUrl.trim());
        return clienteRepository.guardar(cliente);
    }
}
