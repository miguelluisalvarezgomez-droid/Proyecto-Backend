package service.Inputports;

import domain.Comerciante;

public interface CuentaComercianteInputPort {

    // US-15
    Comerciante iniciarSesion(String correo, String contrasena);
}
