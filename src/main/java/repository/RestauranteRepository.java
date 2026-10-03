package repository;

import domain.Restaurante;
import service.Outputports.RestauranteOutputPort;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RestauranteRepository implements RestauranteOutputPort {

    private final Map<Long, Restaurante> restaurantes = new LinkedHashMap<>();
    private long siguienteId = 1;

    @Override
    public Restaurante guardar(Restaurante restaurante) {
        if (restaurante.getId() == null) {
            restaurante.setId(siguienteId++);
        }
        restaurantes.put(restaurante.getId(), restaurante);
        return restaurante;
    }

    @Override
    public Optional<Restaurante> buscarPorId(Long id) {
        return Optional.ofNullable(restaurantes.get(id));
    }

    @Override
    public List<Restaurante> listarTodos() {
        return new ArrayList<>(restaurantes.values());
    }
}
