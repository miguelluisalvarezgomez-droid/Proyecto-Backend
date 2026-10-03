package domain;

import domain.enums.EstadoAfiliacion;

public class Restaurante {
    private Long id;
    private Long comercianteId;
    private String nombre;
    private String categoria;
    private String direccion;
    private double latitud;
    private double longitud;
    private String horario;
    private String logoUrl;
    private double calificacionPromedio;
    private EstadoAfiliacion estadoAfiliacion;
    private String comentarioAfiliacion;
    private String documentacion;

    public Restaurante() {
    }

    public Restaurante(Long id, Long comercianteId, String nombre, String categoria, String direccion, double latitud, double longitud, String horario, String logoUrl, double calificacionPromedio, EstadoAfiliacion estadoAfiliacion, String comentarioAfiliacion, String documentacion) {
        this.id = id;
        this.comercianteId = comercianteId;
        this.nombre = nombre;
        this.categoria = categoria;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.horario = horario;
        this.logoUrl = logoUrl;
        this.calificacionPromedio = calificacionPromedio;
        this.estadoAfiliacion = estadoAfiliacion;
        this.comentarioAfiliacion = comentarioAfiliacion;
        this.documentacion = documentacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComercianteId() {
        return comercianteId;
    }

    public void setComercianteId(Long comercianteId) {
        this.comercianteId = comercianteId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public double getCalificacionPromedio() {
        return calificacionPromedio;
    }

    public void setCalificacionPromedio(double calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }

    public EstadoAfiliacion getEstadoAfiliacion() {
        return estadoAfiliacion;
    }

    public void setEstadoAfiliacion(EstadoAfiliacion estadoAfiliacion) {
        this.estadoAfiliacion = estadoAfiliacion;
    }

    public String getComentarioAfiliacion() {
        return comentarioAfiliacion;
    }

    public void setComentarioAfiliacion(String comentarioAfiliacion) {
        this.comentarioAfiliacion = comentarioAfiliacion;
    }

    public String getDocumentacion() {
        return documentacion;
    }

    public void setDocumentacion(String documentacion) {
        this.documentacion = documentacion;
    }
}
