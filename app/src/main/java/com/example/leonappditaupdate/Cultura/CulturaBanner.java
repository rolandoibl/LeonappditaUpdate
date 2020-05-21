package com.example.leonappditaupdate.Cultura;

public class CulturaBanner {

    private String imagen;
    private String link;
    private String titulo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public CulturaBanner(String imagen, String link, String titulo, String Id) {
        this.imagen = imagen;
        this.link = link;
        this.titulo = titulo;
        this.id=Id;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }




}

