package env;

import utils.Observer;
import utils.Subject;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class PacmanModel implements Subject {
    public static final double PROBABILITY_TO_SPAWN_SCOREPOINTS = 0.3;
    public static final int NUMBER_OF_POWERUPS = 6;
    private static final int GRID_SIZE = 25;
    private static final int CELL_SIZE = 25;
    public static final double ENEMY_SLIPPERY_PROBABILITY = 0.30;
    public static final int DURATION_POWERUP = 15000;
    public static final int DURATION_DISABLE_ENEMIES = 10000;
    private static final Random RAND = new Random();
    public static final int NUM_ENEMIES = 4;
    private long powerUpEndTime = 0;
    private boolean[][] walls = new boolean[GRID_SIZE][GRID_SIZE];
    private boolean[][] scorePoints = new boolean[GRID_SIZE][GRID_SIZE];
    private boolean[][] powerUps = new boolean[GRID_SIZE][GRID_SIZE];
    private Point pacmanSphere;
    private Point[] enemies;
    private int score = 0;
    private List<Observer> observers = new ArrayList<>();
    private Map<Integer, Long> enemyDisableTimes = new ConcurrentHashMap<>();
    static Logger logger = Logger.getLogger(Arena2DEnvironment.class.getName());

    public PacmanModel(int[][] gridMatrix) {
        initializeWalls(gridMatrix);
        initializeYellowSphere();
        initializeEnemies(NUM_ENEMIES);
        generateScorePoints(PROBABILITY_TO_SPAWN_SCOREPOINTS);
        generatePowerUps(NUMBER_OF_POWERUPS);
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
    public long getPowerUpEndTime() {
        return powerUpEndTime;
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
    /* Methods to handle enemy features. */

    public boolean isEnemyDisabled(int enemyId) {
        Long disableTime = enemyDisableTimes.get(enemyId);
        return disableTime != null && disableTime > System.currentTimeMillis();
    }

    public void disableEnemy(int enemyId, long duration) {
        long disableTime = System.currentTimeMillis() + duration;
        enemyDisableTimes.put(enemyId, disableTime);
        notifyObservers();
    }

    public void enableEnemy(int enemyId) {
        enemyDisableTimes.remove(enemyId);
        notifyObservers();
    }

    public boolean isEnemySlippery() {
        return RAND.nextDouble() < ENEMY_SLIPPERY_PROBABILITY;
    }


    /* ******************************************************************** */
    /* Methods used for pattern Observer. */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
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
    }

    /* ******************************************************************** */
    /* Methods to set up the movement logic. */

    public void setPacmanSphere(int x, int y) {
        pacmanSphere = new Point(x, y);
        notifyObservers();
    }

    public void setEnemyPosition(int index, int x, int y) {
        enemies[index] = new Point(x, y);
        notifyObservers();
    }

    public void setPowerUpEndTime(long powerUpEndTime) {
        this.powerUpEndTime = powerUpEndTime;
    }


    /* ******************************************************************** */
    /* Method to check victory condition. */

    public boolean areAllScorePointsCollected() {
        for (int row = 0; row < getGridSize(); row++) {
            for (int col = 0; col < getGridSize(); col++) {
                if (getScorePoints()[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }
}