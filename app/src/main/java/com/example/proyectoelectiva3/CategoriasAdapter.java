package com.example.proyectoelectiva3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class CategoriasAdapter extends
        RecyclerView.Adapter<CategoriasAdapter.MyItemViewHolder> {
    private List<categorias> items;
    private Context context;
    private FragmentManager fm;

    public CategoriasAdapter(List<categorias> items,Context context, FragmentManager fm) {
        this.items = items;
        this.context = context;
        this.fm = fm;
    }

    public static class MyItemViewHolder extends RecyclerView.ViewHolder
    {
        // Campos respectivos de un item a ser referenciados
        public ImageView imagen;
        public TextView categorias;
        public TextView descripcion;
        public TextView id;
        public RelativeLayout relativeLayout;

        public MyItemViewHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagen);
            categorias = itemView.findViewById(R.id.categorias);
            descripcion =  itemView.findViewById(R.id.descripcion);
            id =  itemView.findViewById(R.id.id);
            relativeLayout = itemView.findViewById(R.id.relativelayout);
        }
    }

    @Override
    public MyItemViewHolder onCreateViewHolder
            (ViewGroup parent, int viewType) {
        // Inflar layout que contiene los campos
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categorias_card, parent, false);
        return new MyItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder
            (final MyItemViewHolder holder, final int position) {
        // Poblar con datos los campos referenciados


        // Glide.with(context).load(items.get(position).getIdImagen()).into(holder.imagen);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        StorageReference imageCate= storageRef.child("categorias/"+items.get(position).getIdImagen());
        final long ONE_MEGABYTE= 1024*1024;
        imageCate.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                holder.imagen.setImageBitmap(bmp);
            }
        });


        // holder.imagen.setImageResource(items.get(position).getIdImagen());
        holder.categorias.setText(items.get(position).getCategorias());
        holder.descripcion.setText(items.get(position).getDescripcion());
        holder.id.setText(items.get(position).getId());

        // Asignar un listener a relativelayout

        holder.relativeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                /*
                Intent i= new Intent(context, RvProductos.class);
                i.putExtra("categoria",items.get(position).getId());
                context.startActivity(i);
                */

                //FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.nav_host_fragment, ProductosByCategoriaFragment.newInstance(items.get(position).getId(),"")  )
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
}
