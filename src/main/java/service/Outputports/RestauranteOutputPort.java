package service.Outputports;

import domain.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteOutputPort {

    Restaurante guardar(Restaurante restaurante);

    Optional<Restaurante> buscarPorId(Long id);

    List<Restaurante> listarTodos();
}
