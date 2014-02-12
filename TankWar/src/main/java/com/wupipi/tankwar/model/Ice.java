package com.wupipi.tankwar.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.tankwar.TankWarImage;

import static com.wupipi.tankwar.ImageType.ICE;

/**
 * Created by xudong on 7/25/13.
 */
public class Ice extends AbstractEntity implements Obstacle {

    @Override
    public void draw(Canvas canvas, Paint paint, Scene scene) {
        canvas
                .drawBitmap(TankWarImage.images[ICE.ordinal()], null, getRect(), paint);
    }

    @Override
    public boolean isCollidable() {
        return false;
    }
}
