package env;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class PacmanLogic {
    public static final double ENEMY_SLIPPERY_PROBABILITY = 0.30;
    private PacmanModel pacmanModel;
    static Logger logger = Logger.getLogger(Arena2DEnvironment.class.getName());
    private static final Random RAND = new Random();
    private volatile boolean gameRunning = true; // Flag per controllare lo stato del gioco

    public PacmanLogic(PacmanModel pacmanModel) {
        this.pacmanModel = pacmanModel;
    }

    public boolean move(Direction direction) {
        if (!gameRunning) {
            logger.info("Game has stopped. No further actions are performed.");
            return false;
        }

        Point currentPos = pacmanModel.getPacmanSphere();
        Point newPos = getNewPosition(currentPos, direction);

        if (isValidMove(newPos.x, newPos.y)) {
            pacmanModel.setPacmanSphere(newPos.x, newPos.y);
            logger.info("Move successful to: (" + newPos.x + ", " + newPos.y + ")");
            pacmanModel.consumeScorePoint(newPos.x, newPos.y);
            pacmanModel.consumePowerUp(newPos.x, newPos.y);
            return true;
        }
        logger.info("Invalid move to: (" + newPos.x + ", " + newPos.y + ")");
        return false;
    }

    public boolean isValidMove(int newX, int newY) {
        return newX >= 0 && newX < pacmanModel.getGridSize() && newY >= 0 && newY < pacmanModel.getGridSize() && !pacmanModel.getWalls()[newX][newY];
    }

    public Direction choosePreferredDirection(Point currentPos, int distance) {
        List<Direction> validDirections = new ArrayList<>();
        Direction preferredDirection = null;

        for (Direction direction : Direction.values()) {
            Point newPos = getNewPosition(currentPos, direction);
            if (isValidMove(newPos.x, newPos.y)) {
                validDirections.add(direction);
                if (pacmanModel.isScorePoint(newPos.x, newPos.y)) {
                    preferredDirection = direction;
                    break; // Prefer score points over power-ups, so break if found
                }
                if (pacmanModel.isPowerUp(newPos.x, newPos.y) && preferredDirection == null) {
                    preferredDirection = direction; // Set preferred direction if it's a power-up
                }
            }
        }

        if (preferredDirection != null) {
            logger.info("Preferring direction towards score point: " + preferredDirection);
            return preferredDirection;
        }

        if (!validDirections.isEmpty()) {
            Direction randomValidDirection = validDirections.get((int) (Math.random() * validDirections.size()));
            logger.info("No score points nearby, moving to a valid direction: " + randomValidDirection);
            return randomValidDirection;
        }

        logger.info("No valid moves available, staying in place");
        return null; // No valid moves available
    }

    private Point getNewPosition(Point currentPos, Direction direction) {
        int newX = currentPos.x;
        int newY = currentPos.y;
        switch (direction) {
            case FORWARD:
                newX--;
                break;
            case BACKWARD:
                newX++;
                break;
            case LEFT:
                newY--;
                break;
            case RIGHT:
                newY++;
                break;
        }
        return new Point(newX, newY);
    }

    /* ***************************************************** */
    /* Enemy Logic */

    public void moveEnemy(int enemyId, Direction direction) {
        if (!gameRunning) {
            logger.info("Game has stopped. No further actions are performed.");
            return;
        }

        Point currentPos = pacmanModel.getEnemies()[enemyId];
        Point newPos = getNewPosition(currentPos, direction);

        if (RAND.nextDouble() < ENEMY_SLIPPERY_PROBABILITY) {
            logger.info("[Logic] Enemy " + enemyId + " is blocked and remains in place.");
            // Enemy remains in place, no movement
            return;
        }

        if (isValidMove(newPos.x, newPos.y)) {
            pacmanModel.setEnemyPosition(enemyId, newPos.x, newPos.y);
            logger.info("[Logic] Enemy " + enemyId + " moved to: (" + newPos.x + ", " + newPos.y + ")");
            checkPacmanCapture(); // Check if the move results in Pacman capture
        } else {
            logger.info("[Logic] Invalid move for enemy " + enemyId + " to: (" + newPos.x + ", " + newPos.y + ")");
        }
    }

    public Direction chooseDirectionTowardsPacman(Point enemyPos, Point pacmanPos) {
        if (!gameRunning) {
            logger.info("Game has stopped. No further actions are performed.");
            return null;
        }

        int dx = pacmanPos.x - enemyPos.x;
        int dy = pacmanPos.y - enemyPos.y;

        Direction preferredDirection = null;

        if (Math.abs(dx) > Math.abs(dy)) {
            // Prefer horizontal movement
            preferredDirection = dx > 0 ? Direction.BACKWARD : Direction.FORWARD;
        } else {
            // Prefer vertical movement
            preferredDirection = dy > 0 ? Direction.RIGHT : Direction.LEFT;
        }

        // Check if the preferred direction is valid
        if (isValidMove(getNewPosition(enemyPos, preferredDirection).x, getNewPosition(enemyPos, preferredDirection).y)) {
            return preferredDirection;
        }

        // If preferred direction is not valid, choose another random valid direction
        List<Direction> validDirections = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (isValidMove(getNewPosition(enemyPos, direction).x, getNewPosition(enemyPos, direction).y)) {
                validDirections.add(direction);
            }
        }

        if (!validDirections.isEmpty()) {
            return validDirections.get((int) (Math.random() * validDirections.size()));
        }

        return null; // No valid directions available
    }

    // Method to check if Pacman has been captured
    private void checkPacmanCapture() {
        Point pacmanPos = pacmanModel.getPacmanSphere();
        for (Point enemyPos : pacmanModel.getEnemies()) {
            if (pacmanPos.equals(enemyPos)) {
                logger.info("Pacman has been captured by an enemy!");
                gameRunning = false; // Impedisci ulteriori azioni
                PacmanGame.stopGame();
                return;
            }
        }
    }

}
