package repository;

import domain.Pago;
import service.Outputports.PagoOutputPort;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class PagoRepository implements PagoOutputPort {

    private final Map<Long, Pago> pagos = new LinkedHashMap<>();
    private long siguienteId = 1;

    @Override
    public Pago guardar(Pago pago) {
        if (pago.getId() == null) {
            pago.setId(siguienteId++);
        }
        pagos.put(pago.getId(), pago);
        return pago;
    }

    @Override
    public Optional<Pago> buscarPorPedido(Long pedidoId) {
        return pagos.values().stream()
                .filter(p -> Objects.equals(p.getPedidoId(), pedidoId))
                .findFirst();
    }
}
