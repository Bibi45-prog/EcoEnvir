package com.example.ecoenvir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Webview4 extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for our webview
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview4);

        // initializing variable for web view.
        webView = findViewById(R.id.idWebView4);

        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app");

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("news").child("url3");

        // calling method to initialize
        // our web view.
        initializeWebView();
    }

    private void initializeWebView() {

        // calling add value event listener method for getting the values from database.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime updates in the data.
                // this method is called when the data is changed in our Firebase console.
                // below line is for getting the data from snapshot of our database.
                String webUrl = snapshot.getValue(String.class);

                // after getting the value for our webview url we are
                // setting our value to our webview view in below line.
                webView.loadUrl(webUrl);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Webview4.this, "Fail to get URL.", Toast.LENGTH_SHORT).show();
            }

        });
    }
}