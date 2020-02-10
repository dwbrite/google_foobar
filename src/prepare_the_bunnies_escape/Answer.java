package prepare_the_bunnies_escape;

import java.util.ArrayList;
import java.util.HashMap;

class Cell {
    int x, y;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object obj) {
        return obj instanceof Cell && this.y == ((Cell) obj).y && this.x == ((Cell) obj).x;
    }

    ArrayList<Cell> getNeighbors() {
        ArrayList<Cell> neighbors = new ArrayList<>();

        if (y > 0 && (Answer.map[y - 1][x] == 0))
            neighbors.add(new Cell(x, y - 1));
        if (y < Answer.map.length - 1 && (Answer.map[y + 1][x] == 0))
            neighbors.add(new Cell(x, y + 1));
        if (x > 0 && (Answer.map[y][x - 1] == 0))
            neighbors.add(new Cell(x - 1, y));
        if (x < Answer.map[0].length - 1 && (Answer.map[y][x + 1] == 0))
            neighbors.add(new Cell(x + 1, y));

        return neighbors;
    }
}

class AStar {
    private static ArrayList<Cell> reconstructPath(HashMap<Cell, Cell> parentMap, Cell current) {
        ArrayList<Cell> path = new ArrayList<>();
        path.add(current);
        while (parentMap.keySet().contains(current)) {
            current = parentMap.get(current);
            path.add(current);
        }
        return path;
    }

    static ArrayList<Cell> findPath(Cell start, Cell goal) {
        // This is just a simplified A* implementation, translated from the pseudo-code on Wikipedia.
        // In this case, we're not using fScore or gScore because we're just using distance as a heuristic.
        // I think this still __technically__ counts as A*. Maybe.

        // Create our open and closed sets
        ArrayList<Cell> closedSet = new ArrayList<>();
        ArrayList<Cell> openSet = new ArrayList<>();
        openSet.add(start);

        // And maps for the parents
        HashMap<Cell, Cell> parentMap = new HashMap<>();

        // While there exist known but unexplored cells...
        while (openSet.size() > 0) {
            // Find the cheapest unexplored cell to travel to
            Cell current = findCheapestCell(openSet, goal);

            // If that cell is the goal, reconstruct and return the path with our known information.
            if (current.equals(goal)) {
                return reconstructPath(parentMap, current);
            }

            // Mark the current cell as explored.
            openSet.remove(current);
            closedSet.add(current);

            // For each neighbor of the current cell...
            for (Cell neighbor : current.getNeighbors()) {
                // Ignore already explored neighbors
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                // Mark the neighbor as known, if it's not already
                // Otherwise, if it's known and has a score
                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }

                // Map the neighbor's parent as the "current" cell.
                parentMap.put(neighbor, current);
            }
        }

        // No path found :(
        return null;
    }

    private static double heuristic(Cell current, Cell goal) {
        // simply the distance from the goal. I think this is a consistent heuristic, but I'm not sure.
        // I should take more discrete math.
        return Math.hypot(current.x - goal.x, current.y - goal.y);
    }

    private static Cell findCheapestCell(ArrayList<Cell> cells, Cell goal) {
        Cell best = cells.get(0);
        for (Cell c : cells) {
            if (heuristic(c, goal) < heuristic(best, goal))
                best = c;
        }
        return best;
    }
}

public class Answer {
    static int[][] map;

    public static int answer(int[][] maze) {
        map = maze;
        Cell start = new Cell(0, 0);
        Cell goal = new Cell(maze[0].length - 1, maze.length - 1);

        // Set lowest path length to higher than the absolute worst path (hitting every cell + 1)
        int lowest = map.length * map[0].length + 1;

        // Find an initial path, avoiding walls.
        ArrayList<Cell> path = AStar.findPath(start, goal);
        if (path != null) {
            lowest = path.size();
        }
        // Here we could improve runtime by using a* with *heavily* weighted walls
        // (so we'll only break through the _one_ necessary wall).
        // Given that the above path == null, because that means the path is blocked.
        // But I'm running out of time and I'm too lazy, so I'll just leave it as a note.

        // For each potential wall to destroy
        for (Cell wall : getPassableWalls()) {
            // If the lowest is currently at the theoretical limit, just return it.
            if (lowest == map.length + map[0].length - 1) {
                return lowest;
            }

            // Find the shortest path from that wall to start, and to the goal
            ArrayList<Cell> pathToEnd = AStar.findPath(wall, goal);
            ArrayList<Cell> pathToStart = AStar.findPath(wall, start);

            // If both endpoints are reachable and the path length is the shortest yet, set it to `lowest`
            if (pathToEnd != null && pathToStart != null) {
                // -1 because we're counting the wall twice.
                int pathLength = pathToEnd.size() + pathToStart.size() - 1;

                if (pathLength < lowest) {
                    lowest = pathLength;
                }
            }
        }

        return lowest;
    }

    private static ArrayList<Cell> getPassableWalls() {
        // For each cell in the maze
        // if it's a wall with more than one neighboring hall
        // add it to the list of "passable" walls.
        ArrayList<Cell> walls = new ArrayList<>();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == 1) {
                    Cell wall = new Cell(x, y);
                    if (wall.getNeighbors().size() > 1) {
                        walls.add(wall);
                    }
                }
            }
        }

        return walls;
    }

    public static void main(String[] args) {
        int[][][] www = {
                {
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                }, {
                {0, 0, 0, 0, 0},
                {1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1},
                {0, 0, 0, 0, 0},
        }, {
                {0, 1, 0, 0, 0},
                {1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
        }, {
                {0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 1, 1, 0, 0},
                {0, 0, 1, 0, 0},
        }, {
                {0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 0, 1, 1},
                {0, 1, 1, 1, 0},
                {0, 0, 1, 1, 0},
        }, {
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        }, {
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        }
        };

        int[] wow = {9, 9, 9, 9, 11, 31, 33};

        for (int i = 0; i < www.length; i++) {
            int answer = answer(www[i]);
            System.out.println(wow[i] + "\t==\t" + answer + "\t| " + (wow[i] == answer));
        }
    }
}
