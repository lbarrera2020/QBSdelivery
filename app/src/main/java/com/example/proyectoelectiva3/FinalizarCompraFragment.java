package com.example.proyectoelectiva3;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.proyectoelectiva3.admin.ProductoCategoriaDummy;

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

        btnFinalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidData())
                {
                    //Get Data para el object Pedidos

                    //Guardar Pedido y mandar el ID al Fragment MsjFinalCompraFragment

                    FragmentManager fm = getParentFragmentManager();
                    fm.beginTransaction().replace(R.id.nav_host_fragment, MsjFinalCompraFragment.newInstance(mParam1,"")  ).commit();
                }
            }
        });

        initDateTimePickers();
        cargarDatosSpinner();
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
                return true;
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