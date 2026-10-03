package service.Outputports;

import domain.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoOutputPort {

    Producto guardar(Producto producto);

    Optional<Producto> buscarPorId(Long id);

    List<Producto> listarPorRestaurante(Long restauranteId);
}
