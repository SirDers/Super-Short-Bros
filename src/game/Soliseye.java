package game;

import javafx.scene.image.Image;

public class Soliseye extends Enemy {

	protected double waveSpeed = Math.PI/60;
	protected double amplitude;
	
	public Soliseye(int tileX, int tileY, double WIDTH, double HEIGHT, double ACCELERATION_X, double ACCELERATION_Y, double MAX_SPEED_X, double MAX_SPEED_Y) {
		super(tileX, tileY, WIDTH, HEIGHT, ACCELERATION_X, ACCELERATION_Y, MAX_SPEED_X, MAX_SPEED_Y);
		stompable = false;
		amplitude = ACCELERATION_Y;
	}

	@Override
	protected void loadImages() {
		images.add(new Image("file:resources/images/objects/enemies/soliseye.png"));
	}

	@Override
	public int getType() {
		return 5;
	}

	@Override
	protected void speedX() {
		// Do nothing
	}
	
	@Override
	protected void speedY() {
		speedY = amplitude * Math.cos(frames*waveSpeed);
	}
}
