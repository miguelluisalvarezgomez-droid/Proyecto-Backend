package service;

import domain.DetallePedido;
import domain.Pedido;
import domain.Producto;
import domain.Restaurante;
import domain.enums.EstadoAfiliacion;
import domain.enums.EstadoPedido;
import exception.NegocioException;
import service.Inputports.CarritoInputPort;
import service.Outputports.PedidoOutputPort;
import service.Outputports.ProductoOutputPort;
import service.Outputports.RestauranteOutputPort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

// El carrito es un Pedido en estado EN_CARRITO
public class CarritoService implements CarritoInputPort {

    private final PedidoOutputPort pedidoRepository;
    private final ProductoOutputPort productoRepository;
    private final RestauranteOutputPort restauranteRepository;

    public CarritoService(PedidoOutputPort pedidoRepository,
                          ProductoOutputPort productoRepository,
                          RestauranteOutputPort restauranteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.restauranteRepository = restauranteRepository;
    }

    // US-05
    @Override
    public Pedido agregarProducto(Long clienteId, Long restauranteId, Long productoId, int cantidad) {
        if (cantidad <= 0) {
            throw new NegocioException("La cantidad debe ser mayor a cero.");
        }
        Restaurante restaurante = restauranteRepository.buscarPorId(restauranteId)
                .filter(r -> r.getEstadoAfiliacion() == EstadoAfiliacion.APROBADA)
                .orElseThrow(() -> new NegocioException("El restaurante no existe."));

        Producto producto = productoRepository.buscarPorId(productoId)
                .filter(p -> Objects.equals(p.getRestauranteId(), restauranteId))
                .orElseThrow(() -> new NegocioException("El producto no existe en este restaurante."));
        if (!producto.isDisponible()) {
            throw new NegocioException("El producto \"" + producto.getNombre() + "\" no está disponible.");
        }

        Pedido carrito = pedidoRepository.buscarCarrito(clienteId)
                .orElseGet(() -> nuevoCarrito(clienteId, restauranteId));

        // Un carrito solo puede tener productos de un restaurante
        if (!carrito.getDetalles().isEmpty() && !Objects.equals(carrito.getRestauranteId(), restauranteId)) {
            throw new NegocioException(
                    "Tu carrito tiene productos de otro restaurante. Quítalos antes de pedir en este.");
        }
        carrito.setRestauranteId(restauranteId);
        carrito.setNombreRestaurante(restaurante.getNombre());

        carrito.getDetalles().stream()
                .filter(d -> Objects.equals(d.getProductoId(), productoId))
                .findFirst()
                .ifPresentOrElse(
                        d -> d.setCantidad(d.getCantidad() + cantidad),
                        () -> carrito.getDetalles().add(nuevoDetalle(producto, cantidad)));

        return guardarConTotal(carrito);
    }

    // US-05
    @Override
    public Pedido quitarProducto(Long clienteId, Long productoId) {
        Pedido carrito = carritoObligatorio(clienteId);
        boolean eliminado = carrito.getDetalles().removeIf(d -> Objects.equals(d.getProductoId(), productoId));
        if (!eliminado) {
            throw new NegocioException("Ese producto no está en tu carrito.");
        }
        return guardarConTotal(carrito);
    }

    // US-05
    @Override
    public Pedido modificarCantidad(Long clienteId, Long productoId, int cantidad) {
        if (cantidad <= 0) {
            throw new NegocioException("La cantidad debe ser mayor a cero (para eliminarlo usa 'quitar').");
        }
        Pedido carrito = carritoObligatorio(clienteId);
        DetallePedido detalle = carrito.getDetalles().stream()
                .filter(d -> Objects.equals(d.getProductoId(), productoId))
                .findFirst()
                .orElseThrow(() -> new NegocioException("Ese producto no está en tu carrito."));
        detalle.setCantidad(cantidad);
        return guardarConTotal(carrito);
    }

    // US-05: si no hay carrito devuelve uno vacío sin guardarlo
    @Override
    public Pedido verCarrito(Long clienteId) {
        return pedidoRepository.buscarCarrito(clienteId)
                .orElseGet(() -> nuevoCarrito(clienteId, null));
    }

    // US-06 (+ QR de US-11)
    @Override
    public Pedido confirmarPedido(Long clienteId) {
        Pedido carrito = carritoObligatorio(clienteId);
        if (carrito.getDetalles().isEmpty()) {
            throw new NegocioException("Tu carrito está vacío.");
        }
        // Se revisa la disponibilidad al momento de confirmar
        for (DetallePedido d : carrito.getDetalles()) {
            Producto producto = productoRepository.buscarPorId(d.getProductoId())
                    .orElseThrow(() -> new NegocioException(
                            "El producto \"" + d.getNombreProducto() + "\" ya no existe. Quítalo del carrito."));
            if (!producto.isDisponible()) {
                throw new NegocioException(
                        "El producto \"" + d.getNombreProducto() + "\" ya no está disponible. Quítalo del carrito.");
            }
        }
        carrito.setTotal(calcularTotal(carrito));
        carrito.setEstado(EstadoPedido.CONFIRMADO);
        carrito.setFecha(LocalDateTime.now());
        carrito.setCodigoQr(UUID.randomUUID().toString());
        return pedidoRepository.guardar(carrito);
    }

    // ---------- Métodos privados ----------
    private Pedido carritoObligatorio(Long clienteId) {
        return pedidoRepository.buscarCarrito(clienteId)
                .orElseThrow(() -> new NegocioException("Tu carrito está vacío."));
    }

    private Pedido nuevoCarrito(Long clienteId, Long restauranteId) {
        Pedido pedido = new Pedido();
        pedido.setClienteId(clienteId);
        pedido.setRestauranteId(restauranteId);
        pedido.setEstado(EstadoPedido.EN_CARRITO);
        pedido.setFecha(LocalDateTime.now());
        pedido.setTotal(BigDecimal.ZERO);
        return pedido;
    }

    private DetallePedido nuevoDetalle(Producto producto, int cantidad) {
        DetallePedido detalle = new DetallePedido();
        detalle.setProductoId(producto.getId());
        detalle.setNombreProducto(producto.getNombre());
        detalle.setPrecioUnitario(producto.getPrecio());
        detalle.setCantidad(cantidad);
        return detalle;
    }

    private Pedido guardarConTotal(Pedido pedido) {
        pedido.setTotal(calcularTotal(pedido));
        return pedidoRepository.guardar(pedido);
    }

    private BigDecimal calcularTotal(Pedido pedido) {
        return pedido.getDetalles().stream()
                .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
