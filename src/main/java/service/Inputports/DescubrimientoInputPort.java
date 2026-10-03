package service.Inputports;

import domain.Producto;
import domain.Restaurante;

import java.util.List;

public interface DescubrimientoInputPort {

    // US-03: ordenados por cercanía
    List<Restaurante> listarCercanos(double latitud, double longitud);

    // US-03: filtro por nombre o categoría
    List<Restaurante> buscar(String texto, String categoria);

    // US-04: menú agrupado por categoría en la vista
    List<Producto> verMenu(Long restauranteId);
}
