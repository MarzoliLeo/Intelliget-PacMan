package env;

import java.util.logging.Logger;


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
            logger.info("Move successful to: (" + newX + ", " + newY + ")"); // Debugging statement
            pacmanModel.consumeScorePoint(newX, newY); // Consuma il punto se presente
            return true;
        }

        logger.info("Invalid move to: (" + newX + ", " + newY + ")"); // Debugging statement
        return false;
    }

    public boolean isValidMove(int newX, int newY) {
        return newX >= 0 && newX < pacmanModel.getGridSize() && newY >= 0 && newY < pacmanModel.getGridSize() && !pacmanModel.getWalls()[newX][newY];
    }

}