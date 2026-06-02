package input;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class Mouse {

	private static double x = 0;
	private static double y = 0;
	private static boolean isDown = false;

	public static double getX() {
		return x;
	}

	public static double getY() {
		return y;
	}

	public static boolean isDown() {
		return isDown;
	}
	
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
