package service.Inputports;

import domain.Pago;

public interface PagoInputPort {

    // US-08: queda como PENDIENTE hasta la recogida
    Pago pagarEfectivo(Long pedidoId);

    // US-09
    Pago pagarTarjeta(Long pedidoId, String numeroTarjeta, String vencimiento, String cvv);

    // Para mostrar el estado del pago en el detalle del pedido
    Pago consultarPago(Long pedidoId);
}
