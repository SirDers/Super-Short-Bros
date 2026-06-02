/*
 * README:
 *
 * This game functions very well, but the programming underneath is NOT good.
 *
 * When I first started making this project, I planned on making a very simple
 * platformer with no tile-scrolling, and tiles that are either just 0 or 1,
 * (1 being ground, 0 being air).
 *
 * Development went much faster than I expected, so I just kept adding to it:
 * including tile-scrolling, level saving/loading, art, animations, importing
 * third-party audio, etc.
 *
 * Because I was so focused on development and adding new things to the project,
 * alongside learning Java and JavaFX, you will find terrible coding practices
 * and wrong use of standard Java coding conventions, as well as a lack of
 * comments.
 *
 * With every update, I still add to the project, and only occasionally
 * restructure some parts of the code if it's quick and easy to make better;
 * sometimes adding more comments.
 *
 * I'm more focused on my current projects, so please take a look at those to
 * see how I've improved as a software engineer.
 */

package core;

import game.Music;
import game.GameWorld;
import game.Player;
import game.Store;
import game.state.Editor;
import input.Controls;
import input.Mouse;
import render.Backdrop;
import render.Camera;
import render.GameRenderer;
import world.Tiles;

import java.io.IOException;
import java.awt.Dimension;
import java.awt.Toolkit;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    private static int TILE_COUNT_X = 33; // Visible tiles horizontally
    private static int TILE_COUNT_Y = 21; // Visible tiles vertically
    private static int CANVAS_WIDTH;
    private static int CANVAS_HEIGHT;

    private Canvas canvas;
    private StackPane root;
    private GraphicsContext gc;
    private Camera camera;
    private Backdrop backdrop;
    private Backdrop layer0;
    private Backdrop layer1;

    private GameWorld world;
    private GameRenderer renderer;

    // TextField for Edit Mode (Not yet used)
    private TextField textField = new TextField();

    private FixedStepGameLoop gameLoop;

    public static int getCanvasWidth() {
        return CANVAS_WIDTH;
    }

    public static int getCanvasHeight() {
        return CANVAS_HEIGHT;
    }

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
        Tiles.setSize((int) (CANVAS_HEIGHT / (TILE_COUNT_Y - 1)));

        // Weird glitch where if Tiles.getSize() is less than 29 it crashes?
        if (Tiles.getSize() < 29) Tiles.setSize(29);


        // Initialize Canvas and GraphicsContext
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.getGraphicsContext2D().setImageSmoothing(false);
        gc = canvas.getGraphicsContext2D();

        // Initialize game components
        Player player = new Player(0, 0, 0.75, 1.75, 0.75/128, 637.0/1920, 35.0/128, 7.0/16);
        world = new GameWorld(player);
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
        world.getPlayer().start(camera);

        // Set and load level
        Tiles.setLevel(1);

        try {
            Store.loadLevel(Tiles.getLevel(), world.getPlayer(), world.getObjects(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        renderer = new GameRenderer(gc, camera, backdrop, layer0, layer1, textField, TILE_COUNT_X, TILE_COUNT_Y);

        // Initialize Music after stage
        Music.setup(stage);

        gameLoop = new FixedStepGameLoop(this::fixedUpdate, this::update);

        // Start game loop
        gameLoop.start();
    }

    private void fixedUpdate() {
        world.savePreviousStates();
        world.fixedUpdate(gc, textField);
    }

    private void update(double alpha, double dt) {
        if (!world.getPlayer().isDead()) camera.update(world.getPlayer(), alpha, dt);
        renderer.render(world, alpha);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
