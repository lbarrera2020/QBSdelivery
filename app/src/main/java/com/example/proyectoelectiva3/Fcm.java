package com.example.proyectoelectiva3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.internal.InternalTokenProvider;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class Fcm extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("token","Mi token es:" + s);
        guardarToken(s);
    }

    private void guardarToken(String s) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("token");
        ref.child("telmoe").setValue(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        Log.e("TAG","Mensaje recibido de:" + from);

       

        if(remoteMessage.getData().size() > 0){

            String titulo = remoteMessage.getData().get("titulo");
            String detalle = remoteMessage.getData().get("detalle");

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                mayorqueoreo(titulo,detalle);
            }
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                menorqueoreo(titulo,detalle);
            }

        }
        


    }

    private void menorqueoreo(String titulo, String detalle) {
    }

    private void mayorqueoreo(String titulo, String detalle) {

        String id = "mensaje";
        NotificationManager  nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this,id);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel nc =  new NotificationChannel(id,"nuevo", NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            assert nm != null;
            nm.createNotificationChannel(nc);
        }
        builder.setAutoCancel(true)
        .setWhen(System.currentTimeMillis())
        .setContentTitle(titulo)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentText(detalle)
                .setContentIntent(clicknoti())
        .setContentInfo("nuevo");

        Random random = new Random();
        int idNotify = random.nextInt( 8000);

        assert nm != null;
        nm.notify(idNotify,builder.build());

    }

    public PendingIntent clicknoti(){
        Intent nf = new Intent(getApplicationContext(),MainActivity.class);
        nf.putExtra("color","azul");
        nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this,0,nf,0);

    }
}


