package com.example.proyectoelectiva3.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.proyectoelectiva3.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class DetalleCategoria extends AppCompatActivity {
    Button btnSalir, btnGuardar, btnSelectArchivo;
    EditText edtNombre, edtDescrip;
    RadioButton rdbEstAct, rdbEstIna;
    FirebaseDatabase dbFireBase;
    DatabaseReference dbRef;
    String uidCat;
    private static final int SEARCH_IMAGE_CODE = 1;
    FirebaseStorage storageFiles;
    StorageReference storageRef;
    ImageView imgCategoria;
    private static final String STORAGE_FOLDER_CATEGORIAS = "categorias";
    Uri uriImageCate = null;
    String nombreImagenUpdateMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_categoria);
        edtNombre = findViewById(R.id.edtNombreCat);
        edtDescrip = findViewById(R.id.edtDesCat);
        btnGuardar = findViewById(R.id.btnSaveArt);
        btnSalir = findViewById(R.id.btnSalirDeArt);
        rdbEstAct = findViewById(R.id.rdbActDetCat);
        rdbEstIna = findViewById(R.id.rdbInaDetCat);
        btnSelectArchivo = findViewById(R.id.btnSelectArchivo);
        imgCategoria = findViewById(R.id.imgFotoCat);

        inicializarFireBaseDB();
        initFireBaseStorage();

        Intent intent = getIntent();
        uidCat = intent.getStringExtra("idCat");
        if (!TextUtils.isEmpty(uidCat))
        {
            cargarDatosCategoria(uidCat);
        }
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombreCat = edtNombre.getText().toString();
                String msj;
                //solo se valida el nombre que sea requerido...

                if (TextUtils.isEmpty(nombreCat))
                {
                    edtNombre.setError("Nombre categoria es requerido");
                    edtNombre.requestFocus();
                }
                else if ( imgCategoria.getDrawable() == null)
                {
                    btnSelectArchivo.setError("Imagen de cateogria es requerido");
                    btnSelectArchivo.requestFocus();
                }
                else
                {
                    //Guardar
                    CategoriaEntity cat = new CategoriaEntity();
                    String imageNameCat = "";
                    if (TextUtils.isEmpty(uidCat))
                    {
                        msj = "Agregado exitosamente";
                        cat.setId(UUID.randomUUID().toString());
                    }
                    else
                    {
                        msj = "Modificado exitosamente";
                        cat.setId(uidCat);
                        //set image name
                        imageNameCat = nombreImagenUpdateMode;//variable Global
                    }

                    if (uriImageCate != null)
                    {
                        //Primero tratar de guardar la imagen en FireStorage..
                        imageNameCat = AdminUtils.cargarArchivoToFireBase(storageRef,uriImageCate,AdminUtils.getExtensionArchivo(uriImageCate,getContentResolver()), getBaseContext());
                        //Verificar si realmente existe...
                        StorageReference imageCate = storageRef.child(imageNameCat);
                        if (imageCate != null && imageCate.getName() != null)
                        {
                        }
                        else
                        {
                            imageNameCat = "";
                        }
                    }

                    if (TextUtils.isEmpty(imageNameCat))
                    {
                        Toast.makeText(getBaseContext(), "Ocurrio un error guardando la imagen. Intente mas tarde...", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String descripCat = edtDescrip.getText().toString();
                        String estadoCat = rdbEstAct.isChecked() ? "activo" : "inactivo";

                        cat.setNombre(nombreCat);
                        cat.setDescripcion(descripCat);
                        cat.setEstado(estadoCat);
                        cat.setImageName(imageNameCat);
                        dbRef.child("Categorias").child(cat.getId()).setValue(cat);

                        Toast.makeText(getBaseContext(), msj, Toast.LENGTH_LONG).show();
                        //Limpiar y salir
                        limpiar();
                        finish();
                    }
                }
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSelectArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarArchivosImgs();
            }
        });
    }

    private void inicializarFireBaseDB()
    {
        FirebaseApp.initializeApp(this);
        dbFireBase = FirebaseDatabase.getInstance();
        //dbFireBase.setPersistenceEnabled(true); //Esto para cuando se trabaja OffLine, se debe crear una clase y registrar en el manifest.xml..
        dbRef = dbFireBase.getReference();
    }

    private void initFireBaseStorage()
    {
        storageFiles = FirebaseStorage.getInstance();
        storageRef = storageFiles.getReference(STORAGE_FOLDER_CATEGORIAS);
    }

    private void limpiar(){
        edtNombre.setText("");
        edtDescrip.setText("");
        rdbEstAct.setChecked(true);
    }

    private void cargarDatosCategoria(String idCategoria)
    {
        dbRef.child("Categorias").child(idCategoria).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    String nombre = snapshot.child("nombre").getValue().toString().trim();
                    String descripcion = snapshot.child("descripcion").getValue().toString().trim();
                    String estado = snapshot.child("estado").getValue().toString().trim();

                    edtNombre.setText(nombre);
                    edtDescrip.setText(descripcion);
                    if ("activo".equals(estado))
                        rdbEstAct.setChecked(true);
                    else
                        rdbEstIna.setChecked(true);

                    nombreImagenUpdateMode = snapshot.child("imageName").getValue().toString().trim();

                    //cargar Imagen desde Storage
                    StorageReference imageCate = storageRef.child(snapshot.child("imageName").getValue().toString().trim());
                    if (imageCate != null && imageCate.getName() != null)
                    {
                        final long ONE_MEGABYTE = 1024 * 1024;
                        imageCate.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(400,400);
                                imgCategoria.setLayoutParams(params);
                                imgCategoria.setImageBitmap(bmp);
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void buscarArchivosImgs()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SEARCH_IMAGE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == SEARCH_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                uriImageCate = resultData.getData();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(400,400);
                imgCategoria.setLayoutParams(params);
                Picasso.get().load(uriImageCate).into(imgCategoria);
                btnSelectArchivo.setError(null);
            }
        }
    }
}