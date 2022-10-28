package com.example.ecoenvir;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends Fragment implements View.OnClickListener{
    private String providerId;
    private ImageView iconEmail, iconPassword, iconProfile;
    private AlertDialog.Builder builder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentUser;
        LinearLayout updateEmail = view.findViewById(R.id.layout_updateEmail);
        LinearLayout changePassword = view.findViewById(R.id.layout_changePassword);
        LinearLayout editProfile = view.findViewById(R.id.layout_editProfile);
        LinearLayout logout = view.findViewById(R.id.layout_logout);
        LinearLayout history= view.findViewById(R.id.layout_history);
        LinearLayout help=view.findViewById(R.id.layout_help);
        TextView name = view.findViewById(R.id.tv_profileName);
        TextView tvUsername = view.findViewById(R.id.tv_username);
        TextView rewardPoints = view.findViewById(R.id.tv_accumulatedPoint);
        CircleImageView profilePic = view.findViewById(R.id.iv_profile_picture);
        LinearLayout reward = view.findViewById(R.id.points_btn);
        LinearLayout rewardhis = view.findViewById(R.id.points_history_btn);
        iconEmail = view.findViewById(R.id.iv_email);
        iconPassword = view.findViewById(R.id.iv_password);
        iconProfile = view.findViewById(R.id.iv_profile);

//display profile details
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("user");


        if (currentUser != null) {
            boolean emailVerified = currentUser.isEmailVerified();
//            if email is not verified
            if(!emailVerified)
            {
                builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to verify your "+currentUser.getEmail()+"?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                currentUser.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getContext(),"Verification email sent. After verifying, please sign in again",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                Toast.makeText(getContext(),"Please verify as soon as possible", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Email Verification");
                alert.show();
            }
            name.setText(currentUser.getDisplayName());
            Glide.with(getContext()).load(currentUser.getPhotoUrl()).into(profilePic);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String currentUserID = currentUser.getUid();
                        String username = dataSnapshot.child(currentUserID).child("username").getValue(String.class);
                        Long points = dataSnapshot.child(currentUserID).child("points").getValue(Long.class);
                        providerId = dataSnapshot.child(currentUserID).child("providerId").getValue(String.class);
                        tvUsername.setText(username);
                        rewardPoints.setText(points.toString());
                        if(providerId !=null && !providerId.equals("firebase"))
                        {
                            iconEmail.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
                            iconPassword.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
                            iconProfile.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        updateEmail.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        editProfile.setOnClickListener(this);
        history.setOnClickListener(this);
        help.setOnClickListener(this);
        logout.setOnClickListener(this);
        reward.setOnClickListener(this);
        rewardhis.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        Fragment next_fragment;
        switch (view.getId())
        {
            case R.id.layout_updateEmail:
                if(providerId.equals("firebase")){
                    next_fragment = new UpdateEmail();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
                }
                else{
                    Toast.makeText(getActivity(), "Third party user not allow to edit email address", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.layout_changePassword:
                if(providerId.equals("firebase")) {
                    next_fragment = new ChangePassword();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, next_fragment).commit();
                }
                else{
                    Toast.makeText(getActivity(), "Third party user not allow to edit password", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.layout_editProfile:
                if(providerId.equals("firebase")) {
                    next_fragment = new EditProfileDetails();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, next_fragment).commit();
                }
                else{
                    Toast.makeText(getActivity(), "Third party user not allow to edit profile details", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.layout_history:
                next_fragment = new RequestHistory();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, next_fragment).commit();
                break;
            case R.id.layout_help:
                next_fragment = new HelpPage();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
                break;

            case R.id.layout_logout:
                builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                // verified whether user sign out successfully
                                if(currentUser == null)
                                {
                                    Toast.makeText(getActivity(), "Logout Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(),LoginActivity.class));
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Logout Failed", Toast.LENGTH_SHORT).show();
                                }
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
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.drawable.red_button);
                    }
                });
                alert.setTitle("Logout");
                alert.show();

                break;

            case R.id.points_btn:
                next_fragment = new RewardsActivity();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).addToBackStack(null).commit();
                break;

            case R.id.points_history_btn:
                next_fragment= new RewardsHistory();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).addToBackStack(null).commit();
                break;
        }
    }
}
