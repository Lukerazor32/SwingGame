package utilz;

import main.GameThread;

public class Constants {

    public static class EnemyConstants {
        public static final int SYNTHETRON = 0;
        public static final int OMNICORE = 1;

        public static final int WAIT = 0;
        public static final int RUN = 1;
        public static final int HIT = 2;
        public static final int DEAD = 3;
        public static final int ATTACK = 4;

        public static final int ENEMY_WIDTH_DEFAULT = 64;
        public static final int ENEMY_HEIGHT_DEFAULT = 64;

        public static final int ENEMY_WIDTH = (int) (ENEMY_WIDTH_DEFAULT * GameThread.SCALE);
        public static final int ENEMY_HEIGHT = (int) (ENEMY_HEIGHT_DEFAULT * GameThread.SCALE);

        public static final int ENEMY_DRAWOFFSET_X = (int) (10 * GameThread.SCALE);
        public static final int ENEMY_DRAWOFFSET_Y = (int) (19 * GameThread.SCALE);

        public static int getSpriteAmount(int enemyType, int enemyState) {
            switch (enemyType) {
                case SYNTHETRON:
                    switch (enemyState) {
                        case WAIT:
                            return 8;
                        case RUN:
                            return 8;
                        case HIT:
                            return 7;
                        case DEAD:
                            return 7;
                        case ATTACK:
                            return 8;
                        default:
                            return 1;
                    }
            }
            return 0;
        }
    }

    public static class BackgroundElements {
        public static final int MICROSHEME_WIDTH = (int) (300 * GameThread.SCALE);
        public static final int MICROSHEME_HEIGHT = (int) (300 * GameThread.SCALE);
    }

    public static class UI {
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 300;
            public static final int B_HEIGHT_DEFAULT = 150;

            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * GameThread.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * GameThread.SCALE);
        }

        public static class UrmButtons {
            public static final int URM_DEFAULT_SIZE = 100;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * GameThread.SCALE);
        }
    }

    public static class Directions {
        public static final int UP = 0;
        public static final int DOWN = 1;
        public static final int RIGHT = 2;
        public static final int LEFT = 3;
    }

    public static class PlayerState {
        public static final int WAIT = 0;
        public static final int RUN = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int HIT = 4;
        public static final int ATTACK = 6;
        public static final int ATTACK_JUMP = 7;

        public static int getSpriteWidth(int action) {
            switch (action) {
                case WAIT:
                    return 5;
                case RUN:
                    return 7;
                case JUMP:
                    return 4;
                case FALLING:
                    return 4;
                case HIT:
                    return 7;
                case ATTACK:
                    return 8;
                case ATTACK_JUMP:
                    return 7;
                default:
                    return 1;
            }
        }
    }

}
