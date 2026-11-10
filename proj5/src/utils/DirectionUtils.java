package utils;

import java.util.Random;

public class DirectionUtils {

    private DirectionUtils() {
    }

    public static int[] chooseDirection(
            int x, int y, int targetX, int targetY,
            int lastDX, int lastDY,
            int width, int height, Random random) {

        int dx = Integer.compare(targetX, x);
        int dy = Integer.compare(targetY, y);

        // Direction order: right, left, up, down.
        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

        double[] probabilities = new double[4];

        probabilities[0] = dx > 0 ? 0.4 : (dx < 0 ? 0.1 : 0.15);
        probabilities[1] = dx < 0 ? 0.4 : (dx > 0 ? 0.1 : 0.15);
        probabilities[2] = dy > 0 ? 0.4 : (dy < 0 ? 0.1 : 0.15);
        probabilities[3] = dy < 0 ? 0.4 : (dy > 0 ? 0.1 : 0.15);

        double total = 0;

        for (int i = 0; i < 4; i++) {
            int nextX = x + directions[i][0];
            int nextY = y + directions[i][1];

            // Cannot move backwards or outside the world.
            if ((directions[i][0] == -lastDX
                    && directions[i][1] == -lastDY)
                    || nextX < 1 || nextX >= width - 1
                    || nextY < 1 || nextY >= height - 1) {
                probabilities[i] = 0;
            }

            total += probabilities[i];
        }

        double choice = random.nextDouble() * total;

        for (int i = 0; i < 4; i++) {
            choice -= probabilities[i];

            if (choice < 0) {
                return directions[i];
            }
        }

        return new int[]{dx, dy};
    }
}