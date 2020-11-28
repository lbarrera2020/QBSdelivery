package com.example.proyectoelectiva3;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.proyectoelectiva3.carrocompras.CarroComprasFireBaseAdapter;
import com.example.proyectoelectiva3.carrocompras.CarroComprasModel;
import com.example.proyectoelectiva3.carrocompras.CarroComprasRepository;
import com.example.proyectoelectiva3.carrocompras.IGetSingleObject;
import com.example.proyectoelectiva3.carrocompras.ItemCarroCompraModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    private static final String NOMBRE_COLLECTION_CARRO_COMPRAS = "carroComprasTmp";
    CarroComprasFireBaseAdapter adapterFireBase;
    private static final String NOMBRE_ITEMS_CARRO_COMPRAS = "items";

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

        final ViewGroup carroFragment = (ViewGroup)inflater.inflate(R.layout.fragment_carro_compras, null);

        auntenticacion = FirebaseAuth.getInstance();

        containerEmptyCart = carroFragment.findViewById(R.id.containerEmptyCart);
        footerLinearLayout = carroFragment.findViewById(R.id.linearLayoutCartFooter);
        tvFooterSubTotal = carroFragment.findViewById(R.id.tvFooterCartSubTotal);
        tvFooterDescuentos = carroFragment.findViewById(R.id.tvFooterCartDescuentos);
        tvFooterTotal = carroFragment.findViewById(R.id.tvFooterCartTotal);
        recyclerViewCarro = carroFragment.findViewById(R.id.recyclerCart);
        btnContinuar = carroFragment.findViewById(R.id.btnCartFooterContinuar);

        cargarDatosCarroComprasAdapterFireBase();

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

    private void cargarDatosCarroComprasAdapterFireBase()
    {
        Log.i("AdapterFireBase","Inicio");
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference().child(NOMBRE_COLLECTION_CARRO_COMPRAS).child(auntenticacion.getCurrentUser().getUid());
        DatabaseReference dbRefChild = dbRef.child(NOMBRE_ITEMS_CARRO_COMPRAS);
        Query query = dbRefChild.limitToFirst(30);

        FirebaseRecyclerOptions<ItemCarroCompraModel> options =
                new FirebaseRecyclerOptions.Builder<ItemCarroCompraModel>()
                    .setQuery(query, ItemCarroCompraModel.class)
                    .setLifecycleOwner(this)
                    .build();

        if (MODO_PANTALLA_READ_ONLY.equals(modoPantalla)) //Modo pantalla confirmacion
        {
            setAdapterFireBase(true, options, dbRef);
        }
        else
        {
            setAdapterFireBase(false, options, dbRef);
        }
        recyclerViewCarro.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getContext());
        recyclerViewCarro.setLayoutManager(lManager);
        recyclerViewCarro.setAdapter(adapterFireBase);

        Log.i("AdapterFireBase","Fin");

    }

    private void setEmptyCartMsg()
    {
        //if (listItems.isEmpty())
        if (adapterFireBase != null &&  adapterFireBase.getItemCount() > 0)
        {
            containerEmptyCart.setVisibility(View.GONE);
            footerLinearLayout.setVisibility(View.VISIBLE);
            recyclerViewCarro.setVisibility(View.VISIBLE);
        }
        else
        {
            containerEmptyCart.setVisibility(View.VISIBLE);
            footerLinearLayout.setVisibility(View.GONE);
            recyclerViewCarro.setVisibility(View.GONE);
        }
    }
    private void setAdapterFireBase(boolean isReadOnly, FirebaseRecyclerOptions<ItemCarroCompraModel> options , final DatabaseReference dbRef)
    {
        Log.i("setAdapterFireBase","Inicio");

        if (isReadOnly)
        {
            adapterFireBase = new CarroComprasFireBaseAdapter(options, true, getContext()){
                @Override
                public void onDataChanged() {

                    super.onDataChanged();
                    Log.i("setAdapterFireBase","Si ingreso en onDataChanged");
                    cargarTotales(dbRef);
                    setEmptyCartMsg();
                }

                @Override
                public void onError(@NonNull DatabaseError error) {
                    super.onError(error);
                    Log.i("setAdapterFireBase","onCancelled");
                    Toast.makeText(getContext(), "Error en setAdapterFireBase " +error.getMessage() , Toast.LENGTH_LONG).show();
                }
            };

            btnContinuar.setText("Confirmar");
        }
        else
        {
            adapterFireBase = new CarroComprasFireBaseAdapter(options, false, getContext()){
                @Override
                public void onDataChanged() {
                    super.onDataChanged();
                    Log.i("setAdapterFireBase","Si ingreso en onDataChanged");
                    cargarTotales(dbRef);
                    setEmptyCartMsg();
                }

                @Override
                public void onError(@NonNull DatabaseError error) {
                    super.onError(error);
                    Log.i("setAdapterFireBase","onCancelled");
                    Toast.makeText(getContext(), "Error en setAdapterFireBase " +error.getMessage() , Toast.LENGTH_LONG).show();
                }
            };
            btnContinuar.setText("Continuar");
        }

        Log.i("setAdapterFireBase","Fin");
    }

    private void cargarTotales(DatabaseReference dbRef)
    {
        Log.i("cargarTotales","Inicio");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("cargarTotales","onDataChange");
                if (snapshot.exists())
                {
                    Log.i("cargarTotales","Si existe carro compras");
                    //Footer Data
                    tvFooterSubTotal.setText("$" + AdminUtils.formatNumber2Decimal(Double.parseDouble( snapshot.child("subTotal").getValue().toString())));
                    tvFooterDescuentos.setText("$-" + AdminUtils.formatNumber2Decimal( Double.parseDouble(snapshot.child("totalDescuentos").getValue().toString())));
                    tvFooterTotal.setText("$" + AdminUtils.formatNumber2Decimal(Double.parseDouble(snapshot.child("total").getValue().toString())));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("cargarTotales","onCancelled");
                Toast.makeText(getContext(), "Error al cargar Datos Footer " +error.getMessage() , Toast.LENGTH_LONG).show();
            }
        });
        Log.i("cargarTotales","Fin");
    }

}