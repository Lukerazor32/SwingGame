package levels;

import gamestates.GameState;
import lombok.Getter;
import main.GameThread;
import utilz.HelpMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {
    private GameThread gameThread;
    private BufferedImage[] levelImg;
    @Getter
    private ArrayList<Level> levels;
    private int lvlIndex = 0;

    public LevelManager(GameThread gameThread) {
        this.gameThread = gameThread;
        importSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.getAllLevels();
        for (BufferedImage lvl : allLevels) {
            levels.add(new Level(lvl));
        }
    }

    public void loadNextLevel() {
        lvlIndex++;
        if (lvlIndex >= levels.size()) {
            lvlIndex = 0;
            GameState.state = GameState.MENU;
        }

        Level newLevel = levels.get(lvlIndex);
        gameThread.getPlaying().getEnemyManager().loadEnemies(newLevel);
        gameThread.getPlaying().getPlayer().loadLvlData(newLevel.getLvlData());
        gameThread.getPlaying().setMaxLvlOffsetX(newLevel.getMaxLvlOffsetX());
        gameThread.getPlaying().setMaxLvlOffsetY(newLevel.getMaxLvlOffsetY());
    }

    private void importSprites() {
        BufferedImage img = LoadSave.getAtlas(LoadSave.BACKGROUND_ELEMENTS);
        levelImg = new BufferedImage[320];
        for (int j = 0; j < 12; j++) {
            for (int i = 0; i < 20; i++) {
                int index = j * 20 + i;
                levelImg[index] = img.getSubimage(i * 64, j * 64, 64, 64);
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (int j = 0; j < levels.get(lvlIndex).getLvlData().length; j++) {
            for (int i = 0; i < levels.get(lvlIndex).getLvlData()[0].length; i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                g.drawImage(levelImg[index], gameThread.TILES_SIZE * i - xLvlOffset, gameThread.TILES_SIZE * j - yLvlOffset, gameThread.TILES_SIZE, gameThread.TILES_SIZE, null);
            }
        }
    }

    public void update() {

    }

    public Level getCurrentLevel() {
        return levels.get(lvlIndex);
    }
}
