package com.example.proyectoelectiva3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoriasFragment extends Fragment {


    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    List items = new ArrayList();

    public CategoriasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup categoriasFragment = (ViewGroup)inflater.inflate(R.layout.fragment_categorias, null);

        FirebaseDatabase database= FirebaseDatabase.getInstance();
        database.getReference().child("Categorias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.removeAll(items);
                for(DataSnapshot ds : snapshot.getChildren()) {
                    //String id = ds.getKey();

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
        recycler = (RecyclerView) categoriasFragment.findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);
        // Crear un nuevo adaptador
        adapter = new CategoriasAdapter(items,getContext(), getParentFragmentManager());
        recycler.setAdapter(adapter);
        return categoriasFragment;
    }
}