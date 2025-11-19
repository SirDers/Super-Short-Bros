package application;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controls {

	public static int left = 0;
	public static int right = 0;
	public static int up = 0;
	public static int down = 0;
	
	public static boolean run = false;
	public static boolean jump = false;
	
	public static int xAxis = 0;
	public static int yAxis = 0;
	

	public static boolean keyP = false;
	public static boolean keyQ = false;
	public static boolean keyT = false;
	public static boolean keyV = false;
	public static boolean keyL = false;
	
	public static boolean key0 = false;
	public static boolean key1 = false;
	public static boolean key2 = false;
	public static boolean key3 = false;
	public static boolean key4 = false;
	public static boolean key5 = false;
	public static boolean key6 = false;
	public static boolean key7 = false;
	public static boolean key8 = false;
	public static boolean key9 = false;

	public static boolean keyLEFT = false;
	public static boolean keyUP = false;
	public static boolean keyRIGHT = false;
	public static boolean keyDOWN = false;
	

	public static boolean keyMIN = false;
	

	public static boolean keyPpressed = false;
	public static boolean keyQpressed = false;
	public static boolean keyTpressed = false;
	public static boolean keyVpressed = false;
	public static boolean keyLpressed = false;
	
	public static boolean key0pressed = false;
	public static boolean key1pressed = false;
	public static boolean key2pressed = false;
	public static boolean key3pressed = false;
	public static boolean key4pressed = false;
	public static boolean key5pressed = false;
	public static boolean key6pressed = false;
	public static boolean key7pressed = false;
	public static boolean key8pressed = false;
	public static boolean key9pressed = false;

	public static boolean keyLEFTpressed = false;
	public static boolean keyUPpressed = false;
	public static boolean keyRIGHTpressed = false;
	public static boolean keyDOWNpressed = false;

	public static boolean keyMINpressed = false;
	

	private static boolean[] beingPressed = new boolean[36];
	
	
	private static final Set<KeyCode> pressedKeys = new HashSet<>();
	
	
	public static void checkControls() {
		left = (pressedKeys.contains(KeyCode.A) ? 1 : 0);
		right = (pressedKeys.contains(KeyCode.D) ? 1 : 0);
		up = (pressedKeys.contains(KeyCode.W) ? 1 : 0);
		down = (pressedKeys.contains(KeyCode.S) ? 1 : 0);
		run = pressedKeys.contains(KeyCode.J);
		jump = pressedKeys.contains(KeyCode.K);
		
		xAxis = right - left;
		yAxis = down - up;
		

		keyP = pressedKeys.contains(KeyCode.P);
		keyQ = pressedKeys.contains(KeyCode.Q);
		keyT = pressedKeys.contains(KeyCode.T);
		keyV = pressedKeys.contains(KeyCode.V);
		keyL = pressedKeys.contains(KeyCode.L);
		
		key0 = pressedKeys.contains(KeyCode.DIGIT0);
		key1 = pressedKeys.contains(KeyCode.DIGIT1);
		key2 = pressedKeys.contains(KeyCode.DIGIT2);
		key3 = pressedKeys.contains(KeyCode.DIGIT3);
		key4 = pressedKeys.contains(KeyCode.DIGIT4);
		key5 = pressedKeys.contains(KeyCode.DIGIT5);
		key6 = pressedKeys.contains(KeyCode.DIGIT6);
		key7 = pressedKeys.contains(KeyCode.DIGIT7);
		key8 = pressedKeys.contains(KeyCode.DIGIT8);
		key9 = pressedKeys.contains(KeyCode.DIGIT9);

		keyLEFT = pressedKeys.contains(KeyCode.LEFT);
		keyUP = pressedKeys.contains(KeyCode.UP);
		keyRIGHT = pressedKeys.contains(KeyCode.RIGHT);
		keyDOWN = pressedKeys.contains(KeyCode.DOWN);

		keyMIN = pressedKeys.contains(KeyCode.MINUS);
	}

	public static void checkPressed() {
		keyPpressed = pressed(keyP, 14);
		keyQpressed = pressed(keyQ, 15);
		keyTpressed = pressed(keyT, 16);
		keyVpressed = pressed(keyV, 17);
		keyLpressed = pressed(keyL, 18);

		key0pressed = pressed(key0, 0);
		key1pressed = pressed(key1, 1);
		key2pressed = pressed(key2, 2);
		key3pressed = pressed(key3, 3);
		key4pressed = pressed(key4, 4);
		key5pressed = pressed(key5, 5);
		key6pressed = pressed(key6, 6);
		key7pressed = pressed(key7, 7);
		key8pressed = pressed(key8, 8);
		key9pressed = pressed(key9, 9);

		keyLEFTpressed = pressed(keyLEFT, 10);
		keyUPpressed = pressed(keyUP, 11);
		keyRIGHTpressed = pressed(keyRIGHT, 12);
		keyDOWNpressed = pressed(keyDOWN, 13);

		keyMINpressed = pressed(keyMIN, 35);
	}

	public static void keyPressed(KeyCode key) {
        pressedKeys.add(key);
    }
	
	public static void keyReleased(KeyCode key) {
        pressedKeys.remove(key);
    }
	
	public static void setup(Scene scene) {
		scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> Controls.keyPressed(event.getCode()));
        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> Controls.keyReleased(event.getCode()));
	}
	
	private static boolean pressed(boolean key, int keyNum) {
		if (!key) {
			beingPressed[keyNum] = false;
			return false;
		}
		if (!beingPressed[keyNum]) {
			beingPressed[keyNum] = true;
			return true;
		}
		return false;
	}
}


