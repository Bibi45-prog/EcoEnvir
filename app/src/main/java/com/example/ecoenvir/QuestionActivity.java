package com.example.ecoenvir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView question, qCount, timer;
    private Button option1, option2, option3, option4;
    private int quesNum, randomInt, score;
    private String nextQuestion, nextA, nextB, nextC, nextD, answer;
    final int numberQues = 30, quesPerQuiz = 10;
    private CountDownTimer countDown;
    private DatabaseReference reference;
    Random random = new Random();
    Set<Integer> set = new HashSet<>();
    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        question = findViewById(R.id.question);
        qCount = findViewById(R.id.quesNum);
        timer = findViewById(R.id.countdown);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        score = 0;
        randomInt = random.nextInt(numberQues)+1; //generate a random integer between 1 and 30
        setQuestion(randomInt);

        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);
    }

    private void setQuestion(int questionNo)
    {
        set.add(questionNo);
        reference = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("quiz").child("q"+String.valueOf(questionNo));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Question questionData = dataSnapshot.getValue(Question.class);

                timer.setText(String.valueOf(10));

                question.setText(questionData.getQuestion());
                option1.setText(questionData.getA());
                option2.setText(questionData.getB());
                option3.setText(questionData.getC());
                option4.setText(questionData.getD());
                answer = questionData.getAnswer();

                qCount.setText(String.valueOf(count) + "/" + String.valueOf(quesPerQuiz));

                startTimer();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void startTimer()
    {
        countDown = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                changeQuestion();
            }
        };

        countDown.start();
    }

    @Override
    public void onClick(View v) {

        String selectedOption = "";

        switch (v.getId())
        {
            case R.id.option1:
                selectedOption = "A";
                break;

            case R.id.option2:
                selectedOption = "B";
                break;

            case R.id.option3:
                selectedOption = "C";
                break;

            case R.id.option4:
                selectedOption = "D";
                break;

            default:
        }
//        disable button upon clicked
        option1.setEnabled(false);
        option2.setEnabled(false);
        option3.setEnabled(false);
        option4.setEnabled(false);

        countDown.cancel();
        checkAnswer(selectedOption, v);
    }

    private void checkAnswer(String selectedOption, View view)
    {
        if(selectedOption.equals(answer))
        {
            // Right Answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;
        }
        else
        {
            // Wrong Answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            switch(answer)
            {
                case "A":
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;

                case "B":
                    option2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;

                case "C":
                    option3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;

                case "D":
                    option4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(() -> changeQuestion(), 2000);
    }

    private boolean changeQuestion()
    {
        if(count < quesPerQuiz)
        {
            do {
                randomInt = random.nextInt(numberQues) + 1;//generate a random integer between 1 and total number of questions in database
            }
            while(set.contains(randomInt));

            set.add(randomInt);
            System.out.println(randomInt);

            count++;
            reference = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("quiz").child("q"+String.valueOf(randomInt));
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Question questionData = dataSnapshot.getValue(Question.class);

                        nextQuestion = questionData.getQuestion();
                        nextA = questionData.getA();
                        nextB = questionData.getB();
                        nextC = questionData.getC();
                        nextD = questionData.getD();
                        answer = questionData.getAnswer();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            playAnim(question, 0, 0);
            playAnim(option1, 0, 1);
            playAnim(option2, 0, 2);
            playAnim(option3, 0, 3);
            playAnim(option4, 0, 4);

//            Enable back button
            option1.setEnabled(true);
            option2.setEnabled(true);
            option3.setEnabled(true);
            option4.setEnabled(true);

            qCount.setText(String.valueOf((count) + "/" + String.valueOf(quesPerQuiz)));

            timer.setText(String.valueOf(10));
            startTimer();

        }
        else
        {
            // Go to Score Activity
            Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
            intent.putExtra("Your Score", String.valueOf(score) + " / " + String.valueOf(quesPerQuiz));
            startActivity(intent);
            QuestionActivity.this.finish();
        }
        return true;
    }

    private void playAnim(View view, final int value, int viewNum)
    {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if(value == 0)
                        {
                            switch(viewNum)
                            {
                                case 0:
                                    ((TextView)view).setText(nextQuestion);
                                    break;

                                case 1:
                                    ((Button)view).setText(nextA);
                                    break;

                                case 2:
                                    ((Button)view).setText(nextB);
                                    break;

                                case 3:
                                    ((Button)view).setText(nextC);
                                    break;

                                case 4:
                                    ((Button)view).setText(nextD);
                                    break;
                            }

                            if(viewNum != 0)
                                ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#14C9CB")));

                            playAnim(view, 1, viewNum);

                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
    }
}