package com.wupipi.tankwar.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.tankwar.TankWarImage;

import static com.wupipi.tankwar.ImageType.GRASS;

/**
 * Created by xudong on 7/25/13.
 */
public class Grass extends AbstractEntity implements Obstacle {

    @Override
    public void draw(Canvas canvas, Paint paint, Scene scene) {
        canvas
                .drawBitmap(TankWarImage.images[GRASS.ordinal()], null, getRect(), paint);
    }

    @Override
    public boolean isCollidable() {
        return false;
    }
}
