package entities;

import lombok.Getter;
import main.GameThread;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected float width, height;
    protected boolean inAir = false;
    @Getter
    protected Rectangle2D.Float hitBox;

    public Entity(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void drawHitBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.black);
        g.drawRect((int) hitBox.getX() - xLvlOffset, (int) hitBox.getY(), (int) hitBox.getWidth(), (int) hitBox.getHeight());
    }

    protected void initHitBox(float x, float y, int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, width, height);
    }
}
