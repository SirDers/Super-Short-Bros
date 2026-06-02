package render;

import core.Main;
import game.GameWorld;
import game.PhysicsObject;
import game.Player;
import game.state.Editor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import world.Tiles;

public class GameRenderer {
    private final GraphicsContext gc;
    private final Camera camera;
    private final Backdrop backdrop;
    private final Backdrop layer0;
    private final Backdrop layer1;
    private final TextField textField;
    private final int tileCountX;
    private final int tileCountY;

    public GameRenderer(GraphicsContext gc, Camera camera, Backdrop backdrop, Backdrop layer0, Backdrop layer1,
                        TextField textField, int tileCountX, int tileCountY) {
        this.gc = gc;
        this.camera = camera;
        this.backdrop = backdrop;
        this.layer0 = layer0;
        this.layer1 = layer1;
        this.textField = textField;
        this.tileCountX = tileCountX;
        this.tileCountY = tileCountY;
    }

    public void render(GameWorld world, double alpha) {
        clearSky();
        drawBackgrounds();
        darkenBackground();
        drawWorld(world, alpha);
        Editor.render(gc);
    }

    private void clearSky() {
        gc.setFill(Color.rgb(100, 200, 250));
        gc.fillRect(0, 0, Main.getCanvasWidth(), Main.getCanvasHeight());
    }

    private void drawBackgrounds() {
        backdrop.draw(gc, camera.getX(), camera.getY(), 0, 1);
        layer1.draw(gc, camera.getX(), camera.getY(), 0.2, 2);
        layer0.draw(gc, camera.getX(), camera.getY() - 80, 0.4, 3);

        // Temporary loop; preserves existing wrap behavior.
        layer1.draw(gc, camera.getX() - 64 / 0.2 * Tiles.getSize(), camera.getY(), 0.2, 2);
        layer0.draw(gc, camera.getX() - 64 / 0.4 * Tiles.getSize(), camera.getY() - 80, 0.4, 3);
    }

    private void darkenBackground() {
        gc.setFill(Color.rgb(0, 0, 0, 0.2));
        if (Tiles.getTheme() == 4) gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillRect(0, 0, Main.getCanvasWidth(), Main.getCanvasHeight());
    }

    private void drawWorld(GameWorld world, double alpha) {
        Player player = world.getPlayer();
        Tiles.render(gc, textField, player, world.getObjects(), camera.getX(), camera.getY(), tileCountX, tileCountY);
        Tiles.renderEdit(gc, player);

        for (PhysicsObject object : world.getObjects()) {
            object.draw(gc, camera.getX(), camera.getY(), alpha);
        }
        player.draw(gc, camera.getX(), camera.getY(), alpha);
    }
}
