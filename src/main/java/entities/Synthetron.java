package entities;
import main.GameThread;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

public class Synthetron extends Enemy {
    private int attackBoxOffsetX;

    public Synthetron(float x, float y) {
        super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, SYNTHETRON);
        initHitBox(45, 45);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (105 * GameThread.SCALE), (int) (45 * GameThread.SCALE));
        attackBoxOffsetX = (int) (GameThread.SCALE * 30);
    }

    public void update(int[][] lvlData, Player player) {
        updateMove(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitBox.x - attackBoxOffsetX;
        attackBox.y = hitBox.y;
    }

    private void updateMove(int[][] lvlData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);
        }
        if (inAir) {
            inAirUpdate(lvlData);
        } else {
            switch (state) {
                case WAIT:
                    newState(RUN);
                    break;
                case RUN:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player)) {
                            newState(ATTACK);
                            return;
                        }
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (animationIndex == 0) {
                        attackChecked = false;
                    }
                    if (animationIndex == 2 && !attackChecked) {
                        checkEnemyHit(attackBox, player);
                    }
                    break;
            }
        }
    }
}
