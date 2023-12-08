package com.cst2335.jumpgame_with_sqlite;

public class Player {
    public float x = 100,y = 500, height;
    public float velocityY = 0;
    private static final float GRAVITY = 0.5f;
    private static final float JUMP_STRENGTH = -15f;

    public float getHeight() {
        return height;
    }
    public void update() {
        y += velocityY;
        velocityY += GRAVITY;
    }
    public boolean collidesWith(Platform platform) {
        return x < platform.x + 200 &&
                x + 100 > platform.x &&
                y + 100 > platform.y &&
                y < platform.y + 50;
    }
    public boolean isAbove(Platform platform) {
        return x + 100 > platform.x &&
                x < platform.x + 200 &&
                y <= platform.y;
    }

    public void jump() {
        velocityY = JUMP_STRENGTH;
    }

}
