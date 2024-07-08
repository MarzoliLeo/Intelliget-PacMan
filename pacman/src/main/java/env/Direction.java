package env;

import java.util.Random;

/**
 * Relative 4-valued direction
 */
public enum Direction {
    FORWARD,
    RIGHT,
    BACKWARD,
    LEFT;

    private static final Random RAND = new Random();

    public static Direction random() {
        return values()[RAND.nextInt(values().length)];
    }
}
