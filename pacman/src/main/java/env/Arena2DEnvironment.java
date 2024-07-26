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

    // action literals
    public static final Literal moveForward = Literal.parseLiteral("move(" + FORWARD.name().toLowerCase() + ")");
    public static final Literal moveRight = Literal.parseLiteral("move(" + RIGHT.name().toLowerCase() + ")");
    public static final Literal moveLeft = Literal.parseLiteral("move(" + LEFT.name().toLowerCase() + ")");
    public static final Literal moveBackward = Literal.parseLiteral("move(" + FORWARD.name().toLowerCase() + ")");
    public static final Literal moveRandom = Literal.parseLiteral("move(random)");

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
        if (action.getFunctor().equals("move")) {
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
        if (moved) {
            informAgsEnvironmentChanged();
            //pacmanModel.notifyObservers();
            return true;  // Action succeeded
        } else {
            logger.warning("Action " + action + " failed for agent " + agName);
            return false;  // Action failed
        }
    }

   /* @Override
    public void updatePercepts() {
        clearPercepts();
        int x = pacmanModel.getPacmanSphere().x;
        int y = pacmanModel.getPacmanSphere().y;
        addPercept(Literal.parseLiteral("position(" + x + "," + y + ")"));
    }*/

    @Override
    public void stop() {
        super.stop();
        pacmanGame.stopGame(); // Stop the Pacman game when the environment stops
    }


}
