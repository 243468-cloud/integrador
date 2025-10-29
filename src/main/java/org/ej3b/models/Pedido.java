package org.ej3b.models;

import java.time.LocalDateTime;

public class Pedido {
    private int idPedido;
    private String numeroPedido;
    private int idUsuario;
    private int idProducto;
    private int cantidad;
    private int idMetodoPago;
    private int idDireccion;
    private double total;
    private String estado;
    private LocalDateTime fecha;

    public Pedido() {}

    public Pedido(int idPedido, String numeroPedido, int idUsuario, int idProducto, int cantidad, 
                  int idMetodoPago, int idDireccion, double total, String estado, LocalDateTime fecha) {
        this.idPedido = idPedido;
        this.numeroPedido = numeroPedido;
        this.idUsuario = idUsuario;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.idMetodoPago = idMetodoPago;
        this.idDireccion = idDireccion;
        this.total = total;
        this.estado = estado;
        this.fecha = fecha;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public int getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
