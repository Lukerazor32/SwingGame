package entities;

import lombok.Getter;
import main.GameThread;
import utilz.HelpMethods;

import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

public abstract class Enemy extends Entity {
    @Getter
    protected int animationIndex, enemyState;
    protected int enemyType;
    protected int animationTick, animationSpeed = 10;
    protected boolean firstUpdate = true;
    protected boolean inAir;
    protected float fallSpeed;
    protected float gravity = 0.04f * GameThread.SCALE;
    protected float enemySpeed = 0.7f * GameThread.SCALE;
    protected int walkDir = LEFT;
    protected int tileY;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitBox(x, y, width, height);
    }

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(enemyType, enemyState)) {
                animationIndex = 0;
                if (enemyState == ATTACK) {
                    enemyState = WAIT;
                }
            }
        }
    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if (!IsEntityOnFloor(hitBox, lvlData)) {
            inAir = true;
        }
        firstUpdate = false;
    }

    protected void inAirUpdate(int[][] lvlData) {
        if (CanMove(hitBox.x, hitBox.y + fallSpeed, hitBox.width, hitBox.height, lvlData)) {
            hitBox.y += fallSpeed;
            fallSpeed += gravity;
        }
        else {
            inAir = false;
            hitBox.y = YPosCheck(hitBox, fallSpeed);
            tileY = (int) (hitBox.y / GameThread.TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData) {
        float xSpeed = 0;

        if (walkDir == LEFT) {
            xSpeed = -enemySpeed;
            if (CanMove(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
                if (IsFloorLeft(hitBox, xSpeed, lvlData)) {
                    hitBox.x += xSpeed;
                    return;
                }
            }
        } else {
            xSpeed = enemySpeed;
            if (CanMove(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
                if (IsFloorRight(hitBox, xSpeed, lvlData)) {
                    hitBox.x += xSpeed;
                    return;
                }
            }
        }

        changeWalkDir();
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        int playerTileY = (int) (player.hitBox.y / GameThread.TILES_SIZE);

        if (playerTileY == tileY && isPlayerInRange(player) && HelpMethods.IsNotBarrier(hitBox, player.hitBox, tileY, lvlData)) {
            return true;
        }
        return false;
    }

    protected void turnTowardsPlayer(Player player) {
        if (player.hitBox.x > hitBox.x) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);

        return absValue <= GameThread.TILES_SIZE * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);

        return absValue <= GameThread.TILES_SIZE;
    }

    protected void newState(int enemyState) {
        this.enemyState = enemyState;
        animationIndex = 0;
        animationTick = 0;
    }

    protected void changeWalkDir() {
        if (walkDir == LEFT) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }
}
