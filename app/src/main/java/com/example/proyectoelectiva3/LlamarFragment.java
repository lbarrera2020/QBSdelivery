package com.example.proyectoelectiva3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyectoelectiva3.carrocompras.CarroCompraService;
import com.example.proyectoelectiva3.carrocompras.IStringResultProcess;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LlamarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LlamarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LlamarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LlamarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LlamarFragment newInstance(String param1, String param2) {
        LlamarFragment fragment = new LlamarFragment();
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

        ViewGroup fragment = (ViewGroup)inflater.inflate(R.layout.fragment_llamar, null);
        final principal_navigation mainActivity = (principal_navigation)getActivity();

        Button boton = fragment.findViewById(R.id.btnTest);
        Button boton2 = fragment.findViewById(R.id.btnTest2);

            boton.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    mainActivity.listData.add(666);

                    CarroCompraService serviceCart = new CarroCompraService();
                    serviceCart.insertar_CarroCompras("px3UNqPkn3bOV50vAOLZucZoWUt1", "da79f0fb-812c-444b-a47d-2201f16f1de5", new IStringResultProcess() {
                        @Override
                        public void onCallBackSuccess(String result) {

                            if (result != null && !result.isEmpty() && !"OK".equals(result) && !"ERR".equals(result) && !"DUP".equals(result))
                            {
                                mainActivity.idShoppingCart = result;
                                Toast.makeText(getContext(),"Agregado con exito, carrito creado, Id " + result, Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                if ("OK".equals(result))
                                {
                                    Toast.makeText(getContext(),"Agregado con exito", Toast.LENGTH_LONG).show();
                                }
                                else if ("DUP".equals(result))
                                {
                                    Toast.makeText(getContext(),"No se puede agregar dos veces el mismo producto", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getContext(),"Ocurrio un error al agregar...", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onCallBackFail(String msjError) {
                            Toast.makeText(getContext(),"Ocurrio un error inesperado...", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });

            boton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                   FirebaseDatabase dbFireBase;
                   DatabaseReference dbRef;
                    dbFireBase = FirebaseDatabase.getInstance();
                    dbRef = dbFireBase.getReference();

                    //dbRef.child("Productos").orderByKey().equalTo("ea4ec72c-4945-4ead-bd8a-fa7f7680bb47").addValueEventListener(new ValueEventListener() {
                    dbRef.child("Productos").child("ea4ec72c-4945-4ead-bd8a-fa7f7680bb47").addValueEventListener(new ValueEventListener() {
                        //dbRef.child("Productos").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            articulosEntity prod = snapshot.getValue(articulosEntity.class);

                            Toast.makeText(getContext(),"Nombre Producto2 " + snapshot.child("nombre").getValue().toString(), Toast.LENGTH_LONG).show();
                            if (prod != null)
                            {

                            }
                            else
                                Toast.makeText(getContext(),"Producto NULL" , Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    */


                    /*
                    CarroComprasRepository repoDB = new CarroComprasRepository();
                    final articulosEntity[] prod = {null};

                    repoDB.getDetalleProducto("ea4ec72c-4945-4ead-bd8a-fa7f7680bb47", new IGetSingleObject<articulosEntity>() {
                        @Override
                        public void onCallBackSuccess(articulosEntity object) {

                            prod[0] = object;
                            if (object != null)
                            {
                                Toast.makeText(getContext(),"Nombre ProductoV5 " + prod[0].getNombre(), Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(getContext(),"Producto NULL" , Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onCallBackFail(String msjError) {
                            Toast.makeText(getContext(),"Ocurrio un error:" + msjError , Toast.LENGTH_LONG).show();
                        }
                    });
                    */



                    /*
                    CarroCompraService serviceCart = new CarroCompraService();
                    String result = serviceCart.insertar_CarroCompras("px3UNqPkn3bOV50vAOLZucZoWUt1","ea4ec72c-4945-4ead-bd8a-fa7f7680bb47");
                    if (result != null && !result.isEmpty() && !"OK".equals(result) && !"ERR".equals(result) && !"NOT".equals(result))
                    {
                        mainActivity.idShoppingCart = result;
                        Toast.makeText(getContext(),"Agregado con exito, carrito creado, Id " + result, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if ("OK".equals(result))
                        {
                            Toast.makeText(getContext(),"Agregado con exito", Toast.LENGTH_LONG).show();
                        }
                        else if ("NOT".equals(result))
                        {
                            Toast.makeText(getContext(),"No se puede agregar dos veces el mismo producto", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(),"Ocurrio un error al agregar", Toast.LENGTH_LONG).show();
                        }
                    }
                    */


                    CarroCompraService serviceCart = new CarroCompraService();
                    serviceCart.insertar_CarroCompras("px3UNqPkn3bOV50vAOLZucZoWUt1", "97acee7d-9d3a-4b94-89bf-8612ba6b5cda", new IStringResultProcess() {
                        @Override
                        public void onCallBackSuccess(String result) {

                            if (result != null && !result.isEmpty() && !"OK".equals(result) && !"ERR".equals(result) && !"DUP".equals(result))
                            {
                                mainActivity.idShoppingCart = result;
                                Toast.makeText(getContext(),"Agregado con exito, carrito creado, Id " + result, Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                if ("OK".equals(result))
                                {
                                    Toast.makeText(getContext(),"Agregado con exito", Toast.LENGTH_LONG).show();
                                }
                                else if ("DUP".equals(result))
                                {
                                    Toast.makeText(getContext(),"No se puede agregar dos veces el mismo producto", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getContext(),"Ocurrio un error al agregar...", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onCallBackFail(String msjError) {
                            Toast.makeText(getContext(),"Ocurrio un error inesperado...", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });


        return fragment;
    }
}