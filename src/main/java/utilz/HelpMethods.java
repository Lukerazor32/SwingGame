package utilz;

import main.GameThread;

import java.awt.geom.Rectangle2D;

public class HelpMethods {

    public static boolean canMove(float x, float y, float width, float height, int[][] lvlData) {
        if (!isObject(x, y, lvlData)) {
            if (!isObject(x + width, y + height, lvlData)) {
                if (!isObject(x + width, y + height / GameThread.SCALE, lvlData)) {
                    if (!isObject(x + width, y, lvlData)) {
                        if (!isObject(x, y + height, lvlData)) {
                            if (!isObject(x, y + height / GameThread.SCALE, lvlData)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean isObject(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * GameThread.TILES_SIZE;
        if ((x < 0 || x >= maxWidth) || (y < 0 || y >= GameThread.GAME_HEIGHT)) {
            return true;
        }

        float xIndex = x / GameThread.TILES_SIZE;
        float yIndex = y / GameThread.TILES_SIZE;

        int value = lvlData[(int) yIndex][(int) xIndex];

        if (value < 0 || (value >= 20 && value < 24) || value == 60) {
            return true;
        }

        return false;
    }

    public static float xPosCheck(Rectangle2D.Float hitBox, float xSpeed) {
        int currentTile = (int)(hitBox.x / GameThread.TILES_SIZE);
        int tileXPos = currentTile * GameThread.TILES_SIZE;
        if (xSpeed > 0) {
            int xOffSet = (int)(GameThread.TILES_SIZE - hitBox.width);
            return tileXPos + xOffSet - 1;
        }
        else {
            return tileXPos;
        }
    }


    public static float yPosCheck(Rectangle2D.Float hitBox, float airSpeed) {
        int currentTile = (int)(hitBox.y / GameThread.TILES_SIZE);
        int tileYPos = currentTile * GameThread.TILES_SIZE;
        if (airSpeed > 0) {
            int yOffSet = (int)(GameThread.TILES_SIZE - hitBox.height);
            return tileYPos + yOffSet - 1;
        }
        else {
            return tileYPos;
        }
    }

    public static boolean isEntityOnFloor(Rectangle2D.Float hitBox, int[][] lvlData) {
        if (!isObject(hitBox.x, hitBox.y + hitBox.height + 1, lvlData)) {
            if (!isObject(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, lvlData)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isFloor(Rectangle2D.Float hitBox, float xSpeed, int[][] lvlData) {
        return isObject(hitBox.x + xSpeed, hitBox.y + hitBox.height + 1, lvlData);
    }
}
