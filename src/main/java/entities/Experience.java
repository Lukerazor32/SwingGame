package entities;

import main.GameThread;
import utilz.LoadSave;

public class Experience extends GameObject {
    public Experience(int x, int y, int objType) {
        super(x, y, objType);
        doAnimation = true;
        initHitBox(32, 32);
        initImages(LoadSave.EXP);
        xDrawOffset = (int) (16 * GameThread.SCALE);
        yDrawOffset = (int) (30 * GameThread.SCALE);

        hitBox.x += xDrawOffset + (int) (GameThread.SCALE * 2);
        hitBox.y += yDrawOffset + (int) (GameThread.SCALE * 2);
    }
}
