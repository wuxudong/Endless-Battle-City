package com.wupipi.tankwar.model;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

import com.wupipi.tankwar.Const;
import com.wupipi.tankwar.Direction;
import com.wupipi.tankwar.FoodType;
import com.wupipi.tankwar.WorkThread;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Created by xudong on 7/26/13.
 */
public class Scene implements Serializable {

    public long frame = 0;

    private Random random = new Random();

    List<FrameAction> frameActions = new ArrayList<FrameAction>();

    /**
     * Current mode of application: READY to run, RUNNING, or you have already lost. static final
     * ints are used instead of an enum for performance reasons.
     */
    public int mMode = WorkThread.RUNNING;

    private Tank playerTank;

    Player player = new Player(3, Ally.PLAYER);

    Player npcPlayer;

    private Set<Tank> playerTanks = new HashSet<Tank>();

    private Set<Tank> npcTanks = new HashSet<Tank>();

    private Set<Bullet> bullets = new HashSet<Bullet>();

    private Set<Animation> animations = new HashSet<Animation>();

    private Food food = null;

    private GameMap gameMap;

    private int homeGodTime = 0;

    private Obstacle[][] tankTiles = new Obstacle[Const.TILE_COUNT][Const.TILE_COUNT];

    private Bullet[][] playerBulletTiles = new Bullet[Const.TILE_COUNT][Const.TILE_COUNT];

    private RandomTankAI npcAI = new RandomTankAI();

    private int stopNpcTime;

    public Scene() {

        playerTank =
                new Tank(new Point(8 * 16, 24 * 16), Ally.PLAYER, TankType.PLAY1, false, player);
        playerTank.beGod(300);
        playerTanks.add(playerTank);

        npcPlayer = new Player(20, Ally.NPC);

        Tank tank1 = new Tank(new Point(0, 0), Ally.NPC, TankType.NPC1, false, npcPlayer);
        npcTanks.add(tank1);

        Tank tank2 = new Tank(new Point(182, 0), Ally.NPC, TankType.NPC2, false, npcPlayer);
        npcTanks.add(tank2);

        Tank tank3 = new Tank(new Point(384, 0), Ally.NPC, TankType.NPC3, false, npcPlayer);
        npcTanks.add(tank3);
    }

    public void nextFrame() {
        frame++;

        for(int row = 0; row <  tankTiles.length; row ++) {
            for (int col = 0; col < tankTiles[row].length; col ++) {
                tankTiles[row][col] = null;
            }
        }

        for (Tank tank : playerTanks) {
            for (Tile tile : Tile.getCrossedTiles(tank.getRect())) {
                tankTiles[tile.row][tile.col] = tank;
            }
        }

        for (Tank tank : npcTanks) {
            npcAI.ai(this, tank);

            for (Tile tile : Tile.getCrossedTiles(tank.getRect())) {
                tankTiles[tile.row][tile.col] = tank;
            }
        }

        for(int row = 0; row <  playerBulletTiles .length; row ++) {
            for (int col = 0; col < playerBulletTiles [row].length; col ++) {
                playerBulletTiles [row][col] = null;
            }
        }

        for (Bullet bullet : bullets) {
            if (bullet.getOwner().getAlly() == Ally.PLAYER) {
                for (Tile tile : Tile.getCrossedTiles(bullet.getRect())) {
                    playerBulletTiles[tile.row][tile.col] = bullet;
                }
            }
        }

        if (stopNpcTime > 0) {
            stopNpcTime--;
        } else {
            for (Tank t : npcTanks) {
                t.nextFrame(this);
            }
        }


        for (Tank t : playerTanks) {
            t.nextFrame(this);
        }

        for (Bullet b : bullets) {
            b.nextFrame(this);
        }

        for (Animation obj : animations) {
            obj.nextFrame(this);
        }

        if (food != null) {
            food.nextFrame(this);
        }

        if (npcPlayer.life > 0) {
            delayedTankStart();
        }

        if (homeGodTime > 0) {
            homeGodTime--;
            if (homeGodTime == 0) {
                delayedProtectHome(false);
            }
        }

        collision();

        for (FrameAction action : frameActions) {
            action.run();
        }


        frameActions.clear();
    }

