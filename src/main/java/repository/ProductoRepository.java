package repository;

import domain.Producto;
import service.Outputports.ProductoOutputPort;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ProductoRepository implements ProductoOutputPort {

    private final Map<Long, Producto> productos = new LinkedHashMap<>();
    private long siguienteId = 1;

    @Override
    public Producto guardar(Producto producto) {
        if (producto.getId() == null) {
            producto.setId(siguienteId++);
        }
        productos.put(producto.getId(), producto);
        return producto;
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        return Optional.ofNullable(productos.get(id));
    }

    @Override
    public List<Producto> listarPorRestaurante(Long restauranteId) {
        return productos.values().stream()
                .filter(p -> Objects.equals(p.getRestauranteId(), restauranteId))
                .toList();
    }
}
