package env;

public class PacmanMap {
    private static final int GRID_SIZE = 25;
    private char[][] mapMatrix;
    private int[][] gridMatrix = new int[GRID_SIZE][GRID_SIZE];

    public PacmanMap() {

        mapMatrix = new char[][]
                {
                        "#########################".toCharArray(),
                        "#.......................#".toCharArray(),
                        "#..########...########..#".toCharArray(),
                        "#.....#.....#.....#.....#".toCharArray(),
                        "##....#.....#.....#....##".toCharArray(),
                        "##....#.#########.#....##".toCharArray(),
                        "##.....................##".toCharArray(),
                        "#.....#.#########.#.....#".toCharArray(),
                        "#...#.......#.......#...#".toCharArray(),
                        "#...#.#####.#.#####.#...#".toCharArray(),
                        "#...#...............#...#".toCharArray(),
                        "###.#.....#####.....#.###".toCharArray(),
                        "#.........#####.........#".toCharArray(),
                        "###.#.....#####.....#.###".toCharArray(),
                        "#...#...............#...#".toCharArray(),
                        "#...#.#####.#.#####.#...#".toCharArray(),
                        "#...#.......#.......#...#".toCharArray(),
                        "#.....#.#########.#.....#".toCharArray(),
                        "##.....................##".toCharArray(),
                        "##....#.#########.#....##".toCharArray(),
                        "##....#.....#.....#....##".toCharArray(),
                        "#.....#.....#.....#.....#".toCharArray(),
                        "#..########...########..#".toCharArray(),
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
