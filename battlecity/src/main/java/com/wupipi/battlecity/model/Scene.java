package com.wupipi.battlecity.model;

import android.graphics.Point;

import com.google.common.collect.Iterables;
import com.wupipi.battlecity.Ally;
import com.wupipi.battlecity.Const;
import com.wupipi.battlecity.Direction;
import com.wupipi.battlecity.FoodType;
import com.wupipi.battlecity.FrameAction;
import com.wupipi.battlecity.FrameAware;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by xudong on 7/26/13.
 */
public class Scene implements Serializable{

    public long frame = 0;

    private transient static Random random = new Random();

    private List<FrameAction> frameActions = new ArrayList<FrameAction>();

    private Tank playerTank;

    private Player player = new Player(1, Ally.PLAYER);

    private Player npcPlayer = new Player(Integer.MAX_VALUE, Ally.NPC);

    private Set<Tank> npcTanks = new HashSet<Tank>();

    private Set<Bullet> bullets = new HashSet<Bullet>();

    private Set<Explosion> explosions = new HashSet<Explosion>();

    private Set<Hit> hits = new HashSet<Hit>();

    private Set<TankBirth> tankBirths = new HashSet<TankBirth>();

    private Set<Score> scores = new HashSet<Score>();

    private Food food = null;

    private BattleField battleField;

    private Tank[][] tankTiles = new Tank[Const.TILE_COUNT][Const.TILE_COUNT];

    private Bullet[][] bulletTiles = new Bullet[Const.TILE_COUNT][Const.TILE_COUNT];

    private transient static RandomTankAI npcAI = new RandomTankAI();

    private int homeGodTime = 0;

    private int stopNpcTime = 0;

    private int highestScore = 0;

    private int score = 0;

    private boolean lose = false;

    private transient SceneManager sceneManager;

    private RestartAction restart;

    public Scene() {

        delayedPlayerBorn();

        delayedNpcBorn();

        // default game level 1
        setBattleField(new GameLevel().getBattleField(1));
    }

