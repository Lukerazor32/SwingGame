package entities;

import lombok.Getter;
import main.GameThread;

import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

public abstract class Enemy extends Entity {
    @Getter
    private int animationIndex, enemyState;
    private int enemyType;
    private int animationTick, animationSpeed = 10;
    private boolean firstUpdate = true;
    private boolean inAir;
    private float fallSpeed;
    private float gravity = 0.04f * GameThread.SCALE;
    private float enemySpeed = 0.7f * GameThread.SCALE;
    private int walkDir = LEFT;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitBox(x, y, width, height);
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(enemyType, enemyState)) {
                animationIndex = 0;
            }
        }
    }

    public void update(int[][] lvlData) {
        updateMove(lvlData);
        updateAnimationTick();
    }

    private void updateMove(int[][] lvlData) {
        if (firstUpdate) {
            if (!isEntityOnFloor(hitBox, lvlData)) {
                inAir = true;
            }
            firstUpdate = false;
        }
        if (inAir) {
            if (canMove(hitBox.x, hitBox.y + fallSpeed, hitBox.width, hitBox.height, lvlData)) {
                hitBox.y += fallSpeed;
                fallSpeed += gravity;
            }
            else {
                inAir = false;
                hitBox.y = yPosCheck(hitBox, fallSpeed);
            }
        } else {
            switch (enemyState) {
                case WAIT:
                    enemyState = RUN;
                    break;
                case RUN:
                    float xSpeed = 0;

                    if (walkDir == LEFT) {
                        xSpeed = -enemySpeed;
                    } else {
                        xSpeed = enemySpeed;
                    }

                    if (canMove(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
                        if (isFloor(hitBox, xSpeed, lvlData)) {
                            hitBox.x += xSpeed;
                            return;
                        }
                    }
                    changeWalkDir();
                    break;
            }
        }
    }

    private void changeWalkDir() {
        if (walkDir == LEFT) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }
}
