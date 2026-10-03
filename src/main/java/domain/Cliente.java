package domain;

import domain.enums.RolUsuario;
import java.time.LocalDateTime;

public class Cliente extends User {
    private String direccion;
    private String fotoUrl;

    public Cliente() {
    }

    public Cliente(Long id, String nombre, String correo, String telefono, String contrasena, RolUsuario rol, boolean activo, LocalDateTime fechaCambioEstado, String motivoDesactivacion, Long adminCambioEstadoId, String direccion, String fotoUrl) {
        super(id, nombre, correo, telefono, contrasena, rol, activo, fechaCambioEstado, motivoDesactivacion, adminCambioEstadoId);
        this.direccion = direccion;
        this.fotoUrl = fotoUrl;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}
