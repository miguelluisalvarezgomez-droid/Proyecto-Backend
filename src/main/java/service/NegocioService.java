package service;

import domain.Comerciante;
import domain.Restaurante;
import exception.NegocioException;
import service.Inputports.NegocioInputPort;
import service.Outputports.ComercianteOutputPort;
import service.Outputports.RestauranteOutputPort;
import validation.Validador;

public class NegocioService implements NegocioInputPort {

    private final ComercianteOutputPort comercianteRepository;
    private final RestauranteOutputPort restauranteRepository;

    public NegocioService(ComercianteOutputPort comercianteRepository,
                          RestauranteOutputPort restauranteRepository) {
        this.comercianteRepository = comercianteRepository;
        this.restauranteRepository = restauranteRepository;
    }

    // US-16
    @Override
    public Restaurante verPerfil(Long comercianteId) {
        return restauranteDe(comercianteId);
    }

    // US-16
    @Override
    public Restaurante editarPerfil(Long comercianteId, String nombre, String direccion,
                                    double latitud, double longitud, String horario, String logoUrl) {
        Restaurante restaurante = restauranteDe(comercianteId);

        // Se valida todo antes de modificar el objeto
        String nombreValido = Validador.requerido(nombre, "nombre del negocio");
        String direccionValida = Validador.requerido(direccion, "dirección");
        String horarioValido = Validador.requerido(horario, "horario");
        if (latitud < -90 || latitud > 90 || longitud < -180 || longitud > 180) {
            throw new NegocioException("Las coordenadas no son válidas.");
        }

        restaurante.setNombre(nombreValido);
        restaurante.setDireccion(direccionValida);
        restaurante.setLatitud(latitud);
        restaurante.setLongitud(longitud);
        restaurante.setHorario(horarioValido);
        restaurante.setLogoUrl(logoUrl == null || logoUrl.isBlank() ? null : logoUrl.trim());
        return restauranteRepository.guardar(restaurante);
    }

    private Restaurante restauranteDe(Long comercianteId) {
        Comerciante comerciante = comercianteRepository.buscarPorId(comercianteId)
                .orElseThrow(() -> new NegocioException("El comerciante no existe."));
        if (comerciante.getRestauranteId() == null) {
            throw new NegocioException("Tu cuenta aún no tiene un negocio asociado. Contacta al administrador.");
        }
        return restauranteRepository.buscarPorId(comerciante.getRestauranteId())
                .orElseThrow(() -> new NegocioException("No se encontró tu negocio."));
    }
}
