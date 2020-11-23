package com.example.proyectoelectiva3;

public class productos {
    private String idImagen;
    private String categorias;
    private String descripcion;
    private String precio;
    private String id;

    public productos(String idImagen,String categorias, String descripcion, String precio, String id){
        this.idImagen= idImagen;
        this.categorias= categorias;
        this.descripcion= descripcion;
        this.precio= precio;
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

    public String getPrecio(){return precio;}

    public String getId(){return id;}
}
