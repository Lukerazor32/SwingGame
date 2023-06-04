package entities;

import lombok.Getter;
import lombok.Setter;
import main.GameThread;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.HelpMethods.*;
import static utilz.Constants.PlayerState.*;

public class Player extends Entity {
    private BufferedImage[][] animations;

    private int animationTick, animationIndex, animationSpeed = 10;
    private int playerSpeed = (int)(3f * GameThread.SCALE);
    private int playerAction = WAIT;
    private boolean playerMove = false;
    @Getter
    @Setter
    private boolean attack = false;
    @Getter
    @Setter
    private boolean up, down, right, left, jump;
    private int[][] lvlData;

    private float xHitBox = 16 * GameThread.SCALE;
    private float yHitBox = 8 * GameThread.SCALE;

    // GRAVITY
    private float airSpeed = 0f;
    private float gravity = 0.08f * GameThread.SCALE;
    private float jumpSpeed = -4.5f * GameThread.SCALE;
    private float fallSpeedAfterCollision = 1f * GameThread.SCALE;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        setAnimations();
        initHitBox(x, y, (int)(26 * GameThread.SCALE), (int)(56 * GameThread.SCALE));
    }

    public void update() {
        updatePosition();
        setAnimation();
        startAnimation();
    }

    public void render(Graphics g, int xLvlOffset) {
        g.drawImage(animations[playerAction][animationIndex], (int) (hitBox.x - xHitBox) - xLvlOffset, (int) (hitBox.y - yHitBox), (int) width, (int) height, null);
        drawHitBox(g, xLvlOffset);
    }

    private void setAnimations() {
        BufferedImage img = LoadSave.getAtlas(LoadSave.PLAYER_ANIM);

        animations = new BufferedImage[10][8];
        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = img.getSubimage(j * 64, i * 64, 64, 64);
            }
        }
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!isEntityOnFloor(hitBox, lvlData)) {
            inAir = true;
        }
    }

    private void startAnimation() {
        animationTick++;
        if (animationTick == animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteWidth(playerAction)) {
                animationIndex = 0;
                attack = false;
            }
        }
    }

    private void setAnimation() {
        int startAnimation = playerAction;

        if (playerMove) {
            playerAction = RUN;
        }
        else {
            playerAction = WAIT;
        }

        if (airSpeed < 0 && inAir) {
            playerAction = JUMP;
        }
        else if (airSpeed > 0){
            playerAction = FALLING;
        }

        if (attack == true) {
            playerAction = ATTACK;
        }

        if (startAnimation != playerAction) {
            animationTick = 0;
            animationIndex = 0;
        }
    }



    private void updatePosition() {
        playerMove = false;

        if (jump) {
            jump();
        }

        if ((!left && !right && !inAir) || (left && right && !inAir)) {
            return;
        }

        float xSpeed = 0;

        if (left) {
            xSpeed -= playerSpeed;
        }
        if (right) {
            xSpeed += playerSpeed;
        }

        if (!inAir) {
            if (!isEntityOnFloor(hitBox, lvlData)) {
                inAir = true;
            }
        }

        if (inAir) {
            if (canMove(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
                hitBox.y += airSpeed;
                airSpeed += gravity;
                updateXPosition(xSpeed);
            }
            else {
                hitBox.y = yPosCheck(hitBox, airSpeed);
                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPosition(xSpeed);
            }
        } else {
            updateXPosition(xSpeed);
        }
        playerMove = true;
    }

    private void jump() {
        if (inAir) {
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void updateXPosition(float xSpeed) {
        if (canMove(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
            hitBox.x += xSpeed;
        } else {
            hitBox.x = xPosCheck(hitBox, xSpeed);
        }
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    public void disableMoving() {
        up = false;
        down = false;
        right = false;
        left = false;
        jump = false;
    }
}
