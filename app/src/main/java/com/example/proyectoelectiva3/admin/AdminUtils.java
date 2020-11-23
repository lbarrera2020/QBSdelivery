package com.example.proyectoelectiva3.admin;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminUtils {
    public static final String NOMBRE_TABLA_DOCUMENTO_CATEGORIAS = "Categorias";
    public static final String STORAGE_FOLDER_PRODUCTOS = "productos";

    public static String getExtensionArchivo(Uri uri, ContentResolver conRe)
    {
        ContentResolver resolver = conRe;
        MimeTypeMap mimeMap = MimeTypeMap.getSingleton();
        return  mimeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    public static String cargarArchivoToFireBase(StorageReference stRef, Uri uriFile, String extension, final Context context)
    {
        String nameFile = System.currentTimeMillis() + "." + extension;
        StorageReference fileRef = stRef.child(nameFile);

        fileRef.putFile(uriFile)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    }
                });

        return nameFile;
    }

    public static String getNowDateToString()
    {
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
        return formater.format(new Date());
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String formatNumber2Decimal(Double value)
    {
        DecimalFormat df2 = new DecimalFormat("#.##");
        return df2.format(value);
    }

}
