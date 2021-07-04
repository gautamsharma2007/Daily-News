package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;



public class MainActivity extends AppCompatActivity {
    Button startbutton;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    TextView score;
    ConstraintLayout layout;
    Button go;
    int point=0;
    int noofquestions=0;
    int correctposition;
    TextView time;
    TextView problem;
    TextView textView;
    Button playagain;
    int wronganswer;
    ArrayList<Integer> answers=new ArrayList<Integer>();

    public void playagain(View view){
        point=0;
        noofquestions=0;
        button1.setEnabled(true );
        button2.setEnabled(true );
        button3.setEnabled(true );
        button4.setEnabled(true );
        textView.setText("");
        time.setText("30s");
        score.setText(point+"/"+noofquestions);
        playagain.setVisibility(View.INVISIBLE);
        newques();
        new CountDownTimer(30000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                int lefttime=(int)millisUntilFinished/1000;
                String timetoprint=Integer.toString(lefttime)+"s";
                time.setText(timetoprint);
            }

            @Override
            public void onFinish() {
                time.setText("Done!");
                playagain.setVisibility(View.VISIBLE);
                button1.setEnabled(false);
                button2.setEnabled(false);
                button3.setEnabled(false);
                button4.setEnabled(false);

            }
        }.start();
    }

    public void  choosed(View view){
        View button=view;
        if (Integer.toString(correctposition).equals(button.getTag().toString())){

            textView.setVisibility(View.VISIBLE);
            textView.setText("Correct!");
            point++;

        }else {
            textView.setVisibility(View.VISIBLE);
            textView.setText("Wrong :(");
        }
        noofquestions++;
        score.setText(point+"/"+noofquestions);
        newques();

    }
    public void newques(){
        Random random=new Random();
        int a = random.nextInt(21);
        int b = random.nextInt(21);
        problem.setText(Integer.toString(a)+"+"+Integer.toString(b));

        correctposition=random.nextInt(4);
        answers.clear();

        for(int i=0;i<4;i++) {
            if (i==correctposition){
                answers.add(a+b);
            }else {
                wronganswer=random.nextInt(41);
                while (wronganswer==a+b){
                    wronganswer=random.nextInt(41);
                }
                answers.add(wronganswer);
            }
        }
        button1.setText(Integer.toString(answers.get(0)));
        button2.setText(Integer.toString(answers.get(1)));
        button3.setText(Integer.toString(answers.get(2)));
        button4.setText(Integer.toString(answers.get(3)));
    }
    public void start(View view){
        playagain(time);
        go.setVisibility(View.INVISIBLE);
        layout.setVisibility(View.VISIBLE);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startbutton=findViewById(R.id.button);
        problem=findViewById(R.id.problem);
        button1=findViewById(R.id.option1);
        button2=findViewById(R.id.option2);
        go=findViewById(R.id.button);
        button3=findViewById(R.id.option3);
        playagain=findViewById(R.id.button2);
        layout=findViewById(R.id.layout);
        score=findViewById(R.id.score);
        button4=findViewById(R.id.option4);
        textView=(TextView) findViewById(R.id.textView);
        time=findViewById(R.id.time);


    }
}