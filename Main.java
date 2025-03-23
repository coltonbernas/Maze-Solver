import java.util.*;

class MazeGenerator {
    private int width, height;
    private Set<String> openPaths;
    private Map<String, List<String>> graph;
    private Random rand = new Random();
    
    public MazeGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.openPaths = new HashSet<>();
        this.graph = new HashMap<>();
    }
    
    public void generate() {
        int startX = 1, startY = 1;
        addPath(startX, startY);
        List<int[]> walls = new ArrayList<>();
        for (int[] neighbor : getNeighbors(startX, startY)) {
            walls.add(new int[]{startX, startY, neighbor[0], neighbor[1]});
        }
        
        while (!walls.isEmpty()) {
            int[] wall = walls.remove(rand.nextInt(walls.size()));
            int x = wall[0], y = wall[1], nx = wall[2], ny = wall[3];
            
            if (!openPaths.contains(coord(nx, ny))) {
                addPath(nx, ny);
                addPath((x + nx) / 2, (y + ny) / 2);
                
                for (int[] newNeighbor : getNeighbors(nx, ny)) {
                    walls.add(new int[]{nx, ny, newNeighbor[0], newNeighbor[1]});
                }
            }
        }
    }
    
    private void addPath(int x, int y) {
        String key = coord(x, y);
        openPaths.add(key);
        graph.putIfAbsent(key, new ArrayList<>());
        for (int[] dir : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
            int nx = x + dir[0], ny = y + dir[1];
            String neighbor = coord(nx, ny);
            if (openPaths.contains(neighbor)) {
                graph.get(key).add(neighbor);
                graph.get(neighbor).add(key);
            }
        }
    }
    
    private List<int[]> getNeighbors(int x, int y) {
        int[][] directions = {{0, 2}, {0, -2}, {2, 0}, {-2, 0}};
        List<int[]> neighbors = new ArrayList<>();
        
        for (int[] dir : directions) {
            int nx = x + dir[0], ny = y + dir[1];
            if (nx >= 1 && nx < width - 1 && ny >= 1 && ny < height - 1) {
                neighbors.add(new int[]{nx, ny});
            }
        }
        return neighbors;
    }
    
    public void display() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(openPaths.contains(coord(x, y)) ? " " : "#");
            }
            System.out.println();
        }
    }
    
    public boolean solve() {
        String start = coord(1, 1), end = coord(width - 2, height - 2);
        if (!graph.containsKey(start) || !graph.containsKey(end)) return false;
        
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);
        
        while (!queue.isEmpty()) {
            String node = queue.poll();
            if (node.equals(end)) return true;
            for (String neighbor : graph.get(node)) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }
        return false;
    }
    
    public String getComplexity() {
        return "Maze Generation: O(W * H), Pathfinding (BFS): O(W * H)";
    }
    
    private String coord(int x, int y) {
        return x + "," + y;
    }
    
    public static void main(String[] args) {
        MazeGenerator maze = new MazeGenerator(21, 21);
        maze.generate();
        maze.display();
        
        if (maze.solve()) {
            System.out.println("Maze is solvable!");
        } else {
            System.out.println("Maze has no solution.");
        }
        
        System.out.println("Complexity: " + maze.getComplexity());
    }
}
