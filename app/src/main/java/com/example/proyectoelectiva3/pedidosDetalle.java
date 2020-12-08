package com.example.proyectoelectiva3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class pedidosDetalle extends AppCompatActivity {

    Intent intent = getIntent();

    private FirebaseAuth auntenticacion;
    FirebaseDatabase dbFireBase;
    DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_detalle);
        Bundle datos= getIntent().getExtras();

        Toast.makeText(getApplicationContext(),datos.getString("idpedido"), Toast.LENGTH_LONG).show();

        Query recentPostsQuery = dbRef.child("pedidos")
                .equalTo(datos.getString("idpedido"));

    }
}