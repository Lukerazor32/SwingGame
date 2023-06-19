package ui;

import gamestates.GameState;
import gamestates.Playing;
import main.GameThread;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOver {
    private Playing playing;

    public GameOver(Playing playing) {
        this.playing = playing;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GameThread.GAME_WIDTH, GameThread.GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawString("GAME OVER", GameThread.GAME_WIDTH / 2, 150);
        g.drawString("Press ESC", GameThread.GAME_WIDTH / 2, 300);
    }

    public void KeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            GameState.state = GameState.MENU;
        }
    }
}
