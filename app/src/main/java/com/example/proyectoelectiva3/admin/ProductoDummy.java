package com.example.proyectoelectiva3.admin;

public class ProductoDummy {
    private String id;
    private String nombre;
    private String drescripcion;
    private String precio;
    private ProductoCategoriaDummy categoria;

    public ProductoDummy() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDrescripcion() {
        return drescripcion;
    }

    public void setDrescripcion(String drescripcion) {
        this.drescripcion = drescripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public ProductoCategoriaDummy getCategoria() {
        return categoria;
    }

    public void setCategoria(ProductoCategoriaDummy categoria) {
        this.categoria = categoria;
    }
}
