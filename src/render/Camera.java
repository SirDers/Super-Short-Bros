package render;

import game.Player;
import world.Tiles;

import core.Main;

public class Camera {
    public static double x;
    public static double y;
    private final double viewportWidth;
    private final double viewportHeight;

    public Camera(double viewportWidth, double viewportHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
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
    	if (x > (Tiles.WIDTH) * Tiles.SIZE - Main.CANVAS_WIDTH - edgeX) {
    		x = (Tiles.WIDTH) * Tiles.SIZE - Main.CANVAS_WIDTH - edgeX;
    	}
    	if (y < edgeY) {
    		y = edgeY;
    	}
    	if (y > (Tiles.HEIGHT) * Tiles.SIZE - Main.CANVAS_HEIGHT - edgeY) {
    		y = (Tiles.HEIGHT) * Tiles.SIZE - Main.CANVAS_HEIGHT - edgeY;
    	}
    }
}
