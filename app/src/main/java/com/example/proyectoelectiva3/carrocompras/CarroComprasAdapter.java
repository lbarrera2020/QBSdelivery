package com.example.proyectoelectiva3.carrocompras;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoelectiva3.R;
import com.example.proyectoelectiva3.admin.AdminUtils;
import com.example.proyectoelectiva3.admin.articulosEntity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarroComprasAdapter extends RecyclerView.Adapter<CarroComprasAdapter.MyItemViewHolder>{

    private List<ItemCarroCompraModel> items;
    private Context context;
    private CarroComprasRepository repoDB;
    boolean readOnly;
    private CarroCompraService service;

    public CarroComprasAdapter(List<ItemCarroCompraModel> items, Context context, boolean readOnly)
    {
        this.items = items;
        this.context = context;
        this.readOnly = readOnly;
    }

    public static class MyItemViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imagen;
        public TextView nombre;
        public TextView precio;
        public TextView descuento;
        public TextView rebaja;
        public TextView cantidad;
        public ImageView btnIncremento;
        public ImageView btnDecremento;
        public TextView subTotal;
        public ImageView btnEliminarItem;

        public MyItemViewHolder(@NonNull View itemView, boolean readOnly) {
            super(itemView);

            imagen = itemView.findViewById(R.id.imgItemCart);
            nombre = itemView.findViewById(R.id.nameItemCart);
            precio = itemView.findViewById(R.id.priceItemCart);
            descuento = itemView.findViewById(R.id.descuentoItemCart);
            rebaja = itemView.findViewById(R.id.rebajaItemCart);
            cantidad = itemView.findViewById(R.id.quantityItemCart);
            btnIncremento = itemView.findViewById(R.id.btnIncreaseItemCart);
            btnDecremento = itemView.findViewById(R.id.btnDecreaseItemCart);
            subTotal = itemView.findViewById(R.id.subTotalItemCart);
            btnEliminarItem = itemView.findViewById(R.id.btnDeleteItemCart);

            if (readOnly)
            {
                btnEliminarItem.setVisibility(View.GONE);
                btnIncremento.setEnabled(false);
                btnDecremento.setEnabled(false);
            }
            else
            {
                btnEliminarItem.setVisibility(View.VISIBLE);
                btnIncremento.setEnabled(true);
                btnDecremento.setEnabled(true);
            }

        }
    }

    @NonNull
    @Override
    public MyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_card, parent, false);
        return new MyItemViewHolder(v, readOnly);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyItemViewHolder holder, final int position) {

        repoDB = new CarroComprasRepository();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference(AdminUtils.STORAGE_FOLDER_PRODUCTOS);
        Log.i("CartAdapter","onBindViewHolder, position: " + position +  " Count Items: " + items.size());
        String nombreImage = items.get(position).getNombreImagen();
        String idProducto =  items.get(position).getIdProducto();

        if (!TextUtils.isEmpty(nombreImage))
        {
            StorageReference imageProd = storageRef.child(nombreImage);
            if (imageProd != null && imageProd.getName() != null)
            {
                imageProd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(holder.imagen);
                    }
                });
            }
        }

        holder.nombre.setText(items.get(position).getNombreProducto());
        holder.precio.setText("$" + AdminUtils.formatNumber2Decimal(items.get(position).getPrecio()));
        holder.cantidad.setText("" + items.get(position).getCantidad());
        holder.subTotal.setText("$" + AdminUtils.formatNumber2Decimal(items.get(position).getSubTotal()));

        //get informacion del producto
        repoDB.getDetalleProducto(idProducto, new IGetSingleObject<articulosEntity>() {
            @Override
            public void onCallBackSuccess(articulosEntity p) {
                if (p != null)
                {
                    holder.rebaja.setText("$" + p.getRebaja());
                    holder.descuento.setText(AdminUtils.formatNumber2Decimal(Double.parseDouble(p.getDescuento())*100) + "%");
                }
            }
            @Override
            public void onCallBackFail(String msjError) {

            }
        });

        //Acciones de los botones
        holder.btnIncremento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(holder.cantidad.getText()))
                {
                    int cantidadItem = Integer.parseInt(holder.cantidad.getText().toString());
                    holder.cantidad.setText("" + (cantidadItem + 1));
                    //Validar si hay stock para el producto
                    //Actualizar el carro de compras, los items y el total...
                }
            }
        });

        holder.btnDecremento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(holder.cantidad.getText()))
                {
                    int cantidadItem = Integer.parseInt(holder.cantidad.getText().toString());
                    if ((cantidadItem-1) > 0 )
                    {
                        holder.cantidad.setText("" + (cantidadItem-1));
                        //Actualizar el carro de compras, los items y el total...
                    }
                }
            }
        });

        holder.btnEliminarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service = new CarroCompraService();

                //Eliminar el item del carro
                service.eliminarItemCarroCompras((ItemCarroCompraModel) items.get(position), new IStringResultProcess() {
                    @Override
                    public void onCallBackSuccess(String result) {
                        if ("OK".equals(result))
                        {
                            Toast.makeText(context,"Item removido con exito", Toast.LENGTH_LONG).show();
                            notifyItemDeleted(position);
                            //Actualizar los totales del carro
                        }
                        else
                            Toast.makeText(context,result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCallBackFail(String msjError) {
                        Toast.makeText(context,"Error inesperado al remover item, " + msjError, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return  items.size();
    }

    private void notifyItemDeleted(int position)
    {
        Log.i("notifyItemDel","Inicio");
        this.notifyItemRemoved(position);
        Log.i("notifyItemDel","Fin");
    }

}