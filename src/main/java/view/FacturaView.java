package view;

import domain.DetallePedido;
import domain.Factura;
import domain.Pedido;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

// Reutilizable también desde el historial de pedidos (US-12)
public class FacturaView {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Consola consola;

    public FacturaView(Consola consola) {
        this.consola = consola;
    }

    public void mostrar(Pedido pedido, Factura factura) {
        consola.mostrar("\n=== FACTURA #" + factura.getId() + " ===");
        consola.mostrar("Pedido #" + pedido.getId() + " | " + factura.getFecha().format(FORMATO));
        for (DetallePedido d : pedido.getDetalles()) {
            BigDecimal subtotal = d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad()));
            consola.mostrar(d.getNombreProducto() + " x" + d.getCantidad()
                    + " @ $" + String.format("%.2f", d.getPrecioUnitario())
                    + " = $" + String.format("%.2f", subtotal));
        }
        consola.mostrar("TOTAL: $" + String.format("%.2f", factura.getTotal()));
    }
}
