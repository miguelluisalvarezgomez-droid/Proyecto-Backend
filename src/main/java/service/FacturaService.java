package service;

import domain.Factura;
import domain.Pago;
import domain.enums.EstadoPago;
import exception.NegocioException;
import service.Inputports.FacturaInputPort;
import service.Outputports.FacturaOutputPort;
import service.Outputports.PagoOutputPort;
import service.Outputports.PedidoOutputPort;

import java.time.LocalDateTime;

public class FacturaService implements FacturaInputPort {

    private final FacturaOutputPort facturaRepository;
    private final PagoOutputPort pagoRepository;
    private final PedidoOutputPort pedidoRepository;

    public FacturaService(FacturaOutputPort facturaRepository,
                          PagoOutputPort pagoRepository,
                          PedidoOutputPort pedidoRepository) {
        this.facturaRepository = facturaRepository;
        this.pagoRepository = pagoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    // US-07: solo existe factura cuando el pago está confirmado
    @Override
    public Factura generarFactura(Long pedidoId) {
        pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new NegocioException("El pedido no existe."));

        Pago pago = pagoRepository.buscarPorPedido(pedidoId)
                .filter(p -> p.getEstado() == EstadoPago.PAGADO)
                .orElseThrow(() -> new NegocioException(
                        "La factura se genera cuando el pago está confirmado."));

        // Si ya existe, se devuelve la misma (no se duplica)
        return facturaRepository.buscarPorPedido(pedidoId).orElseGet(() -> {
            Factura factura = new Factura();
            factura.setPedidoId(pedidoId);
            factura.setPagoId(pago.getId());
            factura.setFecha(LocalDateTime.now());
            factura.setTotal(pago.getMonto());
            return facturaRepository.guardar(factura);
        });
    }

    // US-07 y US-12
    @Override
    public Factura obtenerFactura(Long pedidoId) {
        return facturaRepository.buscarPorPedido(pedidoId)
                .orElseThrow(() -> new NegocioException("Este pedido aún no tiene factura."));
    }
}
