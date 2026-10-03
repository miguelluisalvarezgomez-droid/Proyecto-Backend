package repository;

import domain.Factura;
import service.Outputports.FacturaOutputPort;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class FacturaRepository implements FacturaOutputPort {

    private final Map<Long, Factura> facturas = new LinkedHashMap<>();
    private long siguienteId = 1;

    @Override
    public Factura guardar(Factura factura) {
        if (factura.getId() == null) {
            factura.setId(siguienteId++);
        }
        facturas.put(factura.getId(), factura);
        return factura;
    }

    @Override
    public Optional<Factura> buscarPorPedido(Long pedidoId) {
        return facturas.values().stream()
                .filter(f -> Objects.equals(f.getPedidoId(), pedidoId))
                .findFirst();
    }
}
