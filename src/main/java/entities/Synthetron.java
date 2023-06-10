package entities;
import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

public class Synthetron extends Enemy {

    public Synthetron(float x, float y) {
        super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, SYNTHETRON);
        initHitBox(x, y, 45, 45);
    }

    public void update(int[][] lvlData, Player player) {
        updateMove(lvlData, player);
        updateAnimationTick();
    }

    private void updateMove(int[][] lvlData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);
        }
        if (inAir) {
            inAirUpdate(lvlData);
        } else {
            switch (enemyState) {
                case WAIT:
                    newState(RUN);
                    break;
                case RUN:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                    }
                    if (isPlayerCloseForAttack(player)) {
                        newState(ATTACK);
                    }
                    move(lvlData);
                    break;
            }
        }
    }
}
