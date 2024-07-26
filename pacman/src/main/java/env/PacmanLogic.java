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
        int newX = pacmanModel.getPacmanSphere().x;
        int newY = pacmanModel.getPacmanSphere().y;

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

        if (isValidMove(newX, newY)) {
            pacmanModel.setPacmanSphere(newX, newY);
            logger.info("Move successful to: (" + newX + ", " + newY + ")");
            pacmanModel.consumeScorePoint(newX, newY);
            return true;
        }

        logger.info("Invalid move to: (" + newX + ", " + newY + ")");
        return false;
    }

    public boolean isValidMove(int newX, int newY) {
        return newX >= 0 && newX < pacmanModel.getGridSize() && newY >= 0 && newY < pacmanModel.getGridSize() && !pacmanModel.getWalls()[newX][newY];
    }

    //TODO questi usano POINT dovrebbero usare le coordinate x e y... vediamo poi di refactorizzare.
    public Direction choosePreferredDirection(Point currentPos, int distance) {
        List<Point> surroundingCells = pacmanModel.getSurroundingCells(currentPos, distance);
        for (Point cell : surroundingCells) {
            if (pacmanModel.isScorePoint(cell.x, cell.y)) {
                logger.info("Preferring direction towards score point at: (" + cell.x + ", " + cell.y + ")");
                return getDirectionFromPoints(currentPos, cell);
            }
        }
        logger.info("No score points nearby, moving randomly");
        return Direction.random(); // Move randomly if no score points are nearby
    }

    private Direction getDirectionFromPoints(Point start, Point end) {
        if (end.x < start.x) {
            return Direction.FORWARD;
        } else if (end.x > start.x) {
            return Direction.BACKWARD;
        } else if (end.y < start.y) {
            return Direction.LEFT;
        } else {
            return Direction.RIGHT;
        }
    }

}