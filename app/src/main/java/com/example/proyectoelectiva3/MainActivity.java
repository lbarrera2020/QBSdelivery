package com.example.proyectoelectiva3;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.facebook.appevents.internal.SourceApplicationInfo;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import java.util.HashMap;
import java.util.Map;
import com.example.proyectoelectiva3.EnvioCorreo;

public class MainActivity extends AppCompatActivity {



    EditText edtNombre, edtCorreo, edtContraseña;
    Button btnRegistrar, btnLogin;
    ImageView logo;
    EnvioCorreo envio = new EnvioCorreo();


    String nombre,contraseña,correo,id;
    CallbackManager mCallbackManager;
    FirebaseAuth autenticacion;
    FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference database;
    private LoginButton logginButton;
    private static final String TAG = "FacebookAutentication";
    private AccessTokenTracker accessTokenTracker ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtNombre= findViewById(R.id.edtNombre);
        edtContraseña= findViewById(R.id.edtPassword);
        edtCorreo=findViewById(R.id.edtEmail);
        btnRegistrar= findViewById(R.id.btnRegistrar);
        btnLogin= findViewById(R.id.btnLogin);
        logo = findViewById(R.id.logo);
        logginButton = findViewById(R.id.login_button);
        logginButton.setReadPermissions("email","public_profile");

        autenticacion= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference();

        mCallbackManager = CallbackManager.Factory.create();

        setAutoLogAppEventsEnabled(true);

        logginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d(TAG,"onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG,"onError" + error);
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    UpdateUI(user);
                }
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    autenticacion.signOut();
                }
            }
        };


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombre= edtNombre.getText().toString().trim();
                contraseña= edtContraseña.getText().toString().trim();
                correo= edtCorreo.getText().toString().trim();

                if (TextUtils.isEmpty(nombre)) {
                    edtNombre.requestFocus();
                    edtNombre.setError("Dato requerido");
                }
                else if (TextUtils.isEmpty(contraseña)) {
                    edtContraseña.requestFocus();
                    edtContraseña.setError("Dato requerido");
                }
                else if (TextUtils.isEmpty(correo)) {
                    edtCorreo.requestFocus();
                    edtCorreo.setError("Dato requerido");
                }
                else {
                    Registrar();
                }

            }
        });
    }

    private void setAutoLogAppEventsEnabled(boolean b) {
    }

    private void handleFacebookToken(AccessToken token) {
        Log.d(TAG,"handelFacebookToken" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        autenticacion.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"Sign in with credencial successfull");
                    FirebaseUser user = autenticacion.getCurrentUser();
                    Intent i= new Intent(getApplicationContext(), principal_navigation.class);
                    i.putExtra("nombre", user.getDisplayName());
                    i.putExtra("email", user.getEmail());
                    i.putExtra("urlPhoto",user.getPhotoUrl().toString());
                    startActivity(i);
                    UpdateUI(user);
                }else {
                    Log.d(TAG,"Sign in with credencial failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_LONG).show();
                    UpdateUI(null);
                }

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void UpdateUI(FirebaseUser user) {
        if(user != null){
            edtNombre.setText(user.getDisplayName());
            edtCorreo.setText(user.getEmail());


            if(user.getPhotoUrl() != null){
                String photoUrl = user.getPhotoUrl().toString();
                photoUrl = photoUrl + "?type=large";
                Picasso.get().load(photoUrl).into(logo);
            }else {
                edtNombre.setText("");
                logo.setImageResource(R.drawable.logo);
            }
        }
    }

    void Registrar(){
        autenticacion.createUserWithEmailAndPassword(correo,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){




                    id= autenticacion.getCurrentUser().getUid();

                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", nombre);
                    map.put("correo", correo);
                    map.put("contraseña", contraseña);
                    map.put("uid", id);

                    database.child("Usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                Intent i= new Intent(getApplicationContext(),principal_navigation.class);
                                i.putExtra( "nombre",nombre);
                                i.putExtra("email", correo);

                                envio.EnviarCorreo(correo,nombre);

                                startActivity(i);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"No se pudieron crear datos correctamente",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"No se pudo registrar usuario",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authStateListener != null) {

        } else {

            if (autenticacion.getCurrentUser() != null) {
                Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                // i.putExtra("nombre",edtNombre.getText().toString().trim());
                // i.putExtra("email", edtCorreo.getText().toString().trim());
                startActivity(i);
                finish();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            autenticacion.removeAuthStateListener(authStateListener);
        }

    }
}
