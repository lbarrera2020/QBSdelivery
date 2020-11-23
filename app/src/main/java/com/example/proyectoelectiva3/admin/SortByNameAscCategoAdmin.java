package com.example.proyectoelectiva3.admin;

import java.util.Comparator;

public class SortByNameAscCategoAdmin implements Comparator<CategoriaEntity> {

    @Override
    public int compare(CategoriaEntity o1, CategoriaEntity o2) {
        return o1.getNombre().compareTo(o2.getNombre());
    }
}
