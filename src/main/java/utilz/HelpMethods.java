package utilz;

import main.GameThread;

import java.awt.geom.Rectangle2D;

public class HelpMethods {

    public static boolean CanMove(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsObject(x, y, lvlData)) {
            if (!IsObject(x + width, y + height, lvlData)) {
                if (!IsObject(x + width, y + height / GameThread.SCALE, lvlData)) {
                    if (!IsObject(x + width, y, lvlData)) {
                        if (!IsObject(x, y + height, lvlData)) {
                            if (!IsObject(x, y + height / GameThread.SCALE, lvlData)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean IsObject(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * GameThread.TILES_SIZE;
        if ((x < 0 || x >= maxWidth) || (y < 0 || y >= GameThread.GAME_HEIGHT)) {
            return true;
        }

        float xIndex = x / GameThread.TILES_SIZE;
        float yIndex = y / GameThread.TILES_SIZE;
        return isTileObject((int) xIndex, (int) yIndex, lvlData);
    }

    private static boolean isTileObject(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[yTile][xTile];
        if (value < 0 || (value >= 20 && value < 24) || value == 60) {
            return true;
        }

        return false;
    }

    public static float XPosCheck(Rectangle2D.Float hitBox, float xSpeed) {
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


    public static float YPosCheck(Rectangle2D.Float hitBox, float airSpeed) {
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

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitBox, int[][] lvlData) {
        if (!IsObject(hitBox.x, hitBox.y + hitBox.height + 1, lvlData)) {
            if (!IsObject(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, lvlData)) {
                return false;
            }
        }
        return true;
    }

    public static boolean IsFloorLeft(Rectangle2D.Float hitBox, float xSpeed, int[][] lvlData) {
        return IsObject(hitBox.x + xSpeed, hitBox.y + hitBox.height + 1, lvlData);
    }

    public static boolean IsFloorRight(Rectangle2D.Float hitBox, float xSpeed, int[][] lvlData) {
        return IsObject(hitBox.x + xSpeed + hitBox.width, hitBox.y + hitBox.height + 1, lvlData);
    }

    private static boolean isAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++) {
            if (isTileObject(xStart + i, y, lvlData)) {
                return false;
            }
            if (!isTileObject(xStart + i, y+1, lvlData)) {
                return false;
            }
        }
        return true;
    }

    public static boolean IsNotBarrier(Rectangle2D.Float enemyHitBox, Rectangle2D.Float playerHitBox, int yTile, int[][] lvlData) {
        int enemyXTile = (int) (enemyHitBox.x / GameThread.TILES_SIZE);
        int playerXTile = (int) (playerHitBox.x / GameThread.TILES_SIZE);

        if (enemyXTile > playerXTile) {
            return isAllTilesWalkable(playerXTile, enemyXTile, yTile, lvlData);
        } else {
            return isAllTilesWalkable(enemyXTile, playerXTile, yTile, lvlData);
        }
    }
}
