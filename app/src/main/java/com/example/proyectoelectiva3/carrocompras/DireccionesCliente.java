package com.example.proyectoelectiva3.carrocompras;

public class DireccionesCliente {
    private String key;
    private String departamento;
    private String ciudad;
    private String direccion;

    public DireccionesCliente(){}

    public DireccionesCliente(String key, String departamento, String ciudad, String direccion) {
        this.key = key;
        this.departamento = departamento;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return direccion;
    }
}
