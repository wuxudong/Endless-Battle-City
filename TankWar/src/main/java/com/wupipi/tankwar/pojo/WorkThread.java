package com.wupipi.tankwar.pojo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.wupipi.tankwar.Const;
import com.wupipi.tankwar.TankWarView;

/**
 * Created by xudong on 8/2/13.
 */
public class WorkThread extends Thread {

  private TankWarView tankWarView;

  private final Battle battle;

  private final ScoreBoard scoreBoard;
  public static final int PAUSE = 0;
  public static final int READY = 1;
  public static final int RUNNING = 2;
  public static final int LOSE = 3;


  private long mMoveDelay = 20;

  private final Paint mPaint = new Paint();

  private TankWarImage tankWarImage;

  public void moveTank(int direction) {

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

    scoreBoard = new ScoreBoard(battle);
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

      long start = System.currentTimeMillis();
      try {

        battle.update();

        canvas = tankWarView.getHolder().lockCanvas();

        canvas.drawColor(Color.GRAY);

        float sx = (float) canvas.getWidth() / (float) 512;

        float sy = (float) canvas.getHeight() / (float) 448;

        canvas.scale(sx, sy);


        scoreBoard.draw(canvas, mPaint, tankWarImage);



        canvas.translate(32, 16);



        canvas.clipRect(0, 0, Const.OFFSET_PER_TILE * Const.TILE_COUNT, Const.OFFSET_PER_TILE
            * Const.TILE_COUNT);
        canvas.drawColor(Color.BLACK);
        for (Ally ally : Ally.values()) {

          for (Drawable bullet : battle.getBullets(ally)) {
            bullet.draw(canvas, mPaint, tankWarImage);
          }

          for (Drawable tank : battle.getTanks(ally)) {
            tank.draw(canvas, mPaint, tankWarImage);
          }
        }

        for (Drawable tankStart : battle.tankStarts) {
          tankStart.draw(canvas, mPaint, tankWarImage);
        }



        // TODO: grass should draw after bullet, but bullet is after ice and water
        for (int i = 0; i < Const.TILE_COUNT; i++) {
          for (int j = 0; j < Const.TILE_COUNT; j++) {
            Obstacle obstacle = battle.gameMap.get(i, j);
            if (obstacle != null) {
              obstacle.draw(canvas, mPaint, tankWarImage);
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

        if (battle.getFood() != null) {
          battle.getFood().draw(canvas, mPaint, tankWarImage);
        }



      } finally {
        if (canvas != null)
        {
          tankWarView.getHolder().unlockCanvasAndPost(canvas);
        }
      }


      Log.d("Tank_War", "spend " + (System.currentTimeMillis() - start));
      try {

        long delay = mMoveDelay - (System.currentTimeMillis() - start);
        if (delay > 0) {
          sleep(mMoveDelay);
        }
      } catch (InterruptedException e) {}
    }

  }
}
