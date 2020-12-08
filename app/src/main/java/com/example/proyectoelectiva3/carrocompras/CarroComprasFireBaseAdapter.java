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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class CarroComprasFireBaseAdapter  extends FirebaseRecyclerAdapter<ItemCarroCompraModel, CarroComprasFireBaseAdapter.MyItemFBViewHolder> {

    private Context context;
    boolean readOnly;
    private CarroComprasRepository repoDB;
    private CarroCompraService service;

    public CarroComprasFireBaseAdapter(@NonNull FirebaseRecyclerOptions<ItemCarroCompraModel> options, boolean readOnly, Context context) {
        super(options);
        this.readOnly = readOnly;
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyItemFBViewHolder holder, final int position, @NonNull final ItemCarroCompraModel model) {

        repoDB = new CarroComprasRepository();
        service = new CarroCompraService();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference(AdminUtils.STORAGE_FOLDER_PRODUCTOS);
        String nombreImage = model.getNombreImagen();
        String idProducto =  model.getIdProducto();

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

        holder.nombre.setText(model.getNombreProducto());
        holder.precio.setText("$" + AdminUtils.formatNumber2Decimal(model.getPrecio()));
        holder.cantidad.setText("" + model.getCantidad());
        holder.subTotal.setText("$" + AdminUtils.formatNumber2Decimal(model.getSubTotal()));

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
                    service.updateCarroCompras(model.getIdCliente(), model.getIdItem(), (cantidadItem + 1), new IStringResultProcess() {
                        @Override
                        public void onCallBackSuccess(String msjOK) {
                            if (!"OK".equals(msjOK))
                            {
                                Toast.makeText(context,msjOK, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCallBackFail(String msjError) {
                            Toast.makeText(context,"Error inesperado al actualizar carro compras, " + msjError, Toast.LENGTH_LONG).show();
                        }
                    });
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
                        service.updateCarroCompras(model.getIdCliente(), model.getIdItem(),(cantidadItem-1), new IStringResultProcess() {
                            @Override
                            public void onCallBackSuccess(String msjOK) {
                                if (!"OK".equals(msjOK))
                                {
                                    Toast.makeText(context,msjOK, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCallBackFail(String msjError) {
                                Toast.makeText(context,"Error inesperado al actualizar carro compras, " + msjError, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });

        //TODO: Escenario que elimino el ultimo item, se debe eliminar tambien todo el carrito !!
        holder.btnEliminarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Eliminar el item del carro
                service.eliminarItemCarroCompras(model, new IStringResultProcess() {
                    @Override
                    public void onCallBackSuccess(String result) {
                        if ("OK".equals(result))
                        {

                            //Mandar a llamar rutina que verifica si eliminar  el carrito, si es el caso no seguir con la actualizacion...
                            repoDB.getCarroComprasByCliente(model.getIdCliente(), new IGetSingleObject<CarroComprasModel>() {
                                @Override
                                public void onCallBackSuccess(CarroComprasModel car) {
                                    if (car != null)
                                    {
                                        if (car.getItems()!= null && !car.getItems().isEmpty())
                                        {
                                            //Actualizar los totales del carro
                                            service.updateCarroCompras(model.getIdCliente(), "", 0, new IStringResultProcess() {
                                                @Override
                                                public void onCallBackSuccess(String msjOK) {
                                                    if (!"OK".equals(msjOK))
                                                    {
                                                        Toast.makeText(context,msjOK, Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                @Override
                                                public void onCallBackFail(String msjError) {
                                                    Toast.makeText(context,"Error inesperado al actualizar carro compras, " + msjError, Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                        else //Eliminar el carro de compras dado que ya no hay items..
                                        {
                                            repoDB.eliminmarCarroCompras(model.getIdCliente(), new IStringResultProcess() {
                                                @Override
                                                public void onCallBackSuccess(String result) {
                                                    Toast.makeText(context,result, Toast.LENGTH_LONG).show();
                                                }

                                                @Override
                                                public void onCallBackFail(String msjError) {
                                                    Toast.makeText(context,"Error inesperado al eliminar carro compras, " + msjError, Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }
                                    else
                                        Toast.makeText(context,"Error al obtener carro compras", Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void onCallBackFail(String msjError) {
                                    Toast.makeText(context,"Error inesperado al obtener carro compras, " + msjError, Toast.LENGTH_LONG).show();
                                }
                            });

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

    @NonNull
    @Override
    public MyItemFBViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { ;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_card, parent, false);
        return new CarroComprasFireBaseAdapter.MyItemFBViewHolder(v, readOnly);
    }

    class MyItemFBViewHolder extends RecyclerView.ViewHolder
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

        public MyItemFBViewHolder(@NonNull View itemView, boolean readOnly) {
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

}
