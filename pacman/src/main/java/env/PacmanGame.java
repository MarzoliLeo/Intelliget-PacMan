package env;

import javax.swing.*;
import java.awt.*;

public class PacmanGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pacman Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            PacmanMap pacmanMap = new PacmanMap();
            PacmanPanel gamePanel = new PacmanPanel(pacmanMap.getMatrix());
            frame.add(gamePanel, BorderLayout.CENTER);

            frame.pack(); //adjust the size based on the grid.

            frame.setVisible(true);
        });
    }
}
