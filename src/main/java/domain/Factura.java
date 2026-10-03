package domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Factura {
    private Long id;
    private Long pedidoId;
    private Long pagoId;
    private LocalDateTime fecha;
    private BigDecimal total;

    public Factura() {
    }

    public Factura(Long id, Long pedidoId, Long pagoId, LocalDateTime fecha, BigDecimal total) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.pagoId = pagoId;
        this.fecha = fecha;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getPagoId() {
        return pagoId;
    }

    public void setPagoId(Long pagoId) {
        this.pagoId = pagoId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
