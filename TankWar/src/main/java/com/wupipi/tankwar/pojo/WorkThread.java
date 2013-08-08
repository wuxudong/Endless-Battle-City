package com.wupipi.tankwar.pojo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.wupipi.tankwar.Const;
import com.wupipi.tankwar.TankWarView;

/**
 * Created by xudong on 8/2/13.
 */
public class
    WorkThread extends Thread {

  private TankWarView tankWarView;

  private final Battle battle;


  public static final int PAUSE = 0;
  public static final int READY = 1;
  public static final int RUNNING = 2;
  public static final int LOSE = 3;


  private long mMoveDelay = 20;
  /**
   * mLastMove: Tracks the absolute time when the snake last moved, and is used to determine if
   * com.wupipi.tankwar.a
   * move should be made based on mMoveDelay.
   */
  private long mLastMove;

  private final Paint mPaint = new Paint();

  private TankWarImage tankWarImage;

  public void moveTank(int direction) {

    // Log.d("TANK_WAR", "move to " + direction);

    if (direction == Const.MOVE_UP) {
      battle.playerTank.head(Direction.NORTH);
      battle.playerTank.setMove(true);
      return;
    }

    if (direction == Const.MOVE_DOWN) {
      battle.playerTank.head(Direction.SOUTH);
      battle.playerTank.setMove(true);
      return;
    }

    if (direction == Const.MOVE_LEFT) {
      battle.playerTank.head(Direction.WEST);
      battle.playerTank.setMove(true);
      return;
    }

    if (direction == Const.MOVE_RIGHT) {
      battle.playerTank.head(Direction.EAST);
      battle.playerTank.setMove(true);
      return;
    }

    if (direction == Const.MOVE_NONE) {
      battle.playerTank.setMove(false);
      return;
    }
  }

  public void fire(boolean b) {
    battle.playerTank.fire(b);
  }


  public WorkThread(TankWarView view) {
    tankWarView = view;
    tankWarImage = new TankWarImage(view.getResources());
    battle = new Battle();
    battle.gameMap = new MapLevel().getGameMap(3);
  }

  public void startThread()
  {
    battle.mMode = RUNNING;
    super.start();
  }

  public void stopThread()
  {
    battle.mMode = PAUSE;
  }



  @Override
  public void run() {
    Canvas canvas = null;
    while (!isInterrupted()) {
      try {

        canvas = tankWarView.getHolder().lockCanvas();

        int min = Math.min(canvas.getWidth(), canvas.getHeight());

        float scale =
            (float) min
                / (float) (Const.OFFSET_PER_TILE * Const.TILE_COUNT);


        canvas.scale(scale, scale);
        Log.d("Tank_War", "current matrix " + canvas.getMatrix());
        Log.d("Tank_War", "newly matrix " + new Matrix());


        battle.update();

        canvas.drawColor(Color.BLACK);
        for (Drawable bullet : battle.bullets) {
          bullet.draw(canvas, mPaint, tankWarImage);
        }

        for (Drawable tank : battle.tanks) {
          tank.draw(canvas, mPaint, tankWarImage);
        }

        for (Drawable tankStart : battle.tankStarts) {
          tankStart.draw(canvas, mPaint, tankWarImage);
        }



        for (int i = 0; i < Const.TILE_COUNT; i++) {
          for (int j = 0; j < Const.TILE_COUNT; j++) {
            if (battle.gameMap.obstacles[i][j] != null) {
              battle.gameMap.obstacles[i][j].draw(canvas, mPaint, tankWarImage);
            }
          }
        }

        for (Drawable hit : battle.hits) {
          hit.draw(canvas, mPaint, tankWarImage);
        }

        for (Drawable hit : battle.bombs) {
          hit.draw(canvas, mPaint, tankWarImage);
        }


        for (Drawable hit : battle.scores) {
          hit.draw(canvas, mPaint, tankWarImage);
        }



      } finally {
        if (canvas != null)
        {
          tankWarView.getHolder().unlockCanvasAndPost(canvas);
        }
      }

      try {
        sleep(mMoveDelay);
      } catch (InterruptedException e) {}
    }

  }
}
