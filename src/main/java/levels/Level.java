package levels;

import lombok.Getter;

public class Level {
    @Getter
    private int[][] lvlData;

    public Level(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }
}
