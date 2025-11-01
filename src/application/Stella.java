package application;

import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.image.Image;

public class Stella extends PhysicsObject {
	
	public Stella(int tileX, int tileY, double width, double height, int level) {
		super(tileX, tileY, width, height, level);
		property = Tiles.level + 1;
	}
	
	public int getType() {
    	return 1;
    }
	
	protected void loadImages() {
		images.add(new Image("file:resources/images/objects/Stella.png"));
	}
	
	public void update() {
		// Do nothing
	}
	
	public void nextLevel(ArrayList<PhysicsObject> objects, Player player) {
		try {
			Store.loadLevel(property, player, objects, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


