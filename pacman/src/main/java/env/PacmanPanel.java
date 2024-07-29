package env;

import utils.Observer;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class PacmanPanel extends JPanel implements Observer {

    private PacmanModel pacmanModel;

    static Logger logger = Logger.getLogger(Arena2DEnvironment.class.getName());

    public PacmanPanel(PacmanModel pacmanModel) {
        this.pacmanModel = pacmanModel;
        this.pacmanModel.addObserver(this);
        this.setPreferredSize(new Dimension(pacmanModel.getGridSize() * pacmanModel.getCellSize(), pacmanModel.getGridSize() * pacmanModel.getCellSize()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawScore(g);
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

    //TODO QUESTO E' UNO SCORE NELLA GUI CHE PERO' NON VIENE MOSTRATO DA NESSUNA PARTE, NON SERVE COME PENSAVI TU A TOGLIERE GLI SCOREPOINTS.
    private void drawScore(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("Score: " + pacmanModel.getScore(), 10, pacmanModel.getGridSize() * pacmanModel.getCellSize() + 20);
    }


    @Override
    public void update() {
        repaint();
    }
}