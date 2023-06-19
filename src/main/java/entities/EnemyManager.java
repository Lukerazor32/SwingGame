package entities;

import static utilz.Constants.EnemyConstants.*;
import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] synthArr;

    private ArrayList<Synthetron> synths = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        synths = LoadSave.getSynths();
        System.out.println("size of synths: " + synths.size());
    }

    public void update(int[][] lvlData, Player player) {
        for (Synthetron synth : synths) {
            synth.update(lvlData, player);
        }
    }

    public void draw(Graphics g, int xLevelOffset) {
        drawSynths(g, xLevelOffset);
    }

    private void drawSynths(Graphics g, int xLevelOffset) {
        for (Synthetron synth : synths) {
            g.drawImage(synthArr[synth.getEnemyState()][synth.getAnimationIndex()],
                    (int) synth.getHitBox().x - ENEMY_DRAWOFFSET_X - xLevelOffset + synth.flipX, (int) synth.getHitBox().y - ENEMY_DRAWOFFSET_Y,
                    ENEMY_WIDTH * synth.flipW, ENEMY_HEIGHT, null);
            synth.drawHitBox(g, xLevelOffset);
            synth.drawAttackBox(g, xLevelOffset);
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
}

