package env;

import jason.asSyntax.Structure;
import jason.environment.Environment;
import java.awt.*;
import java.util.logging.Logger;


import static env.Direction.*;
/**
 * Any Jason environment "entry point" should extend
 * jason.environment.Environment class to override methods init(),
 * updatePercepts() and executeAction().
 */


public class Arena2DEnvironment extends Environment {

    public static final int DISTANCE_TO_CHASE_PACMAN = 5;
    public static final int PACMAN_DIST_TO_CHECK_ENV = 2;

    static Logger logger = Logger.getLogger(Arena2DEnvironment.class.getName());

    private PacmanLogic pacmanLogic;
    private PacmanMap pacmanMap;
    private PacmanModel pacmanModel;

    @Override
    public void init(final String[] args) {

        pacmanMap = new PacmanMap(); //set up of map.
        pacmanModel = new PacmanModel(pacmanMap.getMatrix()); //load the model data.
        pacmanLogic = new PacmanLogic(pacmanModel);

        // Start the game with the existing model
        PacmanGame.startGame(pacmanModel);
    }


    //actions that the agent can perform.
    @Override
    public boolean executeAction(String agName, Structure action) {
        if (pacmanLogic == null) {
            logger.warning("PacmanLogic is null. Initialization error?");
            return false;
        }
        boolean moved = false;

        if (action.getFunctor().equals("move")) { // Check movement of Pacman
            Direction direction;
            if (action.getTerm(0).toString().equalsIgnoreCase("preferential")) {
                direction = pacmanLogic.choosePreferredDirection(pacmanModel.getPacmanSphere(), PACMAN_DIST_TO_CHECK_ENV); // Using a distance of 1
            } else {
                direction = Direction.random();
                if (!action.getTerm(0).toString().equalsIgnoreCase("random")) {
                    direction = Direction.valueOf(action.getTerm(0).toString().toUpperCase());
                }
            }

            if (direction != null) {
                logger.info("Agent " + agName + " is attempting to move in direction: " + direction);
                moved = pacmanLogic.move(direction);
            } else {
                logger.warning("No valid moves available for agent " + agName);
            }
        }

        if (action.getFunctor().equals("move_enemy")) {
            int enemyId = Integer.parseInt(action.getTerm(0).toString());
            Direction direction = Direction.random(); // Default direction if no specific direction is provided

            // Find the enemy's current position and Pacman's position
            Point enemyPos = pacmanModel.getEnemies()[enemyId];
            Point pacmanPos = pacmanModel.getPacmanSphere();

            // Check if Pacman is within 5 cells of the enemy
            if (Math.abs(pacmanPos.x - enemyPos.x) <= DISTANCE_TO_CHASE_PACMAN && Math.abs(pacmanPos.y - enemyPos.y) <= DISTANCE_TO_CHASE_PACMAN) {
                // Choose direction towards Pacman
                direction = pacmanLogic.chooseDirectionTowardsPacman(enemyPos, pacmanPos);
            } else {
                // Move randomly if Pacman is not within the range
                if (action.getTerms().size() > 1) {
                    String moveType = action.getTerm(1).toString();
                    if (moveType.equalsIgnoreCase("random")) {
                        direction = Direction.random();
                    } else {
                        direction = Direction.valueOf(moveType.toUpperCase());
                    }
                }
            }

            if (direction != null) {
                logger.info("Agent " + agName + " is attempting to move enemy " + enemyId + " in direction: " + direction);
                pacmanLogic.moveEnemy(enemyId, direction);
                moved = true;
            } else {
                logger.warning("No valid moves available for enemy " + enemyId + " by agent " + agName);
            }
        }


        if (moved) {
            informAgsEnvironmentChanged();
            return true;  // Action succeeded
        } else {
            logger.warning("Action " + action + " failed for agent " + agName);
            return false;  // Action failed
        }
    }


    @Override
    public void stop() {
        super.stop();
        PacmanGame.stopGame();
    }
}
