package com.example.proyectoelectiva3.carrocompras;

import java.util.List;

public class InformacionCliente {
    private String idCliente;
    private String correo;
    private String nombre;
    private List<DireccionesCliente> direcciones;

    public InformacionCliente() {
    }

    public InformacionCliente(String idCliente, String correo, String nombre, List<DireccionesCliente> direcciones) {
        this.idCliente = idCliente;
        this.correo = correo;
        this.nombre = nombre;
        this.direcciones = direcciones;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<DireccionesCliente> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<DireccionesCliente> direcciones) {
        this.direcciones = direcciones;
    }
}
