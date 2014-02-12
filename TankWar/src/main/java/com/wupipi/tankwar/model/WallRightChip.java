package com.wupipi.tankwar.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.tankwar.Const;
import com.wupipi.tankwar.TankWarImage;

/**
 * Created by xudong on 7/25/13.
 */
public class WallRightChip extends AbstractEntity implements Obstacle {

    @Override
    protected int getWidth() {
        return Const.OFFSET_PER_TILE / 2;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, Scene scene) {
        canvas
                .drawBitmap(TankWarImage.wallRightChip, null, getRect(), paint);
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

}
