package env;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class PacmanLogic {
    private PacmanModel pacmanModel;
    static Logger logger = Logger.getLogger(Arena2DEnvironment.class.getName());
    private volatile boolean gameRunning = true;
    private ScheduledExecutorService powerUpScheduler = Executors.newSingleThreadScheduledExecutor();

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

            // Check victory condition.
            if (pacmanModel.areAllScorePointsCollected()) {
                logger.info("Victory! All score points collected.");
                PacmanGame.stopGame();
                return true;
            }

            if (pacmanModel.consumePowerUp(newPos.x, newPos.y)) {
                handlePowerUp();
            }
            checkPacmanCapture();
            return true;
        }
        logger.info("Invalid move to: (" + newPos.x + ", " + newPos.y + ")");
        return false;
    }

    private void handlePowerUp() {
        pacmanModel.setPowerUpEndTime(System.currentTimeMillis() + PacmanModel.DURATION_POWERUP); // Power-up dura 15 secondi
        logger.info("Power-up attivato. Scade tra 15 secondi.");
        powerUpScheduler.schedule(() -> {
            pacmanModel.setPowerUpEndTime(0);
            logger.info("Power-up scaduto.");
        }, PacmanModel.DURATION_POWERUP, TimeUnit.MILLISECONDS);
    }

    private void disableEnemy(int enemyId) {
        long disableTime = System.currentTimeMillis() + PacmanModel.DURATION_DISABLE_ENEMIES; // 10 secondi di disattivazione
        pacmanModel.disableEnemy(enemyId, disableTime);
        logger.info("Enemy " + enemyId + " disabilitato per 10 secondi.");
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            pacmanModel.enableEnemy(enemyId); // Riabilita il nemico tramite il modello
            logger.info("Enemy " + enemyId + " riabilitato.");
        }, PacmanModel.DURATION_DISABLE_ENEMIES, TimeUnit.MILLISECONDS);
        scheduler.shutdown();
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
        return null;
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

        if (pacmanModel.isEnemyDisabled(enemyId)) {
            logger.info("[Logic] Enemy " + enemyId + " is disabled and cannot move.");
            return;
        }

        Point currentPos = pacmanModel.getEnemies()[enemyId];
        Point newPos = getNewPosition(currentPos, direction);

        if (pacmanModel.isEnemySlippery()) {
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

        if (isValidMove(getNewPosition(enemyPos, preferredDirection).x, getNewPosition(enemyPos, preferredDirection).y)) {
            return preferredDirection;
        }

        List<Direction> validDirections = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (isValidMove(getNewPosition(enemyPos, direction).x, getNewPosition(enemyPos, direction).y)) {
                validDirections.add(direction);
            }
        }

        if (!validDirections.isEmpty()) {
            return validDirections.get((int) (Math.random() * validDirections.size()));
        }

        return null;
    }

    private void checkPacmanCapture() {
        Point pacmanPos = pacmanModel.getPacmanSphere();
        for (int i = 0; i < pacmanModel.getEnemies().length; i++) {
            Point enemyPos = pacmanModel.getEnemies()[i];
            if (pacmanPos.equals(enemyPos)) {
                if (pacmanModel.getPowerUpEndTime() > System.currentTimeMillis()) {
                    disableEnemy(i);
                    pacmanModel.addScore(100);
                    logger.info("Enemy " + i + " disabilitato e 100 punti aggiunti.");
                } else {
                    logger.info("Pacman has been captured by an enemy!");
                    gameRunning = false;
                    PacmanGame.stopGame();
                    return;
                }
            }
        }
    }
}

