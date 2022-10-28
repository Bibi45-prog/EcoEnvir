package com.example.ecoenvir;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class HelpPage extends Fragment {

    RecyclerView recyclerView;
    private ImageView back;
    List<FAQ> faqList;
    public HelpPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_help_page, container, false);
        recyclerView = v.findViewById(R.id.recyclerview);
        back = v.findViewById(R.id.btn_backHelp);
        initData();
        setRecyclerView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment nextFragment = new Profile();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, nextFragment).commit();
            }
        });

        return v;
    }


    private void setRecyclerView() {
        FAQAdapter faqAdapter = new FAQAdapter(faqList);
        recyclerView.setAdapter(faqAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void initData() {
        faqList=new ArrayList<>();
        //faqList.add(new FAQ("Q: Is there any way I can convert my points into money?","A: No, all points are only used to redeem items as shown in the rewards page."));
        faqList.add(new FAQ("Q: I forgot my password and can’t login, what can I do?","A: You may click on the forgot password link at the login page and reset your password."));
        faqList.add(new FAQ("Q: Can my points be transferred over to another account?","A: No, all points gained on an account cannot be transferred to another account."));
        faqList.add(new FAQ("Q: How do I cancel my appointment?","A: You may contact the designated recycling centre 24 hours in advance to cancel the appointment. "));



    }
}