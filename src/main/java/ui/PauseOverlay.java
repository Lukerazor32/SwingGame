package ui;

import gamestates.GameState;
import gamestates.Playing;
import main.GameThread;
import utilz.Constants;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.UrmButtons.URM_SIZE;

public class PauseOverlay {
    private BufferedImage background;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuB, replayB, unpauseB;
    private Playing playing;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadImgs();
        createUrmButtons();
    }

    private void createUrmButtons() {
        int menuX = (int) (444 * GameThread.SCALE);
        int replayX = (int) (592 * GameThread.SCALE);
        int unpauseX = (int) (740 * GameThread.SCALE);
        int bY = (int) (580 * GameThread.SCALE);

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }

    private void loadImgs() {
        background = LoadSave.getAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int) (background.getWidth() * GameThread.SCALE);
        bgH = (int) (background.getHeight() * GameThread.SCALE);
        bgX = GameThread.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (15 * GameThread.SCALE);
    }

    public void update() {
        menuB.update();
        replayB.update();
        unpauseB.update();
    }

    public void draw(Graphics g) {
        g.drawImage(background, bgX, bgY, bgW, bgH, null);

        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        if (hover(e, menuB)) {
            menuB.setMousePressed(true);
        } else if (hover(e, replayB)) {
            replayB.setMousePressed(true);
        } else if (hover(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (hover(e, menuB)) {
            if (menuB.isMousePressed()) {
                GameState.state = GameState.MENU;
            }
        } else if (hover(e, replayB)) {
            if (replayB.isMousePressed()) {
                System.out.println("replay");
            }
        } else if (hover(e, unpauseB)) {
            if (unpauseB.isMousePressed()) {
                playing.unpauseGame();
            }
        }

        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if (hover(e, menuB)) {
            menuB.setMouseOver(true);
        } else if (hover(e, replayB)) {
            replayB.setMouseOver(true);
        } else if (hover(e, unpauseB)) {
            unpauseB.setMouseOver(true);
        }
    }

    public boolean hover(MouseEvent e, UrmButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
