package service.Inputports;

import domain.Pedido;

public interface CarritoInputPort {

    // US-05
    Pedido agregarProducto(Long clienteId, Long restauranteId, Long productoId, int cantidad);

    Pedido quitarProducto(Long clienteId, Long productoId);

    Pedido modificarCantidad(Long clienteId, Long productoId, int cantidad);

    Pedido verCarrito(Long clienteId);

    // US-06 (también genera el QR de US-11)
    Pedido confirmarPedido(Long clienteId);
}
