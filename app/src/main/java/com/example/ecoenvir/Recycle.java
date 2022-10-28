package com.example.ecoenvir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Recycle extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycle, container, false);
    }

    ImageView imageView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase itemDescriptionForm;

        imageView = view.findViewById(R.id.imgPaper);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment next_fragment = new ItemDescription();
                Bundle bundle = new Bundle();
                bundle.putString("recycleName", "paper");
                next_fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
            }
        });

        ImageView btnBack = view.findViewById(R.id.btnBack6);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Direct to home page
                Fragment next_fragment = new HomePage();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
            }
        });


        imageView = view.findViewById(R.id.imgPlastic);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment next_fragment = new ItemDescription();
                Bundle bundle = new Bundle();
                bundle.putString("recycleName", "plastic");
                next_fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
            }
        });

        imageView = view.findViewById(R.id.imgGlass);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment next_fragment = new ItemDescription();
                Bundle bundle = new Bundle();
                bundle.putString("recycleName", "glass");
                next_fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
            }
        });

        imageView = view.findViewById(R.id.imgLightBulbs);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment next_fragment = new ItemDescription();
                Bundle bundle = new Bundle();
                bundle.putString("recycleName", "lightBulbs");
                next_fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
            }
        });

        imageView = view.findViewById(R.id.imgOrganic);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment next_fragment = new ItemDescription();
                Bundle bundle = new Bundle();
                bundle.putString("recycleName", "organic");
                next_fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
            }
        });

        imageView = view.findViewById(R.id.imgMixed);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment next_fragment = new ItemDescription();
                Bundle bundle = new Bundle();
                bundle.putString("recycleName", "mixed");
                next_fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
            }
        });

        imageView = view.findViewById(R.id.imgMetal);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment next_fragment = new ItemDescription();
                Bundle bundle = new Bundle();
                bundle.putString("recycleName", "metal");
                next_fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
            }
        });

        imageView = view.findViewById(R.id.imgWaste);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment next_fragment = new ItemDescription();
                Bundle bundle = new Bundle();
                bundle.putString("recycleName", "waste");
                next_fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
            }
        });

        imageView = view.findViewById(R.id.imgBatteries);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment next_fragment = new ItemDescription();
                Bundle bundle = new Bundle();
                bundle.putString("recycleName","batteries");
                next_fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
            }
        });





    }
}