package com.wupipi.tankwar.pojo;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * here is com.wupipi.tankwar.a restriction, any movable object's speed < smallest Object size.
 * the smallest Object is Bullet, and size is 6
 *
 *
 * Created by xudong on 7/28/13.
 */
public abstract class Movable implements Drawable{
  public Point position;

  abstract int getWidth();
  abstract int getHeight();
  Rect getRect() {
    return new Rect(position.x, position.y , position.x + getWidth(), position.y + getHeight());
  }
  abstract void move();
}
