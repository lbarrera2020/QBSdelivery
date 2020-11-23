package com.example.proyectoelectiva3.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

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
import java.util.Collections;

public class Categorias extends AppCompatActivity {

    ListView lvCategorias;
    private ArrayList<CategoriaEntity> listCategorias = new ArrayList<CategoriaEntity>();
    //ArrayAdapter<CategoriaEntity> arrayAdapterCategorias;
    CategoriasAdminAdapter categoriasAdapter;

    FirebaseDatabase dbFireBase;
    DatabaseReference dbRef;
    int posicion = -1;
    private static final String STORAGE_FOLDER_CATEGORIAS = "categorias";
    FirebaseStorage storageFiles;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        lvCategorias = findViewById(R.id.lvArticulos);

        registerForContextMenu(lvCategorias);
        inicializarFireBaseDB();
        cargarDatosCategorias();
        initFireBaseStorage();
    }

    private void inicializarFireBaseDB()
    {
        FirebaseApp.initializeApp(this);
        dbFireBase = FirebaseDatabase.getInstance();
        //dbFireBase.setPersistenceEnabled(true);
        dbRef = dbFireBase.getReference();
    }

    private void initFireBaseStorage()
    {
        storageFiles = FirebaseStorage.getInstance();
        storageRef = storageFiles.getReference(STORAGE_FOLDER_CATEGORIAS);
    }
    private void cargarDatosCategorias()
    {
        dbRef.child("Categorias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCategorias.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren())
                {
                    CategoriaEntity c = objSnapshot.getValue(CategoriaEntity.class);
                    listCategorias.add(c);
                }
                //arrayAdapterCategorias = new ArrayAdapter<CategoriaEntity>(Categorias.this, android.R.layout.simple_list_item_1,listCategorias);
                categoriasAdapter = new CategoriasAdminAdapter(Categorias.this, R.layout.fila_lisview_categorias, listCategorias);


                //lvCategorias.setAdapter(arrayAdapterCategorias);
                lvCategorias.setAdapter(categoriasAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_categorias,menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search_cat);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                categoriasAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_add_cat:
                Intent i= new Intent(getApplicationContext(), DetalleCategoria.class);
                startActivity(i);
                break;
            case R.id.menu_ordenAZ_cat:
                Collections.sort(listCategorias, new SortByNameAscCategoAdmin());
                categoriasAdapter.notifyDataSetChanged();
                break;
            case R.id.menu_ordenZA_cat:
                Collections.sort(listCategorias, new SortByNameDescCategoAdmin());
                categoriasAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_categorias, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.modificarCat:
                Intent i= new Intent(getApplicationContext(), DetalleCategoria.class);
                //CategoriaEntity cat = arrayAdapterCategorias.getItem(info.position);
                CategoriaEntity cat = categoriasAdapter.getItem(info.position);
                i.putExtra("idCat", cat.getId());
                startActivity(i);
                return true;
            case R.id.eliminarCat:
                posicion = info.position;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Esta seguro que desea continuar?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CategoriaEntity cat2 = categoriasAdapter.getItem(posicion);
                                storageRef.child(cat2.getImageName()).delete();
                                dbRef.child("Categorias").child(cat2.getId()).removeValue();
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