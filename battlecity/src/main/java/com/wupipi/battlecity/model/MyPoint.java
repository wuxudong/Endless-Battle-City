package com.wupipi.battlecity.model;

import android.graphics.Point;

import java.io.Serializable;

/**
 * Created by xudong on 8/23/14.
 */
public class MyPoint implements Serializable{
    public int x;
    public int y;

    public MyPoint() {}

    public MyPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public MyPoint(Point src) {
        this.x = src.x;
        this.y = src.y;
    }

    /**
     * Set the point's x and y coordinates
     */
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
