package com.example.proyectoelectiva3.carrocompras;

import java.io.Serializable;

public class MsjFinalData implements Serializable {
    private String id_pedido;
    private String id_carrito;
    private InformacionCliente cliente;

    public MsjFinalData() {
    }

    public MsjFinalData(String id_pedido, String id_carrito, InformacionCliente cliente) {
        this.id_pedido = id_pedido;
        this.id_carrito = id_carrito;
        this.cliente = cliente;
    }

    public String getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(String id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getId_carrito() {
        return id_carrito;
    }

    public void setId_carrito(String id_carrito) {
        this.id_carrito = id_carrito;
    }

    public InformacionCliente getCliente() {
        return cliente;
    }

    public void setCliente(InformacionCliente cliente) {
        this.cliente = cliente;
    }
}
