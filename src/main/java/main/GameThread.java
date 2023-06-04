package main;

import entities.Player;
import gamestates.GameState;
import gamestates.Menu;
import gamestates.Playing;
import levels.LevelManager;
import lombok.Getter;
import utilz.LoadSave;

import java.awt.*;

public class GameThread implements Runnable {

    @Getter
    private GamePanel gamePanel;
    private GameWindow gameWindow;
    private Thread gameThread;
    private final int FPS = 60;
    private final int UPS = 100;

    @Getter
    private Playing playing;
    @Getter
    private Menu menu;

    public final static int TILES_DEFAULT = 64;
    public final static float SCALE = 1.0f;
    public final static int TILES_IN_WIDTH = 20;
    public final static int TILES_IN_HEIGHT = 12;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public GameThread() {
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        System.out.println(GAME_WIDTH + " " + GAME_HEIGHT);

        gameLoop();
    }

    private void initClasses() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    public void gameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (GameState.state) {
            case PLAYING:
                playing.update();
                break;
            case MENU:
                menu.update();
                break;
        }
    }

    public void render(Graphics g) {
        switch (GameState.state) {
            case PLAYING:
                playing.draw(g);
                break;
            case MENU:
                menu.draw(g);
                break;
        }
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000 / FPS;
        double timePerUpdate = 1000000000 / UPS;

        long previousTime = System.nanoTime();
        long currentTime;
        long lastCheck = System.currentTimeMillis();

        int updates = 0;
        int frames = 0;

        double deltaU = 0;
        double deltaF = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;

            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                frames = 0;
                updates = 0;
            }
        }
    }

    public void disable() {
        if (GameState.state == GameState.PLAYING) {
            playing.disable();
        }
    }
}
