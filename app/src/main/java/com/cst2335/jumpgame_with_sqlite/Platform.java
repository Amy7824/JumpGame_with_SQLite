package com.cst2335.jumpgame_with_sqlite;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class Platform {

    public float x, y;
    private static final float SPEED = 5f;
    public boolean isScored = false;
    public Platform(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public  void  update() {
        x -= SPEED;
    }
}