package service;

import domain.Calificacion;
import domain.Pedido;
import domain.Restaurante;
import domain.enums.EstadoPedido;
import exception.NegocioException;
import service.Inputports.CalificacionInputPort;
import service.Outputports.CalificacionOutputPort;
import service.Outputports.PedidoOutputPort;
import service.Outputports.RestauranteOutputPort;

import java.time.LocalDateTime;
import java.util.Objects;

public class CalificacionService implements CalificacionInputPort {

    private static final int MAX_COMENTARIO = 300;

    private final CalificacionOutputPort calificacionRepository;
    private final PedidoOutputPort pedidoRepository;
    private final RestauranteOutputPort restauranteRepository;

    public CalificacionService(CalificacionOutputPort calificacionRepository,
                               PedidoOutputPort pedidoRepository,
                               RestauranteOutputPort restauranteRepository) {
        this.calificacionRepository = calificacionRepository;
        this.pedidoRepository = pedidoRepository;
        this.restauranteRepository = restauranteRepository;
    }

    // US-14
    @Override
    public Calificacion calificarRestaurante(Long clienteId, Long pedidoId, int estrellas, String comentario) {
        if (estrellas < 1 || estrellas > 5) {
            throw new NegocioException("La calificación debe ser de 1 a 5 estrellas.");
        }
        String comentarioLimpio = comentario == null ? "" : comentario.trim();
        if (comentarioLimpio.length() > MAX_COMENTARIO) {
            throw new NegocioException("El comentario no puede superar " + MAX_COMENTARIO + " caracteres.");
        }

        // El pedido debe ser del cliente que califica
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .filter(p -> Objects.equals(p.getClienteId(), clienteId))
                .orElseThrow(() -> new NegocioException("El pedido no existe."));
        if (pedido.getEstado() != EstadoPedido.ENTREGADO) {
            throw new NegocioException("Solo puedes calificar pedidos ya entregados.");
        }
        if (calificacionRepository.existePorPedido(pedidoId)) {
            throw new NegocioException("Ya calificaste este pedido.");
        }
        Restaurante restaurante = restauranteRepository.buscarPorId(pedido.getRestauranteId())
                .orElseThrow(() -> new NegocioException("El restaurante no existe."));

        Calificacion calificacion = new Calificacion();
        calificacion.setPedidoId(pedidoId);
        calificacion.setClienteId(clienteId);
        calificacion.setRestauranteId(restaurante.getId());
        calificacion.setEstrellas(estrellas);
        calificacion.setComentario(comentarioLimpio.isEmpty() ? null : comentarioLimpio);
        calificacion.setFecha(LocalDateTime.now());
        calificacion = calificacionRepository.guardar(calificacion);

        // Se refleja en el promedio del comercio
        double promedio = calificacionRepository.listarPorRestaurante(restaurante.getId()).stream()
                .mapToInt(Calificacion::getEstrellas)
                .average()
                .orElse(0);
        restaurante.setCalificacionPromedio(promedio);
        restauranteRepository.guardar(restaurante);

        return calificacion;
    }
}
