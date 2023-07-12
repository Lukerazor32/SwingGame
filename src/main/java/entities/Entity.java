package entities;

import lombok.Getter;
import lombok.Setter;
import main.GameThread;
import utilz.Constants;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected float width, height;
    @Getter
    protected boolean inAir = false;
    protected float airSpeed = 0f;
    @Setter
    @Getter
    protected float speed;
    @Getter
    protected Rectangle2D.Float hitBox;
    protected int animationIndex;
    protected int animationTick;
    protected int state;
    protected int maxHealth;
    protected int healthIndicator;

    protected int flipX = 0;
    protected int flipW = 1;

    //ATTACK
    protected Rectangle2D.Float attackBox;
    protected boolean attackChecked;

    public Entity(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, (int ) (width * GameThread.SCALE), (int) (height * GameThread.SCALE));
    }

    protected void drawHitBox(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.setColor(Color.black);
        g.drawRect((int) hitBox.getX() - xLvlOffset, (int) hitBox.getY() - yLvlOffset, (int) hitBox.getWidth(), (int) hitBox.getHeight());
    }

    protected void drawAttackBox(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x - xLvlOffset, (int) attackBox.y - yLvlOffset, (int) attackBox.width, (int) attackBox.height);
    }
}
