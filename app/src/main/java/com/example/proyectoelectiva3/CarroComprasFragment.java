package com.example.proyectoelectiva3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoelectiva3.admin.AdminUtils;
import com.example.proyectoelectiva3.carrocompras.CarroComprasAdapter;
import com.example.proyectoelectiva3.carrocompras.CarroComprasModel;
import com.example.proyectoelectiva3.carrocompras.CarroComprasRepository;
import com.example.proyectoelectiva3.carrocompras.IGetSingleObject;
import com.example.proyectoelectiva3.carrocompras.ItemCarroCompraModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CarroComprasFragment extends Fragment {

    private static final String ARG_PARAM1 = "modo";
    private static final String ARG_PARAM2 = "IdCart";
    private static final String MODO_PANTALLA_READ_ONLY = "readonly";

    private String modoPantalla;

    private CarroComprasRepository repoDB;
    private FirebaseAuth auntenticacion;

    private RecyclerView recyclerViewCarro;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private List<ItemCarroCompraModel> listItems = new ArrayList<>();
    LinearLayout containerEmptyCart, footerLinearLayout;
    TextView tvFooterTotal, tvFooterSubTotal, tvFooterDescuentos;
    Button btnContinuar;
    String idCarroCompras;

    public CarroComprasFragment() {
        // Required empty public constructor
    }

    public static CarroComprasFragment newInstance(String param1, String idCarroCompras) {
        CarroComprasFragment fragment = new CarroComprasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, idCarroCompras);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            modoPantalla = getArguments().getString(ARG_PARAM1);
            String aux =  getArguments().getString(ARG_PARAM2);
            if (aux != null && !aux.isEmpty())
            {
                idCarroCompras = aux;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*
        principal_navigation mainActivity = (principal_navigation)getActivity();
        int count = mainActivity.listData.size();
        Toast.makeText(getContext(), "From CarritoFragment count: " + count, Toast.LENGTH_LONG).show();
        */
        final ViewGroup carroFragment = (ViewGroup)inflater.inflate(R.layout.fragment_carro_compras, null);

        auntenticacion = FirebaseAuth.getInstance();

        containerEmptyCart = carroFragment.findViewById(R.id.containerEmptyCart);
        footerLinearLayout = carroFragment.findViewById(R.id.linearLayoutCartFooter);
        tvFooterSubTotal = carroFragment.findViewById(R.id.tvFooterCartSubTotal);
        tvFooterDescuentos = carroFragment.findViewById(R.id.tvFooterCartDescuentos);
        tvFooterTotal = carroFragment.findViewById(R.id.tvFooterCartTotal);
        recyclerViewCarro = carroFragment.findViewById(R.id.recyclerCart);
        btnContinuar = carroFragment.findViewById(R.id.btnCartFooterContinuar);

        recyclerViewCarro.setHasFixedSize(true);

        lManager = new LinearLayoutManager(getContext());
        recyclerViewCarro.setLayoutManager(lManager);

        if (MODO_PANTALLA_READ_ONLY.equals(modoPantalla))
        {
            adapter = new CarroComprasAdapter(listItems, getContext(), true);
            btnContinuar.setText("Confirmar");
        }
        else
        {
            adapter = new CarroComprasAdapter(listItems, getContext(), false);
            btnContinuar.setText("Continuar");
        }

        recyclerViewCarro.setAdapter(adapter);

        cargarDatosCarroCompras();
        setEmptyCartMsg();

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getParentFragmentManager();
                if (MODO_PANTALLA_READ_ONLY.equals(modoPantalla))
                {
                    //llamar fragment compra final
                    fm.beginTransaction().replace(R.id.nav_host_fragment, FinalizarCompraFragment.newInstance(idCarroCompras,"")  ).commit();
                }
                else
                {
                    //llamar fragment confirmation...
                    fm.beginTransaction().replace(R.id.nav_host_fragment, CarroComprasFragment.newInstance(MODO_PANTALLA_READ_ONLY, idCarroCompras)).commit();
                }
            }
        });

        return carroFragment;
    }

    private void cargarDatosCarroCompras()
    {
        Log.i("FragmentCart","Inicio");
        repoDB = new CarroComprasRepository();
        //Toast.makeText(getContext(), "En load cart " +auntenticacion.getCurrentUser().getUid(), Toast.LENGTH_LONG).show();

        repoDB.getCarroComprasByCliente(auntenticacion.getCurrentUser().getUid(), new IGetSingleObject<CarroComprasModel>() {
            @Override
            public void onCallBackSuccess(CarroComprasModel car) {
                //Toast.makeText(getContext(), "En load cart debug2 " + car, Toast.LENGTH_LONG).show();
                Log.i("FragmentCart","Ingreso en onCallBackSuccess");
                listItems.clear();
                if (car != null)
                {
                    Log.i("FragmentCart","Ingreso en onCallBackSuccess -> car Not Null");
                    listItems.addAll(car.getItems());
                    idCarroCompras = car.getIdCart();
                    if (MODO_PANTALLA_READ_ONLY.equals(modoPantalla))
                        adapter = new CarroComprasAdapter(listItems, getContext(), true);
                    else
                        adapter = new CarroComprasAdapter(listItems, getContext(), false);
                    adapter.notifyDataSetChanged();
                    setEmptyCartMsg();

                    //Footer Data
                    tvFooterSubTotal.setText("$" + AdminUtils.formatNumber2Decimal(car.getSubTotal()));
                    tvFooterDescuentos.setText("$-" + AdminUtils.formatNumber2Decimal(car.getTotalDescuentos()));
                    tvFooterTotal.setText("$" + AdminUtils.formatNumber2Decimal(car.getTotal()));
                    Log.i("FragmentCart","Ingreso en onCallBackSuccess -> car Not Null Fin");
                }
            }

            @Override
            public void onCallBackFail(String msjError) {
                Toast.makeText(getContext(), "Error al cargar Datos de carro de compras. " +msjError, Toast.LENGTH_LONG).show();
                Log.e("CarroCompras", msjError);
            }
        });
        Log.i("FragmentCart","Fin");
    }

    private void setEmptyCartMsg()
    {
        if (listItems.isEmpty())
        {
            containerEmptyCart.setVisibility(View.VISIBLE);
            footerLinearLayout.setVisibility(View.GONE);
            recyclerViewCarro.setVisibility(View.GONE);
        }
        else
        {
            containerEmptyCart.setVisibility(View.GONE);
            footerLinearLayout.setVisibility(View.VISIBLE);
            recyclerViewCarro.setVisibility(View.VISIBLE);
        }
    }
}