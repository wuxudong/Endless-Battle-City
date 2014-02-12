package com.wupipi.tankwar.model;

import com.wupipi.tankwar.Direction;
import com.wupipi.tankwar.model.Scene;
import com.wupipi.tankwar.model.Tank;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by xudong on 8/3/13.
 */
public class RandomTankAI {

  private Random random = new Random();

  private Map<Tank, Long> lastCommandFrame = new HashMap<Tank, Long>();

  public void ai(Scene scene, Tank tank) {
    // every tank receive command every half second
    if (scene.frame % 25 == 0) {
      tank.setMove(true);
      tank.setFire(true);
      tank.head(Direction.values()[random.nextInt(4)]);
    }
  }
}
