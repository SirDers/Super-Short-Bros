/*
VERSION 0.4.8
- Level 5
- Better organized Player draw() and animate() scriptsdw
	
*/

package application;

import java.io.IOException;import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private static int TILE_COUNT_X = 33; // Visible tiles horizontally
    private static int TILE_COUNT_Y = 21; // Visible tiles vertically
    public static int CANVAS_WIDTH;
    public static int CANVAS_HEIGHT;

    private Canvas canvas;
    private StackPane root;
    private GraphicsContext gc;
    private Camera camera;
    private Backdrop backdrop;
    private Backdrop layer0;
    private Backdrop layer1;

    private Player player;
    private ArrayList<PhysicsObject> objects = new ArrayList<PhysicsObject>();
    private ArrayList<PhysicsObject> toRemove = new ArrayList<>();

    // TextField for Edit Mode (Not yet used)
    private TextField textField = new TextField();
    
    AnimationTimer gameLoop = new AnimationTimer() {
        @Override
        public void handle(long now) {
            update();
            render();
        }
    };
    
    @Override
    public void start(Stage stage) {
    	// Get computer screen resolution
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        CANVAS_WIDTH = (int) screenSize.getWidth();
        CANVAS_HEIGHT = (int) screenSize.getHeight();
        

        //CANVAS_WIDTH /= 2;
        //CANVAS_HEIGHT /= 2;
        
        // Always have 21 tiles visible vertically
        // Wider screen = more horizontal tiles visible
        TILE_COUNT_X = TILE_COUNT_Y * CANVAS_WIDTH / CANVAS_HEIGHT;
        

        // Set tile size relative to resolution
    	Tiles.SIZE = (int) (CANVAS_HEIGHT / (TILE_COUNT_Y - 1));
        
        // Weird glitch where if Tiles.SIZE is less than 29 it crashes?
        if (Tiles.SIZE < 29) Tiles.SIZE = 29;
        
        
        // Initialize Canvas and GraphicsContext
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.getGraphicsContext2D().setImageSmoothing(false);
        gc = canvas.getGraphicsContext2D();
        
        // Initialize game components
        player = new Player(0, 0, 0.75, 1.75, 0.75/128, 637.0/1920, 35.0/128, 7.0/16);
        camera = new Camera(CANVAS_WIDTH, CANVAS_HEIGHT);
        backdrop = new Backdrop();
        layer0 = new Backdrop();
        layer1 = new Backdrop();

        

        // Setup textField
        textField.setPromptText("Enter text here");
        textField.setMaxWidth(200);
        
        textField.setOnAction(e -> {
            String text = textField.getText();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.fillText(text, 100, 100);
        });
        
        textField.setVisible(false);
        textField.setManaged(false);
        
        // Add Canvas to the Scene
        root = new StackPane();
        root.getChildren().addAll(canvas, textField);
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Super Short Bros.");
        stage.show();
        stage.setFullScreen(true);
        
        // Handle input
        Controls.setup(scene);
        Mouse.setup(scene);
        
        // Set and load level
        Tiles.level = 1;
        
        try {
			Store.loadLevel(Tiles.level, player, objects, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        // Initialize Music after stage
        Music.setup(stage);
        
        // Start game loop
        gameLoop.start();
    }

    private void update() {
		Controls.checkControls();
        Editor.keyCheck(gc, player, objects);
    	if (!player.isDead()) {
            
            // Update objects
            player.update(objects);
            for (PhysicsObject object : objects) {
                object.update(objects);
            }
            
            // Check object collision
            if (!Editor.editMode) {
            	toRemove.clear();
            	player.sensor(objects, toRemove);
            	for (PhysicsObject object : objects) {
            	    object.sensor(objects, toRemove);
            	}
            	objects.removeAll(toRemove);
            }
            camera.update(player);
    	} else {
    		player.playDead(objects);
    	}
    }

    private void render() {
    	gc.setFill(Color.rgb(100, 200, 250));
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        
        backdrop.draw(gc, camera.getX(), camera.getY(), 0, 1);
        layer1.draw(gc, camera.getX(), camera.getY(), 0.2, 2);
        layer0.draw(gc, camera.getX(), camera.getY()-80, 0.4, 3);
        
        // Temporary loop
        layer1.draw(gc, camera.getX() - 64/0.2 * Tiles.SIZE, camera.getY(), 0.2, 2);
        layer0.draw(gc, camera.getX() - 64/0.4 * Tiles.SIZE, camera.getY()-80, 0.4, 3);
        
        // Darken background
        gc.setFill(Color.rgb(0, 0, 0, 0.2));
        if (Tiles.theme == 4) gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        
        Tiles.render(gc, textField, player, objects, camera.getX(), camera.getY(), TILE_COUNT_X, TILE_COUNT_Y);
        
        // Draw objects
        for (PhysicsObject object : objects) {
            object.draw(gc, camera.getX(), camera.getY());
        }
        player.draw(gc, camera.getX(), camera.getY());
        
        Editor.render(gc);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
