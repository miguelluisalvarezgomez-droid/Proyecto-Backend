package domain;

import domain.enums.RolUsuario;
import java.time.LocalDateTime;

public class Administrador extends User {

    public Administrador() {
    }

    public Administrador(Long id, String nombre, String correo, String telefono, String contrasena, RolUsuario rol, boolean activo, LocalDateTime fechaCambioEstado, String motivoDesactivacion, Long adminCambioEstadoId) {
        super(id, nombre, correo, telefono, contrasena, rol, activo, fechaCambioEstado, motivoDesactivacion, adminCambioEstadoId);
    }
}
