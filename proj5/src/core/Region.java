package core;

import utils.IndexUtils;
import java.util.ArrayList;
import java.util.Random;

import static core.World.*;


public class Region {

        public int row;
        public int col;
        public int id;

        public int xStart;
        public int xEnd;
        public int yStart;
        public int yEnd;

        public IndexUtils mid;
        public RoomTile roomTile;
        public boolean visited;


        public Region(int row,int col,Random random) {
                this.row = row;
                this.col = col;
                this.id = row * REGION_COLS + col;

                this.xStart = col * REGION_W;
                this.xEnd = xStart + REGION_W;
                this.yStart = row * REGION_H;
                this.yEnd = yStart + REGION_H;

                this.mid =  new IndexUtils((random.nextInt(
                        xStart + SEED_MARGIN,
                        xEnd - SEED_MARGIN)),(random.nextInt(
                        yStart + SEED_MARGIN,
                        yEnd - SEED_MARGIN)))
                ;

                this.roomTile = new RoomTile(mid.getX(),mid.getY(),random);
                this.visited = false;
        }

        public ArrayList<Region> getUnvisitedNeighbor(Region[][] Graph){
                ArrayList<Region> neighbors = new ArrayList<>();

                if (row > 0 && !Graph[row - 1][col].visited) {
                        neighbors.add(Graph[row - 1][col]);
                }
                if (row < REGION_ROWS - 1 && !Graph[row + 1][col].visited) {
                        neighbors.add(Graph[row + 1][col]);
                }
                if (col > 0 && !Graph[row][col - 1].visited) {
                        neighbors.add(Graph[row][col - 1]);
                }
                if (col < REGION_COLS - 1 && !Graph[row][col + 1].visited) {
                        neighbors.add(Graph[row][col + 1]);
                }

                return neighbors;
        }

}
