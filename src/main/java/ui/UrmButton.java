package ui;

import lombok.Getter;
import lombok.Setter;
import utilz.Constants;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.Buttons.B_HEIGHT;
import static utilz.Constants.UI.Buttons.B_WIDTH;
import static utilz.Constants.UI.UrmButtons.URM_DEFAULT_SIZE;
import static utilz.Constants.UI.UrmButtons.URM_SIZE;

public class UrmButton extends PauseButton {
    private BufferedImage[] imgs;
    private int rowIndex, index;
    @Getter @Setter
    private boolean mouseOver, mousePressed;

    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.getAtlas(LoadSave.PAUSE_ELEMENTS);
        imgs = new BufferedImage[3];

        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
        }
    }

    public void update() {
        index = 0;

        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE, null);
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }
}
