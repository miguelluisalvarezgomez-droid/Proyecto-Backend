package domain;

import domain.enums.RolUsuario;
import java.time.LocalDateTime;

public class Comerciante extends User {
    private Long restauranteId;

    public Comerciante() {
    }

    public Comerciante(Long id, String nombre, String correo, String telefono, String contrasena, RolUsuario rol, boolean activo, LocalDateTime fechaCambioEstado, String motivoDesactivacion, Long adminCambioEstadoId, Long restauranteId) {
        super(id, nombre, correo, telefono, contrasena, rol, activo, fechaCambioEstado, motivoDesactivacion, adminCambioEstadoId);
        this.restauranteId = restauranteId;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }
}
