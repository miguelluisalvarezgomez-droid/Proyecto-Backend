package domain;

import domain.enums.EstadoPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Long id;
    private Long clienteId;
    private Long restauranteId;
    private String nombreRestaurante;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private List<DetallePedido> detalles;
    private BigDecimal total;
    private int tiempoEstimadoMin;
    private String codigoQr;

    public Pedido() {
        this.detalles = new ArrayList<>();
    }

    public Pedido(Long id, Long clienteId, Long restauranteId, String nombreRestaurante, LocalDateTime fecha, EstadoPedido estado, List<DetallePedido> detalles, BigDecimal total, int tiempoEstimadoMin, String codigoQr) {
        this.id = id;
        this.clienteId = clienteId;
        this.restauranteId = restauranteId;
        this.nombreRestaurante = nombreRestaurante;
        this.fecha = fecha;
        this.estado = estado;
        this.detalles = detalles;
        this.total = total;
        this.tiempoEstimadoMin = tiempoEstimadoMin;
        this.codigoQr = codigoQr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public String getNombreRestaurante() {
        return nombreRestaurante;
    }

    public void setNombreRestaurante(String nombreRestaurante) {
        this.nombreRestaurante = nombreRestaurante;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getTiempoEstimadoMin() {
        return tiempoEstimadoMin;
    }

    public void setTiempoEstimadoMin(int tiempoEstimadoMin) {
        this.tiempoEstimadoMin = tiempoEstimadoMin;
    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }
}
