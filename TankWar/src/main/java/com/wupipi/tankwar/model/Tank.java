package com.wupipi.tankwar.model;

import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.wupipi.tankwar.Ally;
import com.wupipi.tankwar.Const;
import com.wupipi.tankwar.Direction;
import com.wupipi.tankwar.FrameAware;
import com.wupipi.tankwar.TankWarImage;

import java.util.List;

/**
 * Created by xudong on 7/25/13.
 */
public class Tank extends Obstacle implements FrameAware {

    private int speed = 4;

    private int health = 1;

    private int fireCoolDown = 20;

    private int power = 1;

    private long coolingFrame = 0;

    private int bulletSpeed = 6;

    private boolean move = false;

    private boolean fire = false;

    private int stopTime = 0;

    private boolean god = false;

    protected int godTime = 0;

    private Ally ally;

    private TankType tankType;

    private boolean carryFood = false;

    private Player player;

    private long age = 0;

    /**
     * Current direction the tank is headed.
     */
    protected Direction direction = Direction.NORTH;
    private Direction nextDirection = Direction.NORTH;

    public Tank(Point position, Ally ally, TankType tankType, boolean carryFood, Player player) {
        this.position = position;
        this.ally = ally;
        this.tankType = tankType;
        this.carryFood = carryFood;
        this.player = player;
    }

    @Override
    public int getWidth() {
        return 2 * Const.OFFSET_PER_TILE;
    }

    @Override
    public int getHeight() {
        return 2 * Const.OFFSET_PER_TILE;
    }


    public void head(Direction direction) {
        nextDirection = direction;
    }

    @Override
    public void nextFrame(final Scene scene) {
        age ++;

        if (stopTime > 0) {
            stopTime--;
            return;
        }

        boolean isCurrentDirectionHorizontal =
                direction == Direction.WEST || direction == Direction.EAST;
        boolean isNextDirectionHorizontal =
                nextDirection == Direction.WEST || nextDirection == Direction.EAST;

        if (isCurrentDirectionHorizontal ^ isNextDirectionHorizontal) {
            if (isNextDirectionHorizontal) {
                position.y = ((position.y + 8) / 16) * 16;
            } else {
                position.x = ((position.x + 8) / 16) * 16;
            }
        }

        direction = nextDirection;

        if (move) {
            switch (direction) {
                case NORTH: {
                    int leftUpX = position.x;
                    int leftUpY = Math.max(position.y - speed, 0);

                    Point nextReachablePosition =
                            reachablePosition(scene,
                                    new Rect(leftUpX, leftUpY, leftUpX + getWidth(), position.y),
                                    direction);
                    position = nextReachablePosition;
                    break;
                }
                case SOUTH: {
                    int leftUpX = position.x;
                    int leftUpY = position.y + getHeight();

                    Point nextReachablePosition =
                            reachablePosition(scene,
                                    new Rect(leftUpX, leftUpY, leftUpX + getWidth(), Math.min(leftUpY + speed,
                                            Const.TILE_COUNT * Const.OFFSET_PER_TILE)),
                                    direction);
                    position = new Point(nextReachablePosition.x, nextReachablePosition.y - getHeight());
                    break;
                }
                case WEST: {
                    int leftUpX = Math.max(position.x - speed, 0);
                    int leftUpY = position.y;

                    Point nextReachablePosition =
                            reachablePosition(scene,
                                    new Rect(leftUpX, leftUpY, position.x, position.y + getHeight()),
                                    direction);
                    position = nextReachablePosition;
                    break;
                }
                case EAST: {
                    int leftUpX = position.x + getWidth();
                    int leftUpY = position.y;

                    Point nextReachablePosition =
                            reachablePosition(scene,
                                    new Rect(leftUpX, leftUpY, Math.min(leftUpX + speed,
                                            Const.TILE_COUNT
                                                    * Const.OFFSET_PER_TILE), position.y + getHeight()),
                                    direction);
                    position = new Point(nextReachablePosition.x - getWidth(), nextReachablePosition.y);
                    break;
                }

            }
        }

        if (godTime > 0) {
            godTime--;
            if (godTime == 0) {
                god = false;
            }
        }

        if (coolingFrame > 0) {
            coolingFrame--;
        }

        final Bullet bullet = fire(scene);
        if (bullet != null) {
            scene.delayedTankFires(bullet);
        }

    }

