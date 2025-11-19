package game;

/*
 * Tile Types:
 * 0: Air
 * 1, 2: Plain
 * 3-8: Grass
 * 9: Spawn
 * 10: Stella
 * 11: Asteo
 * */

import java.util.ArrayList;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Tiles {
    public static int SIZE = 40; // Tile size in pixels
    public static int WIDTH = 128; // Number of tiles horizontally in the level
    public static int HEIGHT = 40; // Number of tiles vertically in the level

    public static int level;
    public static int[][] grid;
    
    public static int theme;

    // Edit tile type
    private static int brush = 1;
    private static int editSet = 0;

    // Edit tile position
    private static double drawEditTileX = 0;
    private static double drawEditTileY = 0;
    
    // Edit group
    private static int[][] tileKeyMap = {
    		{
	    		9,
	    		1, 1,
	    		2, 2, 2, 2, 2, 2,
	    		3,
	    		4,
	    		5, 
	    		0,
	    		5, 5,
	    		6,
	    		7, 7, 7, 7, 7, 7, 7, 7, 7,
	    		8, 8, 8, 8
    		},
    		{
    			2, 2, 2, 2, 2, 2, 2, 2, 2, 2
    		}
    		};

	private static int[] objectMap = {
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			1,
			2,
			0,
			3,
			4,
			5,
			0, 0, 0, 0, 0, 0, 0, 0, 0
	};

    // Tile list
    private static Image[] tileImage = new Image[] {
    		new Image("file:resources/images/tiles/eraser.png"),
    	    new Image("file:resources/images/tiles/plain-brown.png"),
    	    new Image("file:resources/images/tiles/plain-purple.png"),
    	    new Image("file:resources/images/tiles/grass-1.png"),
    	    new Image("file:resources/images/tiles/grass-2.png"),
    	    new Image("file:resources/images/tiles/grass-3.png"),
    	    new Image("file:resources/images/tiles/grass-4.png"),
    	    new Image("file:resources/images/tiles/grass-5.png"),
    	    new Image("file:resources/images/tiles/grass-6.png"),
    	    new Image("file:resources/images/objects/player/walk1.png"),
    	    new Image("file:resources/images/objects/stella.png"),
    	    new Image("file:resources/images/objects/enemies/asteo/asteo-1.png"),
    		new Image("file:resources/images/tiles/eraseObjects.png"),
    	    new Image("file:resources/images/objects/enemies/rasteo/rasteo-1.png"),
    	    new Image("file:resources/images/objects/enemies/masteo/masteo-1.png"),
    	    new Image("file:resources/images/objects/enemies/soliseye.png"),
    	    new Image("file:resources/images/tiles/future-1.png"),
    	    new Image("file:resources/images/tiles/future-2.png"),
    	    new Image("file:resources/images/tiles/future-3.png"),
    	    new Image("file:resources/images/tiles/future-4.png"),
    	    new Image("file:resources/images/tiles/future-5.png"),
    	    new Image("file:resources/images/tiles/future-6.png"),
    	    new Image("file:resources/images/tiles/future-7.png"),
    	    new Image("file:resources/images/tiles/future-8.png"),
    	    new Image("file:resources/images/tiles/future-9.png"),
    	    new Image("file:resources/images/tiles/spike-1.png"),
    	    new Image("file:resources/images/tiles/spike-2.png"),
    	    new Image("file:resources/images/tiles/spike-3.png"),
    	    new Image("file:resources/images/tiles/spike-4.png"),
    	    new Image("file:resources/images/tiles/grass2-1.png"),
    	    new Image("file:resources/images/tiles/grass2-2.png"),
    	    new Image("file:resources/images/tiles/grass2-3.png"),
    	    new Image("file:resources/images/tiles/grass2-4.png"),
    	    new Image("file:resources/images/tiles/grass2-5.png"),
    	    new Image("file:resources/images/tiles/grass2-6.png"),
    	    new Image("file:resources/images/tiles/grass2-7.png"),
    	    new Image("file:resources/images/tiles/grass2-8.png"),
    	    new Image("file:resources/images/tiles/grass2-9.png"),
    	    new Image("file:resources/images/tiles/grass2-mg.png"),
    	    new Image("file:resources/images/tiles/objectEdit.png"),
    };
    
    
    
    public static void genNew(Player player, ArrayList<PhysicsObject> objects) {
    	// Set width and height
    	WIDTH = 100;
    	HEIGHT = 20;

        grid = new int[WIDTH][HEIGHT];
        
        // Remove objects
        objects.clear();
    	
    	// Set spawn
    	grid[(int) player.spawnX/SIZE][(int) player.spawnY/SIZE + 1] = 9;

    	// Generate starting ground
    	for (int i = 0 ; i < 7 ; i++) {
    		grid[i][HEIGHT - 1] = 4;
    		grid[i][HEIGHT - 2] = 7;
    	}
    	grid[7][HEIGHT - 1] = 5;
		grid[7][HEIGHT - 2] = 8;
    }

    
    public static void render(GraphicsContext gc, TextField tf, Player player, ArrayList<PhysicsObject> objects, double cameraX, double cameraY, int tileCountX, int tileCountY) {
        int startX = (int) (cameraX / SIZE);
        int startY = (int) (cameraY / SIZE);

        for (int i = 0; i < tileCountX; i++) {
            for (int j = 0; j < tileCountY; j++) {
                int gridX = (startX + i) % WIDTH;
                int gridY = (startY + j) % HEIGHT;
                
                if (gridX < 0)
                    gridX += 50;
                if (gridY < 0)
                    gridY += 50;

                double drawX = i * SIZE - (cameraX % SIZE);
                double drawY = j * SIZE - (cameraY % SIZE);
                
                if (Editor.editMode) {
                    gc.setStroke(Color.rgb(220, 220, 220, 0.1));
                    gc.setLineWidth(2);
                    gc.strokeRect(drawX, drawY, SIZE, SIZE);
                }
                
                drawTile(gc, player, grid[gridX][gridY], drawX, drawY, false);
                
            }
        }
    }

    public static void renderEdit(GraphicsContext gc, Player player) {
        if (!Editor.editMode) return;

        drawTile(gc, player, brush, drawEditTileX, drawEditTileY, true);

        drawEditBox(gc, 16, 8, 0); // Edit Mode
        switch (editSet) {
            case 1:
                drawEditBox(gc, 1, 0, 30); // Plain Orange tiles
                break;
            default: // 0
                drawEditBox(gc, 0, 0, 1); // Plain tiles
                drawEditBox(gc, 1, 0, 6); // Grass tiles
                drawEditBox(gc, 2, 0, 9); // Spawn tile
                drawEditBox(gc, 3, 0, 10); // Stella
                drawEditBox(gc, 4, 0, 11); // Asteos
                drawEditBox(gc, 5, 0, 15); // Soliseye
                drawEditBox(gc, 6, 0, 16); // Future tiles
                drawEditBox(gc, 7, 0, 25); // Spike tiles
                break;
        }

        drawEditBox(gc, 8, 0, 39); // Edit Set
        drawEditBox(gc, 0, 1, 0); // Eraser
    }
    
    private static void drawTile(GraphicsContext gc, Player player, int index, double drawX, double drawY, boolean isEdit) {
    	if (index == 9) {
    		if (Editor.editMode)
    			gc.drawImage(tileImage[index], drawX + Tiles.SIZE*(0.375 - 1*1.25) + SIZE/2 - player.width/2, drawY - Tiles.SIZE/2.1 - player.height + Tiles.SIZE, SIZE*2.5, SIZE*2.5);
    		return;
    	}
    	if (index != 0 || isEdit)
    		gc.drawImage(tileImage[index], drawX, drawY, SIZE, SIZE);
    }

    public static void editor(GraphicsContext gc, TextField tf, Player player, ArrayList<PhysicsObject> objects) {
    	if (Editor.editMode) {
            getBrush();
    		
        	int tileX = (int) (Math.floor((Mouse.x + Camera.x) / SIZE));
        	int tileY = (int) (Math.floor((Mouse.y + Camera.y) / SIZE));
        	
        	// Editing Tile:
        	double offsetX = Camera.x % SIZE;
        	double offsetY = Camera.y % SIZE;
        	drawEditTileX = (int) ((Mouse.x + offsetX) / SIZE) * SIZE - offsetX;
            drawEditTileY = (int) ((Mouse.y + offsetY) / SIZE) * SIZE - offsetY;
        	
        	// Edit level:
        	if (Mouse.isDown) {
        		editLevel(objects, player, tileX, tileY);
        		Editor.setChanged(true);
        	}
        }
    }
    
    private static void editLevel(ArrayList<PhysicsObject> objects, Player player, int tileX, int tileY) {
    	
    	if (brush == 10 || brush == 11 || brush == 13 || brush == 14 || brush == 15)  {
			// Places objects
			
			// Checks for overlap
			boolean canPlace = true;
			for (PhysicsObject checkObject : objects) {
				if (tileX == checkObject.tileX && tileY == checkObject.tileY) {
					canPlace = false;
					break;
				}
			}
			// Place object if no overlap
			if (canPlace) {
				PhysicsObject object = ObjectFactory.createObject(objectMap[brush], tileX, tileY, 0);
				objects.add(object);
			}
		} else if (brush == 12) {
			// Erases objects
			for (PhysicsObject checkObject : objects) {
				if (tileX == checkObject.tileX && tileY == checkObject.tileY) {
					objects.remove(checkObject);
					break;
				}
			}
		} else if (brush == 14) {
			// Edit objects
			for (PhysicsObject object : objects) {
				if (tileX == object.tileX && tileY == object.tileY) {
					// Change object property
				}
			}
		} else if (brush == 15) {
			// Edit tiles
			for (PhysicsObject object : objects) {
				if (tileX == object.tileX && tileY == object.tileY) {
					// Change tile property
				}
			}
		} else {
			// Places tiles
    		if (brush == 9) {
    			delPrevSpawn(player, grid, 9);
    		}
    		grid[tileX][tileY] = brush;
		}
    }
    
    private static void getBrush() {
    	if (Controls.isPressed(KeyCode.DIGIT1)) {
    		nextBrush(1);
    	}
    	if (Controls.isPressed(KeyCode.DIGIT2)) {
    		nextBrush(2);
    	}
    	if (Controls.isPressed(KeyCode.DIGIT3)) {
    		nextBrush(3);
    	}
    	if (Controls.isPressed(KeyCode.DIGIT4)) {
    		nextBrush(4);
    	}
    	if (Controls.isPressed(KeyCode.DIGIT5)) {
    		nextBrush(5);
    	}
    	if (Controls.isPressed(KeyCode.DIGIT6)) {
    		nextBrush(6);
    	}
    	if (Controls.isPressed(KeyCode.DIGIT7)) {
    		nextBrush(7);
    	}
    	if (Controls.isPressed(KeyCode.DIGIT8)) {
    		nextBrush(8);
    	}
    	if (Controls.isPressed(KeyCode.DIGIT9)) {
    		nextEditSet(9);
    	}
    	
    	if (Controls.isPressed(KeyCode.Q)) {
    		if (brush == 0)
    			brush = 12;
    		else
    			brush = 0;
    	}
    }
    
    private static void nextBrush(int key) {
    	
    	if (editSet == 1) {
    		if (key != 2) return;
    		
			if (brush != 0 && brush != 12) brush -= 29;
		}
    	
    	for ( int i = 0 ; i < tileKeyMap[editSet].length ; i++) {
    		if (brush < tileKeyMap[editSet].length - 1)
    			brush += 1;
    		else
    			brush = 0;
    		
    		
    		if (tileKeyMap[editSet][brush] == key) {
    			if (editSet == 1) {
        			brush += 29;
        		}
    			return;
    		}
    	}
    }
    
    private static void nextEditSet(int key) {
    	if (editSet < tileKeyMap.length - 1)
			editSet += 1;
		else
			editSet = 0;
    	
    	if (editSet == 0) {
			brush = 6;
		} else if (editSet == 1) {
			brush = 29;
		}
    }
    
    private static void drawEditBox(GraphicsContext gc, int col, int row, int tile) {
    	double xPos = col * 1.8 * SIZE;
    	double yPos = row * 2 * SIZE;
    	double offset = SIZE/2;
    	double curvature = 0.4 * SIZE;
    	double boxSize = 1.4 * SIZE;
    	
    	gc.setFill(Color.gray(0.7, 0.8));
    	gc.setStroke(Color.gray(0, 0.8));
    	gc.setLineWidth(0.075 * SIZE);
    	gc.fillRoundRect(xPos + offset, yPos + offset, boxSize, boxSize, curvature, curvature);
    	gc.strokeRoundRect(xPos + offset, yPos + offset, boxSize, boxSize, curvature, curvature);

		gc.drawImage(tileImage[tile], xPos + 0.05*SIZE + 1.4*offset, yPos + 0.05*SIZE + 1.4*offset, SIZE*0.9, SIZE*0.9);
    	//drawTile(gc, tile, xPos + 1.4*offset, yPos + 1.4*offset, true);
    	
    	// Draw letter/number
    	String str = Integer.toString(col+1);
    	switch(row) {
    	case 1:
    		str = "Q";
    		break;
    	case 8:
    		str = "0";
    		break;
    	}
    	
    	gc.setLineWidth(3);
    	gc.setFill(Color.WHITE);
		gc.setFont(Font.font(15));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		
		offset += 10;
    	gc.strokeText(str, xPos + offset, yPos + offset);
    	gc.fillText(str, xPos + offset, yPos + offset);
    }
    
    private static void delPrevSpawn(Player player, int[][] grid, int element) {
    	for (int i = 0; i < grid.length; i++) { // Iterate through rows
    	    for (int j = 0; j < grid[i].length; j++) { // Iterate through columns
    	        if (grid[i][j] == element) {
    	            grid[i][j] = 0;
    	            player.setSpawn(i, j);
    	            break;
    	        }
    	    }
    	}
    }
    
    public static int getTileType(double x, double y) {
        int tileX = (int) Math.floor(x / Tiles.SIZE);
        int tileY = (int) Math.floor(y / Tiles.SIZE);
        if (tileX >= 0 && tileX < WIDTH) {
        	if (tileY >= 0 && tileY < HEIGHT) {
                return grid[tileX][tileY];
        	} else {
        		return 0;
        	}
        } else {
        	return 1;
        }
    }

    public void setTileType(double x, double y, int type) {
        int tileX = (int) Math.floor(x / Tiles.SIZE);
        int tileY = (int) Math.floor(y / Tiles.SIZE);
        if (tileX >= 0 && tileX < WIDTH && tileY >= 0 && tileY < HEIGHT) {
            grid[tileX][tileY] = type;
        }
    }
}
