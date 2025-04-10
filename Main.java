import java.util.*;

class Point {
    int x, y;
    Point(int x_, int y_) {
        x = x_;
        y = y_;
    }
}

class QNode {
    Point p;
    int d;
    List<Point> path;
    QNode(Point p_, int d_, List<Point> path_) {
        p = p_;
        d = d_;
        path = new ArrayList<>(path_);
    }
}

public class MazeSolver {
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter maze size (rows columns): ");
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();

        int[][] maze = generateMazeWithPath(rows, cols);
        System.out.println("Generated Maze:");
        printMaze(maze);

        long startTime = System.nanoTime();
        List<Point> shortestPath = BFS(maze, new Point(0, 0), new Point(rows - 1, cols - 1));
        long endTime = System.nanoTime();

        if (shortestPath != null) {
            System.out.println("Shortest Path Length: " + (shortestPath.size() - 1));
            printPath(shortestPath);
        } else {
            System.out.println("No path found!");
        }

        System.out.println("BFS Execution Time: " + (endTime - startTime) / 1e6 + " ms");
    }

    private static int[][] generateMazeWithPath(int rows, int cols) {
        int[][] maze = new int[rows][cols];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = 0;
            }
        }
        carvePath(maze, 0, 0, random);

        if (maze[rows - 1][cols - 2] == 0 && maze[rows - 2][cols - 1] == 0) {
            maze[rows - 1][cols - 2] = 1;
            maze[rows - 2][cols - 1] = 1;
        }
        maze[rows - 1][cols - 1] = 1;

        return maze;
    }


    private static void carvePath(int[][] maze, int i, int j, Random random) {
        int rows = maze.length, cols = maze[0].length;
        maze[i][j] = 1; // Mark the starting cell as open (path)

        List<int[]> directions = new ArrayList<>(Arrays.asList(DIRECTIONS));
        Collections.shuffle(directions, random);

        for (int[] dir : directions) {
            int ni = i + dir[0] * 2, nj = j + dir[1] * 2;

            if (ni >= 0 && ni < rows && nj >= 0 && nj < cols && maze[ni][nj] == 0) {
                maze[i + dir[0]][j + dir[1]] = 1;
                maze[ni][nj] = 1;
                carvePath(maze, ni, nj, random);
            }
        }
    }



    private static void printMaze(int[][] maze) {
        for (int[] row : maze) {
            for (int cell : row) {
                System.out.print(cell == 1 ? " " : "#"); // ` ` for paths, `#` for walls
            }
            System.out.println();
        }
    }

    private static boolean isValid(int x, int y, int r, int c) {
        return x >= 0 && x < r && y >= 0 && y < c;
    }

    private static List<Point> BFS(int[][] maze, Point src, Point dest) {
        int r = maze.length, c = maze[0].length;
        if (maze[src.x][src.y] == 0 || maze[dest.x][dest.y] == 0) return null;
        boolean[][] vis = new boolean[r][c];
        Queue<QNode> q = new LinkedList<>();
        List<Point> initialPath = new ArrayList<>();
        initialPath.add(src);
        q.add(new QNode(src, 0, initialPath));
        vis[src.x][src.y] = true;

        while (!q.isEmpty()) {
            QNode node = q.poll();
            Point p = node.p;
            int d = node.d;
            List<Point> path = node.path;

            if (p.x == dest.x && p.y == dest.y) return path;

            for (int[] dir : DIRECTIONS) {
                int nx = p.x + dir[0], ny = p.y + dir[1];
                if (isValid(nx, ny, r, c) && maze[nx][ny] == 1 && !vis[nx][ny]) {
                    vis[nx][ny] = true;
                    List<Point> newPath = new ArrayList<>(path);
                    newPath.add(new Point(nx, ny));
                    q.add(new QNode(new Point(nx, ny), d + 1, newPath));
                }
            }
        }
        return null;
    }

    private static void printPath(List<Point> path) {
        System.out.println("Shortest Path:");
        for (Point p : path) {
            System.out.print("(" + p.x + ", " + p.y + ") -> ");
        }
        System.out.println("End");
    }
}
