package env;

import utils.Observer;
import utils.Subject;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.util.logging.Logger;

public class PacmanModel implements Subject {
    private static final int GRID_SIZE = 25;
    private static final int CELL_SIZE = 25;
    private boolean[][] walls = new boolean[GRID_SIZE][GRID_SIZE];
    private boolean[][] scorePoints = new boolean[GRID_SIZE][GRID_SIZE];
    private boolean[][] powerUps = new boolean[GRID_SIZE][GRID_SIZE];
    private Point pacmanSphere;
    private Point[] enemies;
    private int score = 0; // Punteggio iniziale
    private List<Observer> observers = new ArrayList<>();

    static Logger logger = Logger.getLogger(Arena2DEnvironment.class.getName());

    public PacmanModel(int[][] gridMatrix) {
        // Initialize walls and yellowSphere
        initializeWalls(gridMatrix);
        initializeYellowSphere();
        initializeEnemies(4); // Initialize 4 enemies.
        generateScorePoints(0.2); // 20% probability for a score point at each non-wall cell.
        generatePowerUps(30); // Generate a maximum of 6 power-ups
    }


    private void initializeWalls(int[][] gridMatrix) {
        // Set cells to be walls or non-walls based on gridMatrix
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                //If 0 it is a wall, 1 its not a wall.
                walls[i][j] = gridMatrix[i][j] == 0;
            }
        }
    }

    private void initializeYellowSphere() {
        // Find a random non-wall cell
        int x, y;
        do {
            x = (int) (Math.random() * GRID_SIZE);
            y = (int) (Math.random() * GRID_SIZE);
        } while (walls[x][y]);

        pacmanSphere = new Point(x, y);

        notifyObservers();
    }

    private void initializeEnemies(int numEnemies) {
        enemies = new Point[numEnemies];
        for (int i = 0; i < numEnemies; i++) {
            int x, y;
            do {
                x = (int) (Math.random() * GRID_SIZE);
                y = (int) (Math.random() * GRID_SIZE);
            } while (walls[x][y] || (x == pacmanSphere.x && y == pacmanSphere.y));
            enemies[i] = new Point(x, y);
        }
        notifyObservers();
    }

    private void generateScorePoints(double probability) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (!walls[i][j] && Math.random() < probability) {
                    scorePoints[i][j] = true;
                }
            }
        }
    }

    private void generatePowerUps(int maxPowerUps) {
        int count = 0;
        while (count < maxPowerUps) {
            int i = (int) (Math.random() * GRID_SIZE);
            int j = (int) (Math.random() * GRID_SIZE);

            if (!walls[i][j] && !scorePoints[i][j] && !powerUps[i][j]) {
                powerUps[i][j] = true;
                count++;
            }
        }
    }


    public int getGridSize() {
        return GRID_SIZE;
    }

    public int getCellSize() {
        return CELL_SIZE;
    }

    public boolean[][] getWalls() {
        return walls;
    }

    public boolean[][] getScorePoints() {
        return scorePoints;
    }

    public boolean[][] getPowerUps() {
        return powerUps;
    }

    public Point getPacmanSphere() {
        return pacmanSphere;
    }
    public Point[] getEnemies() {
        return enemies;
    }

    /* ******************************************************************** */
    /* Methods used to represent score logic. */
    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
        logger.info("[Model]Score updated: " + this.score);
    }

    public void incrementScore() {
        score++;
        logger.info("[Model] Score incremented: " + score);
        notifyObservers();
    }

    public void consumeScorePoint(int x, int y) {
        if (scorePoints[x][y]) {
            scorePoints[x][y] = false;
            incrementScore();
        }
    }

    public boolean consumePowerUp(int x, int y) {
        if (powerUps[x][y]) {
            powerUps[x][y] = false;
            logger.info("[Model] Power-up consumed at: (" + x + ", " + y + ")");
            // Implement any power-up effects here
            notifyObservers();
            return true;
        }
        return false;
    }

    /* ******************************************************************** */
    /* Methods used for perceiving the surroundings. */

    public boolean isScorePoint(int x, int y) {
        return scorePoints[x][y];
    }

    public boolean isPowerUp(int x, int y) {
        return powerUps[x][y];
    }

    /* ******************************************************************** */
    /* Methods used for pattern Observer. */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
        //logger.info("Observer added: " + observer); // Debugging statement
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
        //logger.warning("Observers notified: " + observers.size()); // Debugging statement
    }

    //TODO put a comment here to describe.
    public void setPacmanSphere(int x, int y) {
        pacmanSphere = new Point(x, y);
        notifyObservers();
    }

    public void setEnemyPosition(int index, int x, int y) {
        enemies[index] = new Point(x, y);
        notifyObservers();
    }
}