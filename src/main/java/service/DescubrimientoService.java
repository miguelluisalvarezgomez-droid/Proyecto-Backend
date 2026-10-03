package service;

import domain.Producto;
import domain.Restaurante;
import domain.enums.EstadoAfiliacion;
import exception.NegocioException;
import helpers.DistanciaHelper;
import service.Inputports.DescubrimientoInputPort;
import service.Outputports.ProductoOutputPort;
import service.Outputports.RestauranteOutputPort;

import java.util.Comparator;
import java.util.List;

public class DescubrimientoService implements DescubrimientoInputPort {

    private final RestauranteOutputPort restauranteRepository;
    private final ProductoOutputPort productoRepository;

    public DescubrimientoService(RestauranteOutputPort restauranteRepository,
                                 ProductoOutputPort productoRepository) {
        this.restauranteRepository = restauranteRepository;
        this.productoRepository = productoRepository;
    }

    // US-03: ordenados por cercanía
    @Override
    public List<Restaurante> listarCercanos(double latitud, double longitud) {
        if (latitud < -90 || latitud > 90 || longitud < -180 || longitud > 180) {
            throw new NegocioException("Las coordenadas no son válidas.");
        }
        return restauranteRepository.listarTodos().stream()
                .filter(this::estaAprobado)
                .sorted(Comparator.comparingDouble((Restaurante r) ->
                        DistanciaHelper.kilometros(latitud, longitud, r.getLatitud(), r.getLongitud())))
                .toList();
    }

    // US-03: filtro por nombre o categoría (los vacíos se ignoran)
    @Override
    public List<Restaurante> buscar(String texto, String categoria) {
        String textoBuscado = texto == null ? "" : texto.trim().toLowerCase();
        String categoriaBuscada = categoria == null ? "" : categoria.trim();

        return restauranteRepository.listarTodos().stream()
                .filter(this::estaAprobado)
                .filter(r -> textoBuscado.isEmpty()
                        || (r.getNombre() != null && r.getNombre().toLowerCase().contains(textoBuscado)))
                .filter(r -> categoriaBuscada.isEmpty()
                        || (r.getCategoria() != null && r.getCategoria().equalsIgnoreCase(categoriaBuscada)))
                .toList();
    }

    // US-04: ordenado por categoría para que la vista los agrupe
    @Override
    public List<Producto> verMenu(Long restauranteId) {
        restauranteRepository.buscarPorId(restauranteId)
                .filter(this::estaAprobado)
                .orElseThrow(() -> new NegocioException("El restaurante no existe."));

        return productoRepository.listarPorRestaurante(restauranteId).stream()
                .sorted(Comparator
                        .comparing(Producto::getCategoria, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                        .thenComparing(Producto::getNombre, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .toList();
    }

    // Solo se muestran restaurantes cuya afiliación fue aprobada (US-27)
    private boolean estaAprobado(Restaurante restaurante) {
        return restaurante.getEstadoAfiliacion() == EstadoAfiliacion.APROBADA;
    }
}
