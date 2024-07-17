package env;

import javax.swing.*;
import java.awt.*;

public class PacmanGame {

    private static JFrame frame;

    public static void startGame(PacmanModel pacmanModel) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Pacman Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            PacmanPanel gamePanel = new PacmanPanel(pacmanModel); // draw the GUI.
            frame.add(gamePanel, BorderLayout.CENTER);

            frame.pack(); //adjust the size based on the grid.
            frame.setVisible(true);
        });
    }

    public static void stopGame() {
        if (frame != null) {
            frame.dispose();
        }
    }
}

