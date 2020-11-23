package com.example.proyectoelectiva3;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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

public class MantUsuario extends AppCompatActivity {
    private List<Usuarios> listPerson = new ArrayList<Usuarios>();
    ArrayAdapter<Usuarios> arrayAdapterPersona;

    EditText nomP, passwP, correoP, rollP;
    ListView lisVP;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Usuarios personaSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantusuario);

        nomP = findViewById(R.id.txt_nombrePersona);
        passwP = findViewById(R.id.txt_passwd);
        correoP = findViewById(R.id.txt_correo);
        rollP = findViewById(R.id.txt_roll);
        lisVP = findViewById(R.id.lv_datospersonas);

        inicialisarFirebase();
        listarDatos();
        lisVP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                personaSelected = (Usuarios) parent.getItemAtPosition(position);
                nomP.setText(personaSelected.getNombre());
                passwP.setText(personaSelected.getContraseña());
                correoP.setText(personaSelected.getCorreo());
                rollP.setText(personaSelected.getRoll());
            }
        });
    }

    private void listarDatos() {
        databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPerson.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Usuarios p = objSnaptshot.getValue(Usuarios.class);
                    listPerson.add(p);

                    arrayAdapterPersona = new ArrayAdapter<Usuarios>(MantUsuario.this, android.R.layout.simple_list_item_1, listPerson);
                    lisVP.setAdapter(arrayAdapterPersona);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicialisarFirebase() {
        FirebaseApp.initializeApp(MantUsuario.this);
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
        String nombre= nomP.getText().toString();
        String pass= passwP.getText().toString();
        String correo= correoP.getText().toString();
        String roll= rollP.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add: {
                if (nombre.equals("")||pass.equals("")||correo.equals("")||roll.equals("")){
                    validacion();
                }else{
                    Usuarios p = new Usuarios();
                    Direcciones d = new Direcciones();
                    List<Direcciones> lista = new ArrayList<>();
                    d.setCiudad("San Savador");
                    d.setDireccion("Col.Escalon");
                    lista.add(d);
                    p.setUid(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setContraseña(pass);
                    p.setCorreo(correo);
                    p.setRoll(roll);
                    p.setDirecciones(lista);
                    databaseReference.child("Usuarios").child(p.getUid()).setValue(p);
                    Toast.makeText(getApplicationContext(), "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            }
            case R.id.icon_save: {
                Usuarios p = new Usuarios();
                p.setUid(personaSelected.getUid());
                p.setNombre(nomP.getText().toString().trim());
                p.setContraseña(passwP.getText().toString().trim());
                p.setCorreo(correoP.getText().toString().trim());
                p.setRoll(rollP.getText().toString().trim());
                databaseReference.child("Usuarios").child(p.getUid()).setValue(p);
                Toast.makeText(this, "Guardar", Toast.LENGTH_LONG).show();
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
        nomP.setText("");
        passwP.setText("");
        correoP.setText("");
        rollP.setText("");
    }

    private void validacion(){
        String nombre= nomP.getText().toString();
        if (nombre.equals("")){
            nomP.setError("Required");
        }
    }
}
