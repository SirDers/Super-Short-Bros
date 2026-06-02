package game.state;

import core.Main;
import game.Music;
import game.PhysicsObject;
import game.Player;
import game.Store;
import input.Controls;
import world.Tiles;

import java.io.IOException;
//import java.util.Scanner;
import java.util.ArrayList;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Editor {

    private static boolean devMode = true;
    private static boolean editMode = true;

    private static String notif = "";
    private static int frames = 0;
    private static boolean changed = false;

    public static void keyCheck(GraphicsContext gc, Player player, ArrayList<PhysicsObject> objects) {

        if (Controls.isPressed(KeyCode.MINUS)) {
            devMode = !devMode;
        }

        if (!devMode) {
            editMode = false;
            return;
        }

        // Handle notification time
        if (!notif.isEmpty() && !"Unsaved changes".equals(notif)) {
            frames += 1;
            if (frames > 180) {
                notif = "";
            }
        } else {
            frames = 0;
        }

        // Checks for changes
        if (changed) {
            changed = false;
            notif = "Unsaved changes";
        }

        // Toggle edit mode
        if (Controls.isPressed(KeyCode.DIGIT0)) {
            editMode = !editMode;
            if (editMode) {
                try {
                    Store.loadLevel(Tiles.getLevel(), player, objects, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Store.saveLevel(Tiles.getLevel(), objects, true);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Level failed to save!");
                }
            }
        }

        if (editMode) {
            // P: Saves level
            if (Controls.isPressed(KeyCode.P)) {
                try {
                    Store.saveLevel(Tiles.getLevel(), objects, false);
                    notif = "Saved level " + Tiles.getLevel() + "!";
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Level failed to save!");
                }
            }

            // T: Changes theme
            if (Controls.isPressed(KeyCode.T)) {
            if (Tiles.getTheme() < 4)
                Tiles.setTheme(Tiles.getTheme() + 1);
                else {
                    Tiles.setTheme(1);
                }
                Music.setMusic(Tiles.getTheme());
            }

            // Change Width and Height with arrows
            if (Controls.isPressed(KeyCode.LEFT)) {
                Tiles.setWidth(Tiles.getWidth() - 1);
                recreateGrid("LEFT");
            }
            if (Controls.isPressed(KeyCode.UP)) {
                Tiles.setHeight(Tiles.getHeight() + 1);
                recreateGrid("UP");
                for (PhysicsObject object : objects) {
                    object.setSpawn(object.getTileX(), object.getTileY() + 1);
                }
            }
            if (Controls.isPressed(KeyCode.RIGHT)) {
                Tiles.setWidth(Tiles.getWidth() + 1);
                recreateGrid("RIGHT");
            }
            if (Controls.isPressed(KeyCode.DOWN) && Tiles.getHeight() > 20) {
                Tiles.setHeight(Tiles.getHeight() - 1);
                recreateGrid("DOWN");
                for (PhysicsObject object : objects) {
                    object.setSpawn(object.getTileX(), object.getTileY() - 1);
                }
            }
        }
    }

    public static void render(GraphicsContext gc) {
        if (!devMode) return;

        // Edit Mode text
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);
        gc.setFill(Color.LIME);
        gc.setFont(Font.font(20));
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setTextBaseline(VPos.BOTTOM);

        gc.strokeText("Edit Mode", Main.getCanvasWidth() - 10, Main.getCanvasHeight() - 10);
        gc.fillText("Edit Mode", Main.getCanvasWidth() - 10, Main.getCanvasHeight() - 10);

        // Notification text
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(20));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.BOTTOM);

        gc.strokeText(notif, 10, Main.getCanvasHeight() - 10);
        gc.fillText(notif, 10, Main.getCanvasHeight() - 10);
    }

    private static void recreateGrid(String str) {
        int newGrid[][] = new int[Tiles.getWidth()][Tiles.getHeight()];

        switch(str) {
        case "LEFT":
            for (int i = 0; i < Tiles.getWidth(); i++) {
                for (int j = 0; j < Tiles.getGrid()[i].length; j++) {
                    newGrid[i][j] = Tiles.getGrid()[i][j];
                }
            }
            break;

        case "UP":
            for (int i = 0; i < Tiles.getGrid().length; i++) {
                for (int j = 0; j < Tiles.getGrid()[i].length; j++) {
                    newGrid[i][j+1] = Tiles.getGrid()[i][j];
                }
            }
            break;

        case "RIGHT":
            for (int i = 0; i < Tiles.getGrid().length; i++) {
                for (int j = 0; j < Tiles.getGrid()[i].length; j++) {
                    newGrid[i][j] = Tiles.getGrid()[i][j];
                }
            }
            break;

        case "DOWN":
            for (int i = 0; i < Tiles.getGrid().length; i++) {
                for (int j = 0; j < Tiles.getHeight(); j++) {
                    newGrid[i][j] = Tiles.getGrid()[i][j+1];
                }
            }
            break;
        }

        Tiles.setGrid(newGrid);
    }

    public static boolean isEditMode() {
        return editMode;
    }

    public static void setEditMode(boolean editMode) {
        Editor.editMode = editMode;
    }

    public static boolean isChanged() {
        return changed;
    }

    public static void setChanged(boolean changed) {
        Editor.changed = changed;
    }

    public static boolean isDevMode() {
        return devMode;
    }

    public static void setDevMode(boolean devMode) {
        Editor.devMode = devMode;
    }


}
