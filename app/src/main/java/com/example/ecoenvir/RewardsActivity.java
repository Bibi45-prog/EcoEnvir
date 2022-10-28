package com.example.ecoenvir;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RewardsActivity extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    DatabaseReference mbase;
    RewardsAdapter adapter;
    RewardsAdapter rewardsAdapter;
    ArrayList<Reward> list;
    private FirebaseUser currentUser;
    ConstraintLayout constraintLayout;
    private DatabaseReference databaseReference;
    DatabaseReference ImgRef;
    TextView points;
    public RewardsActivity()
    {
        //empty constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rewards, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.rewards_list);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        constraintLayout=view.findViewById(R.id.constraint);
        points=view.findViewById(R.id.user_points);
        ImageView back = view.findViewById(R.id.btn_backReward);

//        back button OnClickListener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                    Direct to profile fragment
                Fragment next_fragment = new Profile();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
            }
        });

        mbase= FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("reward");
        ImgRef=FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("rewardImg");
        databaseReference = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("user");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        list = new ArrayList<>();
        adapter = new RewardsAdapter(this.getContext(),list);
     //   FirebaseRecyclerOptions<reward> options = new FirebaseRecyclerOptions.Builder<reward>().setQuery(mbase,reward.class).build();
       // adapter= new RewardsAdapter(options);
        recyclerView.setAdapter(adapter);




        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentUserID = currentUser.getUid();
                if (dataSnapshot.exists())
                {
                    Long message = dataSnapshot.child(currentUserID).child("points").getValue(Long.class);
                    String p= message.toString();
                    points.setText("Current Points: "+ p +" points");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reward reward = dataSnapshot. getValue(Reward.class);
                    list.add(reward);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return  view;
    }





}
