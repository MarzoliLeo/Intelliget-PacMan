package env;

import java.awt.*;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;


public class PacmanLogic {
    private PacmanModel pacmanModel;
    static Logger logger = Logger.getLogger(Arena2DEnvironment.class.getName());

    public PacmanLogic(PacmanModel pacmanModel) {
        this.pacmanModel = pacmanModel;
    }

    public boolean move(Direction direction) {
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

    public void moveEnemies(Direction direction) {
        for (int i = 0; i < pacmanModel.getEnemies().length; i++) {
            Point currentPos = pacmanModel.getEnemies()[i];
            Point newPos = getNewPosition(currentPos, direction);
            if (isValidMove(newPos.x, newPos.y)) {
                pacmanModel.setEnemyPosition(i, newPos.x, newPos.y);
                logger.info("[Logic] Enemy " + i + " moved to: (" + newPos.x + ", " + newPos.y + ")");
            }
        }
    }
}
