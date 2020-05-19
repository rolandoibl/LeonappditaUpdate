package com.example.leonappditaupdate.Inicio;

public class Banner {

    private String imagen;
    private String link;

    public Banner(String imagen, String link) {
        this.imagen = imagen;
        this.link = link;
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

}
