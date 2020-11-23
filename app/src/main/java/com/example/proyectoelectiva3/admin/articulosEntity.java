package com.example.proyectoelectiva3.admin;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

public class articulosEntity {
    private String nombre, descripcion, precio, rebaja, descuento, fecha, stock, estado, imageName, id;
    private catSpinner categoria;

    public articulosEntity() {
    }

    public catSpinner getCategoria() {
        return categoria;
    }

    public void setCategoria(catSpinner categoria) {
        this.categoria = categoria;
    }

    @Exclude
    private byte[] imageByte;
    @Exclude
    private Bitmap bitMapImage;

   // public String getCantidad() {
     //   return cantidad;
    //}

    //public void setCantidad(String cantidad) {
      //  this.cantidad = cantidad;
    //}

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getRebaja() {
        return rebaja;
    }

    public void setRebaja(String rebaja) {
        this.rebaja = rebaja;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }

    public Bitmap getBitMapImage() {
        return bitMapImage;
    }

    public void setBitMapImage(Bitmap bitMapImage) {
        this.bitMapImage = bitMapImage;
    }

    @Override
    public String toString() {
        return nombre;

    }
}

/*
    public static class articulos {
        private String idImagen;
        private String nombre;
        private String descripcion;

        public articulos(String idImagen,String nombre, String descripcion){
            this.idImagen= idImagen;
            this.nombre= nombre;
            this.descripcion= descripcion;
        }

        public String getIdImagen(){
            return idImagen;
        }
        public String getNombre(){
            return nombre;
        }
        public String getDescripcion(){
            return descripcion;
        }
    }
}
*/