    public void nextFrame() {
        frame++;

        for (int row = 0; row < tankTiles.length; row++) {
            for (int col = 0; col < tankTiles[row].length; col++) {
                tankTiles[row][col] = null;
            }
        }

        if (playerTank != null) {
            for (Tile tile : Tile.getCrossedTiles(playerTank.getRect())) {
                tankTiles[tile.row][tile.col] = playerTank;
            }
        }

        for (Tank tank : npcTanks) {
            npcAI.ai(this, tank);

            for (Tile tile : Tile.getCrossedTiles(tank.getRect())) {
                tankTiles[tile.row][tile.col] = tank;
            }
        }

        for (int row = 0; row < bulletTiles.length; row++) {
            for (int col = 0; col < bulletTiles[row].length; col++) {
                bulletTiles[row][col] = null;
            }
        }

        for (Bullet bullet : bullets) {
            if (bullet.getOwner().getAlly() == Ally.PLAYER) {
                for (Tile tile : Tile.getCrossedTiles(bullet.getRect())) {
                    bulletTiles[tile.row][tile.col] = bullet;
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

        if (playerTank != null) {
            playerTank.nextFrame(this);
        }

        for (Bullet b : bullets) {
            b.nextFrame(this);
        }

        for (Explosion e : explosions) {
            e.nextFrame(this);
        }

        for (Hit h : hits) {
            h.nextFrame(this);
        }

        for (Score s : scores) {
            s.nextFrame(this);
        }

        for (TankBirth b : tankBirths) {
            b.nextFrame(this);
        }


        if (food != null) {
            food.nextFrame(this);
        }

        if (npcPlayer.life > 0) {
            delayedNpcBorn();
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

        if (restart != null) {
            restart.nextFrame(this);
        }
    }

    private void collision() {

        Set shotTanks = new HashSet<Tank>();
        for (final Bullet bullet : bullets) {

            shotTanks.clear();

            boolean hit = false;
            for (final Tile tile : Tile.getCrossedTiles(bullet.getRect())) {
                for (Obstacle obstacle : getObstacles(tile)) {
                    if (!obstacle.isBlock()) {
                        continue;
                    } else if (obstacle.getRect().intersect(bullet.getRect())) {
                        if (obstacle instanceof Wall) {
                            hit = true;

                            frameActions.add(new FrameAction() {
                                @Override
                                public void run() {
                                    switch (bullet.getDirection()) {
                                        case NORTH: {
                                            battleField.setWallTopChip(tile.row, tile.col);
                                            break;
                                        }
                                        case SOUTH: {
                                            battleField.setWallBottomChip(tile.row, tile.col);
                                            break;
                                        }
                                        case WEST: {
                                            battleField.setWallLeftChip(tile.row, tile.col);
                                            break;
                                        }
                                        case EAST: {
                                            battleField.setWallRightChip(tile.row, tile.col);
                                            break;
                                        }
                                    }
                                    hits.add(new Hit(new Point(bullet.getRect().centerX(), bullet.getRect().centerY())));
                                }
                            });


                        } else if (obstacle instanceof Grid) {
                            hit = true;

                            frameActions.add(new FrameAction() {
                                @Override
                                public void run() {
                                    if (bullet.getPower() >= 2) {
                                        battleField.clear(tile.row, tile.col);
                                    }

                                    hits.add(new Hit(new Point(bullet.getRect().centerX(), bullet.getRect().centerY())));
                                }
                            });
                        } else if (obstacle instanceof WallTopChip || obstacle instanceof WallBottomChip || obstacle instanceof WallLeftChip || obstacle instanceof WallRightChip) {
                            hit = true;
                            frameActions.add(new FrameAction() {
                                @Override
                                public void run() {
                                    battleField.clear(tile.row, tile.col);
                                    hits.add(new Hit(new Point(bullet.getRect().centerX(), bullet.getRect().centerY())));
                                }
                            });
                        } else if (obstacle instanceof Tank) {
                            final Tank t = (Tank) obstacle;

                            if (!shotTanks.contains(t)) {

                                shotTanks.add(t);

                                if (bullet.getOwner().getAlly() != t.getAlly()) {
                                    hit = true;

                                    if (!t.isGod()) {
                                        t.setHealth(t.getHealth() - 1);

                                        if (t.getHealth() <= 0) {
                                            delayedDestroyTank(t);

                                            if (t.getAlly() == Ally.PLAYER) {
                                                if (!delayedPlayerBorn()) {
                                                    lose = true;
                                                    restart = new RestartAction();
                                                }
                                            } else {
                                                if (t.scoreNumber() != null) {

                                                    score += t.scoreNumber().getValue();
                                                    if (score > highestScore) {
                                                        highestScore = score;
                                                    }
                                                }
                                            }

                                            frameActions.add(new FrameAction() {
                                                @Override
                                                public void run() {
                                                    explosions.add(new Explosion(t.position, t.scoreNumber()));

                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        } else if (obstacle instanceof Home) {
                            hit = true;
                            ((Home) obstacle).setDestroyed(true);
                            if (playerTank != null) {

                                final Tank pTank = playerTank;
                                delayedDestroyTank(playerTank);
                                frameActions.add(new FrameAction() {
                                    @Override
                                    public void run() {
                                        explosions.add(new Explosion(pTank.position, pTank.scoreNumber()));
                                    }
                                });
                            }

                            if (!lose) {

                                lose = true;

                                restart = new RestartAction();
                            }


                        }

                    }
                }

                if (bullet.getOwner().getAlly() == Ally.NPC && bulletTiles[tile.row][tile.col] != null) {
                    if (bullet.getRect().intersect(bulletTiles[tile.row][tile.col].getRect())) {
                        hit = true;
                        delayedDestroyBullet(bulletTiles[tile.row][tile.col]);
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
                        if (obstacle == playerTank) {
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
                                case PROMOTION:
                                    playerTank.promote();
                                    break;
                                case FREEZE:
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

        sceneManager.getScoreListener().onScoreChange(highestScore, score);

    }


    class RestartAction implements FrameAware , Serializable{
        private int delay = 100;


        @Override
        public void nextFrame(Scene scene) {
            delay--;

            if (delay < 0) {
                restart();
            }
        }

        private void restart() {

            Scene scene = new Scene();
            scene.setSceneManager(sceneManager);

            scene.highestScore = highestScore;
            sceneManager.setScene(scene);
        }

    }

    private void moreFood() {

        int x = random.nextInt((Const.TILE_COUNT - 2) * Const.OFFSET_PER_TILE);
        int y = random.nextInt((Const.TILE_COUNT - 2) * Const.OFFSET_PER_TILE);

        int index = random.nextInt(FoodType.values().length);
        FoodType type = FoodType.values()[index];

        food = new Food(new MyPoint(x, y), type);
    }

    private void delayedNpcBorn() {

        if (frame % 50 == 0 && npcTanks.size() < 100) {
            npcPlayer.life--;
            int where = random.nextInt(3);

            int type = random.nextInt(3);

            final boolean carryFood = random.nextInt(2) == 0;
            TankType tankType = null;
            switch (type) {
                case 0: {
                    tankType = TankType.NPC_NORMAL;
                    break;
                }
                case 1: {
                    tankType = TankType.TANK_NPC_RACER;
                    break;
                }
                case 2: {
                    tankType = TankType.TANK_NPC_ARMOR;
                    break;
                }
            }

            Tank tank = null;

            switch (where) {
                case 0: {
                    tank = new Tank(new MyPoint(192, 0), Ally.NPC, tankType, carryFood, npcPlayer);
                    break;
                }
                case 1: {
                    tank = new Tank(new MyPoint(0, 0), Ally.NPC, tankType, carryFood, npcPlayer);
                    break;
                }
                case 2: {
                    tank = new Tank(new MyPoint(384, 0), Ally.NPC, tankType, carryFood, npcPlayer);
                    break;
                }
            }

            final TankBirth tankBirth = new TankBirth(tank);
            frameActions.add(new FrameAction() {
                @Override
                public void run() {
                    tankBirths.add(tankBirth);
                }
            });
        }
    }


    public List<Obstacle> getObstacles(Tile tile) {
        List<Obstacle> result = new ArrayList<Obstacle>();

        if (battleField.get(tile.row, tile.col) != null) {
            result.add(battleField.get(tile.row, tile.col));
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
            final Explosion explosion = new Explosion(tank.position, null);

            frameActions.add(new FrameAction() {
                @Override
                public void run() {
                    npcTanks.remove(tank);
                    explosions.add(explosion);
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
                battleField.protectHome(god);
            }
        });
    }

    public void stopNpc() {
        stopNpcTime = 500;
    }

    public boolean delayedPlayerBorn() {
        if (player.life > 0) {
            player.life--;

            Tank tank =
                    new Tank(new MyPoint(8 * 16, 24 * 16), Ally.PLAYER, TankType.PLAY1, false,
                            player);
            tank.beGod(30);

            final TankBirth tankBirth = new TankBirth(tank);

            frameActions.add(new FrameAction() {
                @Override
                public void run() {
                    tankBirths.add(tankBirth);
                }
            });

            return true;
        }

        return false;

    }

    public void actPlayerMove(Direction direction) {
        if (playerTank == null) {
            return;
        }


        if (direction == null) {
            playerTank.setMove(false);
        } else {
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

            }
        }
    }

    public void actPlayerFire(boolean b) {
        if (playerTank != null) {
            playerTank.setFire(b);
        }
    }

    public void delayedTankFires(final Bullet bullet) {
        frameActions.add(new FrameAction() {
            @Override
            public void run() {
                Scene.this.bullets.add(bullet);
            }
        });

    }

    public void delayedDestroyBomb(final Explosion explosion) {
        frameActions.add(new FrameAction() {
            @Override
            public void run() {
                Scene.this.explosions.remove(explosion);
                if (explosion.getScoreNumber() != null) {
                    Scene.this.scores.add(new Score(new Point(explosion.getRect().centerX(), explosion.getRect().centerY()), explosion.getScoreNumber()));
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
                hits.remove(hit);
            }
        });

    }

    public void delayedDestroyScore(final Score score) {
        frameActions.add(new FrameAction() {
            @Override
            public void run() {
                scores.remove(score);
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
                        playerTank = null;
                        break;
                    case NPC:
                        npcTanks.remove(tank);
                        break;
                }
            }
        });


    }

    public void destroyTankBirth(final TankBirth tankBirth) {
        frameActions.add(new FrameAction() {
            @Override
            public void run() {
                tankBirths.remove(tankBirth);
                Tank tank = tankBirth.getTank();
                switch (tank.getAlly()) {
                    case PLAYER:
                        if (!lose) {
                            playerTank = tank;
                        }
                        break;
                    case NPC:
                        npcTanks.add(tank);
                        break;
                }
            }
        });
    }

    public void setBattleField(BattleField battleField) {
        this.battleField = battleField;
    }

    public Set<Tank> getNpcTanks() {
        return npcTanks;
    }

    public Set<Bullet> getBullets() {
        return bullets;
    }

    public BattleField getBattleField() {
        return battleField;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getNpcPlayer() {
        return npcPlayer;
    }

    public Tank getPlayerTank() {
        return playerTank;
    }

    public Iterable<? extends AbstractEntity> getAnimations() {
        return Iterables.concat(explosions, hits, tankBirths, scores);
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public boolean isLose() {
        return lose;
    }
}
