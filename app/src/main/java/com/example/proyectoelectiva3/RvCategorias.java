package com.example.proyectoelectiva3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
public class RvCategorias extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    List items = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_categorias);

        FirebaseDatabase database= FirebaseDatabase.getInstance();
        database.getReference().child("Categorias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.removeAll(items);
                for(DataSnapshot ds : snapshot.getChildren()) {
                    // String id = ds.getKey();

                    String categoria = ds.child("nombre").getValue(String.class);
                    String descripcion = ds.child("descripcion").getValue(String.class);
                    String imagen = ds.child("imageName").getValue(String.class);
                    String id = ds.child("id").getValue(String.class);

                    items.add(new categorias(imagen, categoria, descripcion,id));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.recicladorOld);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);
        // Crear un nuevo adaptador
        //adapter = new CategoriasAdapter(items,this);
        recycler.setAdapter(adapter);
    }
}
