package com.example.ecoenvir;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.rewardsViewHolder> {
    Context context;
    ArrayList<Reward> list;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private AlertDialog.Builder builder;




    public RewardsAdapter(Context context, ArrayList<Reward> list) {
        this.context = context;
        this.list = list;
    }

    public static class rewardsViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, purchasePoint,redeem;
        ImageView rewardImage;
        public rewardsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_rewardName);
            purchasePoint = itemView.findViewById(R.id.tv_points);
            rewardImage = itemView.findViewById(R.id.iv_rewardHistoryImage);
            redeem = itemView.findViewById(R.id.tv_redeemDate);
        }
    }

    @NonNull
    @Override
    public RewardsAdapter.rewardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.rewards_list_layout,parent,false);
        return new rewardsViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull rewardsViewHolder holder, int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("user");
        Reward reward = list.get(position);
        holder.name.setText(reward.getName());
        holder.purchasePoint.setText(reward.getPurchasePoint() + " points");
        Glide.with(this.context).load(reward.getImageUrl()).into(holder.rewardImage);


        if (currentUser != null)
        {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String currentUserID = currentUser.getUid();
                    Long pp = Long.parseLong(reward.getPurchasePoint()); //get all purchase points
                    if (dataSnapshot.exists()) {
                        final Long points = dataSnapshot.child(currentUserID).child("points").getValue(Long.class); //get points of user

                        holder.redeem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                builder = new AlertDialog.Builder(context);
                                builder.setMessage("Are you sure to redeem " + reward.getName() + " with " + pp.toString() + " points?")
                                        .setCancelable(false)
                                        .setPositiveButton("Redeem", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (points >= pp) // need to minus the points after redemption
                                                {
                                                    Toast.makeText(context, "Successful Redemption", Toast.LENGTH_SHORT).show();
                                                    Map rewardHistory = new HashMap();
//                                  Remove leading zeroes of output of holder.getAdapterPosition()
                                                    String rewardNum = String.valueOf(holder.getAdapterPosition() + 1).replaceFirst("0", "");
                                                    rewardHistory.put("timestamp", ServerValue.TIMESTAMP);
                                                    rewardHistory.put("rewardId", "rw" + rewardNum);

//                                  insert record into firebase
                                                    databaseReference.child(currentUserID).child("rewardHistory").push().setValue(rewardHistory);
                                                    databaseReference.child(currentUserID).child("points").setValue(points - pp);
                                                } else
                                                    Toast.makeText(context, "Failed redemption, insufficient points", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface dialogInterface) {
                                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.drawable.red_button);
                                    }
                                });
                                alert.setTitle(reward.getName());
                                alert.show();


                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
