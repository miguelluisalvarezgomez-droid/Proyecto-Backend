package service.Inputports;

import domain.Administrador;

public interface AdministradorInputPort {

    // US-26
    Administrador iniciarSesion(String correo, String contrasena);
}
