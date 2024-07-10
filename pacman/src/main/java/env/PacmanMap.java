package env;

public class PacmanMap {
    private static final int GRID_SIZE = 25; //TODO Questo non dovrebbe stare qui, ma dovrebbe essere il model. Spostare tutta la classe in model?
    private char[][] mapMatrix;
    private int[][] gridMatrix = new int[GRID_SIZE][GRID_SIZE];

    public PacmanMap() {
        // Initialize gridMatrix (TESTING PURPOSES)
        // This is just an example, modify it according to your needs
        /*for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (i == 0 || i == GRID_SIZE - 1 || j == 0 || j == GRID_SIZE - 1) {
                    gridMatrix[i][j] = 0; // Wall
                } else {
                    gridMatrix[i][j] = 1; // Non-wall
                }
            }
        }*/
        mapMatrix = new char[][]
                {
                        "#########################".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#........#.......#......#".toCharArray(),
                        "#........#.......#......#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.........#.....#.......#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#########################".toCharArray(),
                };

        // Convert charMatrix to gridMatrix.
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                gridMatrix[i][j] = mapMatrix[i][j] == '#' ? 0 : 1;
            }
        }
    }

    public int[][] getMatrix() {
        return gridMatrix;
    }
}
