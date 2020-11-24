package com.example.proyectoelectiva3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class repartidorActivity extends AppCompatActivity {

    CardView cvAdminPedidos, cvAdminSalir;
    private FirebaseAuth auntenticacion;
    private DatabaseReference objdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartidor);

        auntenticacion = FirebaseAuth.getInstance();
        objdatabase= FirebaseDatabase.getInstance().getReference();

        cvAdminPedidos = findViewById(R.id.cvAdminPedidos);
        cvAdminSalir = findViewById(R.id.cvAdminSalir);


        cvAdminPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getApplicationContext(), pedidos_repartidor.class);
                startActivity(i);
                finish();
            }
        });

        cvAdminSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auntenticacion.signOut();
                Intent i= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });



    }
}