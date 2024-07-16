package env;

public class PacmanLogic {

    private PacmanModel pacmanModel;

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
            pacmanModel.getPacmanSphere().x = newX;
            pacmanModel.getPacmanSphere().y = newY;
            return true;
        }

        return false;
    }

    public boolean isValidMove(int newX, int newY) {
        return newX >= 0 && newX < pacmanModel.getGridSize() && newY >= 0 && newY < pacmanModel.getGridSize() && !pacmanModel.getWalls()[newX][newY];
    }

}