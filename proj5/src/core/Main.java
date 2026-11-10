package core;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.Hexagon;
import java.awt.image.ImageObserver;


public class Main {
    private static int S_WIDTH = 80;
    private static int S_HEIGHT = 48;

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.interactWithKeyboard();
    }
}
