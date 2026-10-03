package service.Inputports;

import domain.Calificacion;

public interface CalificacionInputPort {

    // US-14: solo pedidos ENTREGADOS
    Calificacion calificarRestaurante(Long clienteId, Long pedidoId, int estrellas, String comentario);
}
