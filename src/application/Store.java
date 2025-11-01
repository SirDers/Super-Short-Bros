package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Store {
	
	// NEW LEVEL SAVE
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
    	
    	// Save width and height
    	writeValue(dos, Tiles.WIDTH, 0);
    	writeValue(dos, Tiles.HEIGHT, 0);

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
    
	// NEW LEVEL LOAD
	public static void loadLevel(int num, Player player, ArrayList<PhysicsObject> objects, boolean isBackup) throws IOException {
		int prevLevel = Tiles.level;
		Tiles.level = num;
		
		if (Tiles.level == 1 || Tiles.level == 2) {
			Tiles.theme = 1;
		} else if (Tiles.level == 3) {
			Tiles.theme = 2;
		} else {
			Tiles.theme = 4;
		}
		
		if (prevLevel != Tiles.level) {
			Music.setMusic(Tiles.theme);
		}
		
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
    	
    	// Set width and height
    	Tiles.WIDTH = readValue(dis);
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
    
	
	
    // OLD LEVEL LOADER
    /*
    
    // Level storage
	public static String[] levelStore = 
		{"100.20.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.2.2.2.2.2.2.2.2.2.2.2.2.2.2.2.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.10.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.2.2.2.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.10.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.6.7.8.0.0.0.0.0.0.0.0.0.9.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.1.1.1.0.0.0.0.3.4.5.0.0.0.0.7.7.7.7.7.7.7.8.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.1.1.1.0.0.0.0.0.0.0.0.0.0.3.4.5.0.0.0.0.4.4.4.4.4.4.4.5.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.1.1.1.1.1.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.6.7.7.7.7.7.7.8.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.3.4.5.0.0.0.0.4.4.4.4.4.4.4.5.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.6.7.7.7.7.7.7.7.7.7.4.4.4.4.4.4.4.5.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.3.4.5.0.0.0.0.4.4.4.4.4.4.4.5.0.0.0.0.0.6.7.7.7.7.7.8.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.3.4.4.4.4.4.4.4.4.4.4.4.4.4.4.4.4.5.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.3.4.5.0.0.0.0.4.4.4.4.4.4.4.5.0.0.0.0.0.3.4.4.4.4.4.5.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.3.4.4.4.4.4.4.4.4.4.4.4.4.4.4.4.4.5.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.3.4.5.0.0.0.0.4.4.4.4.4.4.4.5.0.0.0.0.0.3.4.4.4.4.4.5.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.3.4.4.4.4.4.4.4.4.4.4.4.4.4.4.4.4.5.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.3.4.5.0.0.0.0.4.4.4.4.4.4.4.5.0.0.0.0.0.3.4.4.4.4.4.5.0.0.0.0.0.0.0.0.0.0.0.6.7.7.7.7.7.7.7.7.7.7.7.7.7.8.0.0.0.6.7.7.7.7.8.0.0.0.3.4.4.4.4.4.4.4.4.4.4.4.4.4.4.4.4.5.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.3.4.5.0.0.0.0.4.4.4.4.4.4.4.5.0.0.0.0.0.3.4.4.4.4.4.5.0.0.0.0.0.0.0.0.0.0.0.3.4.4.4.4.4.4.4.4.4.4.4.4.4.5.0.0.0.3.4.4.4.4.5.0.0.0.3.4.4.4.4.4.4.4.4.4.4.4.4.4.4.4.4.5.0.0.0.0.0.0.0.0.0.0.0.2.0.0.0.0.0.3.4.5.0.0.0.0."
			};
			
	private static String encoded;
	private static String value;
	private static char piece;
	private static int readIndex;
	
	public static void printGrid() {
		System.out.print(Tiles.WIDTH + ".");
		System.out.print(Tiles.HEIGHT + ".");
		for (int i = 0 ; i < Tiles.HEIGHT ; i++) {
			for (int j = 0 ; j < Tiles.WIDTH ; j++) {
				System.out.print(Tiles.grid[j][i] + ".");
			}
		}
		System.out.println();
	}
    
	
    public static void loadLevelOld(int num) {
    	encoded = levelStore[num];
    	
    	// Decode level
    	readIndex = 0;
    	
    	// Set width and height
    	readValueOld();
    	Tiles.WIDTH = Integer.parseInt(value);
    	readValueOld();
    	Tiles.HEIGHT = Integer.parseInt(value);
    	
        Tiles.grid = new int[Tiles.WIDTH][Tiles.HEIGHT];
        
        // Set tile types
        for (int i = 0 ; i < Tiles.HEIGHT ; i++) {
        	for (int j = 0 ; j < Tiles.WIDTH ; j++) {
        		
        		// Set tile value
        		readValueOld();
        		Tiles.grid[j][i] = Integer.parseInt(value);
        		
        		// Set Spawn
				if (Tiles.grid[j][i] == 9) {
    	            Player.spawnX = j*Tiles.SIZE + Player.spawnXoffset;
    	            Player.spawnY = i*Tiles.SIZE + Player.spawnYoffset - Player.TINY;
				}
        	}
    	}
    }
    
    private static void readValueOld() {
    	value = "";
    	
    	piece = encoded.charAt(readIndex);
    	readIndex += 1;
    	
    	while (piece != '.') {
    		value += piece;
    		piece = encoded.charAt(readIndex);
        	readIndex += 1;
    	}
    }
    */
    
    /*
    public static void saveLevel() {
    	encoded = "";
    	
    	// Set width and height
    	writeValue(Integer.toString(Tiles.WIDTH), '.');
    	writeValue(Integer.toString(Tiles.HEIGHT), '.');
    	
        
        // Set tile types
        for (int i = 0 ; i < Tiles.HEIGHT ; i++) {
        	for (int j = 0 ; j < Tiles.WIDTH ; j++) {
            	writeValue(Integer.toString(Tiles.grid[j][i]), '.');
        	}
    	}
        
        //Store level
        System.out.println(encoded);
    }
    */
	
	
    /**
     * @param value
     * @param piece - Delimiter
     */
	/*
    private static void writeValue(String value, char piece) {
    	encoded += value + piece;
    }
    */
}
