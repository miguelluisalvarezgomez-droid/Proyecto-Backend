package domain;

import java.time.LocalDateTime;

public class Calificacion {
    private Long id;
    private Long pedidoId;
    private Long clienteId;
    private Long restauranteId;
    private int estrellas;
    private String comentario;
    private LocalDateTime fecha;

    public Calificacion() {
    }

    public Calificacion(Long id, Long pedidoId, Long clienteId, Long restauranteId, int estrellas, String comentario, LocalDateTime fecha) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.restauranteId = restauranteId;
        this.estrellas = estrellas;
        this.comentario = comentario;
        this.fecha = fecha;
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

    public int getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(int estrellas) {
        this.estrellas = estrellas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
