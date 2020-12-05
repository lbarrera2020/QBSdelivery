package com.example.proyectoelectiva3;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.proyectoelectiva3.FormasPago.FormasPagoEntity;
import com.example.proyectoelectiva3.admin.AdminUtils;
import com.example.proyectoelectiva3.admin.ProductoCategoriaDummy;
import com.example.proyectoelectiva3.admin.articulosEntity;
import com.example.proyectoelectiva3.carrocompras.CarroComprasModel;
import com.example.proyectoelectiva3.carrocompras.CarroComprasRepository;
import com.example.proyectoelectiva3.carrocompras.DireccionesCliente;
import com.example.proyectoelectiva3.carrocompras.EstadosPedido;
import com.example.proyectoelectiva3.carrocompras.IGetSingleObject;
import com.example.proyectoelectiva3.carrocompras.IStringResultProcess;
import com.example.proyectoelectiva3.carrocompras.InformacionCliente;
import com.example.proyectoelectiva3.carrocompras.ItemCarroCompraModel;
import com.example.proyectoelectiva3.carrocompras.ItemPedidoModel;
import com.example.proyectoelectiva3.carrocompras.MsjFinalData;
import com.example.proyectoelectiva3.carrocompras.PedidoModel;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class FinalizarCompraFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Spinner spinerDireccion;
    private Button btnFinalizarCompra;
    private EditText fechaEntrega, horaEntrega;
    private CarroComprasRepository repoDB;
    private FirebaseAuth auntenticacion;
    private InformacionCliente infoCliente;
    private RadioGroup rdbGroup;
    private FormasPagoEntity formaPagoSelected;

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
        btnFinalizarCompra = fragment.findViewById(R.id.btnFinalizarCompra);
        fechaEntrega = fragment.findViewById(R.id.edtFechaEntrega);
        horaEntrega = fragment.findViewById(R.id.edtHoraEntrega);
        rdbGroup = fragment.findViewById(R.id.rdbGroup);

        auntenticacion = FirebaseAuth.getInstance();

        btnFinalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidData())
                {

                    //Data del carro de compras
                    repoDB = new CarroComprasRepository();
                    repoDB.getCarroComprasByCliente(auntenticacion.getCurrentUser().getUid(), new IGetSingleObject<CarroComprasModel>() {
                        @Override
                        public void onCallBackSuccess(CarroComprasModel carro) {
                            if (carro!= null)
                            {
                                //Get Data para el object Pedidos
                                PedidoModel nuevoPedido = new PedidoModel();
                                nuevoPedido.setId_pedido(UUID.randomUUID().toString());
                                nuevoPedido.setFechaCreacion(AdminUtils.getNowDateToString());
                                nuevoPedido.setFechaEntrega(fechaEntrega.getText().toString());
                                nuevoPedido.setHoraEntrega(horaEntrega.getText().toString());
                                nuevoPedido.setFechaPago("");
                                infoCliente.setDirecciones(null); //No son utiles las N direcciones
                                nuevoPedido.setCliente(infoCliente);
                                nuevoPedido.setDireccionEnvio((DireccionesCliente)spinerDireccion.getSelectedItem());
                                nuevoPedido.setFormaPago(formaPagoSelected);
                                nuevoPedido.setRepartidor(null);
                                EstadosPedido est = EstadosPedido.REC;
                                nuevoPedido.setEstado(est.toString());
                                //Data carro de compras
                                nuevoPedido.setId_carrito(carro.getIdCart());
                                nuevoPedido.setDescuentos(carro.getDescuentos());
                                nuevoPedido.setRebajas(carro.getRebajas());
                                nuevoPedido.setSubTotal(carro.getSubTotal());
                                nuevoPedido.setTotal(carro.getTotal());
                                nuevoPedido.setTotalDescuentos(carro.getTotalDescuentos());

                                ItemPedidoModel ipe = null;
                                List<ItemPedidoModel> items = new ArrayList<>();
                                for (ItemCarroCompraModel ica: carro.getItems())
                                {
                                    ipe = new ItemPedidoModel();
                                    ipe.setIdItem(UUID.randomUUID().toString());
                                    ipe.setCantidad(ica.getCantidad());
                                    ipe.setDescuento(ica.getDescuento());
                                    ipe.setIdPedido(nuevoPedido.getId_pedido());
                                    ipe.setIdProducto(ica.getIdProducto());
                                    ipe.setNombreImagen(ica.getNombreImagen());
                                    ipe.setNombreProducto(ica.getNombreProducto());
                                    ipe.setPrecio(ica.getPrecio());
                                    ipe.setRebaja(ica.getRebaja());
                                    ipe.setSubTotal(ica.getSubTotal());
                                    ipe.setTotal(ica.getTotal());
                                    items.add(ipe);
                                }
                                nuevoPedido.setItems(items);
                                final MsjFinalData data = new MsjFinalData(nuevoPedido.getId_pedido(), nuevoPedido.getId_carrito(), nuevoPedido.getCliente());

                                //Guardar Pedido y mandar el ID al Fragment MsjFinalCompraFragment
                                repoDB.crearPedido(nuevoPedido, new IStringResultProcess() {
                                    @Override
                                    public void onCallBackSuccess(String result) {
                                        FragmentManager fm = getParentFragmentManager();
                                        fm.beginTransaction().replace(R.id.nav_host_fragment, MsjFinalCompraFragment.newInstance(data,"")).commit();
                                    }

                                    @Override
                                    public void onCallBackFail(String msjError) {
                                        Toast.makeText(getContext(), "Error creando pedido" , Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                            else
                                Toast.makeText(getContext(), "Carro de compras Null,no puede continuar" , Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCallBackFail(String msjError) {
                            Toast.makeText(getContext(), "Ocurrio un error al recuperar info de carro de compras" , Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });

        rdbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rdb = (RadioButton)group.findViewById(checkedId);
                formaPagoSelected = (FormasPagoEntity) rdb.getTag();
            }
        });

        initDateTimePickers();
        cargarDatosSpinner();
        cargarFormasPago();
        return fragment;
    }

    private void initDateTimePickers() {
        fechaEntrega.setInputType(InputType.TYPE_NULL);
        horaEntrega.setInputType(InputType.TYPE_NULL);

        fechaEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(fechaEntrega);
            }
        });

        horaEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(horaEntrega);
            }
        });

    }

    private void showDateDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
                editText.setText(formater.format(calendar.getTime()));
                editText.setError(null);
            }
        };

        DatePickerDialog dpd = new DatePickerDialog(getContext(), dateSetListener, calendar.get(Calendar.YEAR),
                              calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dpd.setTitle("Fecha Entrega");
        dpd.setButton(DialogInterface.BUTTON_POSITIVE, "ACEPTAR",dpd);
        dpd.show();
    }

    private void showTimeDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                if (!TextUtils.isEmpty(fechaEntrega.getText()))
                {
                    Calendar now = Calendar.getInstance();
                    Calendar dateSelected = Calendar.getInstance();
                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
                    Date dateSelectedAux = null;
                    try {
                        dateSelectedAux = format.parse(fechaEntrega.getText().toString());
                        dateSelected.setTime(dateSelectedAux);
                        if (dateSelected.after(now))
                        {
                            editText.setText(formater.format(calendar.getTime()));
                            editText.setError(null);
                        }
                        else
                        {
                            if (calendar.getTimeInMillis() >  now.getTimeInMillis() )
                            {
                                editText.setText(formater.format(calendar.getTime()));
                                editText.setError(null);
                            }
                            else
                            {
                                editText.setText("");
                                Toast.makeText(getContext(), "Hora invalida" , Toast.LENGTH_LONG).show();
                            }

                        }

                    } catch (ParseException e) {
                        editText.setText("");
                        Toast.makeText(getContext(), "Ocurrio un error con las fecha seleccionada " , Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                else
                {
                    editText.setText("");
                    Toast.makeText(getContext(), "Seleccione una fecha primero" , Toast.LENGTH_LONG).show();
                }


            }
        };

        TimePickerDialog tpd = new TimePickerDialog(getContext(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                                                    calendar.get(Calendar.MINUTE),false);

        tpd.setTitle("Hora Entrega");
        tpd.setButton(DialogInterface.BUTTON_POSITIVE, "ACEPTAR",tpd);
        tpd.show();
    }

    private boolean isValidData()
    {
        if (!TextUtils.isEmpty(fechaEntrega.getText()))
        {
            if (!TextUtils.isEmpty(horaEntrega.getText()))
            {
                DireccionesCliente dire = (DireccionesCliente)spinerDireccion.getSelectedItem();
                if (dire != null)
                {
                    if (formaPagoSelected != null)
                    {
                        return true;
                    }
                    else
                        Toast.makeText(getContext(), "Debe seleccionar una forma de pago" , Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Debe indicar la dirección de envio" , Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                horaEntrega.setError("Hora de entrega es requerida");
                horaEntrega.requestFocus();
            }
        }
        else
        {
            fechaEntrega.setError("Fecha de entrega es requerida");
            fechaEntrega.requestFocus();
        }
        return false;
    }

    private void cargarDatosSpinner()
    {
        repoDB = new CarroComprasRepository();

        //get informacion del cliente..
        repoDB.getInformationClient(auntenticacion.getCurrentUser().getUid() , new IGetSingleObject<InformacionCliente>() {
            @Override
            public void onCallBackSuccess(InformacionCliente c) {
                if (c != null)
                {
                    infoCliente = c;
                    ArrayAdapter<DireccionesCliente> adapter = new ArrayAdapter<DireccionesCliente>(getContext(), android.R.layout.simple_spinner_dropdown_item, c.getDirecciones());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinerDireccion.setAdapter(adapter);
                }
            }

            @Override
            public void onCallBackFail(String msjError) {
                Toast.makeText(getContext(), "Error al cargar direcciones" , Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarFormasPago()
    {
        repoDB = new CarroComprasRepository();
        repoDB.getInformationPaymentForms(new IGetSingleObject<List<FormasPagoEntity>>() {
            @Override
            public void onCallBackSuccess(List<FormasPagoEntity> listData) {
                if (listData != null && !listData.isEmpty())
                {
                    for (FormasPagoEntity obj: listData)
                    {
                        if ("activo".equalsIgnoreCase(obj.getEstado()))
                        {
                            RadioButton rdb = new RadioButton(getContext());
                            rdb.setText(obj.getNombre());
                            rdb.setTag(obj); //Save All the object "Forma de pago"
                            rdbGroup.addView(rdb);
                        }
                    }
                }
            }

            @Override
            public void onCallBackFail(String msjError) {
                Toast.makeText(getContext(), "Error al cargar formas de pago" , Toast.LENGTH_LONG).show();
            }
        });
    }
}