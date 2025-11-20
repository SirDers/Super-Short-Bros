package game;

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
        private long previousTime = 0; // Time passed last frame in nanoseconds
        private double accumulator = 0.0; //
        private static final double FIXED_DT = 1.0 / 60.0; // Logic update every second

        @Override
        public void handle(long now) {
            if (previousTime == 0) {
                previousTime = now;
                return;
            }

            double deltaTime = (now - previousTime) / 1_000_000_000.0; // Time between frames in seconds
            previousTime = now;
            deltaTime = Math.min(deltaTime, FIXED_DT);

            accumulator += deltaTime;

            // Fixed Update (loop for lag spikes)
            if (accumulator >= FIXED_DT) {
                savePreviousStates();
                fixedUpdate();
                accumulator -= FIXED_DT;
                Controls.endFrame();
            }

            // For interpolation in rendering
            double alpha = accumulator / FIXED_DT;
            alpha = Math.min(alpha, 1.0);

            // Update (every frame)
            update(alpha, deltaTime);
        }
    };

    @Override
    public void start(Stage stage) {
    	// Get computer screen resolution
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        CANVAS_WIDTH = (int) screenSize.getWidth();
        CANVAS_HEIGHT = (int) screenSize.getHeight();
        
        // Always have 21 tiles visible vertically
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

        // Setup objects before level load
        player.start(camera);
        
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

    private void savePreviousStates() {
        player.savePreviousState();
        for (PhysicsObject object : objects) {
            object.savePreviousState();
        }
    }

    private void fixedUpdate() {
        Editor.keyCheck(gc, player, objects);
        Tiles.editor(gc, textField, player, objects);
    	if (!player.isDead()) {
            // Update objects
            player.fixedUpdate(objects);
            for (PhysicsObject object : objects) {
                object.fixedUpdate(objects);
            }
            
            // Resolve physics
            if (!Editor.editMode) {
            	toRemove.clear();
            	player.sensor(objects, toRemove);
            	for (PhysicsObject object : objects) {
            	    object.sensor(objects, toRemove);
            	}
            	objects.removeAll(toRemove);
            }
    	} else {
    		player.playDead(objects);
    	}
    }

    private void update(double alpha, double dt) {
        if (!player.isDead()) camera.update(player, alpha, dt);
        render(alpha);
    }

    private void render(double alpha) {
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
        Tiles.renderEdit(gc, player);
        
        // Draw objects
        for (PhysicsObject object : objects) {
            object.draw(gc, camera.getX(), camera.getY(), alpha);
        }
        player.draw(gc, camera.getX(), camera.getY(), alpha);
        
        Editor.render(gc);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
