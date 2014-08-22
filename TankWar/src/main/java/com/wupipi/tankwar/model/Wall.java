package com.wupipi.tankwar.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.tankwar.TankWarImage;

import static com.wupipi.tankwar.ImageType.WALL;

/**
 * Created by xudong on 7/25/13.
 */
public class Wall extends Obstacle {

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas
                .drawBitmap(TankWarImage.images[WALL.ordinal()], position.x, position.y, paint);
    }

    @Override
    public boolean isBlock() {
        return true;
    }
}
