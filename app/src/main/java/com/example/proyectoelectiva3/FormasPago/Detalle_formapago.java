package com.example.proyectoelectiva3.FormasPago;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoelectiva3.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class Detalle_formapago extends AppCompatActivity {

    Button btnSalir, btnGuardar;
    EditText edtNombre, edtDescrip;
    RadioButton rdbEstAct, rdbEstIna;
    FirebaseDatabase dbFireBase;
    DatabaseReference dbRef;
    String uidFp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_formapago);

        edtNombre = findViewById(R.id.edtNombreFp);
        edtDescrip = findViewById(R.id.edtDesFp);
        btnGuardar = findViewById(R.id.btnSaveDetFp);
        btnSalir = findViewById(R.id.btnSalirDetFp);
        rdbEstAct = findViewById(R.id.rdbActDetFp);
        rdbEstIna = findViewById(R.id.rdbInaDetFp);

        inicializarFireBaseDB();

        Intent intent = getIntent();
        uidFp = intent.getStringExtra("idFp");
        if (!TextUtils.isEmpty(uidFp))
        {
            cargarDatosFormaPago(uidFp);
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreFp = edtNombre.getText().toString();
                String msj;
                //solo se valida el nombre que sea requerido...
                if (TextUtils.isEmpty(nombreFp))
                {
                    edtNombre.setError("Nombre forma de pago es requerido");
                    edtNombre.requestFocus();
                }
                else
                {
                    String descripFp = edtDescrip.getText().toString();
                    String estadoFp = rdbEstAct.isChecked() ? "activo" : "inactivo";

                    //Guardar
                    FormasPagoEntity fp = new FormasPagoEntity();
                    if (TextUtils.isEmpty(uidFp))
                    {
                        msj = "Agregado exitosamente";
                        fp.setId(UUID.randomUUID().toString());
                    }
                    else
                    {
                        msj = "Modificado exitosamente";
                        fp.setId(uidFp);
                    }
                    fp.setNombre(nombreFp);
                    fp.setDescripcion(descripFp);
                    fp.setEstado(estadoFp);
                    dbRef.child("FormasPago").child(fp.getId()).setValue(fp);

                    Toast.makeText(getBaseContext(), msj, Toast.LENGTH_LONG).show();
                    //Limpiar y salir
                    limpiar();
                    finish();
                }
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void limpiar() {
        edtNombre.setText("");
        edtDescrip.setText("");
        rdbEstAct.setChecked(true);
    }



    private void cargarDatosFormaPago(String idFormaPago) {
        dbRef.child("FormasPago").child(idFormaPago).addValueEventListener(new ValueEventListener() {
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicializarFireBaseDB() {
        FirebaseApp.initializeApp(this);
        dbFireBase = FirebaseDatabase.getInstance();
        //dbFireBase.setPersistenceEnabled(true); //Ya se define en la clase MyFireBaseApp..
        dbRef = dbFireBase.getReference();
    }
}