package com.example.leonappditaupdate.Comida;

public class Producto {

    private String titulo;
    private String imagen;
    private String proveedor;
    private String contacto;
    private String descripcion;

    public Producto(String titulo, String imagen, String proveedor, String contacto, String descripcion) {
        this.titulo = titulo;
        this.imagen = imagen;
        this.proveedor = proveedor;
        this.contacto = contacto;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
