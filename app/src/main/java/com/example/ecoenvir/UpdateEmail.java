package com.example.ecoenvir;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UpdateEmail extends Fragment {

    public UpdateEmail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText currentEmail = view.findViewById(R.id.et_currentEmail);
        EditText password = view.findViewById(R.id.et_password1);
        EditText newEmail = view.findViewById(R.id.et_newEmail);
        EditText reNewEmail = view.findViewById(R.id.et_reNewEmail);
        ImageView backBut = view.findViewById(R.id.but_back);
        Button confirm = view.findViewById(R.id.btn_updateEmail);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentEmail.setText(currentUser.getEmail());

        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                    Direct to profile fragment
                Fragment next_fragment = new Profile();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, next_fragment).commit();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtEmail = currentEmail.getText().toString();
                String txtPassword = password.getText().toString();
                String txtNewEmail = newEmail.getText().toString();
                String txtReNewEmail = reNewEmail.getText().toString();
                if (txtEmail.equals("") || txtPassword.equals("") || txtNewEmail.equals("") || txtReNewEmail.equals("")) {
                    Toast.makeText(getActivity(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (txtNewEmail.equals(txtReNewEmail)) {
//                    re-authenticate the user as changing primary email is a sensitive action
                        AuthCredential credential = EmailAuthProvider.getCredential(txtEmail, txtPassword);
                        currentUser.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        currentUser.updateEmail(txtNewEmail)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
//                                                        Update email in firebase database
                                                            DatabaseReference userReference = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("user");
                                                            userReference.child(currentUser.getUid()).child("email").setValue(txtNewEmail);
                                                            Toast.makeText(getActivity(), "Your email address updated successful", Toast.LENGTH_SHORT).show();
                                                            Fragment next_fragment = new Profile();
                                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, next_fragment).commit();


                                                        } else {
                                                            Toast.makeText(getActivity(), "Your email address updated failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                });

                    } else {
                        Toast.makeText(getActivity(), "The email addresses are not match", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}