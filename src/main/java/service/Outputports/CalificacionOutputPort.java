package service.Outputports;

import domain.Calificacion;

import java.util.List;

public interface CalificacionOutputPort {

    Calificacion guardar(Calificacion calificacion);

    List<Calificacion> listarPorRestaurante(Long restauranteId);

    boolean existePorPedido(Long pedidoId);
}
