package domain;

import domain.enums.EstadoPago;
import domain.enums.MetodoPago;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pago {
    private Long id;
    private Long pedidoId;
    private MetodoPago metodo;
    private EstadoPago estado;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private String ultimosDigitosTarjeta;

    public Pago() {
    }

    public Pago(Long id, Long pedidoId, MetodoPago metodo, EstadoPago estado, BigDecimal monto, LocalDateTime fecha, String ultimosDigitosTarjeta) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.metodo = metodo;
        this.estado = estado;
        this.monto = monto;
        this.fecha = fecha;
        this.ultimosDigitosTarjeta = ultimosDigitosTarjeta;
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

    public MetodoPago getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPago metodo) {
        this.metodo = metodo;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getUltimosDigitosTarjeta() {
        return ultimosDigitosTarjeta;
    }

    public void setUltimosDigitosTarjeta(String ultimosDigitosTarjeta) {
        this.ultimosDigitosTarjeta = ultimosDigitosTarjeta;
    }
}
