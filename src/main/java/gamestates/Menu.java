package gamestates;

import main.GameThread;
import ui.MenuButton;
import utilz.Constants;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements Statemethods {
    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage background;
    private BufferedImage background_second;
    private int xPos, yPos, menuWidth, menuHeight;

    public Menu(GameThread gameThread) {
        super(gameThread);
        loadButtons();
        loadBackground();
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(GameThread.GAME_WIDTH / 2, (int) (145 * GameThread.SCALE), 0, GameState.PLAYING);
        buttons[1] = new MenuButton(GameThread.GAME_WIDTH / 2, (int) (305 * GameThread.SCALE), 1, GameState.OPTIONS);
        buttons[2] = new MenuButton(GameThread.GAME_WIDTH / 2, (int) (465 * GameThread.SCALE), 2, GameState.QUIT);
    }

    private void loadBackground() {
        background = LoadSave.getAtlas(LoadSave.MAIN_MENU_BACKGROUND);
        background_second = LoadSave.getAtlas(LoadSave.MAIN_MENU_BACKGROUND_SECOND);
        menuWidth = (int) (background.getWidth() * GameThread.SCALE);
        menuHeight= (int) (background.getHeight() * GameThread.SCALE);

        xPos = GameThread.GAME_WIDTH / 2 - menuWidth / 2;
        yPos = (int) (12 * GameThread.SCALE);
    }

    @Override
    public void update() {
        for (MenuButton menuButton : buttons) {
            menuButton.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(background_second, 0, 0, GameThread.GAME_WIDTH, GameThread.GAME_HEIGHT, null);
        g.setColor(new Color(0, 0, 0, 50));
        g.fillRect(0, 0, GameThread.GAME_WIDTH, GameThread.GAME_HEIGHT);
        g.drawImage(background, xPos, yPos, menuWidth, menuHeight, null);

        for (MenuButton menuButton : buttons) {
            menuButton.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton menuButton : buttons) {
            if (hover(e, menuButton)) {
                menuButton.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton menuButton : buttons) {
            if (hover(e, menuButton)) {
                if (menuButton.isMousePressed()) {
                    menuButton.applyGamesState();
                    break;
                }
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for (MenuButton menuButton : buttons) {
            menuButton.resetBools();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton menuButton : buttons) {
            menuButton.setMouseOver(false);
        }
        for (MenuButton menuButton : buttons) {
            if (hover(e, menuButton)) {
                menuButton.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                GameState.state = GameState.PLAYING;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
