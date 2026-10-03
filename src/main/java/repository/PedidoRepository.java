package repository;

import domain.Pedido;
import domain.enums.EstadoPedido;
import service.Outputports.PedidoOutputPort;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class PedidoRepository implements PedidoOutputPort {

    private final Map<Long, Pedido> pedidos = new LinkedHashMap<>();
    private long siguienteId = 1;

    @Override
    public Pedido guardar(Pedido pedido) {
        if (pedido.getId() == null) {
            pedido.setId(siguienteId++);
        }
        pedidos.put(pedido.getId(), pedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return Optional.ofNullable(pedidos.get(id));
    }

    @Override
    public Optional<Pedido> buscarCarrito(Long clienteId) {
        return pedidos.values().stream()
                .filter(p -> Objects.equals(p.getClienteId(), clienteId)
                        && p.getEstado() == EstadoPedido.EN_CARRITO)
                .findFirst();
    }

    @Override
    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidos.values().stream()
                .filter(p -> Objects.equals(p.getClienteId(), clienteId))
                .toList();
    }
}