    public void setFire(boolean b) {
        this.fire = b;
    }

    private Bullet fire(final Scene scene) {
        if (!fire) {
            return null;
        } else {

            int x = position.x;
            int y = position.y;
            if (coolingFrame == 0) {
                coolingFrame = fireCoolDown;
                switch (this.direction) {
                    case NORTH:
                        x += 13;
                        y += 3;
                        break;

                    case SOUTH:
                        x += 13;
                        y += (32 - 6);
                        break;

                    case EAST:
                        y += 13;
                        x += (32 - 6);
                        break;

                    case WEST:
                        y += 13;
                        x += 3;
                        break;

                }

                return new Bullet(power, bulletSpeed, new Point(x, y), direction, this);
            } else {
                return null;
            }
        }
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    private Point reachablePosition(Scene scene, Rect rect, Direction direction) {

        List<Tile> crossedTiles = Tile.getCrossedTiles(rect);

        int reachableNorthY = rect.top;
        int reachableSouthY = rect.bottom;
        int reachableWestX = rect.left;
        int reachableEastX = rect.right;


        for (Tile tile : crossedTiles) {

            for (Obstacle object : scene.
                    getObstacles(tile)) {
                if (object == this) {
                    // do nothing
                } else if (!object.isBlock()) {
                    // do nothing
                } else {
                    reachableNorthY = Math.max(reachableNorthY, tile.getRect().bottom);
                    reachableSouthY = Math.min(reachableSouthY, tile.getRect().top);
                    reachableWestX = Math.max(reachableWestX, tile.getRect().right);
                    reachableEastX = Math.min(reachableEastX, tile.getRect().left);
                }
            }
        }

        switch (direction) {
            case NORTH:
                return new Point(rect.left, reachableNorthY);
            case SOUTH:
                return new Point(rect.left, reachableSouthY);
            case WEST:
                return new Point(reachableWestX, rect.top);
            default:
                return new Point(reachableEastX, rect.top);
        }
    }

    public void beGod(int time) {
        god = true;
        godTime = time;
    }

    public boolean isGod() {
        return god;
    }

    public void promote() {
        fireCoolDown /= 2;
        power++;

        bulletSpeed = Math.min(2 * bulletSpeed, 7);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        if (carryFood) {
            if ((age / 5) % 2 == 0)
                paint.setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00FF0000));
        }

        switch (tankType) {
            case PLAY1: {
                canvas
                        .drawBitmap(TankWarImage.play1[direction.ordinal()], position.x, position.y,
                                paint);

                break;
            }

            case NPC_NORMAL: {
                canvas
                        .drawBitmap(TankWarImage.tank1[direction.ordinal()], null, getRect(),
                                paint);

                break;
            }

            case TANK_NPC_RACER: {
                canvas
                        .drawBitmap(TankWarImage.tank2[direction.ordinal()], null, getRect(),
                                paint);

                break;
            }

            case TANK_NPC_ARMOR: {
                canvas
                        .drawBitmap(TankWarImage.tank3[direction.ordinal()], null, getRect(),
                                paint);

                break;
            }

        }

        if (isGod()) {
            int frame = (godTime / 6) % 2;

            canvas
                    .drawBitmap(TankWarImage.shield[frame], position.x, position.y,
                            paint);
        }

        paint.setColorFilter(null);
    }

    public Ally getAlly() {
        return ally;
    }

    public ScoreNumber scoreNumber() {
        switch (tankType) {
            case NPC_NORMAL:
                return ScoreNumber._100;
            case TANK_NPC_RACER:
                return ScoreNumber._200;
            case TANK_NPC_ARMOR:
                return ScoreNumber._400;
            default:
                return null;
        }
    }

    public void fastCool() {
        coolingFrame = Math.min(coolingFrame, 4);
    }

    public boolean isCarryFood() {
        return carryFood;
    }

    public Player getPlayer() {
        return player;
    }

    public void forceStop(int stopTime) {
        this.stopTime = stopTime;
    }

    @Override
    public boolean isBlock() {
        return true;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
     }
}
