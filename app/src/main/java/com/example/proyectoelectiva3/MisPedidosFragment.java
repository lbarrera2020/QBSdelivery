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
import android.widget.Toast;

public class MisPedidosFragment extends Fragment {

    ListView lvPedidos;
    String[] elementos = {"Pedido 1"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mis_pedidos, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        lvPedidos = (ListView) getView().findViewById(R.id.lvPedidos);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, elementos);
        lvPedidos.setAdapter(adapter);
        lvPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();

                FragmentManager fm = getParentFragmentManager();
                fm.beginTransaction().replace(R.id.nav_host_fragment, Fragment_estado_pedidos.newInstance("","")  ).commit();



            }
        });



    }
}