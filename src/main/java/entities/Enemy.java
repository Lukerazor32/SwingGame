package entities;

import lombok.Getter;
import main.GameThread;
import utilz.Constants;
import utilz.HelpMethods;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import static utilz.Constants.ANIMATIONSPEED;
import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.GRAVITY;
import static utilz.Constants.PlayerState.WAIT;
import static utilz.HelpMethods.*;

public abstract class Enemy extends Entity {
    @Getter
    protected int enemyType;
    protected boolean firstUpdate = true;
    protected int walkDir = LEFT;
    protected int tileY;

    @Getter
    protected boolean active = true;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        speed = 0.7f * GameThread.SCALE;
        maxHealth = Constants.EnemyConstants.getMaxHealth(enemyType);
        healthIndicator = maxHealth;
        this.state = WAIT;
    }

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATIONSPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(enemyType, state)) {
                animationIndex = 0;
                switch (state) {
                    case ATTACK:
                    case HIT:
                        state = WAIT;
                        break;
                    case DEAD:
                        active = false;
                        break;
                    default: break;
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
        if (CanMove(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
            hitBox.y += airSpeed;
            airSpeed += GRAVITY;
        }
        else {
            inAir = false;
            hitBox.y = YPosCheck(hitBox, airSpeed);
            tileY = (int) (hitBox.y / GameThread.TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData) {
        float xSpeed = 0;

        if (walkDir == LEFT) {
            flipX = (int) width;
            flipW = -1;
            xSpeed = -speed;
            if (CanMove(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
                if (IsFloorLeft(hitBox, xSpeed, lvlData)) {
                    hitBox.x += xSpeed;
                    return;
                }
            }
        } else {
            flipX = 0;
            flipW = 1;
            xSpeed = speed;
            if (CanMove(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
                if (IsFloorRight(hitBox, xSpeed, lvlData)) {
                    hitBox.x += xSpeed;
                    return;
                }
            }
        }

        changeWalkDir();
    }

    protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitBox)) {
            player.changeHealth(-getDamage(enemyType));
        }
        attackChecked = true;
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

        return absValue <= GameThread.TILES_SIZE * 5f;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue;
        if (walkDir == LEFT) {
            absValue = (int) Math.abs((player.hitBox.x - 38) - hitBox.x);
        } else {
            absValue = (int) Math.abs((player.hitBox.x) - hitBox.x);
        }

        return absValue <= GameThread.TILES_SIZE;
    }

    protected void newState(int enemyState) {
        this.state = enemyState;
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

    protected void hit(int amount) {
        healthIndicator -= amount;
        if (healthIndicator <= 0) {
            newState(DEAD);
        } else {
            newState(HIT);
        }
    }

    public void resetEnemy() {
        healthIndicator = maxHealth;
        hitBox.x = x;
        hitBox.y = y;
        firstUpdate = true;
        newState(WAIT);
        active = true;
        airSpeed = 0;
    }
}
