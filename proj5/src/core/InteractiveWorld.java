package core;

import tileengine.TETile;
import tileengine.Tileset;
import utils.IndexUtils;

public class InteractiveWorld {
    private long seed;
    private TETile[][] worldMap;;
    private int avatarX;
    private int avatarY;

    public InteractiveWorld(long seed){
        this.seed = seed;
        World generatedWorld = new World(seed);
        this.worldMap = generatedWorld.getWorldMap();

        placeAvatar();
    }

    private void placeAvatar() {
        for (int x = 0; x < worldMap.length; x++) {
            for (int y = 0; y < worldMap[x].length; y++) {
                if (worldMap[x][y] == Tileset.FLOOR) {
                    avatarX = x;
                    avatarY = y;
                    worldMap[x][y] = Tileset.AVATAR;
                    return;
                }
            }
        }
        throw new IllegalArgumentException("WHERE Should I be born???");
    }

    public boolean moveInDirection(char direction){
        int nextX = avatarX;
        int nextY = avatarY;

        switch (Character.toLowerCase(direction)){
            case 'w':
                nextY++;
                break;
            case 's':
                nextY--;
                break;
            case 'a':
                nextX--;
                break;
            case 'd':
                nextX++;
                break;
            default:
                return false;
        }

        if (!isValidMove(nextX, nextY)) {
            return false;
        }
        worldMap[avatarX][avatarY] = Tileset.FLOOR;

        avatarX = nextX;
        avatarY = nextY;

        worldMap[avatarX][avatarY] = Tileset.AVATAR;
        return true;
    }

    public boolean isValidMove(int nextX,int nextY){
        if (nextX < 0 || nextX >= worldMap.length
                || nextY < 0 || nextY >= worldMap[0].length) {
            return false;
        }

        if (worldMap[nextX][nextY] != Tileset.FLOOR) {
            return false;
        }
        return true;
    }

    public TETile[][] getWorldMap(){
        return this.worldMap;
    }

    public long getSeed(){
        return this.seed;
    }
}
