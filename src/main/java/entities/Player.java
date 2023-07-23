package entities;

import gamestates.Playing;
import lombok.Getter;
import lombok.Setter;
import main.GameThread;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.Constants.ANIMATIONSPEED;
import static utilz.Constants.GRAVITY;
import static utilz.HelpMethods.*;
import static utilz.Constants.PlayerState.*;

public class Player extends Entity {
    private BufferedImage[][] animations;

    private boolean isWalk = false;
    @Getter
    private boolean isRun = false;
    @Getter
    @Setter
    private boolean attack = false;
    @Getter
    @Setter
    private boolean right, left, jump;
    private int[][] lvlData;

    private float xHitBox = 16 * GameThread.SCALE;
    private float yHitBox = 8 * GameThread.SCALE;

    // GRAVITY
    private float jumpSpeed = -4.5f * GameThread.SCALE;
    private float fallSpeedAfterCollision = 1f * GameThread.SCALE;

    // STATUS BAR
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (100 * GameThread.SCALE);
    private int statusBarHeight = (int) (50 * GameThread.SCALE);
    private int statusBarX = (int) (10 * GameThread.SCALE);
    private int statusBarY = (int) (10 * GameThread.SCALE);
    private int healthBarWidth = (int) (64 * GameThread.SCALE);
    private int healthBarHeight = (int) (17 * GameThread.SCALE);
    private int healthBarXStart = (int) (28 * GameThread.SCALE);
    private int healthBarYStart = (int) (26 * GameThread.SCALE);

    private int healthWidth = healthBarWidth;

    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        setAnimations();
        initHitBox(26, 56);
        initAttackBox();
        speed = (int) (3f * GameThread.SCALE);
        maxHealth = 100;
        healthIndicator = maxHealth;

        this.state = WAIT;
        this.playing = playing;
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitBox.x = x;
        hitBox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(hitBox.x + hitBox.width + (int) (GameThread.SCALE * 15),
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

        if (isWalk) {
            checkObjectTouched();
        }

        if (attack) {
            checkAttack();
        }
        setAnimation();
        playAnimation();
    }

    private void checkObjectTouched() {
        playing.getObjectManager().isObjectTouched(hitBox);
    }

    private void checkAttack() {
        if (attackChecked || animationIndex != 1) {
            return;
        }
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {
        if (right && !left) {
            attackBox.x = hitBox.x + hitBox.width + (int) (GameThread.SCALE * 5);
        } else if (left && !right) {
            attackBox.x = hitBox.x - hitBox.width - (int) (GameThread.SCALE * 5);
        }
        attackBox.y = hitBox.y + (GameThread.SCALE * 10);
    }

    public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.drawImage(animations[state][animationIndex],
                (int) (hitBox.x - xHitBox) - xLvlOffset + flipX,
                (int) (hitBox.y - yHitBox) - yLvlOffset,
                (int) (width * flipW), (int) (height), null);
//        drawHitBox(g, xLvlOffset, yLvlOffset);
//        drawAttackBox(g, xLvlOffset, yLvlOffset);
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        g.setColor(new Color(88, 15, 214));
        g.fillRect(healthBarXStart, healthBarYStart, healthBarWidth, healthBarHeight);
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
        if (animationTick == ANIMATIONSPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteWidth(state)) {
                animationIndex = 0;
                attack = false;
                attackChecked = false;
            }
        }
    }

    private void setAnimation() {
        int startAnimation = state;

        if (isWalk && isRun) {
            state = RUN;
        }
        else if (isWalk && !isRun) {
            state = WALK;
        }
        else {
            state = WAIT;
        }

        if (airSpeed < 0 && inAir) {
            state = JUMP;
        }
        else if (airSpeed > 0){
            state = FALLING;
        }

        if (attack == true) {
            state = ATTACK;
        }

        if (startAnimation != state) {
            animationTick = 0;
            animationIndex = 0;
        }
    }



    private void updatePosition() {
        isWalk = false;

        if (jump) {
            jump();
        }

        if ((!left && !right && !inAir) || (left && right && !inAir)) {
            return;
        }

        float xSpeed = 0;

        if (left) {
            xSpeed -= speed;
            flipX = (int) width;
            flipW = -1;
        }
        if (right) {
            xSpeed += speed;
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
                airSpeed += GRAVITY;
            }
            else {
                hitBox.y = YPosCheck(hitBox, airSpeed);
                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
            }
        }

        updateXPosition(xSpeed);
        isWalk = true;
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
        right = false;
        left = false;
        jump = false;
    }

    public void resetAll() {
        disableMoving();
        resetInAir();
        attack = false;
        isWalk = false;
        isRun = false;
        state = WAIT;
        healthIndicator = maxHealth;

        hitBox.x = x;
        hitBox.y = y;
        initAttackBox();

        if (!IsEntityOnFloor(hitBox, lvlData)) {
            inAir = true;
        }
    }

    public void updateRun(boolean isRun) {
        this.isRun = isRun;
        if (isRun) {
            speed = 4f * GameThread.SCALE;
        } else {
            speed = 3f * GameThread.SCALE;
        }
    }
}
