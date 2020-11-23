package com.example.proyectoelectiva3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class HomeCarruselFragment extends Fragment {

    private  int[] mImages = new int[]{
            R.drawable.comali2,R.drawable.juanv2,R.drawable.juanv3
    };

    public HomeCarruselFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup homeFragment = (ViewGroup)inflater.inflate(R.layout.fragment_home_carrusel, null);

        CarouselView carouselView = homeFragment.findViewById(R.id.carrusel);
        carouselView.setPageCount(mImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(mImages[position]);

            }
        });

        return homeFragment;
    }
}