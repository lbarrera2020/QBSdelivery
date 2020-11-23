package com.example.proyectoelectiva3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RvProductos extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    String categoria;

    List items = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_productos);

        Bundle datos= getIntent().getExtras();
        categoria= datos.getString("categoria");


        FirebaseDatabase database= FirebaseDatabase.getInstance();
        database.getReference().child("Productos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.removeAll(items);
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String id_= ds.child("categoria").child("id").getValue(String.class);

                    if(id_.equals(categoria)){
                        String categoria = ds.child("nombre").getValue(String.class);
                        String descripcion = ds.child("descripcion").getValue(String.class);
                        String imagen = ds.child("imageName").getValue(String.class);
                        String precio = ds.child("precio").getValue(String.class);
                        String id = ds.child("id").getValue(String.class);

                        items.add(new productos(imagen, categoria, descripcion,"$"+precio,id));
                    }

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.recicladorProducto);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);
        // Crear un nuevo adaptador
        adapter = new ProductosAdapter(items,this);
        recycler.setAdapter(adapter);
    }
}
