package core;

import tileengine.TETile;
import tileengine.Tileset;
import utils.IndexUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class RoomTile {
    //this class is just for expanding room tiles

    private static final int MIN_ROOMTILES = 30;
    private static final int MAX_ROOMTILES = 50;

    private static final TETile ROOM_BIOME = Tileset.FLOOR;

    // Four possible expansion directions.
    // left, right, up, down
    private static final int[][] DIRECTIONS = {
        {-1, 0},
        {1, 0},
        {0, 1},
        {0, -1}
    };

    //expand from the mid
    private IndexUtils midRoom;
    private int tilesCounts;
    private Random random;

    // Constructor
    public RoomTile(int midX, int midY) {
        this(midX, midY, new Random());
    }

    // Constructor with external Random.
    // WorldGenerator can pass the same Random instance to all generators.
    public RoomTile(int midX, int midY, Random random) {
        this.midRoom = new IndexUtils(midX, midY);
        this.tilesCounts = 0;
        this.random = random;
    }

    public int getMidX() {
        return this.midRoom.getX();
    }

    public int getMidY() {
        return this.midRoom.getY();
    }

    public int getTilesCounts() {
        return this.tilesCounts;
    }

    // Generate an irregular room using BFS.
    public void generateRoom(TETile[][] world) {

        // Reset count in case this method is called again.
        tilesCounts = 0;

        //random amounts of room tiles
        int tilesTotal = random.nextInt(
            MIN_ROOMTILES, MAX_ROOMTILES + 1
        );

        // The center position must be empty.
        if (!canPlace(getMidX(), getMidY(), world)) {
            return;
        }

        //I choose queue as expand structure
        Queue<IndexUtils> queue = new LinkedList<>();

        // Place the initial floor tile.
        world[getMidX()][getMidY()] = ROOM_BIOME;
        tilesCounts++;

        queue.add(midRoom);

        // BFS expansion.
        while (!queue.isEmpty() && tilesCounts < tilesTotal) {

            IndexUtils current = queue.remove();

            int curX = current.getX();
            int curY = current.getY();

            //expand in random direction
            ArrayList<int[]> directions = new ArrayList<>();
            Collections.addAll(directions, DIRECTIONS);
            Collections.shuffle(directions, random);

            // Try expanding in all four directions.
            for (int[] direction : directions) {

                if (tilesCounts >= tilesTotal) {
                    break;
                }

                int nextX = curX + direction[0];
                int nextY = curY + direction[1];

                if (!canPlace(nextX, nextY, world)) {
                    continue;
                }

                // Count adjacent floor tiles 
                // to make the room look "square" not "slim"
                int adjacentCount = countAdjacentRoomTiles(
                    nextX, nextY, world
                );

                
                double probability;

                if (tilesCounts < MIN_ROOMTILES) {
                    // Ensure the room grows to at least its minimum size
                    // whenever enough space is available.
                    probability = 1.0;
                } else if (adjacentCount >= 3) {
                    probability = 0.95;
                } else if (adjacentCount == 2) {
                    probability = 0.70;
                } else {
                    probability = 0.45;
                }

                // Decide whether to place the new floor tile.
                if (random.nextDouble() < probability) {
                    world[nextX][nextY] = ROOM_BIOME;
                    tilesCounts++;
                    queue.add(new IndexUtils(nextX, nextY));
                }
            }
        }
    }

    // Check whether a position can be used for room expansion.
    private boolean canPlace(int x, int y, TETile[][] world) {

        // Keep the outermost world boundary empty.
        if (x <= 0 || x >= world.length - 1
                || y <= 0 || y >= world[0].length - 1) {
            return false;
        }

        // Only NOTHING can be replaced by room floor.
        return world[x][y] == Tileset.NOTHING;
    }

    // Count the number of adjacent floor tiles.
    private int countAdjacentRoomTiles(
            int x, int y, TETile[][] world) {
        int count = 0;

        for (int[] direction : DIRECTIONS) {

            int nextX = x + direction[0];
            int nextY = y + direction[1];

            if (nextX >= 0 && nextX < world.length
                    && nextY >= 0 && nextY < world[0].length
                    && world[nextX][nextY] == ROOM_BIOME) {
                count++;
            }
        }

        return count;
    }
}
