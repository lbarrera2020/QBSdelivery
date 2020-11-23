package com.example.proyectoelectiva3.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.proyectoelectiva3.AdminMenu;
import com.example.proyectoelectiva3.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class articulosList extends AppCompatActivity {
    ListView lvArticulos;
    private ArrayList<articulosEntity> listProductos = new ArrayList<articulosEntity>();
    //ArrayAdapter<articulosEntity> arrayAdapterArticulos;
productosAdminAdapter productosAdapter;

    FirebaseDatabase dbFireBase;
    DatabaseReference dbRef;
    int posicion = -1;
    private static final String STORAGE_FOLDER_PRODUCTOS = "productos";
    FirebaseStorage storageFiles;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulos_list);
        lvArticulos = findViewById(R.id.lvProductos);
        registerForContextMenu(lvArticulos);
        inicalizarFireBase();
        initFireBaseStorage();
        listarDatos();
    }
    private void initFireBaseStorage()
    {
        storageFiles = FirebaseStorage.getInstance();
        storageRef = storageFiles.getReference(STORAGE_FOLDER_PRODUCTOS);
    }

    private void listarDatos() {
       // articulos obj = new articulos();

        dbRef.child("Productos").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProductos.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    articulosEntity a = objSnapshot.getValue(articulosEntity.class);

                    listProductos.add(a);
                }
                    // arrayAdapterArticulos = new ArrayAdapter<articulosEntity>(articulosList.this, R.layout.support_simple_spinner_dropdown_item, listArticulos);
                    productosAdapter = new productosAdminAdapter(articulosList.this, R.layout.fila_lista_articulos,listProductos);
                    lvArticulos.setAdapter(productosAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicalizarFireBase() {
        FirebaseApp.initializeApp(this);
        dbFireBase = FirebaseDatabase.getInstance();
        dbRef = dbFireBase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_articulos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case  R.id.menu_add_arti:
              //  Intent i = new  Intent(getApplicationContext(),DetalleArticulos.class);
                Intent obj = new Intent(getApplicationContext(),DetalleArticulos.class);
                startActivity(obj);
                break;
            case R.id.menu_exit_art:
                Intent exit = new Intent(getApplicationContext(), AdminMenu.class);
                startActivity(exit);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_articulos,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case  R.id.modificarArt:
                Intent i = new Intent(getApplicationContext(),articulos.class);
                articulosEntity art = productosAdapter.getItem(info.position);
                i.putExtra("idArt",art.getId());
                startActivity(i);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
