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
        /*this.model = new Arena2DModelImpl(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        if (args.length > 2) {
            model.setSlideProbability(Double.parseDouble(args[2]));
        }
        Arena2DGuiView view = new Arena2DGuiView(model);
        this.view = view;
        view.setVisible(true);*/

        pacmanGame = new PacmanGame();
        pacmanGame.main(null);
        pacmanMap = new PacmanMap(); //set up of map.
        pacmanModel = new PacmanModel(pacmanMap.getMatrix()); //load the model data.
        pacmanLogic = new PacmanLogic(pacmanModel);

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
            Direction direction = Direction.random();
            if (!action.getTerm(0).toString().equalsIgnoreCase("random")) {
                direction = Direction.valueOf(action.getTerm(0).toString().toUpperCase());
            }
            logger.info("Agent " + agName + " is attempting to move in direction: " + direction);
            moved = pacmanLogic.move(direction);
        }

        if (moved) {
            informAgsEnvironmentChanged();
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
