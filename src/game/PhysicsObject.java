package game;

import game.state.Editor;
import render.Camera;
import core.Main;
import world.Tiles;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class PhysicsObject {

    // Physics variables
    public static final double TINY = 0.000001 * Tiles.getSize() / 32;
    protected static final double GRAVITY = 1.0/40;

    protected double MAX_SPEED_X = 21.0/64;
    protected double MAX_SPEED_Y = 7.0/16;
    protected double ACCELERATION_X = 1.0/128;
    protected double ACCELERATION_Y = 637.0/1920;
    protected double WIDTH = 0.75;
    protected double HEIGHT = 0.75;

    protected double maxSpeedX = MAX_SPEED_X * Tiles.getSize();
    protected double maxSpeedY = MAX_SPEED_Y * Tiles.getSize();
    protected double accelX = ACCELERATION_X * Tiles.getSize();
    protected double accelY = ACCELERATION_Y * Tiles.getSize();
    protected double height = HEIGHT * Tiles.getSize();
    protected double width = WIDTH * Tiles.getSize();
    protected double gravity = GRAVITY * Tiles.getSize();

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
        double spawnXoffset = Tiles.getSize()/2;
        double spawnYoffset = Tiles.getSize()/2;
        this.tileX = tileX;
        this.tileY = tileY;
        this.spawnX = tileX*Tiles.getSize() + spawnXoffset;
        this.spawnY = tileY*Tiles.getSize() + spawnYoffset - TINY;
    }

    protected void setSize(double WIDTH, double HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.width = WIDTH * Tiles.getSize();
        this.height = HEIGHT * Tiles.getSize();
    }

    protected void setAccel(double ACCELERATION_X, double ACCELERATION_Y) {
        this.ACCELERATION_X = ACCELERATION_X;
        this.ACCELERATION_Y = ACCELERATION_Y;
        this.accelX = ACCELERATION_X * Tiles.getSize();
        this.accelY = ACCELERATION_Y * Tiles.getSize();
    }

    protected void setMaxSpeed(double MAX_SPEED_X, double MAX_SPEED_Y) {
        this.MAX_SPEED_X = MAX_SPEED_X;
        this.MAX_SPEED_Y = MAX_SPEED_Y;
        this.maxSpeedX = MAX_SPEED_X * Tiles.getSize();
        this.maxSpeedY = MAX_SPEED_Y * Tiles.getSize();
    }

    protected abstract void loadImages();

    public abstract int getType();

    public void fixedUpdate(ArrayList<PhysicsObject> objects) {
        // Spawn object when it's in spawnBounds
        boolean xSpawnBounds = (x - width/2 > Camera.getActive().getX() + Tiles.getSize()*2 + Main.getCanvasWidth()) || (x + width/2 < Camera.getActive().getX());
        boolean ySpawnBounds = (y - height/2 > Camera.getActive().getY() + Tiles.getSize()*2 + Main.getCanvasHeight()) || (y + height/2 < Camera.getActive().getY());
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
        boolean xBounds = (x > Camera.getActive().getX() + 1.5*Main.getCanvasWidth()) || (x < Camera.getActive().getX() - 1.5* Main.getCanvasWidth());
        boolean yBounds = (y > Camera.getActive().getY() + 2*Main.getCanvasHeight()) || (y < Camera.getActive().getY() - 2*Main.getCanvasHeight());
        boolean outOfBounds = xBounds || yBounds;

        // If spawn is on screen, dont' reset
        boolean spawnXBounds = (spawnX - width/2 > Camera.getActive().getX() + Main.getCanvasWidth()) || (spawnX + width/2 < Camera.getActive().getX());
        boolean spawnYBounds = (spawnY - height/2 > Camera.getActive().getY() + Main.getCanvasHeight()) || (spawnY + height/2 < Camera.getActive().getY());
        boolean spawnOutOfBounds = spawnXBounds || spawnYBounds;

        if (Editor.isEditMode() || (outOfBounds && spawnOutOfBounds)) {
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

        if (!Tiles.isSolid(onTile)) {
            return;
        }

        hardness = 1;


        double modX = x % Tiles.getSize();
        double modY = y % Tiles.getSize();

        if (modX < 0)
            modX += Tiles.getSize();
        if (modY < 0)
            modY += Tiles.getSize();

        if (dx < 0) {
            this.x += Tiles.getSize() - modX;
            onTileColX();
        }
        if (dx > 0) {
            this.x += -TINY - modX;
            onTileColX();
        }
        if (dy < 0) {
            this.y += Tiles.getSize() - modY;
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

    public void sensor(ArrayList<PhysicsObject> objects, ArrayList<PhysicsObject> toRemove) {
        // Fell into void
        if (y > (Tiles.getHeight() + 2)*Tiles.getSize()) {
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
    public void savePreviousState() {
        prevX = x;
        prevY = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getPrevX() { return prevX; }
    public double getPrevY() { return prevY; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getSpawnX() { return spawnX; }
    public double getSpawnY() { return spawnY; }
    public int getTileX() { return tileX; }
    public int getTileY() { return tileY; }


    // Visuals:
    public void draw(GraphicsContext gc, double cameraX, double cameraY, double alpha) {
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
            gc.drawImage(images.get(temp), drawX + Tiles.getSize()*(1 - scaleToWidth)/2, drawY + Tiles.getSize()*(1 - scaleToHeight)/2, flip*factor*scaleToWidth, factor*scaleToHeight);
        } catch (Exception e) {
        }

    }
}
