package com.example.proyectoelectiva3;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.proyectoelectiva3.admin.ProductoCategoriaDummy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class FinalizarCompraFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Spinner spinerDireccion;
    private Button btnAbrirCalendario, btnFinalizarCompra;


    public FinalizarCompraFragment() {
        // Required empty public constructor
    }

    public static FinalizarCompraFragment newInstance(String param1, String param2) {
        FinalizarCompraFragment fragment = new FinalizarCompraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup fragment = (ViewGroup)inflater.inflate(R.layout.fragment_finalizar_compra, null);
        spinerDireccion = fragment.findViewById(R.id.spDireccionEnvio);
        btnAbrirCalendario = fragment.findViewById(R.id.btnAbrirCalendario);
        btnFinalizarCompra = fragment.findViewById(R.id.btnFinalizarCompra);

        btnAbrirCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), date_time_picker_dialog.class);
                startActivity(i);
            }
        });

        btnFinalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getParentFragmentManager();
                fm.beginTransaction().replace(R.id.nav_host_fragment, MsjFinalCompraFragment.newInstance(mParam1,"")  ).commit();
            }
        });

        cargarDatosSpinner();
        return fragment;
    }

    private void cargarDatosSpinner()
    {
        //Simulo que los obtuve de la DB
        ProductoCategoriaDummy cat1 = new ProductoCategoriaDummy(UUID.randomUUID().toString(), "Casa|Senda 9 block 7 casa #23 Santa Tecla");
        ProductoCategoriaDummy cat2 = new ProductoCategoriaDummy(UUID.randomUUID().toString(), "Trabajo| Avenida Olimpica calle Arturo Araujo casa #101 Col Escalon");

        List<ProductoCategoriaDummy> spinArray = new ArrayList<>();
        spinArray.add(cat1);
        spinArray.add(cat2);

        //ProductoCategoriaDummy objCat = (ProductoCategoriaDummy)spinerCat.getSelectedItem();

        ArrayAdapter<ProductoCategoriaDummy> adapter = new ArrayAdapter<ProductoCategoriaDummy>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerDireccion.setAdapter(adapter);
    }

}