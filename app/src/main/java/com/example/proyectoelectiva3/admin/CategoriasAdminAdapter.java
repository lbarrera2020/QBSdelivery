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

public class CategoriasAdminAdapter extends ArrayAdapter<CategoriaEntity> {

    private Context mContext;
    private int mResource;
    private static final String STORAGE_FOLDER_CATEGORIAS = "categorias";

    public CategoriasAdminAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CategoriaEntity> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);

        final ImageView imageView = convertView.findViewById(R.id.imagCate);
        TextView tvNombreCat = convertView.findViewById(R.id.nameCat);
        TextView tvDesCate = convertView.findViewById(R.id.descripCat);


        StorageReference storageRef = FirebaseStorage.getInstance().getReference(STORAGE_FOLDER_CATEGORIAS);
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
        tvNombreCat.setText(getItem(position).getNombre());
        tvDesCate.setText(getItem(position).getDescripcion());

        return convertView;
    }
}
