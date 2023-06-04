package gamestates;

import lombok.Getter;
import main.GameThread;
import ui.MenuButton;

import java.awt.event.MouseEvent;

public class State {
    @Getter
    protected GameThread gameThread;

    public State(GameThread gameThread) {
        this.gameThread = gameThread;
    }

    public boolean hover(MouseEvent e, MenuButton b) {
        return b.getButtonBox().contains(e.getX(), e.getY());
    }
}
