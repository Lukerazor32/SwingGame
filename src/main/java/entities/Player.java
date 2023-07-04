package entities;

import gamestates.Playing;
import lombok.Getter;
import lombok.Setter;
import main.GameThread;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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

    // STATUS BAR
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (200 * GameThread.SCALE);
    private int statusBarHeight = (int) (100* GameThread.SCALE);
    private int statusBarX = (int) (10 * GameThread.SCALE);
    private int statusBarY = (int) (10 * GameThread.SCALE);
    private int healthBarWidth = (int) (128 * GameThread.SCALE);
    private int healthBarHeight = (int) (34 * GameThread.SCALE);
    private int healthBarXStart = (int) (46 * GameThread.SCALE);
    private int healthBarYStart = (int) (43 * GameThread.SCALE);

    private int maxHealth = 100;
    private int healthIndicator = maxHealth;
    private int healthWidth = healthBarWidth;

    //ATTACK
    private Rectangle2D.Float attackBox;

    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        setAnimations();
        initHitBox(x, y, (int)(26 * GameThread.SCALE), (int)(56 * GameThread.SCALE));
        initAttackBox();

        this.playing = playing;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(hitBox.x + hitBox.width + (int) (GameThread.SCALE * 10),
                hitBox.y + (int) (GameThread.SCALE * 10),
                (int) (20 * GameThread.SCALE), (int) (20 * GameThread.SCALE));
    }

    public void update() {
        updateHealthBar();

        if (healthIndicator <= 0) {
            playing.setGameOver(true);
            return;
        }
        updateAttackBox();
        updatePosition();
        if (attack) {
            checkAttack();
        }
        setAnimation();
        playAnimation();
    }

    private void checkAttack() {
        if (attackChecked || animationIndex != 1) {
            return;
        }
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {
        if (right) {
            attackBox.x = hitBox.x + hitBox.width + (int) (GameThread.SCALE * 10);

        } else if (left) {
            attackBox.x = hitBox.x - hitBox.width - (int) (GameThread.SCALE * 10);
        }
        attackBox.y = hitBox.y + (GameThread.SCALE * 10);
    }

    public void render(Graphics g, int xLvlOffset) {
        g.drawImage(animations[playerAction][animationIndex],
                (int) (hitBox.x - xHitBox) - xLvlOffset + flipX,
                (int) (hitBox.y - yHitBox),
                (int) width * flipW, (int) height, null);
        drawHitBox(g, xLvlOffset);
        drawAttackBox(g, xLvlOffset);
        
        drawUI(g);
    }

    private void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x - xLvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.setColor(new Color(88, 15, 214));
        g.fillRect(healthBarXStart, healthBarYStart, 128, healthBarHeight);
        g.setColor(new Color(183, 32, 32));
        g.fillRect(healthBarXStart, healthBarYStart, healthWidth, healthBarHeight);
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((healthIndicator / (float) maxHealth) * healthBarWidth);
    }

    private void setAnimations() {
        BufferedImage img = LoadSave.getAtlas(LoadSave.PLAYER_ANIM);

        animations = new BufferedImage[10][8];
        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = img.getSubimage(j * 64, i * 64, 64, 64);
            }
        }

        statusBarImg = LoadSave.getAtlas(LoadSave.HEALTH);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitBox, lvlData)) {
            inAir = true;
        }
    }

    private void playAnimation() {
        animationTick++;
        if (animationTick == animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteWidth(playerAction)) {
                animationIndex = 0;
                attack = false;
                attackChecked = false;
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
            flipX = (int) width;
            flipW = -1;
        }
        if (right) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (!inAir) {
            if (!IsEntityOnFloor(hitBox, lvlData)) {
                inAir = true;
            }
        }

        if (inAir) {
            if (CanMove(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
                hitBox.y += airSpeed;
                airSpeed += gravity;
                updateXPosition(xSpeed);
            }
            else {
                hitBox.y = YPosCheck(hitBox, airSpeed);
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

    public void changeHealth(int value) {
        healthIndicator += value;
        if (healthIndicator <= 0) {
            healthIndicator = 0;
        } else if (healthIndicator >= maxHealth) {
            healthIndicator = maxHealth;
        }
    }

    private void jump() {
        if (inAir) {
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void updateXPosition(float xSpeed) {
        if (CanMove(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
            hitBox.x += xSpeed;
        } else {
            hitBox.x = XPosCheck(hitBox, xSpeed);
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

    public void resetAll() {
        disableMoving();
        resetInAir();
        attack = false;
        playerMove = false;
        playerAction = WAIT;
        healthIndicator = maxHealth;

        hitBox.x = x;
        hitBox.y = y;
        initAttackBox();

        if (!IsEntityOnFloor(hitBox, lvlData)) {
            inAir = true;
        }
    }
}
