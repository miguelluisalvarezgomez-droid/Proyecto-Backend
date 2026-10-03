package service.Outputports;

import domain.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoOutputPort {

    Pedido guardar(Pedido pedido);

    Optional<Pedido> buscarPorId(Long id);

    // Pedido del cliente en estado EN_CARRITO
    Optional<Pedido> buscarCarrito(Long clienteId);

    List<Pedido> listarPorCliente(Long clienteId);
}
