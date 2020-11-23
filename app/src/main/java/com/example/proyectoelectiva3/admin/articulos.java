package com.example.proyectoelectiva3.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectoelectiva3.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class articulos extends AppCompatActivity {
    EditText edtnombre,edtdescripcion,edtprecio,edtrebaja,edtdescuento,edtfecha,edtstock;
    Button agregar,cerrar,btnSelectArchivo;
    RadioButton rdbEstAct, rdbEstIna;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private static final int SEARCH_IMAGE_CODE = 1;
    FirebaseStorage storageFiles;
    StorageReference storageRef;
    ImageView imgCategoria;
    private static final String STORAGE_FOLDER_PRODUCTOS = "productos";
    Uri uriImageCate = null;
    String nombreImagenUpdateMode = null;
    String UidArt;
    Spinner spinerCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulos);
        edtnombre = findViewById(R.id.edtNombreArt);
        edtdescripcion = findViewById(R.id.edtDescripcion);
        edtprecio = findViewById(R.id.edtPrecio);
        edtrebaja = findViewById(R.id.edtRebaja);
        edtdescuento = findViewById(R.id.edtDescuento);
        edtfecha = findViewById(R.id.edtFecha);
        edtstock = findViewById(R.id.edtStock);

        rdbEstAct = findViewById(R.id.rdbActDetCat);
        rdbEstIna = findViewById(R.id.rdbInaDetCat);

         btnSelectArchivo = findViewById(R.id.btnSelectArchivo);
        imgCategoria = findViewById(R.id.imgFotoCat);

        agregar = findViewById(R.id.btnSaveArt);
        cerrar = findViewById(R.id.btnSalirDeArt);

        spinerCat = findViewById(R.id.spinerCategorias);

        Intent intent = getIntent();
        UidArt = intent.getStringExtra("idArt");

        cargarDatosSpinner(); //Nuevo codigo

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("articulos");


                String nombre = edtnombre.getText().toString().trim();
                String descripcion = edtdescripcion.getText().toString().trim();
                String precio = edtprecio.getText().toString().trim();
                String rebaja = edtrebaja.getText().toString().trim();
                String descuento = edtdescuento.getText().toString().trim();
                String fecha = edtfecha.getText().toString().trim();
                String stock = edtstock.getText().toString().trim();

                String estadoCat = rdbEstAct.isChecked() ? "activo" : "inactivo";

                articulosEntity art = new articulosEntity();

                art.setId(UUID.randomUUID().toString());
                art.setNombre(nombre);
                art.setDescripcion(descripcion);
                art.setPrecio(precio);
                art.setRebaja(rebaja);
                art.setDescuento(descuento);
                art.setFecha(fecha);
                art.setStock(stock);
                art.setEstado(estadoCat);

                reference.child(art.getId()).setValue(art);

                Toast.makeText(getBaseContext(),"Agregado"+nombre, Toast.LENGTH_LONG).show();

                limpiar();
                */

                //COdigo Eduaro Marroquin

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Productos");

                ProductoDummy prod = new ProductoDummy();
                prod.setId(UUID.randomUUID().toString());
                prod.setNombre("Producto 1");
                prod.setNombre("esta es una descirpcion xyz 123");
                prod.setPrecio("360.85");

                ProductoCategoriaDummy objCat = (ProductoCategoriaDummy)spinerCat.getSelectedItem();
                //Toast.makeText(getBaseContext(),"Id Spinner "+ objCat.getId() + " Texto Spinner" + objCat.getNombre(), Toast.LENGTH_LONG).show();
                prod.setCategoria(objCat);

                reference.child(prod.getId()).setValue(prod);

                Toast.makeText(getBaseContext(),"Agregado", Toast.LENGTH_LONG).show();




            }
        });
btnSelectArchivo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

    }
});


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

    private void cargarDatosSpinner()
    {
        //Simulo que los obtuve de la DB
        ProductoCategoriaDummy cat1 = new ProductoCategoriaDummy("4444", "Calzado");
        ProductoCategoriaDummy cat2 = new ProductoCategoriaDummy("5555", "Verduras Podridas");

        List<ProductoCategoriaDummy> spinArray = new ArrayList<>();
        spinArray.add(cat1);
        spinArray.add(cat2);

        ArrayAdapter<ProductoCategoriaDummy> adapter = new ArrayAdapter<ProductoCategoriaDummy>(this, android.R.layout.simple_spinner_dropdown_item, spinArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerCat.setAdapter(adapter);
    }


}