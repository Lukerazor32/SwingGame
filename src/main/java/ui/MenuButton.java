package ui;

import gamestates.GameState;
import lombok.Getter;
import lombok.Setter;
import utilz.Constants;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.Buttons.*;
import static utilz.LoadSave.MAIN_MENU_ELEMENTS;

public class MenuButton {
    private int xPos, yPos, rowIndex, index;
    private int xOffSetCenter = B_WIDTH / 2;
    private GameState gameState;
    private BufferedImage[] imgs;
    @Getter
    @Setter
    private boolean mouseOver, mousePressed;
    @Getter
    private Rectangle buttonBox;

    public MenuButton(int xPos, int yPos, int rowIndex, GameState gameState) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.gameState = gameState;

        loadImgs();
        initBoxes();
    }

    private void initBoxes() {
        buttonBox = new Rectangle(xPos - xOffSetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.getAtlas(MAIN_MENU_ELEMENTS);

        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
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
        g.drawImage(imgs[index], xPos - xOffSetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    public void applyGamesState() {
        GameState.state = gameState;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }
}
