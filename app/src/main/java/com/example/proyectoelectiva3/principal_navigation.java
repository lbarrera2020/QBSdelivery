package com.example.proyectoelectiva3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class principal_navigation extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    TextView tvNombreUsuario, tvCorreoUsuario;
    private FirebaseAuth auntenticacion;
    private DatabaseReference objdatabase;
    ImageView imglogoFB;
    Intent intent = getIntent();
    public List<Integer> listData = new ArrayList<>();
    public String idShoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_navigation);

        auntenticacion = FirebaseAuth.getInstance();
        objdatabase= FirebaseDatabase.getInstance().getReference();

        Bundle datos= getIntent().getExtras();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeCarruselFragment, R.id.categoriasFragment2, R.id.carroComprasFragment,
                R.id.misPedidosFragment, R.id.llamarFragment, R.id.salirFragment)
                .setDrawerLayout(drawer)
                .build();

        //Codigo para cambia nombre, email y foto(si aplica) para usuario
        tvNombreUsuario = navigationView.getHeaderView(0).findViewById(R.id.tv_nav_nombreUsuario);
        tvCorreoUsuario = navigationView.getHeaderView(0).findViewById(R.id.tv_nav_CorreoUsuario);
        imglogoFB = navigationView.getHeaderView(0).findViewById(R.id.imageView_logo);

        String nombre1 = "";
        String correo1 = "";
        String urlPhoto = "";

        if (datos != null)
        {
           nombre1 = datos.getString("nombre");
           correo1 = datos.getString("email");
           urlPhoto = datos.getString("urlPhoto");
        }

        if(!nombre1.isEmpty()){
            tvCorreoUsuario.setText(correo1);
            tvNombreUsuario.setText(nombre1);
            urlPhoto = urlPhoto + "?=type=large";
            Picasso.get().load(urlPhoto).into(imglogoFB);
        }else {
            getInformacionUsuario();
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //Para menu de abajo
        BottomNavigationView bottomMenuNav = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottomMenuNav, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal_navigation, menu);
         return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void getInformacionUsuario(){

        String id= auntenticacion.getCurrentUser().getUid();

        objdatabase.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombre= snapshot.child("nombre").getValue().toString().trim();
                    String correo= snapshot.child("correo").getValue().toString().trim();

                    tvCorreoUsuario.setText(correo);
                    tvNombreUsuario.setText(nombre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        if (drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        else{
            super.onBackPressed();
        }
    }

}