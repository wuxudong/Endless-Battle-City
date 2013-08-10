package com.wupipi.tankwar.pojo;

import static com.wupipi.tankwar.pojo.TankWarImage.ImageType.*;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.wupipi.tankwar.R;

/**
 * Created by xudong on 7/27/13.
 */
public class TankWarImage {



  enum ImageType {
    WALL, GRID, GRASS, ICE, WATER,
    HOME, HOME_DESTROYED, HOME_PLACEHOLDER,
    FOOD_LIFE, FOOD_FREEZE, FOOD_HOME_DEFENCE, FOOD_BOMB, FOOD_STAR, FOOD_GOD
  }

  private final static Point PLAY1_TANK_ORIGIN = new Point(0, 0);
  private final static Point PLAY2_TANK_ORIGIN = new Point(128, 0);
  private final static Point MAP_ORIGIN = new Point(0, 96);
  private final static Point HOME_ORIGIN = new Point(256, 0);
  private final static Point TANK1_ORIGIN = new Point(0, 32);
  private final static Point TANK2_ORIGIN = new Point(128, 32);
  private final static Point TANK3_ORIGIN = new Point(0, 64);
  private final static Point HIT_ORIGIN = new Point(320, 0);
  private final static Point BOMB_ORIGIN = new Point(0, 160);
  private final static Point BULLET_ORIGIN = new Point(80, 96);
  private final static Point SCORE_ORIGIN = new Point(192, 96);
  private final static Point FOOD_ORIGIN = new Point(256, 110);
  private final static Point SHIELD_ORIGIN = new Point(160, 96);
  private final static Point TANK_START_ORIGIN = new Point(256, 32);


  Bitmap[] play1 = new Bitmap[Direction.values().length];
  Bitmap[] tank1 = new Bitmap[Direction.values().length];
  Bitmap[] tank2 = new Bitmap[Direction.values().length];
  Bitmap[] tank3 = new Bitmap[Direction.values().length];
  Bitmap[] bullet = new Bitmap[Direction.values().length];
  Bitmap[] food = new Bitmap[FoodType.values().length];
  Bitmap[] hit = new Bitmap[3];
  Bitmap[] bomb = new Bitmap[4];
  Bitmap[] tankStart = new Bitmap[7];
  Bitmap[] shield = new Bitmap[2];

  Bitmap[] score = new Bitmap[Score.ScoreNumber.values().length];

  Bitmap[] images = new Bitmap[ImageType.values().length];

  Bitmap wallTopChip = null;
  Bitmap wallLeftChip = null;
  Bitmap wallRightChip = null;
  Bitmap wallBottomChip = null;

  public TankWarImage(Resources resources) {
    BitmapFactory.Options myOptions = new BitmapFactory.Options();
    myOptions.inScaled = false;

    Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.tank_all, myOptions);

    initPlay1(bitmap);

    initTank1(bitmap);
    initTank2(bitmap);
    initTank3(bitmap);

    initBullet(bitmap);


    initMap(bitmap);


    initHit(bitmap);


    initBomb(bitmap);

    initWallChip();

    initScore(bitmap);


    initTankStart(bitmap);

    initFood(bitmap);

