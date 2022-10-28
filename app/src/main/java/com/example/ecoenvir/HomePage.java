package com.example.ecoenvir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import android.text.format.DateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class HomePage extends Fragment {

    private int lastQuiz = 0;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homepage, container, false);
    }

    ImageView imageView;
    ImageView knowledge, change;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.imageView4);
        knowledge = view.findViewById(R.id.imageView3);
        change = view.findViewById(R.id.imageView6);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        sharedPreferences = getActivity().getSharedPreferences(currentUser.getUid(),0);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                get data from sharedPreferences
                lastQuiz = sharedPreferences.getInt("lastQuiz", 0);
                int count = sharedPreferences.getInt("count", 0);

//                get Today date
                Date date = new Date();
                int today = Integer.parseInt(DateFormat.format("dd",   date).toString());
                if (lastQuiz == 0 || today > lastQuiz) //if it is a new day
                {
                    count = 0;
                }
                if (count == 0)
                {
                    Intent intent = new Intent(getActivity(), Quiz.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getActivity(), "You can only answer quiz once a day",Toast.LENGTH_SHORT).show();
                }

            }
        });

        knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Info();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Recycle();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
            }
        });
    }
}
