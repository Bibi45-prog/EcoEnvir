package com.example.ecoenvir;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RewardHistoryAdapter extends RecyclerView.Adapter<RewardHistoryAdapter.RewardHistoryHolder> {
    Context context;
    ArrayList<RewardHistory> historyList;
    FirebaseUser currentUser;
    private DatabaseReference rewardReference;

    public RewardHistoryAdapter(Context context, ArrayList<RewardHistory> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public RewardHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reward_history_layout,parent,false);
        return new RewardHistoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardHistoryHolder holder, int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        rewardReference = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("reward");
        RewardHistory rewardHistory = historyList.get(position);
        String rewardId = rewardHistory.getRewardId();

        rewardReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child(rewardId).child("name").getValue(String.class);
                String points = dataSnapshot.child(rewardId).child("purchasePoint").getValue(String.class);
                String image = dataSnapshot.child(rewardId).child("imageUrl").getValue(String.class);
//                Get date of redemption
                long timestamp = rewardHistory.getTimestamp();
                DateFormat dateFormat = DateFormat.getDateInstance();
                Date netDate = (new Date(timestamp));
                String date = dateFormat.format(netDate);

                Glide.with(context).load(image).into(holder.rewardImage);
                holder.rewardName.setText(name);
                holder.redeemPoint.setText(points +" points");
                holder.redeemDate.setText("Redeemed " + date);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class RewardHistoryHolder extends RecyclerView.ViewHolder{
        TextView rewardName, redeemPoint, redeemDate;
        ImageView rewardImage;

        public RewardHistoryHolder(@NonNull View itemView){
            super(itemView);

            rewardName = itemView.findViewById(R.id.tv_rewardName);
            redeemPoint = itemView.findViewById(R.id.tv_points);
            redeemDate = itemView.findViewById(R.id.tv_redeemDate);
            rewardImage = itemView.findViewById(R.id.iv_rewardHistoryImage);
        }
    }
}
