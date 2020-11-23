package com.example.proyectoelectiva3.carrocompras;

import java.util.List;

public class CarroComprasModel {
    private String idCart;
    private String fechaCreacion;
    private String idCliente;
    private Double descuentos;
    private Double rebajas;
    private Double impuestos;
    private Double subTotal;
    private Double total;
    private Double totalDescuentos;
    List<ItemCarroCompraModel> items;

    public CarroComprasModel(){}

    public CarroComprasModel(String idCart, String fechaCreacion, String idCliente, Double descuentos, Double impuestos, Double subTotal, Double total, List<ItemCarroCompraModel> items) {
        this.idCart = idCart;
        this.fechaCreacion = fechaCreacion;
        this.idCliente = idCliente;
        this.descuentos = descuentos;
        this.impuestos = impuestos;
        this.subTotal = subTotal;
        this.total = total;
        this.items = items;
    }

    public String getIdCart() {
        return idCart;
    }

    public void setIdCart(String idCart) {
        this.idCart = idCart;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public Double getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(Double descuentos) {
        this.descuentos = descuentos;
    }

    public Double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(Double impuestos) {
        this.impuestos = impuestos;
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

    public List<ItemCarroCompraModel> getItems() {
        return items;
    }

    public void setItems(List<ItemCarroCompraModel> items) {
        this.items = items;
    }

    public Double getRebajas() {
        return rebajas;
    }

    public void setRebajas(Double rebajas) {
        this.rebajas = rebajas;
    }

    public Double getTotalDescuentos() {
        return totalDescuentos;
    }

    public void setTotalDescuentos(Double totalDescuentos) {
        this.totalDescuentos = totalDescuentos;
    }
}
