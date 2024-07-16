package env;

import javax.swing.*;
import java.awt.*;

public class PacmanGame {

    //TODO questo andrebbe tutto dentro il metodo Init di Arena2DEnvironment per non essere così brutto.
    private static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Pacman Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            PacmanMap pacmanMap = new PacmanMap(); //set up of map.
            PacmanModel pacmanModel = new PacmanModel(pacmanMap.getMatrix()); //load the model data.
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
