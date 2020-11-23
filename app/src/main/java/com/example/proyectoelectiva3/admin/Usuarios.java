package com.example.proyectoelectiva3.admin;

import java.util.List;

public class Usuarios {
    private String Uid;
    private String nombre;
    private String correo;
    private String contraseña;
    private String rol;
    private List<Direcciones> direcciones;

    public Usuarios() {
    }

    public List<Direcciones> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<Direcciones> direcciones) {
        this.direcciones = direcciones;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getRoll() {
        return rol;
    }

    public void setRoll(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
