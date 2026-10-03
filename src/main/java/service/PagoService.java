package service;

import domain.Pago;
import domain.Pedido;
import domain.enums.EstadoPago;
import domain.enums.EstadoPedido;
import domain.enums.MetodoPago;
import exception.NegocioException;
import service.Inputports.FacturaInputPort;
import service.Inputports.PagoInputPort;
import service.Outputports.PagoOutputPort;
import service.Outputports.PedidoOutputPort;
import validation.TarjetaValidador;

import java.time.LocalDateTime;

public class PagoService implements PagoInputPort {

    private final PagoOutputPort pagoRepository;
    private final PedidoOutputPort pedidoRepository;
    private final FacturaInputPort facturaService;

    public PagoService(PagoOutputPort pagoRepository,
                       PedidoOutputPort pedidoRepository,
                       FacturaInputPort facturaService) {
        this.pagoRepository = pagoRepository;
        this.pedidoRepository = pedidoRepository;
        this.facturaService = facturaService;
    }

    // US-08: queda PENDIENTE hasta que el comercio confirme el cobro (US-22)
    @Override
    public Pago pagarEfectivo(Long pedidoId) {
        Pedido pedido = pedidoPagable(pedidoId);
        Pago pago = nuevoPago(pedido, MetodoPago.EFECTIVO, EstadoPago.PENDIENTE);
        return pagoRepository.guardar(pago);
    }

    // US-09: se valida la tarjeta, se aprueba y se genera la factura
    @Override
    public Pago pagarTarjeta(Long pedidoId, String numeroTarjeta, String vencimiento, String cvv) {
        Pedido pedido = pedidoPagable(pedidoId);

        String numero = TarjetaValidador.numero(numeroTarjeta);
        TarjetaValidador.vencimiento(vencimiento);
        TarjetaValidador.cvv(cvv);

        Pago pago = nuevoPago(pedido, MetodoPago.TARJETA, EstadoPago.PAGADO);
        // Solo se guardan los últimos 4 dígitos; nunca el número completo ni el CVV
        pago.setUltimosDigitosTarjeta(TarjetaValidador.ultimosDigitos(numero));
        pago = pagoRepository.guardar(pago);

        facturaService.generarFactura(pedidoId);
        return pago;
    }

    // Consulta del pago de un pedido (para el seguimiento del cliente)
    @Override
    public Pago consultarPago(Long pedidoId) {
        return pagoRepository.buscarPorPedido(pedidoId)
                .orElseThrow(() -> new NegocioException("Este pedido aún no tiene pago registrado."));
    }

    // ---------- Métodos privados ----------
    private Pedido pedidoPagable(Long pedidoId) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new NegocioException("El pedido no existe."));
        if (pedido.getEstado() == EstadoPedido.EN_CARRITO) {
            throw new NegocioException("Primero debes confirmar el pedido.");
        }
        if (pedido.getEstado() == EstadoPedido.RECHAZADO) {
            throw new NegocioException("El pedido fue rechazado por el restaurante.");
        }
        if (pagoRepository.buscarPorPedido(pedidoId).isPresent()) {
            throw new NegocioException("Este pedido ya tiene un pago registrado.");
        }
        return pedido;
    }

    private Pago nuevoPago(Pedido pedido, MetodoPago metodo, EstadoPago estado) {
        Pago pago = new Pago();
        pago.setPedidoId(pedido.getId());
        pago.setMetodo(metodo);
        pago.setEstado(estado);
        pago.setMonto(pedido.getTotal());
        pago.setFecha(LocalDateTime.now());
        return pago;
    }
}
