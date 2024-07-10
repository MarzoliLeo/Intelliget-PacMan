package env;

import javax.swing.*;
import java.awt.*;

public class PacmanPanel extends JPanel {

    private PacmanModel pacmanModel;

    public PacmanPanel(PacmanModel pacmanModel) {
        this.pacmanModel = pacmanModel;
        this.setPreferredSize(new Dimension(pacmanModel.getGridSize() * pacmanModel.getCellSize(), pacmanModel.getGridSize() * pacmanModel.getCellSize()));
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
    }

    private void drawGrid(Graphics g) {
        for (int row = 0; row < pacmanModel.getGridSize(); row++) {
            for (int col = 0; col < pacmanModel.getGridSize(); col++) {
                //draw walls.
                if (pacmanModel.getWalls()[row][col]) {
                    g.setColor(Color.BLUE);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(col * pacmanModel.getCellSize(), row * pacmanModel.getCellSize(), pacmanModel.getCellSize(), pacmanModel.getCellSize());
                g.setColor(Color.BLACK);
                g.drawRect(col * pacmanModel.getCellSize(), row * pacmanModel.getCellSize(), pacmanModel.getCellSize(), pacmanModel.getCellSize());

                //draw points.
                if (pacmanModel.getScorePoints()[row][col]) {
                    g.setColor(Color.lightGray);
                    g.fillOval(col * pacmanModel.getCellSize() + pacmanModel.getCellSize() / 4, row * pacmanModel.getCellSize() + pacmanModel.getCellSize() / 4, pacmanModel.getCellSize() / 2, pacmanModel.getCellSize() / 2);
                }

                //draw powerups.
                if (pacmanModel.getPowerUps()[row][col]) {
                    g.setColor(Color.GREEN);
                    g.fillOval(col * pacmanModel.getCellSize() + pacmanModel.getCellSize() / 8, row * pacmanModel.getCellSize() + pacmanModel.getCellSize() / 8, pacmanModel.getCellSize() * 3 / 4, pacmanModel.getCellSize() * 3 / 4);
                }

                //draw pacman.
                if (pacmanModel.getPacmanSphere().x == row && pacmanModel.getPacmanSphere().y == col) {
                    g.setColor(Color.YELLOW);
                    g.fillOval(col * pacmanModel.getCellSize(), row * pacmanModel.getCellSize(), pacmanModel.getCellSize(), pacmanModel.getCellSize());
                }
            }
        }
    }
}