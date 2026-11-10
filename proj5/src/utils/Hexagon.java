package utils;


import tileengine.TETile;

import javax.sound.sampled.Line;

import static utils.DrawUtils.DrawHexagonLine;

public class Hexagon {
    private int size;
    private TETile biome;
    private int topLeftX;
    private int topLeftY;

    //my Hexagon design like this:
    //        ###
    //       #####
    //      #######
    //      #######           size = 3 ( topline = 3,height = 3*2)
    //       #####
    //        ###

    public Hexagon(int size,TETile biome,int topLeftX,int topLeftY){
        this.size = size;
        this.biome = biome;
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
    }

    public void addHexagon(TETile[][] world){
        //draw the top half
        for(int LineNum = 0;LineNum < size;LineNum++){
            DrawHexagonLine(topLeftX - LineNum,topLeftY - LineNum,size + LineNum * 2,this.biome,world);
        }

        //draw the bottom half
        int startX = topLeftX - size + 1;
        int startY = topLeftY - size;
        int startSize = size + (size - 1) * 2;
        for(int LineNum = 0;LineNum < size;LineNum++){
            DrawHexagonLine(startX + LineNum,startY - LineNum,startSize - LineNum * 2,this.biome,world);
        }
    }


}
