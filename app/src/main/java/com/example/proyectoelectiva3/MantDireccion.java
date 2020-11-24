package com.example.proyectoelectiva3;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.proyectoelectiva3.admin.Direcciones;
import com.example.proyectoelectiva3.admin.Usuarios;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MantDireccion extends AppCompatActivity {
    private List<Usuarios> listPerson = new ArrayList<Usuarios>();
    ArrayAdapter<Usuarios> arrayAdapterPersona;

    EditText mun, ciudad, direc;
    Spinner dep;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Usuarios personaSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantdireccion);
        dep = findViewById(R.id.txt_dep);
        mun = findViewById(R.id.txt_mun);
        ciudad = findViewById(R.id.txt_ciudad);
        direc = findViewById(R.id.txt_direc);

        String[] arraySpinner = new String[] { "1", "2", "3", "4", "5", "6", "7" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); dep.setAdapter(adapter);

        inicialisarFirebase();
//        listarDatos();
//        lisVP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                personaSelected = (Usuarios) parent.getItemAtPosition(position);
//                nomP.setText(personaSelected.getNombre());
//                passwP.setText(personaSelected.getContraseña());
//                correoP.setText(personaSelected.getCorreo());
//                rollP.setText(personaSelected.getRoll());
//            }
//        });
    }
//    private void listarDatos() {
//        databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                listPerson.clear();
//                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
//                    Usuarios p = objSnaptshot.getValue(Usuarios.class);
//                    listPerson.add(p);
//
//                    arrayAdapterPersona = new ArrayAdapter<Usuarios>(MantUsuario.this, android.R.layout.simple_list_item_1, listPerson);
//                    lisVP.setAdapter(arrayAdapterPersona);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void inicialisarFirebase() {
        FirebaseApp.initializeApp(MantDireccion.this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String depa= "";
        String muni= mun.getText().toString();
        String ciuda= ciudad.getText().toString();
        String direcc= direc.getText().toString();
        String id= getIntent().getExtras().getString("uid");

        switch (item.getItemId()){
            case R.id.icon_add: {
                if (depa.equals("")||muni.equals("")||ciuda.equals("")||direcc.equals("")){
                    validacion();
                }else{
                    Usuarios p = new Usuarios();
                    Direcciones d = new Direcciones();
                    List<Direcciones> lista = new ArrayList<>();
                    d.setCiudad(depa);
                    d.setCiudad(muni);
                    d.setCiudad(ciuda);
                    d.setDireccion(direcc);
                    lista.add(d);
//                    p.setUid(UUID.randomUUID().toString());
//                    p.setNombre("Luis Barrera Prueba");
//                    p.setContraseña("123");
//                    p.setCorreo("barrera@gmail.com");
//                    p.setRol("repartidor");
                    p.setDirecciones(lista);
                    databaseReference.child("Usuarios").child(id).setValue(p);
                    Toast.makeText(getApplicationContext(), "Direccion Agregada", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            }
            case R.id.icon_save: {
                Usuarios p = new Usuarios();
                Direcciones d = new Direcciones();
                List<Direcciones> lista = new ArrayList<>();
                p.setUid(personaSelected.getUid());
                d.setDepartamento("");
                d.setMunicipio(mun.getText().toString().trim());
                d.setCiudad(ciudad.getText().toString().trim());
                d.setDireccion(direc.getText().toString().trim());
//                p.setNombre(nomP.getText().toString().trim());
//                p.setContraseña(passwP.getText().toString().trim());
//                p.setCorreo(correoP.getText().toString().trim());
//                p.setRoll(rollP.getText().toString().trim());
                lista.add(d);
                p.setDirecciones(lista);
                databaseReference.child("Usuarios").child(p.getUid()).setValue(p);
                Toast.makeText(this, "Direccion Guardada", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            case R.id.icon_delete: {
                Usuarios p = new Usuarios();
                p.setUid(personaSelected.getUid());
                databaseReference.child("Usuarios").child(p.getUid()).removeValue();
                Toast.makeText(this, "Borrar", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default:break;
        }
        return true;
    }

    private void limpiarCajas() {
//        dep.setText("");
        mun.setText("");
        ciudad.setText("");
        direc.setText("");
    }

    private void validacion(){
        String direcc= direc.getText().toString();
        if (direcc.equals("")){
            direc.setError("Required");
        }
    }
}
