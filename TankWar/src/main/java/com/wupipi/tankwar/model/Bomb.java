package com.wupipi.tankwar.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.wupipi.tankwar.TankWarImage;

/**
 * Created by xudong on 7/25/13.
 */
public class Bomb extends AbstractEntity implements FrameAware, Animation {

    private int frame = 0;
    private int time = -3;

    private ScoreNumber scoreNumber;

    public Bomb(Point point, ScoreNumber scoreNumber) {
        this.position = new Point(point.x - 16, point.y - 16);
        this.scoreNumber = scoreNumber;
    }

    @Override
    protected int getWidth() {
        return 66;
    }

    @Override
    protected int getHeight() {
        return 66;
    }

    @Override
    public void nextFrame(Scene scene) {
        time++;
        if (time % 4 == 1) {
            frame++;
        }
        if (frame > 3) {
            scene.delayedDestroyBomb(this);
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint, Scene scene) {
        canvas
                .drawBitmap(TankWarImage.bomb[frame], null, getRect(), paint);
    }

    public ScoreNumber getScoreNumber() {
        return scoreNumber;
    }
}
