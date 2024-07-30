package env;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;

import javax.swing.*;
import java.util.Collection;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static env.Direction.*;
/**
 * Any Jason environment "entry point" should extend
 * jason.environment.Environment class to override methods init(),
 * updatePercepts() and executeAction().
 */


public class Arena2DEnvironment extends Environment {

    private static final Random RAND = new Random();

    static Logger logger = Logger.getLogger(Arena2DEnvironment.class.getName());

    private PacmanGame pacmanGame;
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
                direction = pacmanLogic.choosePreferredDirection(pacmanModel.getPacmanSphere(), 1); // Using a distance of 1
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

        if (action.getFunctor().equals("move_enemies")) { // Check movement of enemies
            Direction direction;
            if (action.getTerm(0).toString().equalsIgnoreCase("random")) {
                direction = Direction.random();
            } else {
                direction = Direction.valueOf(action.getTerm(0).toString().toUpperCase());
            }

            if (direction != null) {
                logger.info("Agent " + agName + " is attempting to move enemies in direction: " + direction);
                pacmanLogic.moveEnemies(direction);
                moved = true;
            } else {
                logger.warning("No valid moves available for enemies by agent " + agName);
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



    /*@Override
    public void updatePercepts() {
        clearPercepts();
        int x = pacmanModel.getPacmanSphere().x;
        int y = pacmanModel.getPacmanSphere().y;
        addPercept(Literal.parseLiteral("position(" + x + "," + y + ")"));

        for (int i = 0; i < pacmanModel.getEnemies().length; i++) {
            Point enemy = pacmanModel.getEnemies()[i];
            addPercept(Literal.parseLiteral("enemyPosition(" + i + "," + enemy.x + "," + enemy.y + ")"));
        }
    }*/


    @Override
    public void stop() {
        super.stop();
        pacmanGame.stopGame(); // Stop the Pacman game when the environment stops
    }
}
