package gamestates;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import lombok.Getter;
import lombok.Setter;
import main.GameThread;
import ui.GameOver;
import ui.PauseOverlay;
import ui.Success;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static main.GameThread.SCALE;
import static utilz.Constants.BackgroundElements.*;
import static utilz.LoadSave.*;

public class Playing extends State implements Statemethods {
    @Getter
    private Player player;
    private LevelManager levelManager;
    @Getter
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private Success success;
    private boolean paused;
    @Setter
    private boolean isGameOver;
    private GameOver gameOver;
    @Setter
    private boolean isSuccess;

    private int xLvlOffset;
    private int yLvlOffset;
    private int upBorder = (int) (0.4 * GameThread.GAME_HEIGHT);
    private int bottomBorder = (int) (0.6 * GameThread.GAME_HEIGHT);
    private int leftBorder = (int) (0.4 * GameThread.GAME_WIDTH);
    private int rightBorder = (int) (0.6 * GameThread.GAME_WIDTH);
    @Setter
    private int maxLvlOffsetX;
    @Setter
    private int maxLvlOffsetY;

    //BACKGROUND
    private BufferedImage backgroundColor;
    private BufferedImage building;
    private BufferedImage building_two;
    private BufferedImage building_three;
    private BufferedImage moon;

    private int[] microshemePos;
    private Random random;

    public Playing(GameThread gameThread) {
        super(gameThread);
        initClasses();
        calcLvlOffSet();
        loadStartLevel();
    }

    public void loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
    }

    private void calcLvlOffSet() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getMaxLvlOffsetX();
        maxLvlOffsetY = levelManager.getCurrentLevel().getMaxLvlOffsetY();
    }

    private void initClasses() {
        levelManager = new LevelManager(gameThread);
        enemyManager = new EnemyManager(this);
        player = new Player(200, 100, (int) (64 * SCALE), (int) (64 * SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        pauseOverlay = new PauseOverlay(this);

        backgroundColor = LoadSave.getAtlas(LEVEL_BACKGROUND_COLOR);
        building = LoadSave.getAtlas(BUILDING);
        building_two = LoadSave.getAtlas(BUILDING_TWO);
        building_three = LoadSave.getAtlas(BUILDING_THREE);
        moon = LoadSave.getAtlas(MOON);

        microshemePos = new int[10];
        random = new Random();
        for (int i = 0; i < microshemePos.length; i++) {
            microshemePos[i] = (int) (60 * SCALE) + random.nextInt((int) (90 * SCALE));
        }

        gameOver = new GameOver(this);
        success = new Success(this);
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (isSuccess) {
            success.update();
        } else if (!isGameOver) {
            levelManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLvlData(), player);
            closeToBorder();
        }
    }

    private void closeToBorder() {
        int playerX = (int) player.getHitBox().x;
        int playerY = (int) player.getHitBox().y;
        int diffX = playerX - xLvlOffset;
        int diffY = playerY - yLvlOffset;

        if (diffX > rightBorder) {
            xLvlOffset += diffX - rightBorder;
        }
        else if (diffX < leftBorder){
            xLvlOffset += diffX - leftBorder;
        }

        if (xLvlOffset > maxLvlOffsetX) {
            xLvlOffset = maxLvlOffsetX;
        }
        else if (xLvlOffset < 0) {
            xLvlOffset = 0;
        }


        if (diffY > bottomBorder) {
            yLvlOffset += diffY - bottomBorder;
        }
        else if (diffY < upBorder){
            yLvlOffset += diffY - upBorder;
        }

        if (yLvlOffset > maxLvlOffsetY) {
            yLvlOffset = maxLvlOffsetY;
        }
        else if (yLvlOffset < 0) {
            yLvlOffset = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundColor, 0, 0, GameThread.GAME_WIDTH, GameThread.GAME_HEIGHT, null);
        g.drawImage(moon, GameThread.GAME_WIDTH - (int) (xLvlOffset * 0.9f), ((int) (150 * SCALE) - (int) (yLvlOffset * 0.9)), (int) (500 * SCALE), (int) (350 * SCALE), null);

        for (int i = 0; i < microshemePos.length; i++) {
            g.drawImage(building_two, BUILDING_TWO_WIDTH * i - (int) (xLvlOffset * 0.7), (int) (150 * SCALE) - (int) (yLvlOffset * 0.9), BUILDING_TWO_WIDTH, BUILDING_TWO_HEIGHT, null);
            if (i % 2 == 0) {
                g.drawImage(building, BUILDING_WIDTH * i - (int) (xLvlOffset * 0.9), (int) (40 * SCALE) - (int) (yLvlOffset * 0.9), BUILDING_WIDTH, BUILDING_HEIGHT, null);
            } else {
                g.drawImage(building_three, BUILDING_WIDTH * i - (int) (xLvlOffset * 0.9), (int) (40 * SCALE) - (int) (yLvlOffset * 0.9), BUILDING_WIDTH, BUILDING_HEIGHT, null);
            }
        }

        levelManager.draw(g, xLvlOffset, yLvlOffset);
        player.render(g, xLvlOffset, yLvlOffset);
        enemyManager.draw(g, xLvlOffset, yLvlOffset);

        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, GameThread.GAME_WIDTH, GameThread.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (isGameOver) {
            gameOver.draw(g);
        } else if (isSuccess) {
            success.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!isGameOver) {
            switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    player.setAttack(true);
                    break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!isGameOver) {
            if (paused) {
                pauseOverlay.mousePressed(e);
            } else if (isSuccess) {
                success.mousePressed(e);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!isGameOver) {
            if (paused) {
                pauseOverlay.mouseReleased(e);
            } else if (isSuccess) {
                success.mouseReleased(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isGameOver) {
            if (paused) {
                pauseOverlay.mouseMoved(e);
            } else if (isSuccess) {
                success.mouseMoved(e);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isGameOver) {
            gameOver.KeyPressed(e);
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SHIFT:
                    player.updateRun(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    player.disableMoving();
                    paused = !paused;
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!isGameOver) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SHIFT:
                    player.updateRun(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
        }
    }

    public void unpauseGame() {
        paused = false;
    }



    public void disable() {
        player.disableMoving();
    }

    public void resetAll() {
        isGameOver = false;
        isSuccess = false;
        paused = false;
        player.resetAll();
        enemyManager.resetAll();
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }
}
