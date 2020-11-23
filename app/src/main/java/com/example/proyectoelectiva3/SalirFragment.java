package com.example.proyectoelectiva3;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

public class SalirFragment extends Fragment {

    private FirebaseAuth auntenticacion;

    public SalirFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auntenticacion = FirebaseAuth.getInstance();
        auntenticacion.signOut();
        Intent i= new Intent(getActivity().getApplicationContext(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_salir, container, false);
    }
}