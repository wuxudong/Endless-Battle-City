package com.wupipi.tankwar.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.tankwar.FrameAware;
import com.wupipi.tankwar.TankWarImage;

/**
 * Created by xudong on 7/25/13.
 */
public class TankBirth extends AbstractEntity implements FrameAware {

    private int frame = 0;
    private int time = 0;
    private int num = 0;

    private Tank tank;

    public TankBirth(Tank tank) {
        this.tank = tank;
        this.position = tank.position;
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
        canvas.drawBitmap(TankWarImage.tankBirth[frame], position.x, position.y, paint);
    }

    public Tank getTank() {
        return tank;
    }

    @Override
    public void nextFrame(Scene scene) {
        time++;
        if (time % 3 == 1) {
            frame++;
        }
        if (frame > 6) {
            num++;
            frame = 0;
        }

        if (num >= 3) {
            scene.destroyTankBirth(this);
        }
    }
}