    initShield(bitmap);

  }

  private void initShield(Bitmap bitmap) {
    for (int frame = 0; frame < shield.length; frame++) {
      shield[frame] =
          Bitmap
              .createBitmap(bitmap, SHIELD_ORIGIN.x, 32 * frame + SHIELD_ORIGIN.y, 32, 32);
    }
  }

  private void initFood(Bitmap bitmap) {
    for (int type = 0; type < food.length; type++) {
      food[type] =
          Bitmap
              .createBitmap(bitmap, 30 * type + FOOD_ORIGIN.x, FOOD_ORIGIN.y, 30, 28);
    }
  }

  private void initTankStart(Bitmap bitmap) {
    for (int frame = 0; frame < tankStart.length; frame++) {
      tankStart[frame] =
          Bitmap
              .createBitmap(bitmap, 32 * frame + TANK_START_ORIGIN.x, TANK_START_ORIGIN.y, 32, 32);
    }
  }

  private void initBomb(Bitmap bitmap) {
    for (int bombFrame = 0; bombFrame < 4; bombFrame++) {
      bomb[bombFrame] =
          Bitmap.createBitmap(bitmap, 67 * bombFrame + BOMB_ORIGIN.x, BOMB_ORIGIN.y, 66, 66);
    }
  }

  private void initWallChip() {
    wallTopChip = Bitmap.createBitmap(images[WALL.ordinal()], 0, 0, 16, 8);
    wallBottomChip = Bitmap.createBitmap(images[WALL.ordinal()], 0, 8, 16, 8);
    wallLeftChip = Bitmap.createBitmap(images[WALL.ordinal()], 0, 0, 8, 16);
    wallRightChip = Bitmap.createBitmap(images[WALL.ordinal()], 8, 0, 8, 16);
  }

  private void initHit(Bitmap bitmap) {
    for (int hitFrame = 0; hitFrame < 3; hitFrame++) {
      hit[hitFrame] =
          Bitmap.createBitmap(bitmap, 32 * hitFrame + HIT_ORIGIN.x, HIT_ORIGIN.y, 32, 32);
    }
  }

  private void initMap(Bitmap bitmap) {

    images[WALL.ordinal()] = Bitmap.createBitmap(bitmap, MAP_ORIGIN.x, MAP_ORIGIN.y, 16, 16);
    images[GRID.ordinal()] = Bitmap.createBitmap(bitmap, 16 + MAP_ORIGIN.x, MAP_ORIGIN.y, 16, 16);

    images[WATER.ordinal()] = Bitmap.createBitmap(bitmap, 48 + MAP_ORIGIN.x, MAP_ORIGIN.y, 16, 16);
    images[ICE.ordinal()] = Bitmap.createBitmap(bitmap, 64 + MAP_ORIGIN.x, MAP_ORIGIN.y, 16, 16);
    images[GRASS.ordinal()] = Bitmap.createBitmap(bitmap, 32 + MAP_ORIGIN.x, MAP_ORIGIN.y, 16, 16);

    images[HOME.ordinal()] = Bitmap.createBitmap(bitmap, HOME_ORIGIN.x, HOME_ORIGIN.y, 32, 32);
    images[HOME_DESTROYED.ordinal()] =
        Bitmap.createBitmap(bitmap, 32 + HOME_ORIGIN.x, HOME_ORIGIN.y, 32, 32);
  }

  private void initScore(Bitmap bitmap) {
    for (Score.ScoreNumber scoreNumber : Score.ScoreNumber.values()) {
      score[scoreNumber.ordinal()] =
          Bitmap.createBitmap(bitmap, SCORE_ORIGIN.x
              , SCORE_ORIGIN.y + 14 * scoreNumber.ordinal(), 28, 14);
    }
  }


  private void initBullet(Bitmap bitmap) {
    for (Direction direction : Direction.values()) {
      bullet[direction.ordinal()] =
          Bitmap.createBitmap(bitmap, BULLET_ORIGIN.x
              + direction.ordinal() * 6, BULLET_ORIGIN.y, 6, 6);
    }
  }

  private void initPlay1(Bitmap bitmap) {
    for (Direction direction : Direction.values()) {
      play1[direction.ordinal()] =
          Bitmap.createBitmap(bitmap, PLAY1_TANK_ORIGIN.x + direction.ordinal() * 32,
              PLAY1_TANK_ORIGIN.y, 32, 32);
    }
  }

  private void initTank1(Bitmap bitmap) {
    for (Direction direction : Direction.values()) {
      tank1[direction.ordinal()] =
          Bitmap.createBitmap(bitmap, TANK1_ORIGIN.x + direction.ordinal() * 32,
              TANK1_ORIGIN.y, 32, 32);
    }
  }

  private void initTank2(Bitmap bitmap) {
    for (Direction direction : Direction.values()) {
      tank2[direction.ordinal()] =
          Bitmap.createBitmap(bitmap, TANK2_ORIGIN.x + direction.ordinal() * 32,
              TANK2_ORIGIN.y, 32, 32);
    }
  }

  private void initTank3(Bitmap bitmap) {
    for (Direction direction : Direction.values()) {
      tank3[direction.ordinal()] =
          Bitmap.createBitmap(bitmap, TANK3_ORIGIN.x + direction.ordinal() * 32,
              TANK3_ORIGIN.y, 32, 32);
    }
  }


}
