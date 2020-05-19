package com.example.leonappditaupdate.Inicio;

public class Libro {

    private String imagen;
    private String link;
    private int id;

    public Libro(String imagen, String link, int id) {
        this.imagen = imagen;
        this.link = link;
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
