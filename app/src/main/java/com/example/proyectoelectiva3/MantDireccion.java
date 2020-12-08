package com.example.proyectoelectiva3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.proyectoelectiva3.admin.Departamento;
import com.example.proyectoelectiva3.admin.Direcciones;
import com.example.proyectoelectiva3.admin.Usuarios;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MantDireccion extends AppCompatActivity {
    private List<Direcciones> listDirec = new ArrayList<Direcciones>();
    ArrayAdapter<Direcciones> arrayAdapterDirecciones;

    EditText mun, ciudad, direc;
    Spinner dep;
    Intent intent = getIntent();
    String id,depSelec;
    ListView lisVP;
    Button btnContinuar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Direcciones direccionSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantdireccion);
        dep = findViewById(R.id.spinnerDep);
        mun = findViewById(R.id.txt_mun);
        ciudad = findViewById(R.id.txt_ciudad);
        direc = findViewById(R.id.txt_direc);
        Bundle datos= getIntent().getExtras();
        id= datos.getString("uid");
        ciudad.setText(id);
        lisVP = findViewById(R.id.lv_datospersonas);
        btnContinuar = findViewById(R.id.btnContinuar);

        String[] arraySpinner = new String[] { "La Libertad", "San Salvador" };
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); dep.setAdapter(adapter);
        inicialisarFirebase();
        listarDatos();
        lisVP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                direccionSelected = (Direcciones) parent.getItemAtPosition(position);
                mun.setText(direccionSelected.getMunicipio());
                ciudad.setText(direccionSelected.getCiudad());
                direc.setText(direccionSelected.getDireccion());
            }
        });
        dep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                depSelec = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),depSelec, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void listarDatos() {
        databaseReference.child("Usuarios").child(id).child("direcciones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listDirec.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Direcciones p = objSnaptshot.getValue(Direcciones.class);
                    listDirec.add(p);

                    arrayAdapterDirecciones = new ArrayAdapter<Direcciones>(MantDireccion.this, android.R.layout.simple_list_item_1, listDirec);
                    lisVP.setAdapter(arrayAdapterDirecciones);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
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
        String depa= depSelec;
        String muni= mun.getText().toString();
        String ciuda= ciudad.getText().toString();
        String direcc= direc.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add: {
                if (depa.equals("")||muni.equals("")||ciuda.equals("")||direcc.equals("")){
                    validacion();
                }else{
                    Usuarios p = new Usuarios();
                    Direcciones d = new Direcciones();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("Departamento",depa);
                    result.put("Municipio",muni);
                    result.put("Ciudad",ciuda);
                    result.put("Direccion",direcc);
                    databaseReference.child("Usuarios").child(id).child("direcciones").child(UUID.randomUUID().toString()).setValue(result);
                    Toast.makeText(getApplicationContext(), "Direccion Agregada", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            }
            case R.id.icon_save: {
                Usuarios p = new Usuarios();
                Direcciones d = new Direcciones();
                List<Direcciones> lista = new ArrayList<>();
//                p.setUid(direccionSelected.get());
                d.setDepartamento("");
                d.setMunicipio(mun.getText().toString().trim());
                d.setCiudad(ciudad.getText().toString().trim());
                d.setDireccion(direc.getText().toString().trim());
                lista.add(d);
                p.setDirecciones(lista);
                databaseReference.child("Usuarios").child(p.getUid()).setValue(p);
                Toast.makeText(this, "Direccion Guardada", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            case R.id.icon_delete: {
                Usuarios p = new Usuarios();
//                p.setUid(personaSelected.getUid());
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

    public void Continuar(View view) {
        Intent i= new Intent(getApplicationContext(),principal_navigation.class);
        startActivity(i);
        finish();
    }
}
