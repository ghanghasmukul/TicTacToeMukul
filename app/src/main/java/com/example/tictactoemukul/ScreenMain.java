package com.example.tictactoemukul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import org.jetbrains.annotations.Nullable;

public class ScreenMain extends AppCompatActivity {
    Button btnStart;
    PowerSpinnerView selectCharactersSpinner;
    public final static String selectedPosKey = "selectedPosition",
            player1ScoreKey = "playerScore1Key", player2ScoreKey="playerScore2Key", drawScoreKey = "drawScoreKey";
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_screen_main);

        selectCharactersSpinner = findViewById(R.id.selectCharactersSpinner);


        btnStart = findViewById(R.id.button);

        selectCharactersSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                pos = i1;
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScreenMain.this,MainActivity.class);
                intent.putExtra(selectedPosKey, pos);
                intent.putExtra(player1ScoreKey, getIntent().getIntExtra(player1ScoreKey, 0));
                intent.putExtra(player2ScoreKey, getIntent().getIntExtra(player2ScoreKey, 0));
                intent.putExtra(drawScoreKey, getIntent().getIntExtra(drawScoreKey, 0));
                startActivity(intent);
                finish();
            }
        });
    }
}