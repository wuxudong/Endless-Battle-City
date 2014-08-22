package com.wupipi.tankwar.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.wupipi.tankwar.Const;
import com.wupipi.tankwar.ImageType;

import java.io.Serializable;

/**
 * Created by xudong on 1/25/14.
 */
public abstract class AbstractEntity implements Serializable {
    protected Point position;

    protected int getWidth() {
        return Const.OFFSET_PER_TILE;
    }

    protected int getHeight() {
        return Const.OFFSET_PER_TILE;
    }

    public Rect getRect() {
        return new Rect(position.x, position.y, position.x + getWidth(), position.y + getHeight());
    }

    public abstract void draw(Canvas canvas, Paint paint);
}
