package com.wupipi.tankwar.pojo;

/**
 * Created by xudong on 8/10/13.
 */
public class Player {
  public int life = 20;

  private Ally ally;

  public Player(int life, Ally ally) {
    this.life = life;
    this.ally = ally;
  }

  public Ally getAlly() {
    return ally;
  }
}
