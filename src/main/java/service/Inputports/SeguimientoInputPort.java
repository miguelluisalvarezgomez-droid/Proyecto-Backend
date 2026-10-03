package service.Inputports;

import domain.Pedido;

import java.util.List;

public interface SeguimientoInputPort {

    // US-10
    int consultarTiempoEstimado(Long pedidoId);

    // US-11
    String obtenerCodigoQr(Long pedidoId);

    // US-12: del más reciente al más antiguo
    List<Pedido> verHistorial(Long clienteId);
}
