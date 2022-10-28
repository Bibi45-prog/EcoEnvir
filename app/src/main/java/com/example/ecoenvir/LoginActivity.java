package com.example.ecoenvir;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private EditText email, password;
    private Button emailSignIn;
    private FloatingActionButton googleSignIn, yahooSignIn, facebookSignIn;
    public static GoogleSignInClient client;
    private final String TAG = "LoginActivity";
    private final int RC_SIGN_IN = 1;
    CallbackManager callbackManager;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
//        check if the user is currently signed in
        if(currentUser != null)
        {
            startActivity(new Intent(LoginActivity.this, Navigation.class));

            Toast.makeText(this, "Welcome back, " + currentUser.getDisplayName(),Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        emailSignIn = findViewById(R.id.btnBegin);
        googleSignIn =  findViewById(R.id.fab_google);
        facebookSignIn = findViewById(R.id.fab_facebook);
        yahooSignIn = findViewById(R.id.fab_yahoo);

        userRef = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("user");


//       Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("335714643539-kgnallrstnsgdi5gda83hbaq087ur1tg.apps.googleusercontent.com")
                .requestEmail()
                .build();

        client = GoogleSignIn.getClient(LoginActivity.this, gso);



        TextView btn = findViewById(R.id.tv_noAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        TextView forgot = findViewById(R.id.tv_forgetPass);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        emailSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if(txt_email.equals("") || txt_password.equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    auth.signInWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, Navigation.class));
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                            }

                    });
                }
            }
        });
//Google sign in
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                googleSignIn();
            }
        });

        //        Initialize facebook Login Button
        callbackManager = CallbackManager.Factory.create();
        facebookSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        Toast.makeText(LoginActivity.this, "Facebook Login failed.",Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                        Toast.makeText(LoginActivity.this, "Facebook Login failed.",Toast.LENGTH_SHORT).show();
                    }
                });
            }



        });
//        yahoo sign in
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("yahoo.com");
        yahooSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task<AuthResult> pendingResultTask = auth.getPendingAuthResult();
                if(pendingResultTask != null)
                {
                    pendingResultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(LoginActivity.this, "Yahoo Sign In successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, Navigation.class));
                            FirebaseUser yahooUser = auth.getCurrentUser();
                            userRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(!(dataSnapshot.child(yahooUser.getUid()).exists())){
                                        for(UserInfo profile: yahooUser.getProviderData())
                                        {
                                            String txt_name = profile.getDisplayName();
                                            String txt_providerId = profile.getProviderId();
                                            String txt_email = profile.getEmail();
                                            ThirdPartyUser thirdPartyUser = new ThirdPartyUser(txt_name, "@"+txt_name, txt_email,txt_providerId,0);
                                            userRef.child(yahooUser.getUid()).setValue(thirdPartyUser);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
//                            ((OAuthCredential)authResult.getCredential()).getAccessToken();
                        }
                    })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Yahoo Sign In failed",Toast.LENGTH_SHORT).show();
                       }
                   });
                }
                else{
//                    no pending result, so start sign in
                    auth.startActivityForSignInWithProvider(LoginActivity.this, provider.build())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(LoginActivity.this, "Yahoo Sign In successful",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, Navigation.class));
                                    FirebaseUser yahooUser = auth.getCurrentUser();
                                    userRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(!(dataSnapshot.child(yahooUser.getUid()).exists())){
                                                for(UserInfo profile: yahooUser.getProviderData())
                                                {
                                                    String txt_name = profile.getDisplayName();
                                                    String txt_providerId = profile.getProviderId();
                                                    String txt_email = profile.getEmail();
                                                    ThirdPartyUser thirdPartyUser = new ThirdPartyUser(txt_name, "@"+txt_name, txt_email,txt_providerId,0);
                                                    userRef.child(yahooUser.getUid()).setValue(thirdPartyUser);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
//                              ((OAuthCredential)authResult.getCredential()).getAccessToken();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, "Yahoo Sign In failed",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });


    }

//    Google Sign in
    private void googleSignIn()
    {
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(LoginActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Google sign in failed", e);
            }
        }

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Sign In Successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser googleUser = auth.getCurrentUser();
                            userRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(!(dataSnapshot.child(googleUser.getUid()).exists())){
                                        for(UserInfo profile: googleUser.getProviderData())
                                        {
                                            String txt_providerId = profile.getProviderId();
                                            String txt_name = profile.getDisplayName();
                                            String txt_email = profile.getEmail();
                                            ThirdPartyUser thirdPartyUser = new ThirdPartyUser(txt_name, "@"+txt_name, txt_email,txt_providerId,0);
                                            userRef.child(googleUser.getUid()).setValue(thirdPartyUser);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Log.d(TAG, "signInWithCredential:success");
                            startActivity(new Intent(LoginActivity.this, Navigation.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

//        Facebook sign in
private void handleFacebookAccessToken(AccessToken token) {
    Log.d(TAG, "handleFacebookAccessToken:" + token);

    AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
    auth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(LoginActivity.this, "Facebook Login successful.",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "signInWithCredential:success");
                        startActivity(new Intent(LoginActivity.this,Navigation.class));
                        FirebaseUser facebookUser = auth.getCurrentUser();
                        userRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(!(dataSnapshot.child(facebookUser.getUid()).exists())){
                                    for(UserInfo profile: facebookUser.getProviderData())
                                    {
                                        String txt_name = profile.getDisplayName();
                                        String txt_providerId = profile.getProviderId();
                                        String txt_email = profile.getEmail();
                                        ThirdPartyUser thirdPartyUser = new ThirdPartyUser(txt_name, "@"+txt_name, txt_email,txt_providerId,0);
                                        userRef.child(facebookUser.getUid()).setValue(thirdPartyUser);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Facebook Login failed.",Toast.LENGTH_SHORT).show();

                    }
                }
            });
}

}