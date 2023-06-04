package main;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final KeyHandler keyHandler = new KeyHandler(this);
    private final MouseHandler mouseHandler = new MouseHandler(this);
    @Getter
    private final GameThread game;

    public GamePanel(GameThread game) {
        this.game = game;

        this.setPreferredSize(new Dimension(game.GAME_WIDTH, game.GAME_HEIGHT));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
        this.setFocusable(true);
    }

    public void updateGame() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        game.render(g);
    }


}
