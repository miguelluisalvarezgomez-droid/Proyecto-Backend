package view;

import domain.Factura;
import domain.Pago;
import domain.Pedido;
import exception.NegocioException;
import service.Inputports.FacturaInputPort;
import service.Inputports.PagoInputPort;

public class PagoView {

    private final PagoInputPort pagoService;
    private final FacturaInputPort facturaService;
    private final FacturaView facturaView;
    private final Consola consola;

    public PagoView(PagoInputPort pagoService, FacturaInputPort facturaService,
                    FacturaView facturaView, Consola consola) {
        this.pagoService = pagoService;
        this.facturaService = facturaService;
        this.facturaView = facturaView;
        this.consola = consola;
    }

    // Se repite hasta que el pago quede registrado
    public void seleccionarMetodoPago(Pedido pedido) {
        boolean registrado = false;
        while (!registrado) {
            consola.mostrar("\n=== MÉTODO DE PAGO (total $" + String.format("%.2f", pedido.getTotal()) + ") ===");
            consola.mostrar("1. Efectivo (pagas al recoger)");
            consola.mostrar("2. Tarjeta");
            int opcion = consola.leerEntero("Opción");
            try {
                switch (opcion) {
                    case 1 -> {
                        pagarEfectivo(pedido);
                        registrado = true;
                    }
                    case 2 -> {
                        pagarTarjeta(pedido);
                        registrado = true;
                    }
                    default -> consola.mostrar("Opción no válida.");
                }
            } catch (NegocioException e) {
                consola.mostrar("Error: " + e.getMessage());
            }
        }
    }

    // ---------- Captura ----------
    private void pagarEfectivo(Pedido pedido) {
        Pago pago = pagoService.pagarEfectivo(pedido.getId());
        consola.mostrar("Pago en efectivo registrado. Estado: " + pago.getEstado()
                + ". Pagarás al recoger tu pedido #" + pedido.getId() + ".");
    }

    private void pagarTarjeta(Pedido pedido) {
        String numero = consola.capturar("Número de tarjeta");
        String vencimiento = consola.capturar("Vencimiento (MM/AA)");
        String cvv = consola.capturar("CVV");
        Pago pago = pagoService.pagarTarjeta(pedido.getId(), numero, vencimiento, cvv);
        consola.mostrar("Pago aprobado con tarjeta terminada en " + pago.getUltimosDigitosTarjeta() + ".");

        Factura factura = facturaService.obtenerFactura(pedido.getId());
        facturaView.mostrar(pedido, factura);
    }
}
