package levels;

import main.GameThread;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManager {
    private GameThread gameThread;
    private BufferedImage[] level;
    private Level levelOne;

    public LevelManager(GameThread gameThread) {
        this.gameThread = gameThread;
        importSprites();
        levelOne = new Level(LoadSave.getLevelData());
    }

    private void importSprites() {
        BufferedImage img = LoadSave.getAtlas(LoadSave.BACKGROUND_ELEMENTS);
        level = new BufferedImage[320];
        for (int j = 0; j < 12; j++) {
            for (int i = 0; i < 20; i++) {
                int index = j * 20 + i;
                level[index] = img.getSubimage(i * 64, j * 64, 64, 64);
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        for (int j = 0; j < gameThread.TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < levelOne.getLvlData()[0].length; i++) {
                int index = levelOne.getSpriteIndex(i, j);
                g.drawImage(level[index], gameThread.TILES_SIZE * i - xLvlOffset, gameThread.TILES_SIZE * j, gameThread.TILES_SIZE, gameThread.TILES_SIZE, null);
            }
        }
    }

    public void update() {

    }

    public Level getCurrentLvl() {
        return levelOne;
    }
}
