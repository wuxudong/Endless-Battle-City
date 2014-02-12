package com.wupipi.tankwar.model;

import android.graphics.Rect;

/**
 * Created by xudong on 1/25/14.
 */
// just a flag
public interface Obstacle extends Drawable {
    boolean isCollidable();

    Rect getRect();
}
