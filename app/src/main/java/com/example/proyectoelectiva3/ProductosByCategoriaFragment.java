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


public class ProductosByCategoriaFragment extends Fragment {

    private static final String ARG_PARAM1 = "IdCategoria";
    private static final String ARG_PARAM2 = "param2";

    private String idCategoria;
    private String mParam2;

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;


    List items = new ArrayList();

    public ProductosByCategoriaFragment() {
        // Required empty public constructor
    }

    public static ProductosByCategoriaFragment newInstance(String IdCategoria, String param2) {
        ProductosByCategoriaFragment fragment = new ProductosByCategoriaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, IdCategoria);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idCategoria = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup fragmentProductos = (ViewGroup)inflater.inflate(R.layout.fragment_productos_by_categoria, null);

        cargarProductosPorCategoria();

        // Obtener el Recycler
        recycler = (RecyclerView) fragmentProductos.findViewById(R.id.recicladorProducto);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);
        // Crear un nuevo adaptador
        adapter = new ProductosAdapter(items,getContext());
        recycler.setAdapter(adapter);

        return fragmentProductos;
    }

    private void cargarProductosPorCategoria()
    {
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        database.getReference().child("Productos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.removeAll(items);
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String id_= ds.child("categoria").child("id").getValue(String.class);

                    if(id_.equals(idCategoria)){
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

    }
}