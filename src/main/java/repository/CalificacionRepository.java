package repository;

import domain.Calificacion;
import service.Outputports.CalificacionOutputPort;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CalificacionRepository implements CalificacionOutputPort {

    private final Map<Long, Calificacion> calificaciones = new LinkedHashMap<>();
    private long siguienteId = 1;

    @Override
    public Calificacion guardar(Calificacion calificacion) {
        if (calificacion.getId() == null) {
            calificacion.setId(siguienteId++);
        }
        calificaciones.put(calificacion.getId(), calificacion);
        return calificacion;
    }

    @Override
    public List<Calificacion> listarPorRestaurante(Long restauranteId) {
        return calificaciones.values().stream()
                .filter(c -> Objects.equals(c.getRestauranteId(), restauranteId))
                .toList();
    }

    @Override
    public boolean existePorPedido(Long pedidoId) {
        return calificaciones.values().stream()
                .anyMatch(c -> Objects.equals(c.getPedidoId(), pedidoId));
    }
}
