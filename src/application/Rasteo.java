package application;

import javafx.scene.image.Image;

public class Rasteo extends Asteo {

	public Rasteo(int tileX, int tileY, double WIDTH, double HEIGHT, double ACCELERATION_X, double ACCELERATION_Y, double MAX_SPEED_X, double MAX_SPEED_Y) {
    	super(tileX, tileY, WIDTH, HEIGHT, ACCELERATION_X, ACCELERATION_Y, MAX_SPEED_X, MAX_SPEED_Y);
	}
	
	public int getType() {
		return 3;
	}
	
	protected void loadImages() {
		images.add(new Image("file:resources/images/objects/enemies/rasteo/rasteo-1.png"));
		images.add(new Image("file:resources/images/objects/enemies/rasteo/rasteo-2.png"));
		images.add(new Image("file:resources/images/objects/enemies/rasteo/rasteo-3.png"));
		images.add(new Image("file:resources/images/objects/enemies/rasteo/rasteo-4.png"));
		images.add(new Image("file:resources/images/objects/enemies/asteo/asteo-squish.png"));
	}
	
	protected void speedX() {
		super.speedX();
		frameSpeed = Math.abs(speedX) / 24;
    	if (frameSpeed < 0.1)
    		frameSpeed = 0.1;
    	spriteFrame += frameSpeed;
	}
}
