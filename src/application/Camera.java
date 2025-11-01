package application;

public class Camera {
    public static double x;
    public static double y;
    private final double viewportWidth;
    private final double viewportHeight;

    public Camera(double viewportWidth, double viewportHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
    }

    public void update(Player player) {
        x = player.getX() - player.width - viewportWidth / 2;
        y += (player.getY() - viewportHeight / 2 - y) / 2;
        //x = (x + Tiles.WIDTH * Tiles.SIZE) % (Tiles.WIDTH * Tiles.SIZE);
        //y = (y + Tiles.HEIGHT * Tiles.SIZE) % (Tiles.HEIGHT * Tiles.SIZE);
        
        cameraEdge(0, 0);
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
