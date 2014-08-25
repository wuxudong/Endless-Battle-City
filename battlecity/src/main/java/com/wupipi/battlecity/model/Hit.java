package com.wupipi.battlecity.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.wupipi.battlecity.FrameAware;
import com.wupipi.battlecity.TankWarImage;

/**
 * Created by xudong on 7/25/13.
 */
public class Hit extends AbstractEntity implements FrameAware {

    private int frame = 0;
    private int time = -3;

    public Hit(Point point) {
        this.position = new MyPoint(point.x - 16, point.y - 16);
    }

    @Override
    public int getWidth() {
        return 32;
    }

    @Override
    public int getHeight() {
        return 32;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(TankWarImage.hit[frame], position.x, position.y, paint);
    }

    @Override
    public void nextFrame(Scene scene) {
        time++;
        if (time % 4 == 1) {
            frame++;
        }
        if (frame > 2) {
            scene.delayedDestroyHit(this);
        }
    }

}
