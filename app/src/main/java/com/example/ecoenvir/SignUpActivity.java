package com.example.ecoenvir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    Button signUp;
    EditText name, email, password, repass;
    FirebaseAuth auth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        signUp = findViewById(R.id.btnBegin);
        name = findViewById(R.id.et_name);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        repass = findViewById(R.id.et_repass);


        TextView btn = findViewById(R.id.tv_haveAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_name = name.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_repass = repass.getText().toString();

                if(txt_name.equals("") || txt_email.equals("") || txt_password.equals("") || txt_repass.equals(""))
                {
                    Toast.makeText(SignUpActivity.this, "Please enter all the field", Toast.LENGTH_SHORT).show();
                }
                else{
//                    check if password entered are less than 6 because firebase authentication require minimum 6 character
                    if(txt_password.length() < 6)
                    {
                        Toast.makeText(SignUpActivity.this, "Please make sure your password is more than 6 characters", Toast.LENGTH_LONG).show();
                    }
                    else if(!txt_password.equals(txt_repass))
                    {
                        Toast.makeText(SignUpActivity.this, "Passwords entered are not matched", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        auth.createUserWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String txt_providerId = user.getProviderId();
//                                  set up the user profile
                                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(txt_name).setPhotoUri(Uri.parse("android.resource://"+ getPackageName()+"/"+R.drawable.default_profile_pic)).build();
                                    user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(String.valueOf(SignUpActivity.this), "User profile created.");
                                            }
                                        }
                                    });
                                    rootNode = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app");
                                    UserHelperClass userHelperClass = new UserHelperClass(txt_name, "@"+txt_name, txt_email," ",txt_providerId,0);

                                    reference = rootNode.getReference("user");
                                    reference.child(user.getUid()).setValue(userHelperClass);
                                    startActivity(new Intent(SignUpActivity.this, Navigation.class));
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }


            }
        });
    }
}