package service;

import domain.Pedido;
import domain.enums.EstadoPedido;
import exception.NegocioException;
import service.Inputports.SeguimientoInputPort;
import service.Outputports.PedidoOutputPort;

import java.util.Comparator;
import java.util.List;

public class SeguimientoService implements SeguimientoInputPort {

    private final PedidoOutputPort pedidoRepository;

    public SeguimientoService(PedidoOutputPort pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    // US-10: 0 significa que el restaurante aún no lo ha definido
    @Override
    public int consultarTiempoEstimado(Long pedidoId) {
        return pedidoConfirmado(pedidoId).getTiempoEstimadoMin();
    }

    // US-11
    @Override
    public String obtenerCodigoQr(Long pedidoId) {
        String qr = pedidoConfirmado(pedidoId).getCodigoQr();
        if (qr == null || qr.isBlank()) {
            throw new NegocioException("Este pedido aún no tiene código QR.");
        }
        return qr;
    }

    // US-12: del más reciente al más antiguo, sin contar el carrito
    @Override
    public List<Pedido> verHistorial(Long clienteId) {
        return pedidoRepository.listarPorCliente(clienteId).stream()
                .filter(p -> p.getEstado() != EstadoPedido.EN_CARRITO)
                .sorted(Comparator.comparing(Pedido::getFecha, Comparator.reverseOrder()))
                .toList();
    }

    private Pedido pedidoConfirmado(Long pedidoId) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new NegocioException("El pedido no existe."));
        if (pedido.getEstado() == EstadoPedido.EN_CARRITO) {
            throw new NegocioException("Este pedido aún no ha sido confirmado.");
        }
        return pedido;
    }
}
