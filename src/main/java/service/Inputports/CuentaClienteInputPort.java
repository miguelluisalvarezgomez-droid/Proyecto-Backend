package service.Inputports;

import domain.Cliente;

public interface CuentaClienteInputPort {

    // US-01
    Cliente registrar(String nombre, String correo, String telefono, String contrasena);

    // US-02
    Cliente iniciarSesion(String correo, String contrasena);

    // US-02 (recuperar contraseña)
    void recuperarContrasena(String correo);

    // US-13
    Cliente editarPerfil(Long clienteId, String nombre, String telefono, String direccion, String fotoUrl);
}
