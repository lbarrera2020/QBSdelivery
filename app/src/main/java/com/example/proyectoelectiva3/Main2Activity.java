package com.example.proyectoelectiva3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
private StorageReference mStorageReference;

    private  int[] mImages = new int[]{
            R.drawable.comali2,R.drawable.juanv2,R.drawable.juanv3
    };
    ArrayList <String> imgUrls = new ArrayList<String>();

    Button btnCerrarSesion,btnCategorias;
    private FirebaseAuth auntenticacion;
    TextView tvNombre, tvCorreo;

    private DatabaseReference objdatabase;

    Intent intent = getIntent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Bundle datos= getIntent().getExtras();
        CarouselView carouselView =findViewById(R.id.carrousel);
        carouselView.setPageCount(mImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(mImages[position]);

             }
        });

        auntenticacion = FirebaseAuth.getInstance();
        objdatabase= FirebaseDatabase.getInstance().getReference();

        btnCategorias = findViewById(R.id.btnCategorias);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        tvNombre= findViewById(R.id.tvNombre);
        tvCorreo= findViewById(R.id.tvCorreo);

       /* if(!datos.isEmpty()) {
            String nombre = datos.getString("nombre");
            String correo = datos.getString("email");

            tvNombre.setText(nombre);
            tvCorreo.setText(correo);
        }*/

        btnCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(), RvCategorias.class);
                startActivity(i);
            }
        });


        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auntenticacion.signOut();
                Intent i= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });
            obtenerInformacion();

    }

    private void obtenerInformacion(){

        String id= auntenticacion.getCurrentUser().getUid();


        objdatabase.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombre= snapshot.child("nombre").getValue().toString().trim();
                    String correo= snapshot.child("correo").getValue().toString().trim();

                    tvCorreo.setText(correo);
                    tvNombre.setText(nombre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

