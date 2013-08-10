package com.wupipi.tankwar.pojo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by xudong on 8/8/13.
 */
public class Food extends Movable {
  private FoodType foodType;

  private int time;

  private Battle battle;

  public Food(Battle battle, Point position, FoodType foodType) {
    this.battle = battle;
    this.position = position;
    this.foodType = foodType;
  }

  @Override
  int getWidth() {
    return 30;
  }

  @Override
  int getHeight() {
    return 28;
  }

  @Override
  void move() {
    if (time > 300) {
      battle.setFood(null);
    }

    for (Tank player : battle.getTanks(Ally.PLAYER)) {
      if (Rect.intersects(player.getRect(), getRect())) {

        switch (foodType)
        {
          case LIFE:
            // player.live ++;
            // scoreBoard.drawPlayerLife(tanks[i].name,tanks[i].live);
            break;
          case GOD: {
            player.beGod();
            break;
          }
          case HOME:
            battle.protectHome(true);
            break;
          case STAR:
            player.star();
            break;
          case TIME:
            battle.stopNpc();
            break;
          case BOMB:
            battle.clearNpc();
            break;
          default:
            return;
        }
        battle.setFood(null);
      }
    }

    time++;
  }

  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    if ((time / 5) % 2 == 0) {
      canvas
          .drawBitmap(tankWarImage.food[foodType.ordinal()], null, getRect(), paint);
    }
  }
}