    private void collision() {
        for (final Bullet bullet : bullets) {

            boolean hit = false;
            for (final Tile tile : Tile.getCrossedTiles(bullet.getRect())) {
                for (Obstacle obstacle : getObstacles(tile)) {
                    if (obstacle == bullet) {
                        continue;
                    } else if (!obstacle.isCollidable()) {
                        continue;
                    } else if (obstacle.getRect().intersect(bullet.getRect())) {
                        if (obstacle instanceof Wall) {
                            hit = true;

                            frameActions.add(new FrameAction() {
                                @Override
                                public void run() {
                                    switch (bullet.getDirection()) {
                                        case NORTH: {
                                            gameMap.setWallTopChip(tile.row, tile.col);
                                            break;
                                        }
                                        case SOUTH: {
                                            gameMap.setWallBottomChip(tile.row, tile.col);
                                            break;
                                        }
                                        case WEST: {
                                            gameMap.setWallLeftChip(tile.row, tile.col);
                                            break;
                                        }
                                        case EAST: {
                                            gameMap.setWallRightChip(tile.row, tile.col);
                                            break;
                                        }
                                    }
                                    animations.add(new Hit(new Point(bullet.getRect().centerX(), bullet.getRect().centerY())));
                                }
                            });


                        } else if (obstacle instanceof Grid) {
                            hit = true;

                            frameActions.add(new FrameAction() {
                                @Override
                                public void run() {
                                    if (bullet.getPower() >= 2) {
                                        gameMap.clear(tile.row, tile.col);
                                    }

                                    animations.add(new Hit(new Point(bullet.getRect().centerX(), bullet.getRect().centerY())));
                                }
                            });
                        } else if (obstacle instanceof WallTopChip || obstacle instanceof WallBottomChip || obstacle instanceof WallLeftChip || obstacle instanceof WallRightChip) {
                            hit = true;
                            frameActions.add(new FrameAction() {
                                @Override
                                public void run() {
                                    gameMap.clear(tile.row, tile.col);
                                    animations.add(new Hit(new Point(bullet.getRect().centerX(), bullet.getRect().centerY())));
                                }
                            });
                        } else if (obstacle instanceof Tank) {
                            final Tank t = (Tank) obstacle;

                            if (t.isValid()) {
                                if (bullet.getOwner().getAlly() == t.getAlly()) {
                                    // not collide
                                } else {
                                    hit = true;

                                    if (!t.isGod()) {
                                        t.setHealth(t.getHealth() - 1);

                                        if (t.getHealth() <= 0) {
                                            t.setValid(false);
                                            delayedDestroyTank(t);

                                            if (t.getAlly() == Ally.PLAYER) {
                                                delayedBorn(t.getPlayer());
                                            }

                                            frameActions.add(new FrameAction() {
                                                @Override
                                                public void run() {
                                                    animations.add(new Bomb(t.position, t.scoreNumber()));

                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        } else if (obstacle instanceof Home) {
                            hit = true;
                            ((Home) obstacle).setDestroyed(true);

                        }

                    }
                }

                if (bullet.getOwner().getAlly() == Ally.NPC && playerBulletTiles[tile.row][tile.col] != null) {
                    if (bullet.getRect().intersect(playerBulletTiles[tile.row][tile.col].getRect())) {
                        hit = true;
                        delayedDestroyBullet(playerBulletTiles[tile.row][tile.col]);
                    }
                }


                if (hit) {
                    delayedDestroyBullet(bullet);
                }
            }

        }

        if (food != null) {
            boolean consume = false;
            for (final Tile tile : Tile.getCrossedTiles(food.getRect())) {
                for (Obstacle obstacle : getObstacles(tile)) {
                    if (obstacle instanceof Tank) {
                        if (((Tank) obstacle).getAlly() == Ally.PLAYER) {
                            consume = true;
                            switch (food.getFoodType()) {
                                case LIFE:
                                    player.life++;
                                    break;
                                case GOD:
                                    playerTank.beGod(600);
                                    break;
                                case HOME:
                                    delayedProtectHome(true);
                                    break;
                                case STAR:
                                    playerTank.promote();
                                    break;
                                case TIME:
                                    stopNpc();
                                    break;
                                case BOMB:
                                    delayedClearNpc();
                                    break;
                                default:
                            }
                        }
                    }
                }
            }

            if (consume) {
                frameActions.add(new FrameAction() {
                    @Override
                    public void run() {
                        food = null;
                    }
                });
            }
        }


    }

    private void moreFood() {

        int x = random.nextInt((Const.TILE_COUNT - 2) * Const.OFFSET_PER_TILE);
        int y = random.nextInt((Const.TILE_COUNT - 2) * Const.OFFSET_PER_TILE);

        int index = random.nextInt(FoodType.values().length);
        FoodType type = FoodType.values()[index];

        food = new Food(new Point(x, y), type);
    }

    private void delayedTankStart() {

        if (frame % 250 == 0 && npcTanks.size() < 10) {
            npcPlayer.life--;
            int where = random.nextInt(3);

            int type = random.nextInt(3);

            final boolean carryFood = random.nextInt(2) == 0;
            TankType tankType = null;
            switch (type) {
                case 0: {
                    tankType = TankType.NPC1;
                    break;
                }
                case 1: {
                    tankType = TankType.NPC2;
                    break;
                }
                case 2: {
                    tankType = TankType.NPC3;
                    break;
                }
            }

            Tank tank = null;

            switch (where) {
                case 0: {
                    tank = new Tank(new Point(192, 0), Ally.NPC, tankType, carryFood, npcPlayer);
                    break;
                }
                case 1: {
                    tank = new Tank(new Point(0, 0), Ally.NPC, tankType, carryFood, npcPlayer);
                    break;
                }
                case 2: {
                    tank = new Tank(new Point(384, 0), Ally.NPC, tankType, carryFood, npcPlayer);
                    break;
                }
            }

            final TankStart tankStart = new TankStart(tank);
            frameActions.add(new FrameAction() {
                @Override
                public void run() {
                    animations.add(tankStart);
                }
            });
        }
    }


    public List<Obstacle> getObstacles(Tile tile) {
        List<Obstacle> result = new ArrayList<Obstacle>();

        if (gameMap.get(tile.row, tile.col) != null) {
            result.add(gameMap.get(tile.row, tile.col));
        }

        if (tankTiles[tile.row][tile.col] != null) {
            result.add(tankTiles[tile.row][tile.col]);
        }

        return result;
    }


    public Food getFood() {
        return food;
    }

    public void delayedClearNpc() {
        for (final Tank tank : npcTanks) {
            final Bomb bomb = new Bomb(tank.position, ScoreNumber.NONE);

            frameActions.add(new FrameAction() {
                @Override
                public void run() {
                    npcTanks.remove(tank);
                    animations.add(bomb);
                }
            });
        }
    }

    public void delayedProtectHome(final boolean god) {
        if (god) {
            homeGodTime = 500;
        }

        frameActions.add(new FrameAction() {
            @Override
            public void run() {
                gameMap.protectHome(god);
            }
        });
    }

    public void stopNpc() {
        stopNpcTime = 500;
    }

    public boolean delayedBorn(final Player p) {
        if (p.life > 0) {
            p.life--;

            if (p.getAlly() == Ally.PLAYER) {

                Tank tank =
                        new Tank(new Point(8 * 16, 24 * 16), Ally.PLAYER, TankType.PLAY1, false,
                                p);
                tank.beGod(300);

                final TankStart tankStart = new TankStart(tank);

                frameActions.add(new FrameAction() {
                    @Override
                    public void run() {
                        animations.add(tankStart);
                    }
                });

                return true;
            }
        }
        return false;

    }

    public void actPlayerMove(Direction direction) {
        switch (direction) {
            case NORTH:
                playerTank.head(Direction.NORTH);
                playerTank.setMove(true);
                break;

            case SOUTH:
                playerTank.head(Direction.SOUTH);
                playerTank.setMove(true);
                break;

            case WEST:
                playerTank.head(Direction.WEST);
                playerTank.setMove(true);
                break;

            case EAST:
                playerTank.head(Direction.EAST);
                playerTank.setMove(true);

                break;
            case NONE:
                playerTank.setMove(false);
                break;

        }
    }

    public void actPlayerFire(boolean b) {
        playerTank.setFire(b);
    }

    public void delayedTankFires(final Bullet bullet) {
        frameActions.add(new FrameAction() {
            @Override
            public void run() {
                Scene.this.bullets.add(bullet);
            }
        });

    }

    public void delayedDestroyBomb(final Bomb bomb) {
        frameActions.add(new FrameAction() {
            @Override
            public void run() {
                Scene.this.animations.remove(bomb);
                if (bomb.getScoreNumber() != ScoreNumber.NONE) {
                    Scene.this.animations.add(new Score(new Point(bomb.getRect().centerX(), bomb.getRect().centerY()), bomb.getScoreNumber()));
                }
            }
        });
    }

    public void delayedDestroyBullet(final Bullet bullet) {
        frameActions.add(new FrameAction() {
            @Override
            public void run() {
                Scene.this.bullets.remove(bullet);
                bullet.getOwner().fastCool();
            }
        });
    }

    public void delayDestroyFood(Food food) {
        frameActions.add(new FrameAction() {
            @Override
            public void run() {
                Scene.this.food = null;
            }
        });


    }

    public void delayedDestroyHit(final Hit hit) {
        frameActions.add(new FrameAction() {
            @Override
            public void run() {
                animations.remove(hit);
            }
        });

    }

    public void delayedDestroyScore(final Score score) {
        frameActions.add(new FrameAction() {
            @Override
            public void run() {
                animations.remove(score);
            }
        });

    }

    public void delayedDestroyTank(final Tank tank) {
        frameActions.add(new FrameAction() {
            @Override
            public void run() {
                if (tank.isCarryFood()) {
                    moreFood();
                }
                switch (tank.getAlly()) {
                    case PLAYER:
                        playerTanks.remove(tank);
                        break;
                    case NPC:
                        npcTanks.remove(tank);
                        break;
                }
            }
        });


    }

    public void destroyTankStart(final TankStart tankStart) {
        frameActions.add(new FrameAction() {
            @Override
            public void run() {
                animations.remove(tankStart);
                Tank tank = tankStart.getTank();
                switch (tank.getAlly()) {
                    case PLAYER:
                        playerTank = tank;
                        playerTanks.add(tank);
                        break;
                    case NPC:
                        npcTanks.add(tank);
                        break;
                }
            }
        });
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public Set<Tank> getPlayerTanks() {
        return playerTanks;
    }

    public Set<Tank> getNpcTanks() {
        return npcTanks;
    }

    public Set<Bullet> getBullets() {
        return bullets;
    }

    public Set<Animation> getAnimations() {
        return animations;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public static void dump(Scene scene) {
        File dir = new File("/sdcard/TankWar");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, "dump");
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(scene);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Scene restore() {
        File dir = new File("/sdcard/TankWar");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, "dump");
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            Scene scene = (Scene) input.readObject();
            input.close();

            return scene;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
