package ui;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class PauseButton {
    @Getter
    @Setter
    protected int x, y, width, height;
    @Getter
    protected Rectangle bounds;

    public PauseButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createBounds();
    }

    private void createBounds() {
        bounds = new Rectangle(x, y, width, height);

    }
}
