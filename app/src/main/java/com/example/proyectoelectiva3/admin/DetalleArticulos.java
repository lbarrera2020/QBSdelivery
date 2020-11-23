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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.os.Bundle;

import com.example.proyectoelectiva3.R;

public class DetalleArticulos extends AppCompatActivity {
    EditText edtnombre,edtdescripcion,edtprecio,edtrebaja,edtdescuento,edtfecha,edtstock
          ;
    Button agregar,btnSalir,btnSelectArchivo;
    RadioButton rdbEstAct, rdbEstIna;
    TextView tvData;
    Spinner spCat;
    FirebaseDatabase dbFireBase;
    DatabaseReference dbRef;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseReference ref;
    String uidArt;
    private static final int SEARCH_IMAGE_CODE = 1;
    FirebaseStorage storageFiles;
    StorageReference storageRef;
    ImageView imgCategoria;
    private static final String STORAGE_FOLDER_PRODUCTOS = "productos";
    Uri uriImageCate = null;
    String nombreImagenUpdateMode = null;
    //String uidArt;

    String niño;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_articulos);
        edtnombre = findViewById(R.id.edtNombreArt);
        edtdescripcion = findViewById(R.id.edtDescripcion);
        edtprecio = findViewById(R.id.edtPrecio);
        edtrebaja = findViewById(R.id.edtRebaja);
        edtdescuento = findViewById(R.id.edtDescuento);
        edtfecha = findViewById(R.id.edtFecha);
        edtstock = findViewById(R.id.edtStock);
       // edtcantidad  = findViewById(R.id.edtCantidad);

        rdbEstAct = findViewById(R.id.rdbActDetCat);
        rdbEstIna = findViewById(R.id.rdbInaDetCat);

        btnSelectArchivo = findViewById(R.id.btnSelectArchivo);
        imgCategoria = findViewById(R.id.imgFotoCat);

        agregar = findViewById(R.id.btnSaveArt);
        btnSalir = findViewById(R.id.btnSalirDeArt);

        tvData = findViewById(R.id.tvData);
        spCat = findViewById(R.id.spinnerCat);

        ref = FirebaseDatabase.getInstance().getReference();

        inicializarFireBaseDB();
        initFireBaseStorage();


        Intent intent = getIntent();
        uidArt = intent.getStringExtra("idArt");
        cargarCat();
        if (!TextUtils.isEmpty(uidArt))
        {
            cargarDatosArticulos(uidArt);
        }
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String nombre = edtnombre.getText().toString().trim();


                String msj;
                //solo se valida el nombre que sea requerido...
                if(TextUtils.isEmpty(nombre)){
                    edtnombre.setError("Ingrese nombre");
                    edtnombre.requestFocus();
                }


                 else if ( imgCategoria.getDrawable() == null)
                {
                    btnSelectArchivo.setError("Imagen de articulos es requerido");
                    btnSelectArchivo.requestFocus();
                }
                else {
                    //Guardar
                    articulosEntity art = new articulosEntity();
                    String imageNameCat = "";

                    if (TextUtils.isEmpty(uidArt))
                    {
                        msj = "Agregado exitosamente";
                        art.setId(UUID.randomUUID().toString());
                    }   else
                    {
                        msj = "Modificado exitosamente";
                        art.setId(uidArt);
                        //set image name
                        imageNameCat = nombreImagenUpdateMode;//variable Global
                    }
                    if (uriImageCate != null) {
                        //Primero tratar de guardar la imagen en FireStorage..
                        imageNameCat = AdminUtils.cargarArchivoToFireBase(storageRef, uriImageCate, AdminUtils.getExtensionArchivo(uriImageCate, getContentResolver()), getBaseContext());
                        //Verificar si realmente existe...
                        StorageReference imageCate = storageRef.child(imageNameCat);
                        if (imageCate != null && imageCate.getName() != null) {
                        } else {
                            imageNameCat = "";
                        }
                    }
                        if (TextUtils.isEmpty(imageNameCat))
                        {
                            Toast.makeText(getBaseContext(), "Ocurrio un error guardando la imagen. Intente mas tarde...", Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("Productos");
                            //reference = rootNode.getReference("Arti");
                           // String nombre = edtnombre.getText().toString().trim();
                            String descripcion = edtdescripcion.getText().toString().trim();
                            String precio = edtprecio.getText().toString().trim();
                            String rebaja = edtrebaja.getText().toString().trim();
                            String descuento = edtdescuento.getText().toString().trim();
                            String fecha = edtfecha.getText().toString().trim();
                            String stock = edtstock.getText().toString().trim();
                            String spiner = spCat.toString().trim();
                        //    String cantidad = edtcantidad.getText().toString().trim();
                            String estadoCat = rdbEstAct.isChecked() ? "activo" : "inactivo";
                          //  String imageNameCat="";

                            art.setNombre(nombre);
                            art.setDescripcion(descripcion);
                            art.setPrecio(precio);
                            art.setRebaja(rebaja);
                            art.setDescuento(descuento);
                            art.setFecha(fecha);
                            art.setStock(stock);
                            art.setEstado(estadoCat);
                        //    art.setCantidad(cantidad);
                            art.setImageName(imageNameCat);
                            catSpinner objCat = (catSpinner)spCat.getSelectedItem();
                            art.setCategoria(objCat);


                            reference.child(art.getId()).setValue(art);

                           // dbRef.child("Productos").child(art.getNombre()).child(art.getId()).setValue(art);

                            Toast.makeText(getBaseContext(),"Agregado "+nombre, Toast.LENGTH_LONG).show();
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
               // guardar();
            }
        });

        btnSelectArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarArchivosImgs();
            }
        });
    }
    private void guardar(){




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
        storageRef = storageFiles.getReference(STORAGE_FOLDER_PRODUCTOS);
    }
    private void limpiar()
    {
        edtnombre.setText("");
        edtrebaja.setText("");
        edtprecio.setText("");
        edtdescuento.setText("");
        edtdescripcion.setText("");
        edtfecha.setText("");
        edtstock.setText("");
        rdbEstAct.setChecked(true);


    }
    private void cargarDatosArticulos(String idCategoria)
    {
        dbRef.child("Productos").child(idCategoria).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    String nombre = snapshot.child("nombre").getValue().toString().trim();
                    String descripcion = snapshot.child("descripcion").getValue().toString().trim();
                    String estado = snapshot.child("estado").getValue().toString().trim();

                    edtnombre.setText(nombre);
                    edtdescripcion.setText(descripcion);
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

   public void cargarCat(){
        final List<catSpinner> category = new ArrayList<>();
        ref.child("Categorias").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        String id = ds.getKey();
                        String nombre = ds.child("nombre").getValue().toString();
                        category.add(new catSpinner(id,nombre));
                    }
                    ArrayAdapter<catSpinner> arrayAdapter=new ArrayAdapter<>(DetalleArticulos.this, android.R.layout.simple_dropdown_item_1line,category);
                    spCat.setAdapter(arrayAdapter);
                    spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                          //  niño = adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}