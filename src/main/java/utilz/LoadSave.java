package utilz;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

public class LoadSave {
    public static final String PLAYER_ANIM = "player.png";
    public static final String BACKGROUND_ELEMENTS = "background_elements.png";
    public static final String LEVEL_ONE = "level/level_1.png";
    public static final String LEVEL_TWO = "level/level_2.png";
    public static final String MAIN_MENU_ELEMENTS = "main_menu_elements.png";
    public static final String MAIN_MENU_BACKGROUND = "pause_background.png";
    public static final String MAIN_MENU_BACKGROUND_SECOND = "main_menu_background_2.png";
    public static final String PAUSE_BACKGROUND = "pause_background.png";
    public static final String PAUSE_ELEMENTS = "pause_elements.png";
    public static final String HEALTH = "health.png";
    public static final String SUCCESS_BACKGROUND = "success_background.png";

    public static final String LEVEL_BACKGROUND_COLOR = "background_color.png";
    public static final String MICROSHEME = "microsheme.png";
    public static final String MICROSHEME_TWO = "microsheme2.png";
    public static final String BUILDING = "building_one.png";
    public static final String BUILDING_TWO = "building 2.png";
    public static final String BUILDING_THREE = "building 3.png";
    public static final String MOON = "moon.png";

    public static final String EXP = "exp_animation.png";
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

    public static BufferedImage[] getAllLevels() {
        URL url = LoadSave.class.getResource("/level");
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        Arrays.sort(files);

        BufferedImage[] lvls = new BufferedImage[files.length];

        for (int i = 0; i < lvls.length; i++) {
            try {
                lvls[i] = ImageIO.read(files[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lvls;
    }
}
