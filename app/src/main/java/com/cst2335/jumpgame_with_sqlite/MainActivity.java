package com.cst2335.jumpgame_with_sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);

        // Create a GameView instance and set it as the main content view
        gameView = new GameView(this);
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    public void storeHighScore(int score) {
        databaseHelper.saveScore(score);
    }

    public int retrieveHighScore() {
        return databaseHelper.getTopScore();
    }
}
