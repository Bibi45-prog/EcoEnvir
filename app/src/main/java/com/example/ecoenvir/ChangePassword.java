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

public class ChangePassword extends Fragment {

    public ChangePassword() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText password = view.findViewById(R.id.et_currentPassword);
        EditText newPassword = view.findViewById(R.id.et_newPassword);
        EditText reNewPassword = view.findViewById(R.id.et_reNewPassword);
        ImageView backBut = view.findViewById(R.id.but_back);
        Button confirm = view.findViewById(R.id.btn_changePassword);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                    Direct to profile fragment
                Fragment next_fragment = new Profile();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtPassword = password.getText().toString();
                String txtNewPassword = newPassword.getText().toString();
                String txtReNewPassword = reNewPassword.getText().toString();

                if (txtPassword.equals("") || txtNewPassword.equals("") || txtReNewPassword.equals("")) {
                    Toast.makeText(getActivity(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (txtNewPassword.equals(txtReNewPassword)) {
                        if (txtNewPassword.length() < 6) {
                            Toast.makeText(getActivity(), "The new password should more than 6 characters", Toast.LENGTH_LONG).show();
                        } else {
                            String email = currentUser.getEmail();
                            AuthCredential credential = EmailAuthProvider.getCredential(email, txtPassword);
                            currentUser.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            currentUser.updatePassword(txtNewPassword)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getActivity(), "Your password updated successful", Toast.LENGTH_SHORT).show();
                                                                Fragment next_fragment = new Profile();
                                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, next_fragment).commit();
                                                            } else {
                                                                Toast.makeText(getActivity(), "Your password updated failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(getActivity(), "Your passwords are not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}