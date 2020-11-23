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

    public void EnviarCorreo(String CuentaCorreo,String Nombre){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        correoAdmin = "grupo.electiva4@gmail.com";
        claveAdmin = "Ge040920";
        //correoAdmin = "autecsv09@gmail.com";
        //claveAdmin = "Tripleh2o";



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
                //String correoE;
                //CuentaCorreo = edtCorreos.getText().toString().trim();
                //MensajeSubject = "Por este medio se le envía la clave temporal para acceder a la app " + ClaveTemp + " ";
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(correoAdmin));
                message.setSubject("Bienvenido(a) a Qb's Delivery");
                message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(CuentaCorreo));

                /*message.setContent("<BR><I>" + "Estimado(a) "+ edtNombre.getText().toString() +": " + "</BR></I>" +
                        "<BR> Es un placer para nosotros que forme parte de nuestros clientes , y que pueda tener una " +
                        "<BR> excelente experiencia de compra." +
                        "<BR>" +
                        "<BR>Correo  :  <I>" + edtCorreo.getText().toString() + " " + "</I>" +
                        "<BR>" +
                        "<BR>" +
                        "</a>" +
                        "<BR>" +
                        "<BR>" +
                        "<img src= https://scontent.fsal2-1.fna.fbcdn.net/v/t1.0-9/120899591_114224473778696_7962673504433208249_n.jpg?_nc_cat=110&ccb=2&_nc_sid=85a577&_nc_ohc=qUMiZvQ34bIAX_Fl2pl&_nc_ht=scontent.fsal2-1.fna&oh=8b5de7c24e74ba96b0849f90d34ed634&oe=5FC8A020 >" +
                        "<BR>" +
                        " Qb's Delivery"+"</a>" +
                        "<BR>", "text/html; charset=utf-8");*/

                /*message.setContent(
                        "<span class=codigo> "+
                        "<HTML>"+
                        "<body>"+
                        "<table style=height:50%;width:50%; border=0 align=center> "+
                        "	<tr align=center bottom=middle>"+
                        "		<td>"+
                        "		<table border=0>"+
                        "			<tr>"+
                        "				<td align=center style=padding: 20px 0 0 0;><p style=font-size:30px>¡Gracias por registrarte con Qb's Delivery Sv!</p></h1></td>"+
                        "			</tr>"+
                        "			<tr>"+
                        "			<td align=center style=padding: 20px 0 0 0;><p style=font-size:20px>Esperamos que sea tengas una experiencia de compra inolvidable con nosotros</p></td>"+
                        "			</tr>"+
                        "			 <table style=height:50%;width:50%; border=0>"+
                        "			  <tr>"+
                        "				<td bgcolor=#000000>"+
                        "					<table style=height:50%;width:50%; border=0>"+
                        "						<table style=height:50%;width:50%; border=0>"+
                        "							<tr align=center bottom=middle>"+
                        "								<td width=50 bgcolor=#000000 style=text-align: center; padding: 0 20px 0 10px;>"+
                        "									<a href=https://www.facebook.com/profile.php?id=100055734325069>"+
                        "										<img src=https://scontent.fsal3-1.fna.fbcdn.net/v/t1.0-9/123879011_137304738137336_3860549595714845770_n.jpg?_nc_cat=109&ccb=2&_nc_sid=8024bb&_nc_ohc=0wMi_lTORZcAX--hY7k&_nc_ht=scontent.fsal3-1.fna&oh=63d7c5a82f348d1a532cfc8becd0770e&oe=5FCA4E9D width = 75 height=75  alt=Facebook border=0 />"+
                        "									</a>"+
                        "								</td>"+
                        "								<td width=50 bgcolor=#000000 style=text-align: center; padding: 0 20px 0 10px;>"+
                        "									<font color = #000000>44444444</font>"+
                        "								</td>"+
                        "								<td width=100 bgcolor=#000000 style=text-align: center; padding: 0 20px 0 10px;>"+
                        "									<font color = #000000>444444</font>"+
                        "								</td>"+
                        "								<td width=100 bgcolor=#000000 style=text-align: center; padding: 0 20px 0 10px;>"+
                        "									<font color = #000000>444444</font>"+
                        "								</td>"+
                        "								<td width=100 bgcolor=#000000 style=text-align: center; padding: 0 20px 0 10px;>"+
                        "									<font color = #000000>444444</font>"+
                        "								</td>"+
                        "							</tr>"+
                        "						</table>"+
                        "				</td>"+
                        "				</tr>"+
                        "					</table>"+
                        "				<tr>"+
                        "					<td align=center style=padding: 20px 0 0 0;>"+
                        "						<img src=https://scontent.fsal2-1.fna.fbcdn.net/v/t1.0-9/120899591_114224473778696_7962673504433208249_n.jpg?_nc_cat=110&ccb=2&_nc_sid=85a577&_nc_ohc=qUMiZvQ34bIAX_Fl2pl&_nc_ht=scontent.fsal2-1.fna&oh=8b5de7c24e74ba96b0849f90d34ed634&oe=5FC8A020 width=450 height=200 alt=Facebook border=0 />"+
                        "					</td>"+
                        "				</tr>"+
                        "		</td>"+
                        "		  <td  align=center style=padding: 20px 0 0 0;>"+
                        "		      <table style=HEIGHT:50%;WIDTH:50%; border=0>"+
                        "			  <tr>"+
                        "			  <td bgcolor=#000000>"+
                        "                    <table style=HEIGHT:50%;WIDTH:50%; border=0>"+
                        "                        <tr align=center bottom=middle>"+
                        "							<td width=50 bgcolor=#000000 style=text-align: center; padding: 0 20px 0 10px;>"+
                        "                                <a href=https://www.facebook.com/profile.php?id=100055734325069>"+
                        "                                    <font color = #000000><img src=https://ci5.googleusercontent.com/proxy/Ey1jicG66qC3TQR6VzwMl7Rf-SGVFEsTpRcT1IsZylh-gsUkeI8cdYkCA9KwdR8lpDHvgR9JfUPx9UPWCzAu3ecpjQS3alRKK9sM7-M5kqXKwU7C39lPXOP_Mig7HEtmSlhGWJlS4n0e9567dzk=s0-d-e1-ft#http://res.mailing.lifemiles.com/res/lifemil_mkt_prod1/1c63eacb3f3b0181087495542b066340.png alt=Facebook border=0 /></font>"+
                        "                                </a>"+
                        "                            </td>"+
                        "                            <td width=50 bgcolor=#000000 style=text-align: center; padding: 0 20px 0 10px;>"+
                        "                                <a href=https://www.facebook.com/profile.php?id=100055734325069>"+
                        "                                    <img src=https://ci5.googleusercontent.com/proxy/dtLOUu0borYPz0kE8xFDEUinbbw-T57VyCqhOIJ-Zmpujn5TsFTD4MS_1CXmvHyLxNZ8QYTDwtRWSWM6d6mgsRGZ9wxVn0fKW9FIWO-3PyMAWkYA89OFzahxiq3KmYAref9neCRzWLb6YvA_USw=s0-d-e1-ft#http://res.mailing.lifemiles.com/res/lifemil_mkt_prod1/9ec37b02f41a122ee08191383f33cc28.png alt=Facebook border=0 />"+
                        "                                </a>"+
                        "                            </td>"+
                        "                            <td width=100 bgcolor=#000000 style=text-align: center; padding: 0 20px 0 10px;>"+
                        "                                <a href=http://www.twitter.com/>"+
                        "                                    <img src=https://ci5.googleusercontent.com/proxy/fUpNtGo5hCAIDn7fEIHnmAh5INN1FoptHuuyY_BEk5F9rM3oWJeGHDRvZTY5AFv9qPYVwglPVJ75QRhPMDR16zKshi8-cy--KmQ8fFnCW6SQDEkEv37qyVc6UPCY2jVkjKiesfKa8jzz2kOyoTg=s0-d-e1-ft#http://res.mailing.lifemiles.com/res/lifemil_mkt_prod1/472c6401ea0ce9737dcabf53b512223d.png  alt=Twitter border=0 />"+
                        "                                </a>"+
                        "                            </td>"+
                        "							<td width=50 bgcolor=#000000 style=text-align: center; padding: 0 20px 0 10px;>"+
                        "                                <a href=http://www.instagram.com/>"+
                        "                                    <img src=https://ci3.googleusercontent.com/proxy/COLFxuFs8tBZQQ062QTt5mOQONhtGODZiLBe-x5lUqrqfaWmAiqwP6MrTKzRoIABe0PyUeEs1UTSN9N3gJQOKK1k9k7VxQntXQl6V_K3WGiVouUVqLC3p25o0Ki2amcYy_LNUWjAF6yl16GzR0Y=s0-d-e1-ft#http://res.mailing.lifemiles.com/res/lifemil_mkt_prod1/938c4baced17ac5a634d0a130a466583.png  alt=Instagram border=0 />"+
                        "                                </a>"+
                        "                            </td>"+
                        "							<td width=50 bgcolor=#000000 style=text-align: center; padding: 0 20px 0 10px;>"+
                        "                                <a href=http://www.youtube.com/>"+
                        "                                    <img src=https://ci5.googleusercontent.com/proxy/77zm5meX4NqAFrn8fJxQ568iZ7upubXXxYUQofK46S9NoaQHedbCqTCmbHEtpIsu8-XKiUAoGU7q2IX5y_ivwBHHyY4Rbx2DBFmYGlhtK9VZfh8aKiihk2p7FF5TGAIJtGNkUwrlpqeOSYgbDo0=s0-d-e1-ft#http://res.mailing.lifemiles.com/res/lifemil_mkt_prod1/094b6ba676ae1461293aabea95a4e148.png  alt=Youtube border=0 />"+
                        "                                </a>"+
                        "                            </td>"+
                        "                        </tr>"+
                        "                    </table>"+
                        "					</td>"+
                        "				</tr>"+
                        "				</table>"+
                        "                </td>"+
                        "	</tr>"+
                        "</table>"+
                        "</body>"+
                        "</HTML>"+
                        "</span>"+
                        "</body>","text/html; charset=utf-8");*/
                message.setContent("<BR><I>" + "Estimado(a) "+ Nombre +": " + "</BR></I>" +
                                "<BR><p style=font-size:30px>¡Gracias por registrarte con Qb's Delivery Sv!</p>" +
                                "<p style=font-size:25px>Esperamos que sea tengas una experiencia de compra inolvidable con nosotros</p>" +
                                "<BR>" +
                                "<img src=https://firebasestorage.googleapis.com/v0/b/electiva-4-proyecto.appspot.com/o/picture%2Fmail.PNG?alt=media&token=516d3803-0b99-4a0f-a226-2a33fb792aa1 alt=Facebook border=0 />"
                        ,"text/html; charset=utf-8");

                Transport.send(message);

            }

        }catch (Exception e ){
            e.printStackTrace();
        }


    }

}
