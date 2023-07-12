package levels;

import entities.Synthetron;
import lombok.Getter;
import main.GameThread;
import utilz.HelpMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level {
    private BufferedImage img;
    @Getter
    private ArrayList<Synthetron> synths;
    @Getter
    private int[][] lvlData;
    private int lvlTilesWide;
    private int maxTilesOffset;
    @Getter
    private int maxLvlOffsetX;

    private int lvlTilesHeight;
    @Getter
    private int maxLvlOffsetY;
    @Getter
    private Point playerSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        createOffSets();
        createPlayerSpawn();
    }

    private void createPlayerSpawn() {
        playerSpawn = HelpMethods.getPlayerSpawn(img);
    }

    private void createOffSets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - GameThread.TILES_IN_WIDTH;
        maxLvlOffsetX = GameThread.TILES_SIZE * maxTilesOffset;

        lvlTilesHeight = img.getHeight();
        maxTilesOffset = lvlTilesHeight - GameThread.TILES_IN_HEIGHT;
        maxLvlOffsetY = GameThread.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        synths = HelpMethods.getSynths(img);
    }

    private void createLevelData() {
        lvlData = HelpMethods.getLevelData(img);
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }
}
