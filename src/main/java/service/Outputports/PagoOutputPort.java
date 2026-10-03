package service.Outputports;

import domain.Pago;

import java.util.Optional;

public interface PagoOutputPort {

    Pago guardar(Pago pago);

    Optional<Pago> buscarPorPedido(Long pedidoId);
}
