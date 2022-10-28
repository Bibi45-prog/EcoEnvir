package com.example.ecoenvir;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class SelectDeliveryMethod1 extends Fragment {

    public String recycleName = "";
    public RequestHelperClass currentRequest;
    private static final String METHOD_PICKUP = "pickup";
    private static final String METHOD_DELIVERY = "delivery";

    RadioGroup radioGroup;
    LinearLayout locationContainer;

    TextView phone, address, hours, website;

    private DatabaseReference databaseReference;
    FirebaseUser currentUser;
    List<String> idsList = new ArrayList<>();
    String recycleCenterKey = "r1";
    RecycleCenter recycleCenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_delivery_method1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setIds();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        ImageView btnBack = view.findViewById(R.id.btnBack6);
        btnBack.setOnClickListener(view12 -> {
            // Direct to Recycle page
            Fragment next_fragment = new ItemDescription();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, next_fragment).commit();
        });

        Button sendRequest = view.findViewById(R.id.btnSendRequest);
        sendRequest.setOnClickListener(view1 -> {
            sendRequestWithData();
        });

        locationContainer = view.findViewById(R.id.location_cont);
        phone = view.findViewById(R.id.field_phone);
        address = view.findViewById(R.id.field_address);
        hours = view.findViewById(R.id.field_hours);
        website = view.findViewById(R.id.field_website);

        radioGroup = view.findViewById(R.id.radio_group);
        currentRequest.setMethod(METHOD_PICKUP);

        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (radioGroup.getCheckedRadioButtonId() == R.id.radio_one) {
                locationContainer.setVisibility(View.GONE);
                currentRequest.setMethod(METHOD_PICKUP);
            } else {
                getAndSetNearestLocation();
                locationContainer.setVisibility(View.VISIBLE);
                currentRequest.setMethod(METHOD_DELIVERY);
            }
        });
    }

    private void getAndSetNearestLocation() {
        databaseReference = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("recycleCenter").child(recycleCenterKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recycleCenter = dataSnapshot.getValue(RecycleCenter.class);
                if (recycleCenter != null) {
                    String phone_str = recycleCenter.getPhone();
                    String address_str = recycleCenter.getAddress();
                    String hours_str = recycleCenter.getHours().get("monday");
                    String website_str = recycleCenter.getWebsite();
                    Log.d("theS", "onDataChange: " + recycleCenter.toString());
                    phone.setText(phone_str);
                    address.setText(address_str);
                    hours.setText(hours_str);
                    website.setText(website_str);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sendRequestWithData() {
        databaseReference = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("requests").child(currentUser.getUid()).child(recycleName);
        String id = UUID.randomUUID().toString();
        databaseReference.child(id).setValue(currentRequest);
        if (currentRequest.getMethod().equals(METHOD_DELIVERY)) {
            databaseReference.child(id).child("recycleCenterId").setValue(recycleCenterKey);
        }


        Fragment next_fragment = new RequestSuccessful1();
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, next_fragment).commit();
    }

    public void setIds() {
        idsList.add("r1");
        idsList.add("r2");
        idsList.add("r3");
        idsList.add("r4");
        idsList.add("r5");
        idsList.add("r6");
        idsList.add("r7");
        idsList.add("r8");
        idsList.add("r9");
        idsList.add("r10");
        idsList.add("r11");
        idsList.add("r12");
        idsList.add("r13");
        idsList.add("r14");
        idsList.add("r15");
        idsList.add("r16");
        idsList.add("r17");
        idsList.add("r18");
        idsList.add("r19");
        idsList.add("r20");
        idsList.add("r21");
        idsList.add("r22");
        idsList.add("r23");
        idsList.add("r24");
        idsList.add("r25");
        idsList.add("r26");
        idsList.add("r27");
        idsList.add("r28");
        idsList.add("r29");
        idsList.add("r30");

        recycleCenterKey = idsList.get(new Random().nextInt(idsList.size()));
    }
}