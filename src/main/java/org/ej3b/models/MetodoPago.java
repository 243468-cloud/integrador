package org.ej3b.models;

public class MetodoPago {
    private int idMetodoPago;
    private int idUsuario;
    private String tipo;
    private String detalles;

    public MetodoPago() {}

    public MetodoPago(int idMetodoPago, int idUsuario, String tipo, String detalles) {
        this.idMetodoPago = idMetodoPago;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.detalles = detalles;
    }

    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
}
