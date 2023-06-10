package gamestates;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import lombok.Getter;
import main.GameThread;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import static main.GameThread.SCALE;
import static utilz.Constants.BackgroundElements.MICROSHEME_HEIGHT;
import static utilz.Constants.BackgroundElements.MICROSHEME_WIDTH;

public class Playing extends State implements Statemethods {
    @Getter
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private boolean paused;

    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * GameThread.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * GameThread.GAME_WIDTH);
    private int lvlTilesWide = LoadSave.getLevelData()[0].length;
    private int maxTilesOffset = lvlTilesWide - GameThread.TILES_IN_WIDTH;
    private int maxLvlOffsetX = maxTilesOffset * GameThread.TILES_SIZE;

    private BufferedImage backgroundColor;
    private BufferedImage microsheme;
    private BufferedImage microsheme_two;

    private int[] microshemePos;
    private Random random;

    public Playing(GameThread gameThread) {
        super(gameThread);
        initClasses();
    }

    private void initClasses() {
        levelManager = new LevelManager(gameThread);
        enemyManager = new EnemyManager(this);
        player = new Player(200, 100, 64 * (int) SCALE, 64 * (int) SCALE);
        player.loadLvlData(levelManager.getCurrentLvl().getLvlData());
        pauseOverlay = new PauseOverlay(this);

        backgroundColor = LoadSave.getAtlas(LoadSave.LEVEL_BACKGROUND_COLOR);
        microsheme = LoadSave.getAtlas(LoadSave.MICROSHEME);
        microsheme_two = LoadSave.getAtlas(LoadSave.MICROSHEME_TWO);

        microshemePos = new int[10];
        random = new Random();
        for (int i = 0; i < microshemePos.length; i++) {
            microshemePos[i] = (int) (60 * SCALE) + random.nextInt((int) (90 * SCALE));
        }
    }

    @Override
    public void update() {
        if (!paused) {
            levelManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLvl().getLvlData(), player);
            closeToBorder();
        } else {
            pauseOverlay.update();
        }

    }

    private void closeToBorder() {
        int playerX = (int) player.getHitBox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder) {
            xLvlOffset += diff - rightBorder;
        }
        else if (diff < leftBorder){
            xLvlOffset += diff - leftBorder;
        }

        if (xLvlOffset > maxLvlOffsetX) {
            xLvlOffset = maxLvlOffsetX;
        }
        else if (xLvlOffset < 0) {
            xLvlOffset = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundColor, 0, 0, GameThread.GAME_WIDTH, GameThread.GAME_HEIGHT, null);

        for (int i = 0; i < microshemePos.length; i++) {
            if (i % 2 == 0) {
                g.drawImage(microsheme_two, MICROSHEME_WIDTH * i - (int) (xLvlOffset * 0.7), microshemePos[i], MICROSHEME_WIDTH, MICROSHEME_HEIGHT, null);
            } else {
                g.drawImage(microsheme, MICROSHEME_WIDTH * i - (int) (xLvlOffset * 0.7), microshemePos[i], MICROSHEME_WIDTH, MICROSHEME_HEIGHT, null);
            }
        }

        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);

        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, GameThread.GAME_WIDTH, GameThread.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                player.setAttack(true);
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused) {
            pauseOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
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

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
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

    public void unpauseGame() {
        paused = false;
    }

    public void disable() {
        player.disableMoving();
    }
}
