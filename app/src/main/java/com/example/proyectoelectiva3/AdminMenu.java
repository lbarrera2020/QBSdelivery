package com.example.proyectoelectiva3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.proyectoelectiva3.FormasPago.Formas_pago;
import com.example.proyectoelectiva3.admin.Categorias;
import com.example.proyectoelectiva3.admin.ProductoDummy;
import com.example.proyectoelectiva3.admin.articulosList;
import com.google.android.datatransport.runtime.retries.Retries;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminMenu extends AppCompatActivity {

    CardView cvAdminCate, cvAdminArt, cvAdminFormasPago, cvAdminUsers, cvAdminSalir;
    private FirebaseAuth auntenticacion;
    private DatabaseReference objdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        auntenticacion = FirebaseAuth.getInstance();
        objdatabase= FirebaseDatabase.getInstance().getReference();

        cvAdminCate = findViewById(R.id.cvAdminCate);
        cvAdminArt = findViewById(R.id.cvAdminArt);
        cvAdminFormasPago = findViewById(R.id.cvAdminFormasPago);
        cvAdminUsers = findViewById(R.id.cvAdminUsers);
        cvAdminSalir = findViewById(R.id.cvAdminSalir);


        cvAdminSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auntenticacion.signOut();
                Intent i= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        cvAdminCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(), Categorias.class);
                startActivity(i);
            }
        });

        cvAdminArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), articulosList.class);
                startActivity(i);
            }
        });

        cvAdminFormasPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Formas_pago.class);
                startActivity(i);
            }
        });
        cvAdminUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MantUsuario.class);
                startActivity(i);
            }
        });

    }
}