package entities;

import main.GameThread;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.Constants.ANIMATIONSPEED;
import static utilz.Constants.ObjectConstants.*;

public class GameObject {
    protected int x, y, width, height, objType;
    protected Rectangle2D.Float hitBox;
    protected boolean doAnimation, active = true;
    protected int animationTick, animationIndex;
    protected int xDrawOffset, yDrawOffset;

    protected BufferedImage image;

    public GameObject(int x, int y, int width, int height, int objType) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.objType = objType;
    }

    protected void initImages(String img) {
        image = LoadSave.getAtlas(img);
    }

    protected void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, (int ) (width * GameThread.SCALE), (int) (height * GameThread.SCALE));
    }

    protected void drawHitBox(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.setColor(Color.black);
        g.drawRect((int) hitBox.getX() - xLvlOffset, (int) hitBox.getY() - yLvlOffset, (int) hitBox.getWidth(), (int) hitBox.getHeight());
    }

    private void playAnimation() {
        animationTick++;
        if (animationTick >= ANIMATIONSPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(objType)) {
                animationIndex = 0;
            }
        }
    }

    public void reset() {
        animationIndex = 0;
        animationTick = 0;
        active = true;

        doAnimation = true;
    }
}
