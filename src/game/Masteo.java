package game;

import javafx.scene.image.Image;

public class Masteo extends Asteo {

	public Masteo(int tileX, int tileY, double WIDTH, double HEIGHT, double ACCELERATION_X, double ACCELERATION_Y, double MAX_SPEED_X, double MAX_SPEED_Y) {
		super(tileX, tileY, WIDTH, HEIGHT, ACCELERATION_X, ACCELERATION_Y, MAX_SPEED_X, MAX_SPEED_Y);
		stompable = false;
	}

	@Override
	public int getType() {
		return 4;
	}
	
	@Override
	protected void loadImages() {
		images.add(new Image("file:resources/images/objects/enemies/masteo/masteo-1.png"));
		images.add(new Image("file:resources/images/objects/enemies/masteo/masteo-2.png"));
		images.add(new Image("file:resources/images/objects/enemies/masteo/masteo-3.png"));
		images.add(new Image("file:resources/images/objects/enemies/masteo/masteo-4.png"));
		images.add(new Image("file:resources/images/objects/enemies/masteo/asteo-squish.png"));
	}
}
