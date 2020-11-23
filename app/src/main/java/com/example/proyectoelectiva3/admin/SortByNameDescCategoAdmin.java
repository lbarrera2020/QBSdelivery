package com.example.proyectoelectiva3.admin;

import java.util.Comparator;

public class SortByNameDescCategoAdmin implements Comparator<CategoriaEntity> {

    @Override
    public int compare(CategoriaEntity o1, CategoriaEntity o2) {
        return o2.getNombre().compareTo(o1.getNombre());
    }
}
