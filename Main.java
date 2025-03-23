import java.util.*;

class MazeToGraph {
    
    // Node class to represent a point in the maze
    static class Node {
        int x, y; // Coordinates of the node

        Node(int x, int y) { 
            this.x = x; 
            this.y = y; 
        }

        // Override equals() to compare nodes based on their coordinates
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Node)) return false;
            Node other = (Node) obj;
            return this.x == other.x && this.y == other.y;
        }

        // Override hashCode() to use Node as a key in HashMap
        @Override
        public int hashCode() { 
            return Objects.hash(x, y); 
        }

        // Override toString() for readable output
        @Override
        public String toString() { 
            return "(" + x + ", " + y + ")"; 
        }
    }

    /**
     * Converts a maze into a graph representation using an adjacency list.
     * @param maze 2D integer array representing the maze (0 = open path, 1 = wall).
     * @return A graph where each node represents an open path, and edges represent possible moves.
     */
    public static Map<Node, List<Node>> mazeToGraph(int[][] maze) {
        int rows = maze.length, cols = maze[0].length;
        Map<Node, List<Node>> graph = new HashMap<>();

        // Directions for moving right, down, left, up
        int[] dx = {0, 1, 0, -1}; 
        int[] dy = {1, 0, -1, 0};

        // Iterate through the maze grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 0) { // If it's an open path (0)
                    Node node = new Node(i, j); // Create a node for this cell
                    graph.put(node, new ArrayList<>()); // Initialize adjacency list

                    // Check all 4 possible movement directions
                    for (int k = 0; k < 4; k++) {
                        int ni = i + dx[k], nj = j + dy[k]; // Calculate neighbor coordinates
                        if (ni >= 0 && ni < rows && nj >= 0 && nj < cols && maze[ni][nj] == 0) {
                            graph.get(node).add(new Node(ni, nj)); // Connect to neighbor node
                        }
                    }
                }
            }
        }
        return graph;
    }

    public static void main(String[] args) {
        // Define a sample maze (0 = open path, 1 = wall)
        int[][] maze = {
            {0, 1, 0, 0},
            {0, 0, 0, 1},
            {1, 0, 1, 0},
            {0, 0, 0, 0}
        };

        // Convert the maze to a graph representation
        Map<Node, List<Node>> graph = mazeToGraph(maze);

        // Print the graph (each node and its connected neighbors)
        for (Node node : graph.keySet()) {
            System.out.println(node + " -> " + graph.get(node));
        }
    }
}
