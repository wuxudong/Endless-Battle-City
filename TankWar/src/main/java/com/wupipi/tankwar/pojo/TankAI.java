package com.wupipi.tankwar.pojo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by xudong on 8/3/13.
 */
public class TankAI {

  private Battle battle;

  private Random random = new Random();

  private int frame = 0;

  private Map<Tank, Long> lastCommandFrame = new HashMap<Tank, Long>();

  public TankAI(Battle battle) {
    this.battle = battle;
  }

  public void ai() {
    for (Tank tank : battle.tanks) {
      if (tank != battle.playerTank) {

        // every tank receive command every half second
        if (frame % 25 == 0) {
          tank.setMove(true);
          tank.fire(true);
          tank.head(Direction.values()[random.nextInt(4)]);
        }
      }
    }

    frame++;
  }
}
