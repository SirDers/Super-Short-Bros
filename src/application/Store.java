package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Store {
	
	// LEVEL SAVE
	public static void saveLevel(int num, ArrayList<PhysicsObject> objects, boolean isBackup) throws IOException {
		File level;
		if (isBackup) {
			level = new File("level" + num + "backup.bin");
			level.deleteOnExit();
		} else {
			level = new File("level" + num + ".bin");
		}

        if (!level.exists()) {
            level.createNewFile();
        }
        
		FileOutputStream fos;
		fos = new FileOutputStream(level);
		DataOutputStream dos = new DataOutputStream(fos);
		
		// Unique Code: 19879
    	writeValue(dos, 19879, 0);
    	
    	// Write Saved-Version
    	writeValue(dos, 2, 0);
    	
    	// Save width and height
    	writeValue(dos, Tiles.WIDTH, 0);
    	writeValue(dos, Tiles.HEIGHT, 0);
    	
    	// Save theme
    	writeValue(dos, Tiles.theme, 0);

        // Save tile types
    	for (int[] row : Tiles.grid) {
	    	int current = row[0];
	    	int count = 0;
	    	
	    	for (int value : row) {
	    		if (value == current) {
	    			count++;
	    		} else {
	    			writeValue(dos, current, count);
	    			current = value;
	    			count = 1;
	    		}
	    	}
			writeValue(dos, current, count);
    	}
    	
    	for (PhysicsObject object : objects) {
            dos.write(object.getType());  // Save type
            dos.write(object.tileX);     // Save spawnX
            dos.write(object.tileY);     // Save spawnY
        }
    }
	
    private static void writeValue(DataOutputStream dos, int current, int count) throws IOException {
    	dos.writeInt(current);
    	if (count != 0)
    		dos.writeInt(count);
    }
    
	// LEVEL LOAD
	public static void loadLevel(int num, Player player, ArrayList<PhysicsObject> objects, boolean isBackup) throws IOException {        
		File level;
		if (isBackup) {
			level = new File("level" + num + "backup.bin");
			if (!level.exists()) {
	            level.createNewFile();
				level.deleteOnExit();
	            saveLevel(num, objects, true);
	            return;
	        }
		} else {
			level = new File("level" + num + ".bin");
			if (!level.exists()) {
	            level.createNewFile();
	            Tiles.genNew(player, objects);
	            saveLevel(num, objects, false);
	            return;
	        }
		}
        
		FileInputStream fis;
		fis = new FileInputStream(level);
	    DataInputStream dis = new DataInputStream(fis);
	    
	    // Check if file contains unique code: 19879
	    int code = readValue(dis);

	    // Try old level load if missing unique code
		if (code != 19879) {
			loadLevelV1(num, player, objects, isBackup, dis, code);
			return;
		}
		
		// Check version number of file to load
		if (readValue(dis) == 2) {
			loadLevelV2(num, player, objects, isBackup, dis);
		} else {
			throw new IOException("Invalid file level version!");
		}
    }
	
	// LEVEL LOAD V0.4.9+
	private static void loadLevelV2(int num, Player player, ArrayList<PhysicsObject> objects, boolean isBackup, DataInputStream dis) throws IOException {
		int prevLevel = Tiles.level;
		Tiles.level = num;
    	
    	// Set width and height
    	Tiles.WIDTH = readValue(dis);
    	Tiles.HEIGHT = readValue(dis);
    	
        Tiles.grid = new int[Tiles.WIDTH][Tiles.HEIGHT];
        
        Tiles.theme = readValue(dis);
        
        if (prevLevel != Tiles.level) {
			Music.setMusic(Tiles.theme);
		}
        
        // Set tile types
        int row = 0, col = 0;
        while (row < Tiles.WIDTH) {
            int value = dis.readInt();
            int count = dis.readInt();
            
            for (int i = 0; i < count; i++) {
            	// Place tile
                Tiles.grid[row][col] = value;
                
                // Set Spawn
    			if (Tiles.grid[row][col] == 9) {
    	            player.setSpawn(row, col);
    	            if (!isBackup) {
        	            player.reset();
    	            }
    			}
    			
    			col++;
                if (col == Tiles.HEIGHT) {
                    col = 0;
                    row++;
                }
            }
        }
        
        // Set objects
        objects.clear();
        
        while (dis.available() > 0) {
        	int type = dis.read();
            int tileX = dis.read();
            int tileY = dis.read();
            
            PhysicsObject object = ObjectFactory.createObject(type, tileX, tileY, num + 1);
        	objects.add(object);
        }
	}
	
	// LEVEL LOAD V0.4.8
	private static void loadLevelV1(int num, Player player, ArrayList<PhysicsObject> objects, 
			boolean isBackup, DataInputStream dis, int value1) throws IOException {
		int prevLevel = Tiles.level;
		Tiles.level = num;
		
		if (Tiles.level == 3) {
			Tiles.theme = 2;
		} else if (Tiles.level == 4) {
			Tiles.theme = 4;
		} else {
			Tiles.theme = 1;
		}
		
		if (prevLevel != Tiles.level) {
			Music.setMusic(Tiles.theme);
		}
    	
    	// Set width and height
    	Tiles.WIDTH = value1;
    	Tiles.HEIGHT = readValue(dis);
    	
        Tiles.grid = new int[Tiles.WIDTH][Tiles.HEIGHT];
        
        // Set tile types
        int row = 0, col = 0;
        while (row < Tiles.WIDTH) {
            int value = dis.readInt();
            int count = dis.readInt();
            
            for (int i = 0; i < count; i++) {
            	// Place tile
                Tiles.grid[row][col] = value;
                
                // Set Spawn
    			if (Tiles.grid[row][col] == 9) {
    	            player.setSpawn(row, col);
    	            if (!isBackup) {
        	            player.reset();
    	            }
    			}
    			
    			col++;
                if (col == Tiles.HEIGHT) {
                    col = 0;
                    row++;
                }
            }
        }
        
        // Set objects
        objects.clear();
        
        while (dis.available() > 0) {
        	int type = dis.read();
            int tileX = dis.read();
            int tileY = dis.read();
            
            PhysicsObject object = ObjectFactory.createObject(type, tileX, tileY, num + 1);
        	objects.add(object);
        }
	}
    
    private static int readValue(DataInputStream dis) throws IOException {
    	return dis.readInt();
    }
}
