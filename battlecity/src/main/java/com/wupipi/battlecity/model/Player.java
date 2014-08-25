package com.wupipi.battlecity.model;

import com.wupipi.battlecity.Ally;

import java.io.Serializable;

/**
 * Created by xudong on 8/10/13.
 */
public class Player implements Serializable{
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
