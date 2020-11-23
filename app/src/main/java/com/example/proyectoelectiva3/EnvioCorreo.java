package com.example.proyectoelectiva3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.os.StrictMode;

import java.util.Properties;


import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnvioCorreo {
    String correoAdmin,claveAdmin;
    Session session;

    public void EnviarCorreo(String CuentaCorreo,String Nombre, String Asunto,String Contenido){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        correoAdmin = "grupo.electiva4@gmail.com";
        claveAdmin = "Ge040920";

        Properties properties = new Properties();
        properties.put("mail.smtp.host","smtp.googlemail.com");
        properties.put("mail.smtp.socketFactory.port","465");
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.port","465");

        try  {
            session = Session.getDefaultInstance (properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correoAdmin, claveAdmin);
                }
            });
            if(session!=null){
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(correoAdmin));
                message.setSubject(Asunto);
                message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(CuentaCorreo));


                message.setContent(Contenido
                        ,"text/html; charset=utf-8");
                Transport.send(message);

            }

        }catch (Exception e ){
            e.printStackTrace();
        }

    }

}
