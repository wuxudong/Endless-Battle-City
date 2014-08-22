package com.wupipi.tankwar.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.wupipi.tankwar.Ally;
import com.wupipi.tankwar.Const;
import com.wupipi.tankwar.Direction;
import com.wupipi.tankwar.FrameAware;
import com.wupipi.tankwar.TankWarImage;

/**
 * Created by xudong on 7/27/13.
 */
public class Bullet extends AbstractEntity implements FrameAware {
    private int speed;

    private Direction direction;

    private int power = 0;

    private Tank owner;

    public Bullet(int power, int speed, Point position, Direction direction, Tank owner) {
        this.power = power;
        this.speed = speed;
        this.position = position;
        this.direction = direction;
        this.owner = owner;
    }

    @Override
    protected int getWidth() {
        return 6;
    }

    @Override
    protected int getHeight() {
        return 6;
    }

    @Override
    public void nextFrame(Scene scene) {

        switch (direction) {
            case EAST: {

                position.set(position.x + speed, position.y);
                break;
            }
            case WEST: {
                position.set(position.x - speed, position.y);
                break;
            }
            case NORTH: {
                position.set(position.x, position.y - speed);
                break;
            }
            default: {
                position.set(position.x, position.y + speed);
                break;
            }
        }

        int centerX = 0;
        int centerY = 0;
        switch (direction) {
            case NORTH:
                centerX = position.x + getWidth() / 2;
                centerY = position.y;
                break;
            case SOUTH:
                centerX = position.x + getWidth() / 2;
                centerY = position.y + getHeight();
                break;
            case WEST:
                centerX = position.x;
                centerY = position.y + getHeight() / 2;
                break;
            default:
                centerX = position.x + getWidth();
                centerY = position.y + getHeight() / 2;
                break;
        }

        // destroy bullet if bullet is out of battlefield
        if ((centerX < 0) || (centerX >= Const.TILE_COUNT * Const.OFFSET_PER_TILE) || (centerY < 0) || (centerY >= Const.TILE_COUNT * Const.OFFSET_PER_TILE)) {
            scene.delayedDestroyBullet(this);
        }
    }


    private Ally getAlly() {
        return owner.getAlly();
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(TankWarImage.bullet[direction.ordinal()], position.x, position.y, paint);
    }

    public int getPower() {
        return power;
    }

    public Tank getOwner() {
        return owner;
    }
}
