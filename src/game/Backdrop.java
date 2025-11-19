package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

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
		
		double yPoint = (Tiles.HEIGHT - 16) * Tiles.SIZE;
		
		switch(type) {
		case 1:
	        gc.drawImage(images[Tiles.theme-1][2], -cameraX * parallax, (yPoint - cameraY) * parallax, Main.CANVAS_WIDTH, Main.CANVAS_HEIGHT);
	        break;
		case 2:
	        gc.drawImage(images[Tiles.theme-1][1], -cameraX * parallax, (yPoint - cameraY) * parallax, 64 * Tiles.SIZE, 18 * Tiles.SIZE);
	        break;
		case 3:
	        gc.drawImage(images[Tiles.theme-1][0], -cameraX * parallax, (yPoint - cameraY) * parallax, 64 * Tiles.SIZE, 18 * Tiles.SIZE);
	        break;
		}
	}
}
