package application;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controls {

    private static final Set<KeyCode> heldKeys = new HashSet<>();
    private static final Set<KeyCode> pressedKeys = new HashSet<>();

    public static void keyPressed(KeyCode key) {
        if (!heldKeys.contains(key)) {
            pressedKeys.add(key);
        }
        heldKeys.add(key);
    }

    public static void keyReleased(KeyCode key) {
        heldKeys.remove(key);
        pressedKeys.remove(key);
    }

    public static void endFrame() {
        pressedKeys.clear();
    }

    public static boolean isHeld(KeyCode key) {
        return heldKeys.contains(key);
    }

    public static boolean isPressed(KeyCode key) {
        return pressedKeys.contains(key);
    }

    public static int xAxis() {
        int left  = heldKeys.contains(KeyCode.A) ? 1 : 0;
        int right = heldKeys.contains(KeyCode.D) ? 1 : 0;
        return right - left;
    }

    public static int yAxis() {
        int up    = heldKeys.contains(KeyCode.W) ? 1 : 0;
        int down  = heldKeys.contains(KeyCode.S) ? 1 : 0;
        return down - up;
    }

    public static boolean run() {
        return heldKeys.contains(KeyCode.J);
    }

    public static boolean jump() {
        return heldKeys.contains(KeyCode.K);
    }

    public static void setup(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> Controls.keyPressed(event.getCode()));
        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> Controls.keyReleased(event.getCode()));
    }
}


