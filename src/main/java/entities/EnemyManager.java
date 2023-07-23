package entities;

import static utilz.Constants.EnemyConstants.*;
import gamestates.Playing;
import levels.Level;
import lombok.Getter;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] synthArr;

    @Getter
    public ArrayList<Synthetron> synths = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        synths = level.getSynths();
        System.out.println("size of synths: " + synths.size());
    }

    public void update(int[][] lvlData, Player player) {
        boolean isAnyActive = false;
        for (Synthetron synth : synths) {
            if (synth.active) {
                synth.update(lvlData, player);
                isAnyActive = true;
            }
        }
        if (!isAnyActive) {
            playing.setSuccess(true);
        }
    }

    public void draw(Graphics g, int xLevelOffset, int yLvlOffset) {
        drawSynths(g, xLevelOffset, yLvlOffset);
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox, ObjectManager objectManager) {
        for(Synthetron synth : synths) {
            if (synth.active) {
                if (attackBox.intersects(synth.getHitBox())) {
                    synth.hit(10, objectManager);
                    return;
                }
            }
        }
    }

    private void drawSynths(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Synthetron synth : synths) {
            if (synth.active) {
                g.drawImage(synthArr[synth.state][synth.animationIndex],
                        (int) synth.getHitBox().x - ENEMY_DRAWOFFSET_X - xLvlOffset + synth.flipX, (int) synth.getHitBox().y - ENEMY_DRAWOFFSET_Y - yLvlOffset,
                        ENEMY_WIDTH * synth.flipW, ENEMY_HEIGHT, null);
//                synth.drawHitBox(g, xLvlOffset, yLvlOffset);
//                synth.drawAttackBox(g, xLvlOffset, yLvlOffset);
            }
        }
    }

    private void loadEnemyImgs() {
        synthArr = new BufferedImage[5][8];
        BufferedImage temp = LoadSave.getAtlas(LoadSave.SIMPLE_ENEMY_ANIM);
        for (int j = 0; j < synthArr.length; j++) {
            for (int i = 0; i < synthArr[j].length; i++) {
                synthArr[j][i] = temp.getSubimage(i * ENEMY_WIDTH_DEFAULT, j * ENEMY_HEIGHT_DEFAULT, ENEMY_WIDTH_DEFAULT, ENEMY_HEIGHT_DEFAULT);
            }
        }
    }

    public void resetAll() {
        for (Synthetron synth : synths) {
            synth.resetEnemy();
        }
    }
}

