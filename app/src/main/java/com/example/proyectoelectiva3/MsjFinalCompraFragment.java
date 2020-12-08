package com.example.proyectoelectiva3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoelectiva3.carrocompras.CarroComprasRepository;
import com.example.proyectoelectiva3.carrocompras.IStringResultProcess;
import com.example.proyectoelectiva3.carrocompras.ItemCarroCompraModel;
import com.example.proyectoelectiva3.carrocompras.MsjFinalData;


public class MsjFinalCompraFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private MsjFinalData dataCompra;
    private CarroComprasRepository repoDB;
    private TextView msjIdPedido;

    public MsjFinalCompraFragment() {
        // Required empty public constructor
    }

    public static MsjFinalCompraFragment newInstance(MsjFinalData data, String param2) {
        MsjFinalCompraFragment fragment = new MsjFinalCompraFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, data);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dataCompra = (MsjFinalData)getArguments().getSerializable(ARG_PARAM1);
        }

        limpiarCarroCompras();
        enviarCorreoNotificacion();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup fragment = (ViewGroup)inflater.inflate(R.layout.fragment_msj_final_compra, null);
        msjIdPedido = fragment.findViewById(R.id.msjIdPedido);

        msjIdPedido.setText(dataCompra.getId_pedido());

        // Inflate the layout for this fragment
        return fragment;
    }

    private void limpiarCarroCompras()
    {

        if (dataCompra != null)
        {
            repoDB = new CarroComprasRepository();
            repoDB.eliminmarCarroCompras(dataCompra.getCliente().getIdCliente(), new IStringResultProcess() {
                @Override
                public void onCallBackSuccess(String result) {
                    if (!"OK".equals(result))
                    {
                        Toast.makeText(getContext(),"No se puedo limpiar carro compras...", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCallBackFail(String msjError) {
                    Toast.makeText(getContext(),"Error inesperado al limpiar carro compras, " + msjError, Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    private void enviarCorreoNotificacion() {

        EnvioCorreo envio = new EnvioCorreo();
        String asunto = "Notificacion Pedido Qb's Delivery";
        String contenido = "<BR><I>" + "Estimado(a) " + dataCompra.getCliente().getNombre()  + ": " + "</BR></I>" +
                "<BR><p style=font-size:30px>¡Gracias por realizar su compra con Qb's Delivery Sv!</p>" +
                "<p style=font-size:25px>Su número de pedido es: " + dataCompra.getId_carrito() + "</p>" +
                "<BR>" +
                "<img src=https://firebasestorage.googleapis.com/v0/b/electiva-4-proyecto.appspot.com/o/picture%2Fmail.PNG?alt=media&token=516d3803-0b99-4a0f-a226-2a33fb792aa1 alt=Facebook border=0 />";

        if (envio.EnviarCorreo(dataCompra.getCliente().getCorreo(),"", asunto,contenido))
        {
            Toast.makeText(getContext(),"Correo enviado con exito", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getContext(),"Ocurrio un error al mandar el correo electronico", Toast.LENGTH_LONG).show();
        }

    }
}
