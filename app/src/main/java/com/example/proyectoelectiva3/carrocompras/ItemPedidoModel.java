package com.example.proyectoelectiva3.carrocompras;

import com.google.firebase.database.Exclude;

public class ItemPedidoModel {
    private String idItem;
    private String idProducto;
    private Integer cantidad;
    private Double precio;
    private Double descuento;
    private Double rebaja;
    private Double subTotal;
    private Double total;
    private String nombreImagen;
    private String nombreProducto;
    private String idPedido;

    public ItemPedidoModel() { }

    public ItemPedidoModel(String idItem, String idProducto, Integer cantidad, Double precio, Double descuento, Double rebaja, Double subTotal, Double total, String nombreImagen, String nombreProducto, String idPedido) {
        this.idItem = idItem;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = descuento;
        this.rebaja = rebaja;
        this.subTotal = subTotal;
        this.total = total;
        this.nombreImagen = nombreImagen;
        this.nombreProducto = nombreProducto;
        this.idPedido = idPedido;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getRebaja() {
        return rebaja;
    }

    public void setRebaja(Double rebaja) {
        this.rebaja = rebaja;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }
}
