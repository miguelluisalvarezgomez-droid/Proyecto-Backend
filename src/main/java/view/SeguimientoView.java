package view;

import domain.DetallePedido;
import domain.Factura;
import domain.Pago;
import domain.Pedido;
import domain.enums.EstadoPedido;
import exception.NegocioException;
import service.Inputports.CalificacionInputPort;
import service.Inputports.FacturaInputPort;
import service.Inputports.PagoInputPort;
import service.Inputports.SeguimientoInputPort;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class SeguimientoView {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final SeguimientoInputPort seguimiento;
    private final PagoInputPort pagoService;
    private final FacturaInputPort facturaService;
    private final CalificacionInputPort calificacionService;
    private final FacturaView facturaView;
    private final Consola consola;

    public SeguimientoView(SeguimientoInputPort seguimiento, PagoInputPort pagoService,
                           FacturaInputPort facturaService, CalificacionInputPort calificacionService,
                           FacturaView facturaView, Consola consola) {
        this.seguimiento = seguimiento;
        this.pagoService = pagoService;
        this.facturaService = facturaService;
        this.calificacionService = calificacionService;
        this.facturaView = facturaView;
        this.consola = consola;
    }

    // US-12
    public void mostrarHistorial(domain.Cliente cliente) {
        int id = -1;
        while (id != 0) {
            List<Pedido> pedidos = seguimiento.verHistorial(cliente.getId());
            if (pedidos.isEmpty()) {
                consola.mostrar("\nAún no has realizado pedidos.");
                return;
            }
            mostrarLista(pedidos);
            id = consola.leerEntero("ID del pedido para ver el detalle (0 para volver)");
            if (id > 0) {
                final long idBuscado = id;
                Optional<Pedido> elegido = pedidos.stream()
                        .filter(p -> p.getId() == idBuscado)
                        .findFirst();
                if (elegido.isEmpty()) {
                    consola.mostrar("Ese pedido no está en tu historial.");
                } else {
                    try {
                        mostrarDetalle(elegido.get());
                    } catch (NegocioException e) {
                        consola.mostrar("Error: " + e.getMessage());
                    }
                }
            }
        }
    }

    // ---------- Muestra ----------
    private void mostrarLista(List<Pedido> pedidos) {
        consola.mostrar("\n=== MIS PEDIDOS ===");
        for (Pedido p : pedidos) {
            StringBuilder productos = new StringBuilder();
            for (DetallePedido d : p.getDetalles()) {
                if (productos.length() > 0) {
                    productos.append(", ");
                }
                productos.append(d.getNombreProducto()).append(" x").append(d.getCantidad());
            }
            consola.mostrar("[" + p.getId() + "] " + p.getFecha().format(FORMATO)
                    + " | " + p.getNombreRestaurante()
                    + " | " + p.getEstado()
                    + " | $" + dinero(p.getTotal())
                    + "\n    " + productos);
        }
    }

    // US-10, US-11 y acceso a la factura (US-12)
    private void mostrarDetalle(Pedido pedido) {
        consola.mostrar("\n=== PEDIDO #" + pedido.getId() + " ===");
        consola.mostrar("Restaurante: " + pedido.getNombreRestaurante());
        consola.mostrar("Estado: " + pedido.getEstado());
        if (pedido.getEstado() == EstadoPedido.LISTO) {
            consola.mostrar(">> ¡Tu pedido está listo para recoger!");
        }
        for (DetallePedido d : pedido.getDetalles()) {
            BigDecimal subtotal = d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad()));
            consola.mostrar("  " + d.getNombreProducto() + " x" + d.getCantidad() + " = $" + dinero(subtotal));
        }
        consola.mostrar("Total: $" + dinero(pedido.getTotal()));

        int minutos = seguimiento.consultarTiempoEstimado(pedido.getId());
        consola.mostrar("Tiempo estimado de recogida: "
                + (minutos > 0 ? minutos + " min" : "aún no definido por el restaurante"));

        try {
            Pago pago = pagoService.consultarPago(pedido.getId());
            consola.mostrar("Pago: " + pago.getMetodo() + " - " + pago.getEstado());
        } catch (NegocioException e) {
            consola.mostrar("Pago: sin registrar");
        }

        consola.mostrar("Código QR para reclamar: " + seguimiento.obtenerCodigoQr(pedido.getId()));

        consola.mostrar("\n1. Ver factura");
        if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
            consola.mostrar("2. Calificar restaurante");
        }
        consola.mostrar("0. Volver");
        int opcion = consola.leerEntero("Opción");
        if (opcion == 1) {
            verFactura(pedido);
        } else if (opcion == 2 && pedido.getEstado() == EstadoPedido.ENTREGADO) {
            calificar(pedido);
        }
    }

    private void verFactura(Pedido pedido) {
        try {
            Factura factura = facturaService.obtenerFactura(pedido.getId());
            facturaView.mostrar(pedido, factura);
        } catch (NegocioException e) {
            consola.mostrar(e.getMessage() + " (se genera cuando el pago se confirma)");
        }
    }

    // US-14
    private void calificar(Pedido pedido) {
        int estrellas = consola.leerEntero("Calificación (1 a 5 estrellas)");
        String comentario = consola.capturar("Comentario (Enter para omitir)");
        calificacionService.calificarRestaurante(pedido.getClienteId(), pedido.getId(), estrellas, comentario);
        consola.mostrar("¡Gracias por tu calificación!");
    }

    private String dinero(BigDecimal valor) {
        return String.format("%.2f", valor);
    }
}
