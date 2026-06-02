package render;

import world.Tiles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import core.Main;

public class Backdrop {

    private Image images[][] = {{
                new Image("file:resources/images/backgrounds/layer0-1.png"),
                new Image("file:resources/images/backgrounds/layer1-1.png"),
                new Image("file:resources/images/backgrounds/sky1.png")
            },
            {
                new Image("file:resources/images/backgrounds/layer0-2.png"),
                new Image("file:resources/images/backgrounds/layer1-2.png"),
                new Image("file:resources/images/backgrounds/sky1.png")
            },
            {
                new Image("file:resources/images/backgrounds/layer0-3.png"),
                new Image("file:resources/images/backgrounds/layer1-3.png"),
                new Image("file:resources/images/backgrounds/sky3.png")
            },
            {
                new Image("file:resources/images/backgrounds/layerChess1.png"),
                new Image("file:resources/images/backgrounds/layerChess0.png"),
                new Image("file:resources/images/backgrounds/sky4.png")
            }
            };

    public void draw(GraphicsContext gc, double cameraX, double cameraY, double parallax, int type) {

        double yPoint = (Tiles.getHeight() - 16) * Tiles.getSize();

        switch(type) {
        case 1:
            gc.drawImage(images[Tiles.getTheme()-1][2], -cameraX * parallax, (yPoint - cameraY) * parallax, Main.getCanvasWidth(), Main.getCanvasHeight());
            break;
        case 2:
            gc.drawImage(images[Tiles.getTheme()-1][1], -cameraX * parallax, (yPoint - cameraY) * parallax, 64 * Tiles.getSize(), 18 * Tiles.getSize());
            break;
        case 3:
            gc.drawImage(images[Tiles.getTheme()-1][0], -cameraX * parallax, (yPoint - cameraY) * parallax, 64 * Tiles.getSize(), 18 * Tiles.getSize());
            break;
        }
    }
}
