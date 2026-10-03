package service.Outputports;

import domain.Comerciante;

import java.util.List;
import java.util.Optional;

public interface ComercianteOutputPort {

    Comerciante guardar(Comerciante comerciante);

    Optional<Comerciante> buscarPorId(Long id);

    Optional<Comerciante> buscarPorCorreo(String correo);

    boolean existeCorreo(String correo);

    List<Comerciante> listarTodos();
}
