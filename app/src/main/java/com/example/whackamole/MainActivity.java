package com.example.whackamole;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.gridlayout.widget.GridLayout;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private GridLayout glMoleGrid;
    private Drawable drwMoleImage;
    private ImageView[] ivMoleView;
    private int intMoleLoc, intMissedWhacks, intScore, intInterval, intCounter, intHighScore;
    private Random ranNum;
    private Handler handler;
    private WhackAMole wamMachine;
    private TextView tvScore, tvScoreTracker, tvMissedWhacks;
    private MoleTimer mtTimer;
    private Counter counter;
    private Button btnStart;
    private boolean firstTick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFields();
        for(int i=0; i<16; i++) {
            ivMoleView[i] = (ImageView) getLayoutInflater().inflate(R.layout.mole_view, null);
            ivMoleView[i].setMinimumHeight(270);
            ivMoleView[i].setMinimumWidth(270);
            glMoleGrid.addView(ivMoleView[i]);
        }
    }

    private void initFields() {
        glMoleGrid = findViewById(R.id.gridLayout);
        drwMoleImage = getDrawable(R.drawable.mole);
        tvScore = findViewById(R.id.tvScore);
        tvScoreTracker = findViewById(R.id.tvScoreTracker);
        btnStart = findViewById(R.id.btnStart);
        tvMissedWhacks = findViewById(R.id.tvMissedWhacks);
        tvScoreTracker = findViewById(R.id.tvScoreTracker);
        tvScore = findViewById(R.id.tvScore);
        tvScore = findViewById(R.id.tvScore);
        ivMoleView = new ImageView[16];
        ranNum = new Random();
        handler = new Handler();
        wamMachine = new WhackAMole();
        mtTimer = new MoleTimer();
        counter = new Counter();
        intHighScore = 0;
        intMoleLoc = ranNum.nextInt(16);
        intScore = 0;
        firstTick = true;
    }

    public void startPressed(View v) {
        intMissedWhacks = 0;
        intCounter = 0;
        intScore = 0;
        intInterval = 5000;
        firstTick = true;
        tvMissedWhacks.setText("Missed Whacks: " + intMissedWhacks);
        tvScoreTracker.setText(intScore + "");
        tvScore.setText("Score: ");
        btnStart.setVisibility(View.GONE);
        tvMissedWhacks.setVisibility(View.VISIBLE);
        handler.postDelayed(wamMachine, 0);
        handler.postDelayed(counter, 0);
        handler.postDelayed(mtTimer, 0);
    }

    private void endGame() {
        handler.removeCallbacks(wamMachine);
        handler.removeCallbacks(mtTimer);
        ivMoleView[intMoleLoc].setOnClickListener(null);
        if(intScore > intHighScore) {
            intHighScore = intScore;
        }
        tvScore.setText("HIGHSCORE: ");
        tvScoreTracker.setText(intHighScore + "");
        tvMissedWhacks.setVisibility(View.GONE);
        btnStart.setVisibility(View.VISIBLE);
    }

    private class MoleTimer implements Runnable {
        public void run(){
            if(intMissedWhacks == 0 && firstTick){
                handler.postDelayed(mtTimer, intInterval);
                tvMissedWhacks.setText("Missed Whacks: " + intMissedWhacks);
                firstTick = false;
            } else if(intMissedWhacks == 2){
                endGame();
            } else {
                intMissedWhacks++;
                tvMissedWhacks.setText("Missed Whacks: " + intMissedWhacks);
                handler.removeCallbacks(wamMachine);
                handler.removeCallbacks(mtTimer);
                handler.postDelayed(mtTimer, intInterval);
                handler.postDelayed(wamMachine, intInterval);
            }
        }
    }

    private class Counter implements Runnable {
        public void run(){
            if(intCounter < 30){
                intCounter++;
                handler.postDelayed(counter, 1000);
            } else {
                intInterval =- 1000;
                intCounter = 0;
                handler.postDelayed(counter, 1000);
            }
        }
    }

    private class WhackAMole implements Runnable {
        public void run(){
            ivMoleView[intMoleLoc].setImageDrawable(null);
            ivMoleView[intMoleLoc].setOnClickListener(null);
            intMoleLoc = ranNum.nextInt(16);
            ivMoleView[intMoleLoc].setImageDrawable(drwMoleImage);
            ivMoleView[intMoleLoc].setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    intMissedWhacks = 0;
                    firstTick = true;
                    intScore++;
                    tvScoreTracker.setText(intScore +"");
                    handler.removeCallbacks(mtTimer);
                    handler.removeCallbacks(wamMachine);
                    handler.postDelayed(wamMachine, 0);
                    handler.postDelayed(mtTimer, 0);
                }
            });
        }
    }
}
