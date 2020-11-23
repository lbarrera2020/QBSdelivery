package com.example.proyectoelectiva3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_estado_pedidos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_estado_pedidos extends Fragment {

    //    ListView lvEstadoPedidos;
//    String[] elementos = {"PEDIDO RECIBIDO","PREPARACION DE PEDIDO","EN RUTA","ENTREGADO"};
    RadioButton rdoPedidoRecibido,rdoPreparacion,rdoEnRuta,rdoEntregado;

    public Fragment_estado_pedidos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_estado_pedidos.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_estado_pedidos newInstance(String param1, String param2) {
        Fragment_estado_pedidos fragment = new Fragment_estado_pedidos();
        Bundle args = new Bundle();

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estado_pedidos, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        //lvEstadoPedidos = (ListView) getView().findViewById(R.id.lvEstadoPedido);
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, elementos);
        // lvEstadoPedidos.setAdapter(adapter);
        //selectedListItem = getIntent().getIntExtra("PositionInList", -1);
        rdoPedidoRecibido = (RadioButton) getView().findViewById(R.id.rdoPedidoRecibido);
        rdoPreparacion = (RadioButton) getView().findViewById(R.id.rdoPreparacionPedido);
        rdoEnRuta = (RadioButton) getView().findViewById(R.id.rdoEnRuta);
        rdoEntregado = (RadioButton) getView().findViewById(R.id.rdoPedidoEntregado);

        rdoPedidoRecibido.setEnabled(true);
        rdoPreparacion.setEnabled(true);
        rdoEnRuta.setEnabled(true);
        rdoEntregado.setEnabled(true);

        rdoPreparacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdoPreparacion.setChecked(false);
            }
        });

        rdoEnRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdoEnRuta.setChecked(false);
            }
        });

        rdoEntregado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdoEntregado.setChecked(false);
            }
        });


    }


}