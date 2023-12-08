package com.cst2335.jumpgame_with_sqlite;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private Thread gameThread;
    private DatabaseHelper databaseHelper;

    private boolean isPlaying = true;

    private Player player;
    private List<Platform> platforms = new ArrayList<>();
    private boolean isGameOver = false;

    int score = 0;

    public GameView(Context context) {
        super(context);
        player = new Player();
        platforms = new ArrayList<>();
        Platform initialPlatform = new Platform(500,600);
        platforms.add(initialPlatform);
        // Set the player's initial position to be on top of the initial platform
        player.y = initialPlatform.y -500;
        databaseHelper = new DatabaseHelper(context);


        resume();  // Start the game loop as soon as GameView is created
        getHolder().addCallback(this);
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        resume();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Handle surface changes if necessary
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        pause();
    }



    private void update() {

        player.update();
        Iterator<Platform> iterator = platforms.iterator();
        while (iterator.hasNext()) {
            Platform platform = iterator.next();
            platform.update();

            if (player.collidesWith(platform) && player.isAbove(platform)) {
                player.y = platform.y - 100;
                player.velocityY = 0;  // Stop downward motion
                if (!platform.isScored) {
                    score++;
                    databaseHelper.saveScore(score);
                    platform.isScored = true;
                }
            }

            if (platform.x + 200 < 0) {
                iterator.remove();
            }
        }

        if (Math.random() < 0.02) {
            float yPosition = (float) (400 + Math.random() * 500);
            platforms.add(new Platform(getWidth(), yPosition));
        }

        if (player.y > getHeight()) {
            gameOver();
        }
    }


    private void gameOver() {
        isPlaying = false;
        isGameOver = true;
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            // Clear screen
            canvas.drawColor(Color.WHITE);

            // Draw player
            Paint playerPaint = new Paint();
            playerPaint.setColor(Color.RED);  // Set player color to red
            canvas.drawRect(player.x, player.y, player.x + 100, player.y + 100, playerPaint);
            // Draw platforms
            Paint platformPaint = new Paint();
            platformPaint.setColor(Color.GREEN);  // Set platform color to green
            for (Platform platform : platforms) {
                canvas.drawRect(platform.x, platform.y, platform.x + 200, platform.y + 50, platformPaint);
            }

            // Draw game over text
            if (isGameOver) {
                Paint gameOverPaint = new Paint();
                gameOverPaint.setTextSize(50);
                gameOverPaint.setColor(Color.BLACK);
                canvas.drawText("Game Over! Tap to restart.", getWidth() / 2 - 250, getHeight() / 2, gameOverPaint);
            }

            Paint scorePaint = new Paint();
            scorePaint.setTextSize(30);
            scorePaint.setColor(Color.BLACK);
            canvas.drawText("Score: " + score, 20, 40, scorePaint);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (isGameOver) {
                    restartGame();
                    resume();
                } else {
                    player.jump();
                    if (!isPlaying) {
                        isPlaying = true;
                    }
                }
        }
        return true;
    }
        private void restartGame() {
            player = new Player();
            platforms.clear();
            platforms.add(new Platform(500, 600));
            isPlaying = true;
            isGameOver = false;
        }


    private void sleep() {
        try {
            Thread.sleep(17);  // Cap the game loop to roughly 60 frames per second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

