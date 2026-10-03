package service.Outputports;

import domain.Factura;

import java.util.Optional;

public interface FacturaOutputPort {

    Factura guardar(Factura factura);

    Optional<Factura> buscarPorPedido(Long pedidoId);
}
