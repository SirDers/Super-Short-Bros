package application;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class PhysicsObject {

	// Physics variables
	public static final double TINY = 0.000001 * Tiles.SIZE / 32;
	protected static final double GRAVITY = 1.0/40;

	protected double MAX_SPEED_X = 21.0/64;
	protected double MAX_SPEED_Y = 7.0/16;
	protected double ACCELERATION_X = 1.0/128;
	protected double ACCELERATION_Y = 637.0/1920;
	protected double WIDTH = 0.75;
	protected double HEIGHT = 0.75;
	
	protected double maxSpeedX = MAX_SPEED_X * Tiles.SIZE;
	protected double maxSpeedY = MAX_SPEED_Y * Tiles.SIZE;
	protected double accelX = ACCELERATION_X * Tiles.SIZE;
	protected double accelY = ACCELERATION_Y * Tiles.SIZE;
	protected double height = HEIGHT * Tiles.SIZE;
	protected double width = WIDTH * Tiles.SIZE;
	protected double gravity = GRAVITY * Tiles.SIZE;
	
    protected double x;
    protected double y;
    protected double speedX;
    protected double speedY;
    protected double curMaxSpeed = maxSpeedX;
    protected int falling;
    protected double speedXOnGround;
    protected double hardness = 0;
    
    // Spawn position
    protected int tileX;
    protected int tileY;
    protected double spawnX;
    protected double spawnY;
    
    // Animation
    protected String action = "walk";
    protected boolean isFacingRight = true;
    protected double spriteFrame = 0;
    protected double frameSpeed = 0.2;
    
    // Collision
    protected double prevX;
    protected double prevY;
    protected boolean collisionOn = true;
    protected boolean collided = false;
    protected boolean stompable = false;
    
    // Spawned
    protected boolean spawned = false;
    protected int frames = 0;
    
    // Property
    protected int property;
    
    // Load images
    protected ArrayList<Image> images = new ArrayList<Image>();
    
    
    
    // Constructors
    
    // Base
    public PhysicsObject(int tileX, int tileY) {
    	setSpawn(tileX, tileY);
    	reset();
    	loadImages();
	}
    
    // With size
    public PhysicsObject(int tileX, int tileY, double WIDTH, double HEIGHT) {
    	this(tileX, tileY);
    	setSize(WIDTH, HEIGHT);
	}
    
    // With size, speed, and acceleration
    public PhysicsObject(int tileX, int tileY, double WIDTH, double HEIGHT, double ACCELERATION_X, double ACCELERATION_Y, double MAX_SPEED_X, double MAX_SPEED_Y) {
    	this(tileX, tileY, WIDTH, HEIGHT);
    	setAccel(ACCELERATION_X, ACCELERATION_Y);
    	setMaxSpeed(MAX_SPEED_X, MAX_SPEED_Y);
	}
    
    // With property
    public PhysicsObject(int tileX, int tileY, int property) {
    	this(tileX, tileY);
    	this.property = property;
	}
    
    // With size, and property
    public PhysicsObject(int tileX, int tileY, double WIDTH, double HEIGHT, int property) {
    	this(tileX, tileY, WIDTH, HEIGHT);
    	this.property = property;
	}

    // With size, speed, acceleration, and property
    public PhysicsObject(int tileX, int tileY, double WIDTH, double HEIGHT, double ACCELERATION_X, double ACCELERATION_Y, double MAX_SPEED_X, double MAX_SPEED_Y, int property) {
    	this(tileX, tileY, WIDTH, HEIGHT, ACCELERATION_X, ACCELERATION_Y, MAX_SPEED_X, MAX_SPEED_Y);
    	this.property = property;
	}
    
    public void setSpawn(int tileX, int tileY) {
    	double spawnXoffset = Tiles.SIZE/2;
    	double spawnYoffset = Tiles.SIZE/2;
    	this.tileX = tileX;
    	this.tileY = tileY;
    	this.spawnX = tileX*Tiles.SIZE + spawnXoffset;
    	this.spawnY = tileY*Tiles.SIZE + spawnYoffset - TINY;
    }
    
    protected void setSize(double WIDTH, double HEIGHT) {
    	this.WIDTH = WIDTH;
    	this.HEIGHT = HEIGHT;
    	this.width = WIDTH * Tiles.SIZE;
    	this.height = HEIGHT * Tiles.SIZE;
    }
    
    protected void setAccel(double ACCELERATION_X, double ACCELERATION_Y) {
    	this.ACCELERATION_X = ACCELERATION_X;
    	this.ACCELERATION_Y = ACCELERATION_Y;
    	this.accelX = ACCELERATION_X * Tiles.SIZE;
    	this.accelY = ACCELERATION_Y * Tiles.SIZE;
    }
    
    protected void setMaxSpeed(double MAX_SPEED_X, double MAX_SPEED_Y) {
    	this.MAX_SPEED_X = MAX_SPEED_X;
    	this.MAX_SPEED_Y = MAX_SPEED_Y;
    	this.maxSpeedX = MAX_SPEED_X * Tiles.SIZE;
    	this.maxSpeedY = MAX_SPEED_Y * Tiles.SIZE;
    }
    
    protected abstract void loadImages();
    
    public abstract int getType();
    
	public void fixedUpdate(ArrayList<PhysicsObject> objects) {
		// Spawn object when it's in spawnBounds
		boolean xSpawnBounds = (x - width/2 > Camera.x + Tiles.SIZE*2 + Main.CANVAS_WIDTH) || (x + width/2 < Camera.x);
		boolean ySpawnBounds = (y - height/2 > Camera.y + Tiles.SIZE*2 + Main.CANVAS_HEIGHT) || (y + height/2 < Camera.y);
		boolean outOfSpawnBounds = xSpawnBounds || ySpawnBounds;
		
		if (!spawned && outOfSpawnBounds && this.getType() != 0) {
			x = spawnX;
			y = spawnY;
			savePreviousState();
			return;
		}
		spawned = true;
		frames += 1;
		
		// Reset object if it leaves bounding area
		boolean xBounds = (x > Camera.x + 1.5*Main.CANVAS_WIDTH) || (x < Camera.x - 1.5* Main.CANVAS_WIDTH);
		boolean yBounds = (y > Camera.y + 2*Main.CANVAS_HEIGHT) || (y < Camera.y - 2*Main.CANVAS_HEIGHT);
		boolean outOfBounds = xBounds || yBounds;
		
		// If spawn is on screen, dont' reset
		boolean spawnXBounds = (spawnX - width/2 > Camera.x + Main.CANVAS_WIDTH) || (spawnX + width/2 < Camera.x);
		boolean spawnYBounds = (spawnY - height/2 > Camera.y + Main.CANVAS_HEIGHT) || (spawnY + height/2 < Camera.y);
		boolean spawnOutOfBounds = spawnXBounds || spawnYBounds;
		
		if (Editor.editMode || (outOfBounds && spawnOutOfBounds)) {
			reset();
			return;
		}

		speedX();
        speedY();
        
        moveX();
        moveY();
	}
	
    // Physics:
	protected void speedX() {
		// Do nothing
	}
	
	protected void speedY() {
		// Apply gravity
		speedY += gravity;
        if (speedY > maxSpeedY) {
            speedY = maxSpeedY;
        }
	}
	
	protected void moveX() {
        x += speedX;
        
        collisionFixDirection(speedX, 0);
        
        if (hardness > 0) {
        	speedX = 0;
        }
    }
	
	protected void moveY() {
    	y += speedY;
    	falling += 1;
    	
    	collisionFixDirection(0, speedY);
    	
    	if (hardness > 0) {
    		if (speedY > 0) {
    			falling = 0;
        		speedXOnGround = speedX;
    		}		
    		speedY = 0;
    	}
    }
	
	protected void collisionFixDirection(double dx, double dy) {
	    hardness = 0;
	    	
	    collisionFixPoint(x - width/2, y + height/2, dx, dy);
	    collisionFixPoint(x - width/2, y, dx, dy);
	    collisionFixPoint(x - width/2, y - height/2, dx, dy);
	    collisionFixPoint(x + width/2 - TINY, y + height/2, dx, dy);
	    collisionFixPoint(x + width/2 - TINY, y, dx, dy);
	    collisionFixPoint(x + width/2 - TINY, y - height/2, dx, dy);
    }
    
	protected int onTile;
	
    protected void collisionFixPoint(double x, double y, double dx, double dy) {
    	onTile = Tiles.getTileType(x, y);
    	
    	if (onTile == 0 || onTile == 9 || onTile == 38) {
    		return;
    	}
    	
    	hardness = 1;
    	if (onTile == 11)
    		hardness = 0;
    	
    	
    	double modX = x % Tiles.SIZE;
    	double modY = y % Tiles.SIZE;
    	
    	if (modX < 0)
    		modX += Tiles.SIZE;
    	if (modY < 0)
    		modY += Tiles.SIZE;
    	
     	if (dx < 0) {
    		this.x += Tiles.SIZE - modX;
    		onTileColX();
    	}
		if (dx > 0) {
		    this.x += -TINY - modX;
    		onTileColX();
		}
		if (dy < 0) {
			this.y += Tiles.SIZE - modY;
			onTileColYUp();
		}
		if (dy > 0) {
			this.y += -TINY - modY;
		}
    }
    
	protected void onTileColX() {
		// Do nothing
	}
	
	protected void onTileColYUp() {
		// Do nothing
	}
	
    protected void sensor(ArrayList<PhysicsObject> objects, ArrayList<PhysicsObject> toRemove) {
    	// Fell into void
    	if (y > (Tiles.HEIGHT + 2)*Tiles.SIZE) {
    		death(objects, toRemove);
    	}
    	
    	// Check for collisions with other objects
    	checkCollision(objects, toRemove);
    }
    
    protected void checkCollision(ArrayList<PhysicsObject> objects, ArrayList<PhysicsObject> toRemove) {
    	for (PhysicsObject object : objects) {
    		if (this == object || object.collisionOn == false || collisionOn == false) continue;
    		
    	    double meRight = x + width / 2;
    	    double meLeft = x - width / 2;
    	    double meBottom = y + height / 2;
    	    double meTop = y - height / 2;

    	    double objLeft = object.prevX - object.width / 2;
    	    double objRight = object.prevX + object.width / 2;
    	    double objBottom = object.prevY + object.height / 2;
    	    double objTop = object.prevY - object.height / 2;

    	    // Check for overlap
    	    boolean horizontalOverlap = meRight > objLeft && meLeft < objRight;
    	    boolean verticalOverlap = meBottom > objTop && meTop < objBottom;

    	    if (horizontalOverlap && verticalOverlap) {
    	        double overlapBottom = meBottom - objTop; // Overlap from top of the object
    	        double overlapTop = objBottom - meTop;    // Overlap from bottom of the object
    	        double overlapRight = meRight - objLeft; // Overlap from left of the object
    	        double overlapLeft = objRight - meLeft;  // Overlap from right of the object

    	        resolveCollision(objects, toRemove, object, overlapBottom, overlapTop, overlapRight, overlapLeft);
    	        if (collided) break;
    	    }

    	}
    }
    
    protected void resolveCollision(ArrayList<PhysicsObject> objects, ArrayList<PhysicsObject> toRemove, PhysicsObject object, double overlapBottom, double overlapTop, double overlapRight, double overlapLeft) {
    	if (overlapBottom < overlapTop && overlapBottom < overlapRight && overlapBottom < overlapLeft) {
            // Collision on the bottom
            y -= overlapBottom;
            speedY = 0;
            falling = 0;
        } else if (overlapTop < overlapBottom && overlapTop < overlapRight && overlapTop < overlapLeft) {
            // Collision on the top
            if (speedY > 0) { // Only resolve if moving upward
                y += overlapTop;
                speedY = 0;
            }
        } else if (overlapRight < overlapLeft) {
            // Collision on the right
            x -= overlapRight;
            accelX *= -1;
        } else {
            // Collision on the left
            x += overlapLeft;
            accelX *= -1;
        }
    }
    
    protected void onObjCollisionX() {
    	
    }
    
    protected void onObjCollisionY() {
    	
    }
    
    protected void death(ArrayList<PhysicsObject> objects, ArrayList<PhysicsObject> toRemove) {
        toRemove.add(this);
    }
    
    protected void reset() {
    	x = spawnX;
        y = spawnY;
		savePreviousState();
        speedX = 0;
        speedY = 0;
        curMaxSpeed = maxSpeedX;
        falling = 0;
        speedXOnGround = 0;
        hardness = 0;
        action = "walk";
        isFacingRight = true;
        spriteFrame = 0;
        frameSpeed = 0.2;
        frames = 0;
    }

	// Save previous states
	protected void savePreviousState() {
		prevX = x;
		prevY = y;
	}

    
    // Visuals:
    protected void draw(GraphicsContext gc, double cameraX, double cameraY, double alpha) {
		double renderX = prevX * (1 - alpha) + x * alpha;
		double renderY = prevY * (1 - alpha) + y * alpha;

		double drawX = renderX - width/2 - cameraX;
        double drawY = renderY - height/2 - cameraY;
        int frameCount = 4;

        // Animation
        if (isFacingRight) {
        	animate(gc, frameCount, width, drawX, drawY, 1);
        } else {
        	animate(gc, frameCount, width, drawX, drawY, -1);
        }
    }
    
    protected void animate(GraphicsContext gc, int frameCount, double factor, double drawX, double drawY, int flip) {
    	int temp = ( ((int) spriteFrame) % frameCount );
    	double scaleToWidth = images.get(temp).getWidth()/64;
    	double scaleToHeight = images.get(temp).getHeight()/64;
    	
    	try {
        	gc.drawImage(images.get(temp), drawX + Tiles.SIZE*(1 - scaleToWidth)/2, drawY + Tiles.SIZE*(1 - scaleToHeight)/2, flip*factor*scaleToWidth, factor*scaleToHeight);
        } catch (Exception e) {
        }
    	
    }
}
