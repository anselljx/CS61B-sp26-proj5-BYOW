
package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import tileengine.TETile;
import tileengine.Tileset;
import utils.IndexUtils;
import utils.DirectionUtils;

public class World {

    private static final int WORLD_WIDTH = 80;
    private static final int WORLD_HEIGHT = 45;

    static final int REGION_W = 10;
    static final int REGION_H = 9;
    static final int REGION_COLS = 8;
    static final int REGION_ROWS = 5;
    static final int SEED_MARGIN = 3;

    private final long SEED;
    private final Random RANDOM;

    private TETile[][] worldMap;
    private int regionAmount;
    private boolean[][] connections;
    private Region[][] regionGraph;

    public World(long seed) {
        this.SEED = seed;
        this.regionAmount = REGION_COLS * REGION_ROWS;
        this.RANDOM = new Random(seed);
        this.regionGraph = new Region[REGION_ROWS][REGION_COLS];

        makeEmptyWorld();
        divideRegions();
        connections = new boolean[regionAmount][regionAmount];

        generateRooms();
        generateHallways();
        drawHallways();

        buildWalls();
    }

    // Initialize an empty world.
    private void makeEmptyWorld() {
        worldMap = new TETile[WORLD_WIDTH][WORLD_HEIGHT];

        for (int x = 0; x < WORLD_WIDTH; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                worldMap[x][y] = Tileset.NOTHING;
            }
        }
    }

    // Divide the world into columns and rows.
    private void divideRegions() {
        for (int row = 0; row < REGION_ROWS; row++) {
            for (int col = 0; col < REGION_COLS; col++) {
                regionGraph[row][col] = new Region(row, col,RANDOM);
            }
        }
    }


    // Generate one room in each region.
    private void generateRooms() {
        for (int row = 0; row < REGION_ROWS; row++) {
            for (int col = 0; col < REGION_COLS; col++) {
                regionGraph[row][col].roomTile.generateRoom(worldMap);
            }
        }
    }

    //plan hallways
    //GPT helps me a lot here , so difficult
    private void generateHallways() {
        ArrayList<Region> frontier = new ArrayList<>();

        // Choose a random starting room.
        int row = RANDOM.nextInt(REGION_ROWS);
        int col = RANDOM.nextInt(REGION_COLS);

        Region start = regionGraph[row][col];
        frontier.add(start);
        start.visited = true;

        int connected = 1;
        int total = REGION_COLS*REGION_ROWS;

        // Grow the connection graph through adjacent regions.
        while (connected < total) {
            int index = RANDOM.nextInt(frontier.size());
            Region current = frontier.get(index);

            ArrayList<Region> neighbors = current.getUnvisitedNeighbor(regionGraph);

            // No unvisited neighbor: remove this frontier position.
            if (neighbors.isEmpty()) {
                frontier.remove(index);
                continue;
            }

            Region next = neighbors.get(RANDOM.nextInt(neighbors.size()));

            connections[current.id][next.id] = true;
            connections[next.id][current.id] = true;

            next.visited = true;
            frontier.add(next);
            connected++;
        }
    }

    // Draw corridors according to the connection plan.
    private void drawHallways() {
        for (int i = 0; i < regionAmount; i++) {
            for (int j = i + 1; j < regionAmount; j++) {
                if (connections[i][j]) {
                    IndexUtils start = regionGraph[i / REGION_COLS][i % REGION_COLS].mid;
                    IndexUtils end = regionGraph[j / REGION_COLS][j % REGION_COLS].mid;
                    connectRooms(start, end);
                }
            }
        }
    }

    // Connect two room centers with a random corridor.
    private void connectRooms(IndexUtils start, IndexUtils end) {
        int startX = start.getX();
        int startY = start.getY();
        int targetX = end.getX();
        int targetY = end.getY();

        int x = startX;
        int y = startY;

        // Same horizontal or vertical line: connect directly.
        if (startX == targetX || startY == targetY) {
            while (x != targetX) {
                worldMap[x][y] = Tileset.FLOOR;
                x += Integer.compare(targetX, x);
            }

            while (y != targetY) {
                worldMap[x][y] = Tileset.FLOOR;
                y += Integer.compare(targetY, y);
            }

            worldMap[targetX][targetY] = Tileset.FLOOR;
            return;
        }

        int lastDX = 0;
        int lastDY = 0;
        int steps = 0;

        while ((x != targetX || y != targetY) && steps < WORLD_HEIGHT*WORLD_WIDTH) {
            worldMap[x][y] = Tileset.FLOOR;

            int[] direction = DirectionUtils.chooseDirection(
                    x, y, targetX, targetY,
                    lastDX, lastDY,
                    WORLD_WIDTH, WORLD_HEIGHT, RANDOM
            );

            int nextX = x + direction[0];
            int nextY = y + direction[1];

            if (nextX < 0 || nextX >= WORLD_WIDTH
                    || nextY < 0 || nextY >= WORLD_HEIGHT) {
                break;
            }

            lastDX = direction[0];
            lastDY = direction[1];

            x = nextX;
            y = nextY;
            steps++;
        }

        // Finish the corridor if random movement did not reach the target.
        while (x != targetX) {
            worldMap[x][y] = Tileset.FLOOR;
            x += Integer.compare(targetX, x);
        }

        while (y != targetY) {
            worldMap[x][y] = Tileset.FLOOR;
            y += Integer.compare(targetY, y);
        }

        worldMap[targetX][targetY] = Tileset.FLOOR;
    }

    // Generate walls around all floor tiles.
    private void buildWalls() {
        for (int x = 1; x < WORLD_WIDTH - 1; x++) {
            for (int y = 1; y < WORLD_HEIGHT - 1; y++) {

                if (worldMap[x][y] != Tileset.FLOOR) {
                    continue;
                }

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {

                        int nx = x + dx;
                        int ny = y + dy;

                        if (worldMap[nx][ny] == Tileset.NOTHING) {
                            worldMap[nx][ny] = Tileset.WALL;
                        }
                    }
                }
            }
        }
    }

    // Return the generated world.
    public TETile[][] getWorldMap() {
        return worldMap;
    }
}
