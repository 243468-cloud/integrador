package org.ej3b.models;

public class Resena {
    private int idResena;
    private int idProducto;
    private String comentario;

    public Resena() {}

    public Resena(int idResena, int idProducto, String comentario) {
        this.idResena = idResena;
        this.idProducto = idProducto;
        this.comentario = comentario;
    }

    public int getIdResena() {
        return idResena;
    }

    public void setIdResena(int idResena) {
        this.idResena = idResena;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
