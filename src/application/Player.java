package application;

import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;

public class Player extends PhysicsObject {
	
	// Piece ability
	private String piece = "pawn"; // Set to "pawn", "knight", "bishop", "rook", or "queen"
	private int abCoolDown = 0; // Ability cool down
	private int specialDirX = 0; // X direction of ability
	private int specialDirY = 0; // Y direction of ability
	
	// Jumping
    private int jumping = 0;
    final int FRAMES_BEFORE_JUMP = 8; // Amount of frames of error the player is allowed when pressing 'jump' before hitting the ground
	int jumpFrame = 0; // Counts the amount of frames jump has been held
    boolean isJumping = false; // Checks if the player is attempting to jump
    double speedHeight = 8 + 4 * Math.abs(speedXOnGround) / maxSpeedX; // Amount of frames player can keep jumping for
    
    // Death
    private boolean dead = false;
    private double deadFrames = 0;
    
    // Load images
    private Image walk1 = new Image("file:resources/images/objects/player/walk1.png");
    private Image walk2 = new Image("file:resources/images/objects/player/walk2.png");
    private Image walk3 = new Image("file:resources/images/objects/player/walk3.png");
    private Image walk4 = new Image("file:resources/images/objects/player/walk4.png");

    private Image run1 = new Image("file:resources/images/objects/player/run1.png");
    private Image run2 = new Image("file:resources/images/objects/player/run2.png");
    private Image run3 = new Image("file:resources/images/objects/player/run3.png");
    private Image run4 = new Image("file:resources/images/objects/player/run4.png");
    
    private Image turn = new Image("file:resources/images/objects/player/turn.png");
    
    private Image jump = new Image("file:resources/images/objects/player/jump.png");
    private Image runjump = new Image("file:resources/images/objects/player/run-jump.png");
    private Image death = new Image("file:resources/images/objects/player/dead.png");
    
    // Load chess images
    private Image pawn = new Image("file:resources/images/pawn.png");
    private Image knight = new Image("file:resources/images/knight.png");
    private Image bishop = new Image("file:resources/images/bishop.png");
    private Image rook = new Image("file:resources/images/rook.png");
    private Image queen = new Image("file:resources/images/queen.png");
    
    
    /**
     * @param spawnX - set x position
     * @param spawnY - set y position
     */
    /*
    public Player(int tileX, int tileY) {
    	super(tileX, tileY);
    }
    */
    public Player(int tileX, int tileY, double WIDTH, double HEIGHT, double ACCELERATION_X, double ACCELERATION_Y, double MAX_SPEED_X, double MAX_SPEED_Y) {
    	super(tileX, tileY, WIDTH, HEIGHT, ACCELERATION_X, ACCELERATION_Y, MAX_SPEED_X, MAX_SPEED_Y);
	}

    @Override
    public int getType() {
    	return 0;
    }
    
    @Override
    protected void loadImages() {
    	
    }
    
    @Override
    public void setSpawn(int tileX, int tileY) {
    	super.setSpawn(tileX, tileY);
    	
    	double spawnYoffset = height/2 - Tiles.SIZE;
    	this.spawnY = tileY*Tiles.SIZE - spawnYoffset - Player.TINY;
    	this.prevY = this.spawnY;
    }
    
    @Override
    protected void draw(GraphicsContext gc, double cameraX, double cameraY) {
    	double drawX = x - width/2 - cameraX;
        double drawY = y - height/2 - cameraY;

    	double sizeFactor = Tiles.SIZE*2.5;
        double spritePosX = sizeFactor*(0.375 - 1.25)/2.5;
    	double spritePosY = sizeFactor/5.25;
        
        // Collision Box
    	/*
    	gc.setFill(Color.BLACK);
        gc.fillRect(drawX, drawY, WIDTH, HEIGHT);
    	*/
    	
    	// Max Speed Graphic
    	/*
    	gc.setFill(Color.BLACK);
        gc.fillRect(10, 10, 20*MAX_SPEED, Tiles.SIZE/2);

    	gc.setFill(Color.WHITE);
        gc.fillRect(10, 10, 20*Math.abs(speedX), Tiles.SIZE/2);
    	*/
    	
    	// Animation
    	
        if (dead) {
        	gc.drawImage(death, drawX + spritePosX, drawY - spritePosY, sizeFactor, sizeFactor);
    		return;
        }
        
    	if (Editor.editMode) {
    		gc.drawImage(walk1, drawX + spritePosX, drawY - spritePosY, sizeFactor, sizeFactor);
    		return;
    	}
    	
    	if (Controls.xAxis == 1)
            leftright = true;
        else if (Controls.xAxis == -1)
        	leftright = false;
		
    	
        if (leftright) {
        	animate(gc, 4, 1, drawX, drawY, 1);
        } else {
        	animate(gc, 4, 1, drawX, drawY, -1);
        }
        

        //gc.drawImage(walk1, drawX + spritePosX, drawY - spritePosY, 1*sizeFactor, sizeFactor);
        
    }
    
