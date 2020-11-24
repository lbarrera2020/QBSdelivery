package com.example.proyectoelectiva3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyectoelectiva3.carrocompras.CarroComprasRepository;
import com.example.proyectoelectiva3.carrocompras.IStringResultProcess;
import com.example.proyectoelectiva3.carrocompras.ItemCarroCompraModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MsjFinalCompraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MsjFinalCompraFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String idCarroCompras;
    private String mParam2;
    private CarroComprasRepository repoDB;

    public MsjFinalCompraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MsjFinalCompraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MsjFinalCompraFragment newInstance(String param1, String param2) {
        MsjFinalCompraFragment fragment = new MsjFinalCompraFragment();
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
            idCarroCompras = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        limpiarCarroCompras();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_msj_final_compra, container, false);
    }

    private void limpiarCarroCompras()
    {

        if (idCarroCompras != null && !idCarroCompras.isEmpty())
        {
            repoDB = new CarroComprasRepository();
            repoDB.eliminmarCarroCompras(idCarroCompras, new IStringResultProcess() {
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
}
