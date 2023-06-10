package utilz;

import entities.Synthetron;
import static utilz.Constants.EnemyConstants.*;
import main.GameThread;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LoadSave {
    public static final String PLAYER_ANIM = "player.png";
    public static final String BACKGROUND_ELEMENTS = "background_elements.png";
    public static final String LEVEL_ONE = "level_one_markup.png";
    public static final String MAIN_MENU_ELEMENTS = "main_menu_elements.png";
    public static final String MAIN_MENU_BACKGROUND = "main_menu_background.png";
    public static final String MAIN_MENU_BACKGROUND_SECOND = "main_menu_background_2.png";
    public static final String PAUSE_BACKGROUND = "main_menu_background.png";
    public static final String PAUSE_ELEMENTS = "pause_elements.png";
    public static final String HEALTH = "health.png";

    public static final String LEVEL_BACKGROUND_COLOR = "background_color.png";
    public static final String MICROSHEME = "microsheme.png";
    public static final String MICROSHEME_TWO = "microsheme2.png";

    public static final String SIMPLE_ENEMY_ANIM = "simple_enemy.png";

    public static BufferedImage getAtlas(String path) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + path);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static ArrayList<Synthetron> getSynths() {
        BufferedImage img = getAtlas(LEVEL_ONE);
        ArrayList<Synthetron> synths = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == SYNTHETRON) {
                    synths.add(new Synthetron(i * GameThread.TILES_SIZE, j * GameThread.TILES_SIZE));
                }
            }
        }
        return synths;
    }

    public static int[][] getLevelData() {
        BufferedImage img = getAtlas(LEVEL_ONE);
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }
}
