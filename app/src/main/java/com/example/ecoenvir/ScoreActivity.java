package com.example.ecoenvir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ScoreActivity extends AppCompatActivity {
    private TextView score;
    private Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("user");
        score = findViewById(R.id.marks);
        done = findViewById(R.id.btnDone);

        String score_str = getIntent().getStringExtra("Your Score");
        String parts[] = score_str.split("/");
        String score1 = parts[0];
        score.setText(score_str);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Long points = dataSnapshot.child(currentUser.getUid()).child("points").getValue(Long.class);
                    final Long result = points + Long.parseLong(score1.trim());

                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            userRef.child(currentUser.getUid()).child("points").setValue(result);
                            SharedPreferences sharedPreferences = getSharedPreferences(currentUser.getUid(), MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("lastQuiz",Integer.parseInt(DateFormat.format("dd", new Date()).toString()));
                            editor.putInt("count", 1);
                            editor.commit();
                            Toast.makeText(getApplicationContext(),score1.trim() + " points had been added to your points",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ScoreActivity.this, Navigation.class);
                            ScoreActivity.this.startActivity(intent);
                            ScoreActivity.this.finish();
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