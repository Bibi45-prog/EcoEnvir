package com.example.ecoenvir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class RewardsHistory extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference userRewardHistoryReference;
    private ConstraintLayout constraintLayout;
    private ArrayList<RewardHistory> list;
    RewardHistoryAdapter adapter;
    public RewardsHistory()
    {
        //empty constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rewards_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rewardHistoryList);
        constraintLayout = view.findViewById(R.id.constraint_history);
        ImageView back = view.findViewById(R.id.btn_backHistory);

        //        back button OnClickListener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                    Direct to profile fragment
                Fragment next_fragment = new Profile();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userRewardHistoryReference = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("user").child(user.getUid()).child("rewardHistory");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        list = new ArrayList<>();
        adapter = new RewardHistoryAdapter(this.getContext(),list);
        recyclerView.setAdapter(adapter);
        list.clear();
        userRewardHistoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    RewardHistory rewardHistory = dataSnapshot1.getValue(RewardHistory.class);
                    list.add(rewardHistory);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
