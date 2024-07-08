package env;

import javax.swing.*;
import java.awt.*;

public class PacmanGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pacman Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new BorderLayout());

            PacmanPanel gamePanel = new PacmanPanel();
            frame.add(gamePanel, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}
