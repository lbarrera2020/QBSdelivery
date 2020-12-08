package com.example.proyectoelectiva3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText edtCorreo, edtContraseña;
    Button btnLogin;


    String correo, password;


    private FirebaseAuth autenticacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtCorreo= findViewById(R.id.edtEmail);
        edtContraseña= findViewById(R.id.edtPassword);
        btnLogin= findViewById(R.id.btnLogin);



        autenticacion= FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo= edtCorreo.getText().toString().trim();
                password= edtContraseña.getText().toString().trim();


                if (correo.isEmpty()) {
                    edtCorreo.setError("Campo requerido");
                    edtCorreo.requestFocus();
                }
                else if(password.isEmpty()){
                    edtContraseña.setError("Campo requerido");
                    edtContraseña.requestFocus();
                }
                else{
                    loginUser();
                }
            }
        });
    }

    private void loginUser(){
        autenticacion.signInWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(correo.equals("admin1@qbsdelivery.com")){
                        //Intent i = new Intent(getApplicationContext(), menu_admin.class);
                        Intent i = new Intent(getApplicationContext(), AdminMenu.class);
                        startActivity(i);
                    }else if(correo.equals("rep01@qbsdelivery.com")){

                        Intent i = new Intent(getApplicationContext(), repartidorActivity.class);
                        startActivity(i);
                    }else{
                        Intent i = new Intent(getApplicationContext(), principal_navigation.class);
                        startActivity(i);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "No se pudo iniciar sesion", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
