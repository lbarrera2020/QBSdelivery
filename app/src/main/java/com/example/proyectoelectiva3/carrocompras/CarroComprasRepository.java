package com.example.proyectoelectiva3.carrocompras;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.proyectoelectiva3.admin.articulosEntity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CarroComprasRepository {

    private FirebaseDatabase dbFireBase;
    private DatabaseReference dbRef;
    private static final String NOMBRE_DOCUMENTO_CARRO = "carroComprasTmp";
    private static final String NOMBRE_DOCUMENTO_PRODUCTOS = "Productos";

    private void initDB()
    {
        dbFireBase = FirebaseDatabase.getInstance();
        dbRef = dbFireBase.getReference();
    }

    public void crearCarroCompras(final CarroComprasModel carro, final IStringResultProcess callBackResultProcess)
    {
        Log.i("CarroRepo","debug 1 init");

        try {
            initDB();
            dbRef.child(NOMBRE_DOCUMENTO_CARRO).child(carro.getIdCart()).setValue(carro)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            callBackResultProcess.onCallBackSuccess(carro.getIdCart());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callBackResultProcess.onCallBackFail("ERR");
                            Log.e("crearCarro","Error al tratar de insertar datos creacion carro compra", e);
                        }
                    });

        }
        catch (Exception ex){
            callBackResultProcess.onCallBackFail("ERR");
            Log.e("crearCarro","Error creando carro de compras", ex);
        }
        Log.i("crearCarro","debug 1 fin");
    }

    public void modificarCarroCompras(CarroComprasModel carro, final IStringResultProcess callBackResultProcess)
    {

        try {
            initDB();
            dbRef.child(NOMBRE_DOCUMENTO_CARRO).child(carro.getIdCart()).setValue(carro)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            callBackResultProcess.onCallBackSuccess("OK");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callBackResultProcess.onCallBackFail("ERR");
                            Log.e("ModifiCarro","Error al tratar de modificar datos del carro compra", e);
                        }
                    });
        }
        catch (Exception ex){
            callBackResultProcess.onCallBackFail("ERR");
            Log.e("ModifiCarro","Error modificando carro de compras", ex);
        }
    }

    public void eliminmarCarroCompras(CarroComprasModel carro, final IStringResultProcess callBackResultProcess)
    {
        try {
            initDB();
            dbRef.child(NOMBRE_DOCUMENTO_CARRO).child(carro.getIdCart()).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            callBackResultProcess.onCallBackSuccess("OK");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callBackResultProcess.onCallBackFail("ERR");
                            Log.e("eliminando","Error al tratar de eliminando carro compra", e);
                        }
                    });
        }
        catch (Exception ex){
            callBackResultProcess.onCallBackFail("ERR");
            Log.e("eliminando","Error eliminando carro de compras", ex);
        }
    }

    public void eliminmarCarroCompras(String idCarro, final IStringResultProcess callBackResultProcess)
    {
        try {
            initDB();
            dbRef.child(NOMBRE_DOCUMENTO_CARRO).child(idCarro).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            callBackResultProcess.onCallBackSuccess("OK");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callBackResultProcess.onCallBackFail("ERR");
                            Log.e("eliminando","Error al tratar de eliminando carro compra", e);
                        }
                    });
        }
        catch (Exception ex){
            callBackResultProcess.onCallBackFail("ERR");
            Log.e("eliminando","Error eliminando carro de compras", ex);
        }
    }

    public void eliminarItemCarroCompra(final ItemCarroCompraModel itemCarro, final IStringResultProcess callBackResultProcess)
    {
        Log.i("eliminando","Inicio");
        try {
            initDB();

            dbRef.child(NOMBRE_DOCUMENTO_CARRO).child(itemCarro.getIdCarroCompras()).child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.i("eliminando","Ingresa en onDataChange");
                    for (DataSnapshot objSnapshot : snapshot.getChildren())
                    {
                        ItemCarroCompraModel itemAux = objSnapshot.getValue(ItemCarroCompraModel.class);
                        if (itemAux.getIdItem().equals(itemCarro.getIdItem()))
                        {
                            Log.i("eliminando","Ingresa en IdItem Match " + itemAux.getIdItem());
                            Log.i("eliminando","Ingresa en IdItem Match, key " + objSnapshot.getKey());
                            dbRef.child(NOMBRE_DOCUMENTO_CARRO).child(itemCarro.getIdCarroCompras()).child("items").child(objSnapshot.getKey()).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("eliminando","Ingresa en onSuccess...");
                                            callBackResultProcess.onCallBackSuccess("OK");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("eliminando","Error al tratar de eliminar Item  de carro compra", e);
                                            callBackResultProcess.onCallBackFail("Error al tratar de eliminar el item");
                                        }
                                    });
                            break;
                        }
                        else
                        {
                            callBackResultProcess.onCallBackFail("Item no encontrado");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("eliminando","Error al tratar de eliminar Item  de carro compra, " + error.getMessage());
                    callBackResultProcess.onCallBackFail("Error inesperado al tratar de eliminar item");
                }
            });

        }
        catch (Exception ex){
            Log.e("eliminando","Error eliminando Item de carro de compras", ex);
            callBackResultProcess.onCallBackFail("Error inesperado al tratar de eliminar item del carro");
        }
        Log.i("eliminando","Fin");
    }


    public void getCarroComprasByCliente(final String idCliente, final IGetSingleObject<CarroComprasModel> callBackGetCarro)
    {

        try {
            initDB();
            dbRef.child(NOMBRE_DOCUMENTO_CARRO).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    CarroComprasModel car = null;

                    for (DataSnapshot objSnapshot : snapshot.getChildren())
                    {
                        CarroComprasModel carAux = objSnapshot.getValue(CarroComprasModel.class);
                        if (carAux.getIdCliente().equals(idCliente))
                        {
                            car = objSnapshot.getValue(CarroComprasModel.class);
                            break;
                        }
                    }
                    callBackGetCarro.onCallBackSuccess(car);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callBackGetCarro.onCallBackFail(error.getMessage());
                }
            });

        }
        catch (Exception e)
        {
            Log.e("CarroRepo","Error al buscar carro en base al Id Usuario", e);
            callBackGetCarro.onCallBackFail(e.getMessage());
        }
    }

    public void getCarroComprasByClienteWithListener(final String idCliente, final IGetSingleObject<CarroComprasModel> callBackGetCarro)
    {

        try {
            initDB();
            dbRef.child(NOMBRE_DOCUMENTO_CARRO).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists())
                    {
                        CarroComprasModel car = null;

                        for (DataSnapshot objSnapshot : snapshot.getChildren())
                        {
                            CarroComprasModel carAux = objSnapshot.getValue(CarroComprasModel.class);
                            if (carAux.getIdCliente().equals(idCliente))
                            {
                                car = objSnapshot.getValue(CarroComprasModel.class);
                                break;
                            }
                        }
                        callBackGetCarro.onCallBackSuccess(car);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callBackGetCarro.onCallBackFail(error.getMessage());
                }
            });

        }
        catch (Exception e)
        {
            Log.e("CarroRepo","Error al buscar carro en base al Id Usuario", e);
            callBackGetCarro.onCallBackFail(e.getMessage());
        }
    }

    public void getDetalleProducto(String idProducto, final IGetSingleObject<articulosEntity> callBackGetProducto)
    {

        try {
            initDB();
            dbRef.child(NOMBRE_DOCUMENTO_PRODUCTOS).child(idProducto).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    articulosEntity prod = snapshot.getValue(articulosEntity.class);
                    callBackGetProducto.onCallBackSuccess(prod);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callBackGetProducto.onCallBackFail(error.getMessage());
                }
            });
        }
        catch (Exception e)
        {
            Log.e("CarroRepo","Error al buscar producto en base al Id producto", e);
            callBackGetProducto.onCallBackFail(e.getMessage());
        }
    }


}
