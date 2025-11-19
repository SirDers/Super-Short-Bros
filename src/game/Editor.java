package game;

import java.awt.TextField;
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
	public static boolean editMode = true;
	
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
		
		//Scanner scr = new Scanner(System.in);
		TextField numberInput = new TextField();
        numberInput.setText("Type a number: ");
		
		// Handle notification time
		if (notif != "" && notif != "Unsaved changes") {
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
    				Store.loadLevel(Tiles.level, player, objects, true);
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	} else {
        		try {
    				Store.saveLevel(Tiles.level, objects, true);
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
					Store.saveLevel(Tiles.level, objects, false);
    				notif = "Saved level " + Tiles.level + "!";
    			} catch (IOException e) {
    				e.printStackTrace();
    				System.out.println("Level failed to save!");
    			}
        	}
    		
    		// T: Changes theme
    		if (Controls.isPressed(KeyCode.T)) {
    			if (Tiles.theme < 4)
    				Tiles.theme += 1;
    			else {
    				Tiles.theme = 1;
    			}
    			Music.setMusic(Tiles.theme);
        	}
    		
    		// Change Width and Height with arrows
    		if (Controls.isPressed(KeyCode.LEFT)) {
    			Tiles.WIDTH--;
    			recreateGrid("LEFT");
        	}
    		if (Controls.isPressed(KeyCode.UP)) {
    			Tiles.HEIGHT++;
    			recreateGrid("UP");
    			for (PhysicsObject object : objects) {
    				object.setSpawn(object.tileX, object.tileY + 1);
    			}
        	}
    		if (Controls.isPressed(KeyCode.RIGHT)) {
    			Tiles.WIDTH++;
    			recreateGrid("RIGHT");
        	}
    		if (Controls.isPressed(KeyCode.DOWN) && Tiles.HEIGHT > 20) {
    			Tiles.HEIGHT--;
    			recreateGrid("DOWN");
    			for (PhysicsObject object : objects) {
    				object.setSpawn(object.tileX, object.tileY - 1);
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
				
		gc.strokeText("Edit Mode", Main.CANVAS_WIDTH - 10, Main.CANVAS_HEIGHT - 10);
		gc.fillText("Edit Mode", Main.CANVAS_WIDTH - 10, Main.CANVAS_HEIGHT - 10);
		
		// Notification text
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(4);
    	gc.setFill(Color.WHITE);
		gc.setFont(Font.font(20));
		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.BOTTOM);
		
        gc.strokeText(notif, 10, Main.CANVAS_HEIGHT - 10);
        gc.fillText(notif, 10, Main.CANVAS_HEIGHT - 10);
	}
	
	private static void recreateGrid(String str) {
		int newGrid[][] = new int[Tiles.WIDTH][Tiles.HEIGHT];
		
		switch(str) {
		case "LEFT":
			for (int i = 0; i < Tiles.WIDTH; i++) {
				for (int j = 0; j < Tiles.grid[i].length; j++) {
					newGrid[i][j] = Tiles.grid[i][j];
		        }
	        }
			break;
			
		case "UP":
			for (int i = 0; i < Tiles.grid.length; i++) {
				for (int j = 0; j < Tiles.grid[i].length; j++) {
					newGrid[i][j+1] = Tiles.grid[i][j];
		        }
	        }
			break;
			
		case "RIGHT":
			for (int i = 0; i < Tiles.grid.length; i++) {
				for (int j = 0; j < Tiles.grid[i].length; j++) {
					newGrid[i][j] = Tiles.grid[i][j];
		        }
	        }
			break;
			
		case "DOWN":
			for (int i = 0; i < Tiles.grid.length; i++) {
				for (int j = 0; j < Tiles.HEIGHT; j++) {
					newGrid[i][j] = Tiles.grid[i][j+1];
		        }
	        }
			break;
		}

		Tiles.grid = newGrid;
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
