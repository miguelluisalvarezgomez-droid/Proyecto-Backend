package service.Inputports;

import domain.Cliente;
import domain.Comerciante;
import domain.Restaurante;
import domain.enums.RolUsuario;

import java.util.List;

public interface GestionPlataformaInputPort {

    // US-28: crea el comerciante y su restaurante (queda inactivo y pendiente de revisión)
    Comerciante crearComercio(Long adminId, String nombre, String correo, String telefono, String contrasena,
                              String nombreNegocio, String categoria, String direccion,
                              String horario, String documentacion);

    // US-28: listado de administración
    List<Comerciante> listarComercios();

    List<Cliente> listarClientes();

    // US-27
    List<Restaurante> listarSolicitudesPendientes();

    Restaurante revisarAfiliacion(Long adminId, Long restauranteId, boolean aprobar, String comentario);

    // US-29 y US-30: se indica el rol porque los ids se repiten entre clientes y comerciantes
    void activarPerfil(Long adminId, Long usuarioId, RolUsuario rol);

    void desactivarPerfil(Long adminId, Long usuarioId, RolUsuario rol, String motivo);
}
