package com.wupipi.tankwar.pojo;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by xudong on 8/10/13.
 */
public class ScoreBoard implements Drawable {

  private Battle battle;

  public ScoreBoard(Battle battle) {
    this.battle = battle;
  }

  private void drawPlayer(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    // TODO: replace text with images
    for (int i = 0; i < battle.players.length; i++) {
      canvas.drawBitmap(tankWarImage.playerLife[i], 464, 52 * i + 256, paint);
      // TODO: remove 10 when use images
      canvas.drawText(String.valueOf(battle.players[i].life), 482, 48 * i + 272 + 10, paint);
    }
  }

  private void drawNpc(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    // TODO: replace text with images


    for (int i = 0; i < battle.npcPlayer.life; i++) {
      int tx = 0;
      if (i % 2 == 0)
      {
        tx = 0;
      }
      else
      {
        tx = 16;
      }

      int ty = (i / 2) * 16;

      canvas.drawBitmap(tankWarImage.npcLife, 466 + tx, 34 + ty, paint);
    }
  }


  private void drawLevel(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    canvas.drawBitmap(tankWarImage.level, 464, 352, paint);
    canvas.drawText(String.valueOf(4), 468, 384, paint);
  }


  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    drawPlayer(canvas, paint, tankWarImage);
    drawLevel(canvas, paint, tankWarImage);
    drawNpc(canvas, paint, tankWarImage);
  }
}
