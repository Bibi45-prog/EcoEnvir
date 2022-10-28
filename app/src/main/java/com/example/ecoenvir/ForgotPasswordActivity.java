package com.example.ecoenvir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        Button retrieve = findViewById(R.id.btnBegin);
        EditText email = findViewById(R.id.et_email);

        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ForgotPasswordActivity.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ForgotPasswordActivity.this, PasswordRequestSuccess.class));
                                }
                                else{
                                    Toast.makeText(ForgotPasswordActivity.this, "Email is not registered", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}