    @Override
    protected void animate(GraphicsContext gc, int frameCount, double factor, double drawX, double drawY, int flip) {

    	double sizeFactor = Tiles.SIZE*2.5;
    	double spritePosX = sizeFactor*(0.375 - flip*1.25)/2.5;
    	double spritePosY = sizeFactor/5.25;
    	
    	if (Tiles.level == 4) {
        	switch (piece) {
        	case "pawn":
                gc.drawImage(pawn, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		break;
        	case "knight":
                gc.drawImage(knight, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		break;
        	case "bishop":
                gc.drawImage(bishop, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		break;
        	case "rook":
                gc.drawImage(rook, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		break;
        	case "queen":
                gc.drawImage(queen, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		break;
        	default:
                gc.drawImage(pawn, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		break;
        	}
        	return;
    	}
    	
    	int temp = 1 + ( ((int) spriteFrame) % frameCount );
    	
    	if (action == "run") {
    		if (falling > 1) {
                gc.drawImage(runjump, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		return;
            }
    		if (action == "turn") {
                gc.drawImage(turn, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		return;
            }
    		switch(temp) {
        	case 1:
                gc.drawImage(run1, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		break;
        	case 2:
        		gc.drawImage(run2, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		break;
        	case 3:
        		gc.drawImage(run3, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		break;
        	case 4:
        		gc.drawImage(run4, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		break;
        	default:
        		gc.setFill(Color.BLACK);
                gc.fillRect(drawX, drawY, width, height);
                break;
        	}
        } else {
        	if (falling > 1) {
                gc.drawImage(jump, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		return;
            }
        	if (action == "turn") {
                gc.drawImage(turn, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		return;
            }
        	switch(temp) {
        	case 1:
                gc.drawImage(walk1, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		break;
        	case 2:
        		gc.drawImage(walk2, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		break;
        	case 3:
        		gc.drawImage(walk3, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		break;
        	case 4:
        		gc.drawImage(walk4, drawX + spritePosX, drawY - spritePosY, flip*sizeFactor, sizeFactor);
        		break;
        	default:
        		gc.setFill(Color.BLACK);
                gc.fillRect(drawX, drawY, width, height);
                break;
        	}
        }
        
    }

	@Override
    public void update(ArrayList<PhysicsObject> objects) {
    	if (Editor.editMode) {
        	moveEditMode();
    	}
    	else {
    		if (abCoolDown <= 0) {
        		speedX();
                speedY();
    		}
            
    		if (piece != "pawn")
    			specialMove();
            
            moveX();
            moveY();
            changeState();
    	}
    }
	
	private void changeState() {
		if (Tiles.level != 4) {
			return;
			}
		
		if (x < 800)
			piece = "pawn";
		else if (x < 2040)
			piece = "knight";
		else if (x < 4230)
			piece = "bishop";
		else if (x < 6100)
			piece = "rook";
		else
			piece = "queen";
	}
	
	public void playDead(ArrayList<PhysicsObject> objects) {
		if (deadFrames == 0) speedY = -accelY*1.5;
		
		deadFrames += 1;
		if (!Editor.isDevMode()) {
			if (deadFrames >= 180) {
				try {
					dead = false;
					deadFrames = 0;
					reset();
					Store.loadLevel(Tiles.level, this, objects, false);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (deadFrames >= 20) {
				if (speedY < 21) {
					speedY += 1.5;
				}
				y += speedY;
			}
		} else {
			if (deadFrames >= 45) {
				try {
					dead = false;
					deadFrames = 0;
					Store.loadLevel(Tiles.level, this, objects, true);
					Editor.editMode = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (deadFrames >= 20) {
				if (speedY < 21) {
					speedY += 1.5;
				}
				y += speedY;
			}
		}
		
	}

    @Override
    protected void speedX() {
        action = "walk";
        
    	if (Controls.run)
    		curMaxSpeed = maxSpeedX;
    	else 
    		curMaxSpeed = maxSpeedX * 0.4;
    	
    	// When left or right isn't pressed
    	if (Controls.xAxis == 0) {
    		decelerateX();
    	}
    	// When left or right is pressed
    	else {
    		if (Controls.xAxis * speedX < curMaxSpeed) {
    			if (Controls.xAxis * speedX < 0) {
        			//Turning
        			speedX += accelX * 3 * Controls.xAxis;
        			action = "turn";
        		}
        		else {
        			speedX += accelX * Controls.xAxis;
        		}
        	}
        	else {
        		if (curMaxSpeed < maxSpeedX)
        			decelerateX();
        		else {
        			speedX = curMaxSpeed * Controls.xAxis;
        			action = "run";
        		}
        	}
    	}
    	
    	// Speed of walk animation
    	frameSpeed = Math.abs(speedX) / 24;
    	if (frameSpeed < 0.1)
    		frameSpeed = 0.1;
    	spriteFrame += frameSpeed;
    }
    
    @Override
	protected void speedY() {
        super.speedY();
        
        // Jumping
    	if (Controls.jump) {
    		if (jumpFrame <= FRAMES_BEFORE_JUMP) {
        		isJumping = true;
    		}
    		//pressedJump = false;
    		jumpFrame += 1;
    	} else {
    		isJumping = false;
    		//pressedJump = true;
    		jumpFrame = 0;
    	}
        
    	boolean canJump = 0 < jumping && jumping <= speedHeight;
    	
        if (isJumping && (falling < 4 || canJump)) {
        	jumping += 1;
            speedY = -accelY;
        } else {
        	jumping = 0;
        	isJumping = false;
        }
    }
    
    @Override
    protected void moveY() {
    	super.moveY();
    	if (hardness > 0) {
    		if (speedY > 0) {
    			jumping = 0;
    		}		
    		speedY = 0;
    	}
    }
    
    // Special Ability
    protected void specialMove() {
    	if (abCoolDown <= 0) {
    		if (Controls.keyLpressed) {
    			abCoolDown = 10;
    			speedX = 0;
    			speedY = 0;
    			specialDirX = Controls.xAxis;
    			specialDirY = Controls.yAxis;
    		}
    		return;
    	}
    	
    	if (piece == "knight") {
    		speedX = specialDirX * 2 * maxSpeedX;
    		speedY = 0;
    	}
    	if (piece == "bishop") {
    		if (specialDirX == 0)
    			specialDirX = leftright ? 1 : -1;
    		if (specialDirY == 0)
    			specialDirY = -1;
    		speedX = specialDirX * 2 * maxSpeedX;
    		speedY = specialDirY * 2 * maxSpeedX;
    	}
    	if (piece == "rook") {
    		if (specialDirX != 0)
    			speedX = specialDirX * 2 * maxSpeedX;
    		else
    			speedY = specialDirY * 2 * maxSpeedX;
    	}
    	if (piece == "queen") {
    		speedX = specialDirX * 2 * maxSpeedX;
    		speedY = specialDirY * 2 * maxSpeedX;
    	}

    	abCoolDown--;
    	if (abCoolDown <= 0) {
    		speedX = 0;
    		speedY = 0;
    		jumping = 0;
    	}
    }
    
    @Override
    protected void death(ArrayList<PhysicsObject> objects, ArrayList<PhysicsObject> toRemove) {
    	if (y > (Tiles.HEIGHT + 2)*Tiles.SIZE) {
    		//reset();
    		dead = true;
    	} else {
    		speedY = -20;
    		dead = true;
    	}
    }
    
    private void decelerateX() {
    	if (falling < 4) {
			if (speedX > 1) {
    			speedX -= accelX;
    		}
    		else if (speedX < -1) {
    			speedX += accelX;
    		}
    		else {
    			speedX = 0;
    			spriteFrame = 0;
    		}
		}
    }
    
    protected void reset() {
    	super.reset();
        jumping = 0;
    }
    
    private void moveEditMode() {
    	speedX = 7 * (Controls.run ? 3 : 1) * Controls.xAxis;
    	speedY = 7 * (Controls.run ? 3 : 1) * Controls.yAxis;
    	x += speedX;
    	y += speedY;
    	
    	if (x > Tiles.WIDTH * Tiles.SIZE - width/2) {
    		x = Tiles.WIDTH * Tiles.SIZE - width/2;
    	}
    	if (x < width/2) {
    		x = width/2;
    	}
    	if (y > Tiles.HEIGHT * Tiles.SIZE - height/2) {
    		y = Tiles.HEIGHT * Tiles.SIZE - height/2;
    	}
    	if (y < height/2) {
    		y = height/2;
    	}
    }
    
    @Override
    protected void onTileColYUp() {
		jumping = 0;
	}
    
    @Override
    protected void collisionFixPoint(double x, double y, double dx, double dy) {
    	super.collisionFixPoint(x, y, dx, dy);
    	if (onTile >= 25 && onTile <= 28) {
    		dead = true;
    	}
    }
    
    @Override
    protected void onObjCollision(ArrayList<PhysicsObject> objects, ArrayList<PhysicsObject> toRemove, PhysicsObject object, double overlapBottom, double overlapTop, double overlapRight, double overlapLeft) {
    	collided = true;
    	if (object.stompable) {
    		if (speedY > 0) {
                // Collision if falling
        		speedY = -accelY;
                y += speedY - overlapBottom - TINY;
                falling = 0;
                jumping = 0;
                isJumping = true;
                object.death(objects, toRemove);
            } else {
                death(objects, toRemove);
            }
		} else if (object.getClass() != Stella.class) {
            death(objects, toRemove);
        }
    	if (object.getType() == 1) {
    		// Load next level
    		((Stella) object).nextLevel(objects, this);
    	}
    }

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public boolean isDead() {
		return dead;
	}
}
