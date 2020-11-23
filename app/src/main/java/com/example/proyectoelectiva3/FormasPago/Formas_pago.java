package com.example.proyectoelectiva3.FormasPago;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoelectiva3.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Formas_pago extends AppCompatActivity {

    ListView lvFormasPago;
    private List<FormasPagoEntity> listFormasPago = new ArrayList<FormasPagoEntity>();
    ArrayAdapter<FormasPagoEntity> arrayAdapterFormasPago;
    FirebaseDatabase dbFireBase;
    DatabaseReference dbRef;
    int posicion = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formas_pago);

        lvFormasPago = findViewById(R.id.livFormasPago);

        registerForContextMenu(lvFormasPago);
        inicializarFireBaseDB();
        cargarDatosFormasPago();

    }

    private void cargarDatosFormasPago() {

        dbRef.child("FormasPago").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listFormasPago.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren())
                {
                    FormasPagoEntity c = objSnapshot.getValue(FormasPagoEntity.class);
                    listFormasPago.add(c);

                }
                arrayAdapterFormasPago = new ArrayAdapter<FormasPagoEntity>(Formas_pago.this, android.R.layout.simple_list_item_1,listFormasPago);
                lvFormasPago.setAdapter(arrayAdapterFormasPago);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void inicializarFireBaseDB() {
        FirebaseApp.initializeApp(this);
        dbFireBase = FirebaseDatabase.getInstance();
        //dbFireBase.setPersistenceEnabled(true);
        dbRef = dbFireBase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formaspago,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_add_fp:
                Intent i= new Intent(getApplicationContext(), Detalle_formapago.class);
                startActivity(i);
                break;
        }

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_formaspago, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.modificarFp:
                Intent i= new Intent(getApplicationContext(), Detalle_formapago.class);
                FormasPagoEntity fp = arrayAdapterFormasPago.getItem(info.position);
                i.putExtra("idFp", fp.getId());
                startActivity(i);
                return true;
            case R.id.eliminarFp:
                posicion = info.position;
                //Mandar mensaje de confirmacion...
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Esta seguro que desea continuar?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //arrayAdapterCategorias.remove(arrayAdapterCategorias.getItem(posicion));
                                FormasPagoEntity fp2 = arrayAdapterFormasPago.getItem(posicion);
                                dbRef.child("FormasPago").child(fp2.getId()).removeValue();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}