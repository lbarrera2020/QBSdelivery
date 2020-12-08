package com.example.proyectoelectiva3.carrocompras;

import com.example.proyectoelectiva3.FormasPago.FormasPagoEntity;

import java.util.List;

public class PedidoModel {
    private String id_pedido;
    private String fechaCreacion;
    private String fechaEntrega;
    private String horaEntrega;
    private String fechaPago;
    private String id_carrito;
    private Double descuentos;
    private Double rebajas;
    private Double cargos; //o impuestos
    private Double costoEnvio;
    private Double subTotal;
    private Double total;
    private Double totalDescuentos; //descuentos + rebajas
    private DireccionesCliente direccionEnvio;
    private InformacionCliente cliente;
    private InformacionCliente repartidor;
    private String estado;
    private FormasPagoEntity formaPago;
    private List<ItemPedidoModel> items;

    public PedidoModel() {
    }

    public PedidoModel(String id_pedido, String fechaCreacion, String fechaEntrega, String horaEntrega, String id_carrito, Double descuentos, Double rebajas, Double cargos, Double costoEnvio, Double subTotal, Double total, Double totalDescuentos, DireccionesCliente direccionEnvio, InformacionCliente cliente, InformacionCliente repartidor, String estado, FormasPagoEntity formaPago, List<ItemPedidoModel> items, String fechaPago) {
        this.id_pedido = id_pedido;
        this.fechaCreacion = fechaCreacion;
        this.fechaEntrega = fechaEntrega;
        this.horaEntrega = horaEntrega;
        this.id_carrito = id_carrito;
        this.descuentos = descuentos;
        this.rebajas = rebajas;
        this.cargos = cargos;
        this.costoEnvio = costoEnvio;
        this.subTotal = subTotal;
        this.total = total;
        this.totalDescuentos = totalDescuentos;
        this.direccionEnvio = direccionEnvio;
        this.cliente = cliente;
        this.repartidor = repartidor;
        this.estado = estado;
        this.formaPago = formaPago;
        this.items = items;
        this.fechaPago = fechaPago;
    }

    public String getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(String id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(String horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public String getId_carrito() {
        return id_carrito;
    }

    public void setId_carrito(String id_carrito) {
        this.id_carrito = id_carrito;
    }

    public Double getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(Double descuentos) {
        this.descuentos = descuentos;
    }

    public Double getRebajas() {
        return rebajas;
    }

    public void setRebajas(Double rebajas) {
        this.rebajas = rebajas;
    }

    public Double getCargos() {
        return cargos;
    }

    public void setCargos(Double cargos) {
        this.cargos = cargos;
    }

    public Double getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(Double costoEnvio) {
        this.costoEnvio = costoEnvio;
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

    public Double getTotalDescuentos() {
        return totalDescuentos;
    }

    public void setTotalDescuentos(Double totalDescuentos) {
        this.totalDescuentos = totalDescuentos;
    }

    public DireccionesCliente getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(DireccionesCliente direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public InformacionCliente getCliente() {
        return cliente;
    }

    public void setCliente(InformacionCliente cliente) {
        this.cliente = cliente;
    }

    public InformacionCliente getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(InformacionCliente repartidor) {
        this.repartidor = repartidor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public FormasPagoEntity getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormasPagoEntity formaPago) {
        this.formaPago = formaPago;
    }

    public List<ItemPedidoModel> getItems() {
        return items;
    }

    public void setItems(List<ItemPedidoModel> items) {
        this.items = items;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }
}
