package application;

import java.util.ArrayList;

public abstract class Enemy extends PhysicsObject {

	protected boolean isDead = false;
	protected int deathFrames = 0;
	
	public Enemy(int tileX, int tileY) {
		super(tileX, tileY);
		stompable = true;
	}
	
	public Enemy(int tileX, int tileY, double WIDTH, double HEIGHT, double ACCELERATION_X, double ACCELERATION_Y, double MAX_SPEED_X, double MAX_SPEED_Y) {
    	super(tileX, tileY, WIDTH, HEIGHT, ACCELERATION_X, ACCELERATION_Y, MAX_SPEED_X, MAX_SPEED_Y);
    	stompable = true;
	}
	
	@Override
	public void update(ArrayList<PhysicsObject> objects) {
		if (isDead) {
			deathFrames += 1;
		} else {
			super.update(objects);
		}
	}
	
	@Override
	public void sensor(ArrayList<PhysicsObject> objects, ArrayList<PhysicsObject> toRemove) {
		super.sensor(objects, toRemove);
		if (deathFrames > 100) {
			super.death(objects, toRemove);
		}
	}
	
	@Override
	protected void death(ArrayList<PhysicsObject> objects, ArrayList<PhysicsObject> toRemove) {
		isDead = true;
		collisionOn = false;
	}
	
	@Override
	protected void speedX() {
		speedX = accelX;
	}
	
	@Override
	protected void onTileColX() {
        accelX *= -1;
	}
}
