package com.example.ecoenvir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RequestHistory extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference requestRef;
    FirebaseUser user;
    private ArrayList<RecycleRequestHistory> list;
    RecycleRequestAdapter adapter;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.request_history_fragment,container,false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.requestRecycler);

        ImageView backBut = view.findViewById(R.id.but_back);
        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                    Direct to profile fragment
                Fragment next_fragment = new Profile();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
            }
        });
        user= FirebaseAuth.getInstance().getCurrentUser();
        requestRef = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                .child("requests").child(user.getUid()); //need to get requestID i think.
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        list = new ArrayList<>();
        adapter = new RecycleRequestAdapter(this.getContext(),list);
        recyclerView.setAdapter(adapter);

        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long childrenCount;
                List<String> childName = new ArrayList();
                list.clear();
                childrenCount = dataSnapshot.getChildrenCount(); //get children count
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    childName.add(child.getKey());
                }
                for (int i = 0; i < childName.size(); i++) {
                    requestRef = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                            .child("requests").child(user.getUid()).child(childName.get(i));
                    int finalI = i;

                    requestRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            RecycleRequestHistory requestHistory = dataSnapshot.getValue(RecycleRequestHistory.class);
                            requestHistory.requestID = dataSnapshot.getKey();
                            requestHistory.categories = childName.get(finalI);
                            list.add(requestHistory);

                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

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
