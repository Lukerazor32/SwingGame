package entities;
import static utilz.Constants.EnemyConstants.*;

public class Synthetron extends Enemy {

    public Synthetron(float x, float y) {
        super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, SYNTHETRON);
        initHitBox(x, y, 45, 45);
    }

}
