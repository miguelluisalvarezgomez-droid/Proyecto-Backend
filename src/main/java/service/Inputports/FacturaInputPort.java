package service.Inputports;

import domain.Factura;

public interface FacturaInputPort {

    // US-07: se genera al confirmar el pago
    Factura generarFactura(Long pedidoId);

    // US-07 y US-12
    Factura obtenerFactura(Long pedidoId);
}
