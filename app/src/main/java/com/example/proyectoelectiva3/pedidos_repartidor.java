package com.example.proyectoelectiva3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoelectiva3.FormasPago.FormasPagoEntity;
import com.example.proyectoelectiva3.FormasPago.Formas_pago;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class pedidos_repartidor extends AppCompatActivity {


    private FirebaseAuth auntenticacion;
    FirebaseDatabase dbFireBase;
    DatabaseReference dbRef;



    ListView lvPedidos;
    private List<PedidosEntity> listPedidos = new ArrayList<PedidosEntity>();
    ArrayAdapter<PedidosEntity> arrayAdapterPedidos;
    TextView tvtMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_repartidor);

        lvPedidos = findViewById(R.id.lvPedidos);
        tvtMensaje = findViewById(R.id.tvtmensaje);

        inicializarFireBaseDB();
        //mDataBase = FirebaseDatabase.getInstance().getReference()   ;

        dbRef.child("Pedidos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPedidos.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren())
                {
                    PedidosEntity c = objSnapshot.getValue(PedidosEntity.class);
                    listPedidos.add(c);

                }
                if (listPedidos.size()> 0) {
                    tvtMensaje.setText("Pedidos disponibles para despachar");
                }

                arrayAdapterPedidos = new ArrayAdapter<PedidosEntity>(pedidos_repartidor.this, android.R.layout.simple_list_item_1,listPedidos);
                lvPedidos.setAdapter(arrayAdapterPedidos);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lvPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();

                Intent i= new Intent(getApplicationContext(), pedidosDetalle.class);
                startActivity(i);
                finish();



            }
        });

   }



    private void inicializarFireBaseDB() {
        FirebaseApp.initializeApp(this);
        dbFireBase = FirebaseDatabase.getInstance();
        //dbFireBase.setPersistenceEnabled(true);
        dbRef = dbFireBase.getReference();
    }
}