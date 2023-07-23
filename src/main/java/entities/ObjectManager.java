package entities;

import gamestates.Playing;
import lombok.Getter;
import utilz.Constants;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.ObjectConstants.*;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] expImgs;
    @Getter
    private ArrayList<Experience> experiences;
    @Getter
    private ArrayList<Health> healths;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();
        experiences = new ArrayList<>();
        healths = new ArrayList<>();
    }

    private void loadImgs() {
        BufferedImage expImg = LoadSave.getAtlas(LoadSave.EXP);
        expImgs = new BufferedImage[2][6];

        for (int j = 0; j < expImgs.length; j++) {
            for (int i = 0; i < expImgs[j].length; i++) {
                expImgs[j][i] = expImg.getSubimage(64 * i, 64 * j, 64, 64);
            }
        }
    }

    public void update() {
        for (Experience e : experiences) {
            if (e.isActive()) {
                e.update();
            }
        }

        for (Health h : healths) {
            if (h.isActive()) {
                h.update();
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Experience e : experiences) {
            if (e.isActive()) {
                g.drawImage(expImgs[e.getObjType()][e.getAnimationIndex()], (int) (e.getHitBox().x - e.getXDrawOffset() - xLvlOffset),
                        (int) (e.getHitBox().y - e.getYDrawOffset() - yLvlOffset),
                        OBJECT_WIDTH, OBJECT_HEIGHT, null);
            }
        }

        for (Health h : healths) {
            if (h.isActive()) {
                g.drawImage(expImgs[h.getObjType()][h.getAnimationIndex()], (int) (h.getHitBox().x - h.getXDrawOffset() - xLvlOffset),
                        (int) (h.getHitBox().y - h.getYDrawOffset() - yLvlOffset),
                        OBJECT_WIDTH, OBJECT_HEIGHT, null);
            }
        }
    }

    public void isObjectTouched(Rectangle2D.Float hitBox) {
        for (Experience e : experiences) {
            if (e.isActive() && hitBox.intersects(e.getHitBox())) {
                e.setActive(false);
                applyExperience(e);
            }
        }
        for (Health h : healths) {
            if (h.isActive() && hitBox.intersects(h.getHitBox())) {
                h.setActive(false);
                applyExperience(h);
            }
        }
    }

    private void applyExperience(GameObject o) {
        if (o.objType == HEALTH_INDEX) {
            playing.getPlayer().changeHealth(HEALTH_VALUE);
        }
    }

    public void resetAll() {
        experiences.clear();
        healths.clear();
    }
}
