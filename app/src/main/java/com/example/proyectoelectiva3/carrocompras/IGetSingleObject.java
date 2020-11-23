package com.example.proyectoelectiva3.carrocompras;

public interface IGetSingleObject<T> {
    void onCallBackSuccess(T object);
    void onCallBackFail(String msjError);
}
