package com.example.ecoenvir;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecycleRequestAdapter extends RecyclerView.Adapter<RecycleRequestAdapter.RRHolder> {
    Context context;
    ArrayList<RecycleRequestHistory> Rlist;
    FirebaseUser currentUser;
    DatabaseReference requestReference;
    DatabaseReference userPoints;
    private AlertDialog.Builder builder;



    public RecycleRequestAdapter (Context context, ArrayList<RecycleRequestHistory> Rlist)
    {
        this.context=context;
        this.Rlist=Rlist;
    }

    @NonNull
    @Override
    public RRHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycle_request_history_layout,parent,false);
        return new RRHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RRHolder holder, int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        requestReference= FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app").
                getReference().child("requests");
        userPoints=FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("user");
        RecycleRequestHistory requestHistory = Rlist.get(position);


        Glide.with(context).load(requestHistory.getImage()).into(holder.imgUrl);
        holder.date.setText(requestHistory.getDate());
        holder.time.setText(requestHistory.getTime());
        holder.address.setText(requestHistory.getAddress());
        holder.description.setText(requestHistory.getDescription());
        holder.method.setText(requestHistory.getMethod());
        holder.quantity.setText(requestHistory.getQuantity());

        if(requestHistory.isCompleted() == true){
            holder.complete.setVisibility(View.GONE);
            holder.tv_completed.setText("Completed");
        }

        Long p=Long.parseLong(requestHistory.getQuantity());
        userPoints.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentUserID= currentUser.getUid();
                if (dataSnapshot.exists()) {
                    final Long points = dataSnapshot.child(currentUserID).child("points").getValue(Long.class); //get points of user

                    holder.complete.setOnClickListener(new View.OnClickListener() {
                        @Override

                        public void onClick(View view) {
                           // holder.complete.setEnabled(false);
                            builder = new AlertDialog.Builder(context);
                            builder.setMessage("Do you confirm that this request has been completed?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                     userPoints.child(currentUserID).child("points").setValue(points+p);
                                    //adding points into user points
                                    requestReference.child(currentUserID).child(requestHistory.getCategories()).child(requestHistory.getRequestID()).child("completed").setValue(true);
                                    Toast.makeText(context, "Request Completed, "+p+" Points Added",Toast.LENGTH_SHORT).show();
//                                   remove button
                                    notifyDataSetChanged();

                                }
                            })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                            AlertDialog alert=builder.create();
                            alert.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialogInterface) {
                                    alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.drawable.red_button);
                                }
                            });
                            alert.setTitle(requestHistory.getDescription());
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


    @Override
    public int getItemCount()
    {
        return Rlist.size();
    }
    public static class RRHolder extends RecyclerView.ViewHolder
    {
        TextView date, time, description, address, method, quantity, tv_completed;
        ImageView imgUrl;
        Button complete;
        public RRHolder(@NonNull View itemView)
        {
            super(itemView);
            date = itemView.findViewById(R.id.requestDate);
            time = itemView.findViewById(R.id.requestTime);
            description = itemView.findViewById(R.id.requestDesc);
            address = itemView.findViewById(R.id.reqeustAddress);
            imgUrl = itemView.findViewById(R.id.iv_requestHistoryImage);
            method = itemView.findViewById(R.id.requestMethod);
            quantity = itemView.findViewById(R.id.requestQuantity);
            complete = itemView.findViewById(R.id.completebtn);
            tv_completed = itemView.findViewById(R.id.tv_completed);


        }
    }

}
