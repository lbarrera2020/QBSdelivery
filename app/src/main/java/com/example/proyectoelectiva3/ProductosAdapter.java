package com.example.proyectoelectiva3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoelectiva3.carrocompras.CarroCompraService;
import com.example.proyectoelectiva3.carrocompras.IStringResultProcess;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ProductosAdapter extends
        RecyclerView.Adapter<ProductosAdapter.MyItemViewHolder> {
    private List<productos> items2;
    private Context context2;
    String id_usuario;




    public ProductosAdapter(List<productos> items,Context context) {
        this.items2 = items;
        this.context2 = context;
    }



    public static class MyItemViewHolder extends RecyclerView.ViewHolder
    {
        // Campos respectivos de un item a ser referenciados
        public ImageView imagen;
        public TextView categorias;
        public TextView descripcion;
        public TextView precio;
        public TextView id;
        public RelativeLayout relativeLayout2;
        public Button btnAgregar;

        private FirebaseAuth auntenticacion;
        private DatabaseReference objdatabase;


        public MyItemViewHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagen2);
            categorias = itemView.findViewById(R.id.categorias2);
            descripcion =  itemView.findViewById(R.id.descripcion2);
            precio =  itemView.findViewById(R.id.precio);
            id =  itemView.findViewById(R.id.id);
            relativeLayout2 = itemView.findViewById(R.id.relativelayout2);
            btnAgregar= itemView.findViewById(R.id.button2);

            auntenticacion = FirebaseAuth.getInstance();
            objdatabase= FirebaseDatabase.getInstance().getReference();

        }
    }

    @Override
    public MyItemViewHolder onCreateViewHolder
            (ViewGroup parent, int viewType) {
        // Inflar layout que contiene los campos
        View v2 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productos_card, parent, false);
        return new MyItemViewHolder(v2);
    }

    @Override
    public void onBindViewHolder
            (final MyItemViewHolder holder2, final int position) {
        // Poblar con datos los campos referenciados


        // Glide.with(context).load(items.get(position).getIdImagen()).into(holder.imagen);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        StorageReference imageCate= storageRef.child("productos/"+items2.get(position).getIdImagen());
        final long ONE_MEGABYTE= 1024*1024;
        imageCate.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                holder2.imagen.setImageBitmap(bmp);
            }
        });



        holder2.categorias.setText(items2.get(position).getCategorias());
        holder2.descripcion.setText(items2.get(position).getDescripcion());
        holder2.precio.setText(items2.get(position).getPrecio());
        holder2.id.setText(items2.get(position).getId());

        id_usuario= holder2.auntenticacion.getCurrentUser().getUid();

        // Asignar un listener a boton carrito

        holder2.btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Toast.makeText(context2,"Producto:" +
                                items2.get(position).getId()+ "   Usuario: "+id_usuario,
                        Toast.LENGTH_SHORT).show();*/

                CarroCompraService serviceCart = new CarroCompraService();
                serviceCart.insertar_CarroCompras(id_usuario, items2.get(position).getId(), new IStringResultProcess() {
                    @Override
                    public void onCallBackSuccess(String result) {

                        if (result != null && !result.isEmpty() && !"OK".equals(result) && !"ERR".equals(result) && !"DUP".equals(result))
                        {
                            Toast.makeText(context2,"Agregado con exito, carrito creado", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            if ("OK".equals(result))
                            {
                                Toast.makeText(context2,"Agregado con exito", Toast.LENGTH_LONG).show();
                            }
                            else if ("DUP".equals(result))
                            {
                                Toast.makeText(context2,"No se puede agregar dos veces el mismo producto", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(context2,"Ocurrio un error al agregar...", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCallBackFail(String msjError) {
                        Toast.makeText(context2,"Ocurrio un error inesperado...", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });




    }


    @Override
    public int getItemCount() {
        return items2.size();
    }


}
