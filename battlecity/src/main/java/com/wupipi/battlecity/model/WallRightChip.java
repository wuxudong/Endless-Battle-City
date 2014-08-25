package com.wupipi.battlecity.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.battlecity.Const;
import com.wupipi.battlecity.TankWarImage;

/**
 * Created by xudong on 7/25/13.
 */
public class WallRightChip extends Obstacle {

    @Override
    protected int getWidth() {
        return Const.OFFSET_PER_TILE / 2;
    }

    @Override
    boolean isBlock() {
        return true;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        canvas.drawBitmap(TankWarImage.wallRightChip, position.x, position.y, paint);
    }
}
