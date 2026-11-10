package utils;

import core.RoomTile;
import tileengine.TETile;

public class DrawUtils {
    public static void DrawHexagonLine(int x, int y, int Linesize, TETile biome,TETile[][] world){
        for(int xOffset = 0;xOffset < Linesize;xOffset++){
            world[x + xOffset][y] = biome;
        }
    }

}
