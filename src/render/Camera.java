package render;

import game.Player;
import world.Tiles;

import core.Main;

public class Camera {
    private static Camera active;
    private static double x;
    private static double y;
    private final double viewportWidth;
    private final double viewportHeight;

    public Camera(double viewportWidth, double viewportHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        active = this;
    }

    public void update(Player player, double alpha, double dt) {
        // Interpolate camera between player positions
        double playerX = player.getPrevX() * (1 - alpha) + player.getX() * alpha;
        double playerY = player.getPrevY() * (1 - alpha) + player.getY() * alpha;

        // Center of player x and center of viewportHeight
        double targetX = playerX - player.getWidth() - viewportWidth / 2;
        double targetY = playerY - viewportHeight / 2;

        // Camera smoothing
        double smoothSpeed = 12.0;
        double t = 1 - Math.exp(-smoothSpeed * dt);

        // Set camera position
        x = targetX;
        y += (targetY - y) * t;

        cameraEdge(0, 0);
    }

    public void reset(Player player) {
        x = player.getX() - player.getWidth() - viewportWidth / 2;
        y = player.getY() - viewportHeight / 2;
    }

    public static Camera getActive() {
        return active;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    private void cameraEdge(double edgeX, double edgeY) {
        if (x < edgeX) {
            x = edgeX;
        }
        if (x > (Tiles.getWidth()) * Tiles.getSize() - Main.getCanvasWidth() - edgeX) {
            x = (Tiles.getWidth()) * Tiles.getSize() - Main.getCanvasWidth() - edgeX;
        }
        if (y < edgeY) {
            y = edgeY;
        }
        if (y > (Tiles.getHeight()) * Tiles.getSize() - Main.getCanvasHeight() - edgeY) {
            y = (Tiles.getHeight()) * Tiles.getSize() - Main.getCanvasHeight() - edgeY;
        }
    }
}
