package application;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class Mouse {

	public static double x = 0;
	public static double y = 0;
	public static boolean isDown = false;
	
	public static void setup(Scene scene) {
		
		scene.addEventHandler(MouseEvent.ANY, event -> {
			x = event.getX();
			y = event.getY();
		});
		
		scene.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			isDown = true;
		});
		
		scene.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
			isDown = false;
		});
	}
}
