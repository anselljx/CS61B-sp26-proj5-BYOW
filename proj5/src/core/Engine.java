package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Engine {
    private static final int S_WIDTH = 80;
    private static final int S_HEIGHT = 48;
    //higher than World_HEIGHT because HUD will be put on the column

    private static final Font TITLE_FONT =
            new Font("Monaco", Font.BOLD, 32);
    private static final Font MENU_FONT =
            new Font("Monaco", Font.PLAIN, 20);
    private static final Path SAVE_FILE = Path.of("save.txt");

    private World world;
    private TETile[][] worldMap;
    private InteractiveWorld game;
    private TERenderer renderer;


    private long currentSeed;
    private StringBuilder moveHistory = new StringBuilder();

    public Engine(){
        this.renderer = new TERenderer();
    }

    public void interactWithKeyboard() {
        renderer.initialize(S_WIDTH,S_HEIGHT);

        while (true) {
            showMenu();

            char key = waitForKey();

            if (key == 'Q') {
                return;
            }

            if (key == 'N') {
                Long seed = readSeed();

                if (seed == null) {
                    continue;
                }

                startGame(seed);
                playGame();
            }
        }
    }

    private void showMenu(){
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.setFont(TITLE_FONT);
        StdDraw.text(S_WIDTH / 2.0, 32, "MY WORLD");

        StdDraw.setFont(MENU_FONT);
        StdDraw.text(S_WIDTH / 2.0, 22, "New Game (N)");
        StdDraw.text(S_WIDTH / 2.0, 18, "Quit (Q)");

        StdDraw.show();
    }


    private Long readSeed() {
        StringBuilder input = new StringBuilder();

        drawSeedScreen(input);

        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(20);
                continue;
            }

            char key = Character.toUpperCase(StdDraw.nextKeyTyped());
            boolean changed = false;

            if (key == 'Q') {
                return null;
            }

            if (key == 'S') {
                if (input.length() == 0) {
                    continue;
                }

                try {
                    return Long.parseLong(input.toString());
                } catch (NumberFormatException e) {
                    input.setLength(0);
                    changed = true;
                }
            } else if (key == '\b' || key == 127) {
                if (input.length() > 0) {
                    input.deleteCharAt(input.length() - 1);
                    changed = true;
                }
            } else if (key >= '0' && key <= '9') {
                input.append(key);
                changed = true;
            }

            // Redraw only when the input changes.
            if (changed) {
                drawSeedScreen(input);
            }
        }
    }

    // Draw the seed input screen.
    private void drawSeedScreen(StringBuilder input) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.setFont(TITLE_FONT);
        StdDraw.text(S_WIDTH / 2.0, 31, "Enter Seed");

        StdDraw.setFont(MENU_FONT);
        StdDraw.text(S_WIDTH / 2.0, 23, input + "_");
        StdDraw.text(S_WIDTH / 2.0, 16, "Press S to start");
        StdDraw.text(S_WIDTH / 2.0, 12, "Press Q to cancel");

        StdDraw.show();
    }

    //start game!!!
    private void startGame(long seed) {
        currentSeed = seed;
        moveHistory = new StringBuilder();

        game = new InteractiveWorld(seed);
        renderer.renderFrame(game.getWorldMap());
    }

    //save data with .txt
    private void saveGame() {
        String data = currentSeed + "\n" + moveHistory;

        try {
            Files.writeString(SAVE_FILE, data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save game.", e);
        }
    }

    private boolean loadGame() {
        if (!Files.exists(SAVE_FILE)) {
            return false;
        }

        try {
            String[] data = Files.readString(SAVE_FILE).split("\n", -1);

            currentSeed = Long.parseLong(data[0].trim());
            moveHistory = new StringBuilder();

            game = new InteractiveWorld(currentSeed);

            if (data.length > 1) {
                String history = data[1].trim();

                for (int i = 0; i < history.length(); i++) {
                    char direction = history.charAt(i);

                    if (game.moveInDirection(direction)) {
                        moveHistory.append(direction);
                    }
                }
            }

            return true;
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Failed to load game.", e);
        }
    }

    private void playGame() {
        //here GPT helps me ensure smooth when the key is held
        long lastMoveTime = 0;
        final long MOVE_INTERVAL = 100;

        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(10);
                continue;
            }

            char key = Character.toUpperCase(StdDraw.nextKeyTyped());

            if (key == 'Q') {
                return;
            }

            long currentTime = System.currentTimeMillis();

            //limit the input frequency to make it smooth
            if (currentTime - lastMoveTime < MOVE_INTERVAL) {
                continue;
            }

            boolean moved = game.moveInDirection(key);
            if (moved) {
                renderer.renderFrame(game.getWorldMap());
                lastMoveTime = currentTime;
            }
        }
    }

    private char waitForKey() {
        while (!StdDraw.hasNextKeyTyped()) {
            StdDraw.pause(10);
        }

        return Character.toUpperCase(StdDraw.nextKeyTyped());
    }

    public TETile[][] interactWithInputString(String input) {
        int i = 0;

        while (i < input.length()) {
            char key = Character.toUpperCase(input.charAt(i));

            // Start a new game.
            if (key == 'N') {
                i++;

                StringBuilder seedInput = new StringBuilder();

                while (i < input.length()
                        && Character.toUpperCase(input.charAt(i)) != 'S') {
                    seedInput.append(input.charAt(i));
                    i++;
                }

                if (i == input.length()) {
                    throw new IllegalArgumentException("Seed must end with S.");
                }

                i++;

                long seed = Long.parseLong(seedInput.toString());
                startGame(seed);
            }

            // Load the previous game.
            else if (key == 'L') {
                loadGame();
                i++;
            }

            // Quit and save.
            else if (key == ':' && i + 1 < input.length()
                    && Character.toUpperCase(input.charAt(i + 1)) == 'Q') {
                if (game != null) {
                    saveGame();
                }
                break;
            }

            // Move the player.
            else if (key == 'W' || key == 'A'
                    || key == 'S' || key == 'D') {

                if (game != null && game.moveInDirection(key)) {
                    moveHistory.append(key);
                }

                i++;
            }

            else {
                i++;
            }
        }

        if (game == null) {
            return null;
        }
        return game.getWorldMap();
    }
}
