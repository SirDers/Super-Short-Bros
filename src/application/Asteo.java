package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Asteo extends Enemy {

	public Asteo(int tileX, int tileY) {
		super(tileX, tileY);
	}
	
	public Asteo(int tileX, int tileY, double WIDTH, double HEIGHT, double ACCELERATION_X, double ACCELERATION_Y, double MAX_SPEED_X, double MAX_SPEED_Y) {
    	super(tileX, tileY, WIDTH, HEIGHT, ACCELERATION_X, ACCELERATION_Y, MAX_SPEED_X, MAX_SPEED_Y);
	}
	
	@Override
	public int getType() {
		return 2;
	}
	
	@Override
	protected void loadImages() {
		images.add(new Image("file:resources/images/objects/enemies/asteo/asteo-1.png"));
		images.add(new Image("file:resources/images/objects/enemies/asteo/asteo-2.png"));
		images.add(new Image("file:resources/images/objects/enemies/asteo/asteo-3.png"));
		images.add(new Image("file:resources/images/objects/enemies/asteo/asteo-4.png"));
		images.add(new Image("file:resources/images/objects/enemies/asteo/asteo-squish.png"));
	}
	
	@Override
	protected void speedX() {
		super.speedX();
		frameSpeed = Math.abs(speedX) / 24;
    	if (frameSpeed < 0.1)
    		frameSpeed = 0.1;
    	spriteFrame += frameSpeed;
	}
	
	@Override
	public void draw(GraphicsContext gc, double cameraX, double cameraY) {
		if (isDead) {
			double drawX = x - width/2 - cameraX;
	        double drawY = y - height/2 - cameraY;
	        double scaleToWidth = images.get(4).getWidth()/64;
	    	double scaleToHeight = images.get(4).getHeight()/64;
	    	
	    	try {
	    		gc.drawImage(images.get(4), drawX + Tiles.SIZE*(1 - scaleToWidth)/2, drawY + Tiles.SIZE*(1 - scaleToHeight)/2, width*scaleToWidth, height*scaleToHeight);			
	    	} catch (Exception e) {
	        }
		} else {
			super.draw(gc, cameraX, cameraY);
		}
	}
}
