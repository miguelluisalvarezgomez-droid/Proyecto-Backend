package domain;

import domain.enums.RolUsuario;
import java.time.LocalDateTime;

public abstract class User {
    private Long id;
    private String nombre;
    private String correo;
    private String telefono;
    private String contrasena;
    private RolUsuario rol;
    private boolean activo;
    private LocalDateTime fechaCambioEstado;
    private String motivoDesactivacion;
    private Long adminCambioEstadoId;

    public User() {
    }

    public User(Long id, String nombre, String correo, String telefono, String contrasena, RolUsuario rol, boolean activo, LocalDateTime fechaCambioEstado, String motivoDesactivacion, Long adminCambioEstadoId) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.rol = rol;
        this.activo = activo;
        this.fechaCambioEstado = fechaCambioEstado;
        this.motivoDesactivacion = motivoDesactivacion;
        this.adminCambioEstadoId = adminCambioEstadoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCambioEstado() {
        return fechaCambioEstado;
    }

    public void setFechaCambioEstado(LocalDateTime fechaCambioEstado) {
        this.fechaCambioEstado = fechaCambioEstado;
    }

    public String getMotivoDesactivacion() {
        return motivoDesactivacion;
    }

    public void setMotivoDesactivacion(String motivoDesactivacion) {
        this.motivoDesactivacion = motivoDesactivacion;
    }

    public Long getAdminCambioEstadoId() {
        return adminCambioEstadoId;
    }

    public void setAdminCambioEstadoId(Long adminCambioEstadoId) {
        this.adminCambioEstadoId = adminCambioEstadoId;
    }
}
