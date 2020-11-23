package com.example.proyectoelectiva3.admin;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proyectoelectiva3.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class productosAdminAdapter extends ArrayAdapter<articulosEntity> {
private Context mContext;
private int mResourse;
    private static final String STORAGE_FOLDER_PRODUCTOS = "productos";


    public productosAdminAdapter(@NonNull Context context, int resource, @NonNull List<articulosEntity> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResourse = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResourse, parent, false);

        final ImageView imageView = convertView.findViewById(R.id.imagArt);
        TextView tvNombreArt = convertView.findViewById(R.id.nameArt);
        TextView tvDesArt = convertView.findViewById(R.id.descripArt);


        StorageReference storageRef = FirebaseStorage.getInstance().getReference(STORAGE_FOLDER_PRODUCTOS);
        String nombreImage = getItem(position).getImageName();
        if (!TextUtils.isEmpty(nombreImage))
        {
            StorageReference imageCate = storageRef.child(nombreImage);
            if (imageCate != null && imageCate.getName() != null)
            {
                imageCate.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageView);
                    }
                });
            }
        }
        //imageView.setImageBitmap(getItem(position).getBitMapImage());
        tvNombreArt.setText(getItem(position).getNombre());
        tvDesArt.setText(getItem(position).getDescripcion());

        return convertView;
    }
}


