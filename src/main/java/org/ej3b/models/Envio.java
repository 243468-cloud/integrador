package org.ej3b.models;

import java.time.LocalDateTime;

public class Envio {
    private int idEnvio;
    private int idPedido;
    private String numeroGuia;
    private String estado;
    private LocalDateTime fechaEnvio;

    public Envio() {}

    public Envio(int idEnvio, int idPedido, String numeroGuia, String estado, LocalDateTime fechaEnvio) {
        this.idEnvio = idEnvio;
        this.idPedido = idPedido;
        this.numeroGuia = numeroGuia;
        this.estado = estado;
        this.fechaEnvio = fechaEnvio;
    }

    public int getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(int idEnvio) {
        this.idEnvio = idEnvio;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
