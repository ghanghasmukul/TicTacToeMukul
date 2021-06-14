package com.example.tictactoemukul;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class MainActivity extends AppCompatActivity {

    GridLayout mGridLayout, mScoreGrid;
    long timeOfClick;

    int pos = 0;

    int[][] winnerRowsColumns = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    int[][] charactersArrays = {{R.drawable.oimage, R.drawable.ximage}, {R.drawable.tom, R.drawable.jerry}, {R.drawable.virat, R.drawable.rohit}};

    String[][] nameOfPlayersArray = {{"0", "X"}, {"Tom", "Jerry"}, {"Virat", "Rohit"}};

    String winner = "";

    enum Player {
        One, Two, No
    }

    Player[] gridPositions = new Player[9];

    Player currentPlayer = Player.One;

    boolean gameOver = false;
    int filledGrids = 0;
    int scorePlayer1 = 0, scorePlayer2 = 0, scoreDraw = 0;

    TextView playerName, playerTime;

    int remainingTime = 20;

    CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_main);


        //Default value = 0 and if we get some value from intent then that will be assigned to pos
        pos = getIntent().getIntExtra(ScreenMain.selectedPosKey, 0);

        playerName = findViewById(R.id.playerName);
        playerTime = findViewById(R.id.playerTime);

        mGridLayout = findViewById(R.id.gridLayout);
        mScoreGrid = findViewById(R.id.scoreGrid);
        resetEverything();
        setScores();

    }

    public void onTapImage(View view) {
        ImageView clickImageView = (ImageView) view;

        int index = Integer.parseInt(clickImageView.getTag().toString());


        if (gridPositions[index] == Player.No && !gameOver && filledGrids < 9) {
            if (currentPlayer == Player.One) {
                gridPositions[index] = Player.One;
                clickImageView.setImageResource(charactersArrays[pos][0]);
                clickImageView.setAlpha(1.0f);
                currentPlayer = Player.Two;
                playerName.setText(getResources().getString(R.string.player_name, 2));
            } else {
                gridPositions[index] = Player.Two;
                clickImageView.setImageResource(charactersArrays[pos][1]);
                clickImageView.setAlpha(1.0f);
                currentPlayer = Player.One;
                playerName.setText(getResources().getString(R.string.player_name, 1));
            }
            filledGrids++;

            if (filledGrids >= 5 && filledGrids <= 9) {

                for (int[] winnerRow : winnerRowsColumns) {
                    Log.e("winnerRow", winnerRow[0] + "," + winnerRow[1] + "," + winnerRow[2] + " - " + winner);
                    if (gridPositions[winnerRow[0]] == gridPositions[winnerRow[1]] &&
                            gridPositions[winnerRow[1]] == gridPositions[winnerRow[2]] && gridPositions[winnerRow[0]] != Player.No) {
                        if (currentPlayer == Player.One) {
                            winner = nameOfPlayersArray[pos][1] + " - Player 2";
                            displayOnResult("Player 2 Won",
                                    "Player 2 have won this come,\nCome On Player 1.",
                                    R.raw.won,
                                    2);
                        } else {
                            winner = nameOfPlayersArray[pos][0] + " - Player 1";
                            displayOnResult("Player 1 Won",
                                    "Player 1 have won this come,\nCome On Player 2.",
                                    R.raw.won,
                                    1);
                        }
                        gameOver = true;
                        Log.e("winnerRow", winnerRow[0] + "," + winnerRow[1] + "," + winnerRow[2] + " - " + winner);
                        if (mCountDownTimer != null) {
                            mCountDownTimer.cancel();
                            mCountDownTimer = null;
                        }
                        break;
                    }
                }
            }

            if (filledGrids >= 9 && !gameOver) {
                gameOver = true;
                displayOnResult("Game is Drawn"
                        , "No one have won the game.\nTry Again.", R.raw.loader, 0);
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                    mCountDownTimer = null;
                }
            }

            if (!gameOver && filledGrids < 9) {
                remainingTime = 20;
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                    mCountDownTimer = null;
                }
                setTimer(remainingTime);
            }
        } else {
            Toast.makeText(this, "This Grid is already filled or Game is over.", Toast.LENGTH_SHORT).show();
        }

    }


    private void setScores() {

        scorePlayer1 = getIntent().getIntExtra(ScreenMain.player1ScoreKey, 0);
        scorePlayer2 = getIntent().getIntExtra(ScreenMain.player2ScoreKey, 0);
        scoreDraw = getIntent().getIntExtra(ScreenMain.drawScoreKey, 0);


        TextView textView = (TextView) mScoreGrid.getChildAt(3);
        textView.setText(scorePlayer1 + "");
        textView = (TextView) mScoreGrid.getChildAt(4);
        textView.setText(scorePlayer2 + "");
        textView = (TextView) mScoreGrid.getChildAt(5);
        textView.setText(scoreDraw + "");


    }

    private void displayOnResult(String title, String message, Integer gifImage, int whoWon) {

        switch (whoWon) {
            case 1: {
                TextView textView = (TextView) mScoreGrid.getChildAt(3);
                scorePlayer1++;
                textView.setText(String.valueOf(scorePlayer1));
                break;
            }
            case 2: {
                TextView textView = (TextView) mScoreGrid.getChildAt(4);
                scorePlayer2++;
                textView.setText(String.valueOf(scorePlayer2));
                break;
            }
            case 0:
            default: {
                TextView textView = (TextView) mScoreGrid.getChildAt(5);
                scoreDraw++;
                textView.setText(String.valueOf(scoreDraw));
                break;
            }
        }

        new MaterialDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Next", R.drawable.ic_baseline_done_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        resetEverything();
                    }
                })
                .setNegativeButton("Change Characters", R.drawable.ic_outline_cancel_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(MainActivity.this, ScreenMain.class);
                        intent.putExtra(ScreenMain.player1ScoreKey, scorePlayer1);
                        intent.putExtra(ScreenMain.player2ScoreKey, scorePlayer2);
                        intent.putExtra(ScreenMain.drawScoreKey, scoreDraw);
                        startActivity(intent);
                        finish();
                    }
                })
                .setAnimation(gifImage)
                .build().show();
    }

    private void resetEverything() {
        remainingTime = 20;
        gameOver = false;
        filledGrids = 0;
        currentPlayer = Player.One;

        playerName.setText(getResources().getString(R.string.player_name, 1));
        playerTime.setText(getResources().getString(R.string.player_time, remainingTime));

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
            setTimer(remainingTime);
        }
        for (int i = 0; i < 9; i++) {
            gridPositions[i] = Player.No;

            ImageView imageView = (ImageView) mGridLayout.getChildAt(i);
            imageView.setAlpha(0.2f);
            imageView.setImageDrawable(null);
        }
    }

    private void setTimer(int duration) {
        mCountDownTimer = new CountDownTimer(duration * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime--;
                playerTime.setText(getResources().getString(R.string.player_time, remainingTime));
            }

            @Override
            public void onFinish() {
                makeAutomaticMove();
            }
        };

        mCountDownTimer.start();
    }

    private void makeAutomaticMove() {

        boolean isValid = false;

        while (!isValid) {
            //it will return a value between 0 and 8
            int index = new Random().nextInt(9);
            Log.e("indexValue", index + "");

            if (gridPositions[index] == Player.No) {
                isValid = true;

                ImageView imageView = (ImageView) mGridLayout.getChildAt(index);
                onTapImage(imageView);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (timeOfClick + 2000 > System.currentTimeMillis()) {

            super.onBackPressed();
            return;
        } else {
            Toast.makeText(MainActivity.this, "prees again to exit or press exit", Toast.LENGTH_SHORT).show();
        }
        timeOfClick = System.currentTimeMillis();
    }
}