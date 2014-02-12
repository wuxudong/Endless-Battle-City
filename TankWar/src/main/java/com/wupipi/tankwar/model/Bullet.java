package com.wupipi.tankwar.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.wupipi.tankwar.Const;
import com.wupipi.tankwar.Direction;
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
        Point next = null;

        // Log.d("Tank_War", "bullet direction " + direction);
        switch (direction) {
            case EAST: {
                next = new Point(position.x + speed, position.y);
                break;
            }
            case WEST: {
                next = new Point(position.x - speed, position.y);
                break;
            }
            case NORTH: {
                next = new Point(position.x, position.y - speed);
                break;
            }
            default: {
                next = new Point(position.x, position.y + speed);
                break;
            }
        }

        position = next;

        int x = 0;
        int y = 0;
        switch (direction) {
            case NORTH:
                x = position.x + getWidth() / 2;
                y = position.y;
                break;
            case SOUTH:
                x = position.x + getWidth() / 2;
                y = position.y + getHeight();
                break;
            case WEST:
                x = position.x;
                y = position.y + getHeight() / 2;
                break;
            default:
                x = position.x + getWidth();
                y = position.y + getHeight() / 2;
                break;
        }

        if ((x < 0) || (x >= Const.TILE_COUNT * Const.OFFSET_PER_TILE) || (y < 0) || (y >= Const.TILE_COUNT * Const.OFFSET_PER_TILE)) {
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
    public void draw(Canvas canvas, Paint paint, Scene scene) {
        canvas
                .drawBitmap(TankWarImage.bullet[direction.ordinal()], null, getRect(),
                        paint);
    }

    public int getPower() {
        return power;
    }

    public Tank getOwner() {
        return owner;
    }
}
