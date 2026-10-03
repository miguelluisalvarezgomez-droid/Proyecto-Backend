package repository;

import domain.Comerciante;
import service.Outputports.ComercianteOutputPort;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ComercianteRepository implements ComercianteOutputPort {

    private final Map<Long, Comerciante> comerciantes = new LinkedHashMap<>();
    private long siguienteId = 1;

    @Override
    public Comerciante guardar(Comerciante comerciante) {
        if (comerciante.getId() == null) {
            comerciante.setId(siguienteId++);
        }
        comerciantes.put(comerciante.getId(), comerciante);
        return comerciante;
    }

    @Override
    public Optional<Comerciante> buscarPorId(Long id) {
        return Optional.ofNullable(comerciantes.get(id));
    }

    @Override
    public Optional<Comerciante> buscarPorCorreo(String correo) {
        return comerciantes.values().stream()
                .filter(c -> c.getCorreo().equalsIgnoreCase(correo))
                .findFirst();
    }

    @Override
    public boolean existeCorreo(String correo) {
        return buscarPorCorreo(correo).isPresent();
    }

    @Override
    public List<Comerciante> listarTodos() {
        return new ArrayList<>(comerciantes.values());
    }
}
