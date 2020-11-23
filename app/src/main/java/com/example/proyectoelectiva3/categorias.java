package com.example.proyectoelectiva3;


public class categorias {
    private String idImagen;
    private String categorias;
    private String descripcion;
    private String id;

    public categorias(String idImagen,String categorias, String descripcion, String id){
        this.idImagen= idImagen;
        this.categorias= categorias;
        this.descripcion= descripcion;
        this.id= id;
    }

    public String getIdImagen(){
        return idImagen;
    }
    public String getCategorias(){
        return categorias;
    }
    public String getDescripcion(){
        return descripcion;
    }
    public String getId(){
        return id;
    }
}
