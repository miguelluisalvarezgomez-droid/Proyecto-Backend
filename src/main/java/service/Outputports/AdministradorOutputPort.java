package service.Outputports;

import domain.Administrador;

import java.util.Optional;

public interface AdministradorOutputPort {

    Administrador guardar(Administrador administrador);

    Optional<Administrador> buscarPorId(Long id);

    Optional<Administrador> buscarPorCorreo(String correo);
}
