package ui;

import gamestates.GameState;
import gamestates.Playing;
import main.GameThread;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.UrmButtons.URM_SIZE;

public class Success {
    private BufferedImage background;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuB, unpauseB;
    private Playing playing;

    public Success(Playing playing) {
        this.playing = playing;
        loadImgs();
        createUrmButtons();
    }

    private void loadImgs() {
        background = LoadSave.getAtlas(LoadSave.SUCCESS_BACKGROUND);

        bgW = (int) (background.getWidth() * GameThread.SCALE);
        bgH = (int) (background.getHeight() * GameThread.SCALE);
        bgX = GameThread.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (100 * GameThread.SCALE);
    }

    private void createUrmButtons() {
        int menuX = (int) (494 * GameThread.SCALE);
        int unpauseX = (int) (690 * GameThread.SCALE);
        int bY = (int) (450 * GameThread.SCALE);

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }

    public void update() {
        menuB.update();
        unpauseB.update();
    }

    public void draw(Graphics g) {
        g.drawImage(background, bgX, bgY, bgW, bgH, null);

        menuB.draw(g);
        unpauseB.draw(g);
    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        if (hover(e, menuB)) {
            menuB.setMousePressed(true);
        } else if (hover(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (hover(e, menuB)) {
            if (menuB.isMousePressed()) {
                GameState.state = GameState.MENU;
            }
        } else if (hover(e, unpauseB)) {
            if (unpauseB.isMousePressed()) {
                playing.unpauseGame();
            }
        }

        menuB.resetBools();
        unpauseB.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if (hover(e, menuB)) {
            menuB.setMouseOver(true);
        } else if (hover(e, unpauseB)) {
            unpauseB.setMouseOver(true);
        }
    }

    private boolean hover(MouseEvent e, UrmButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
