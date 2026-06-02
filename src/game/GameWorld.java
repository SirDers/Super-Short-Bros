package game;

import game.state.Editor;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import world.Tiles;

public class GameWorld {
    private final Player player;
    private final ArrayList<PhysicsObject> objects = new ArrayList<>();
    private final ArrayList<PhysicsObject> toRemove = new ArrayList<>();

    public GameWorld(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<PhysicsObject> getObjects() {
        return objects;
    }

    public void savePreviousStates() {
        player.savePreviousState();
        for (PhysicsObject object : objects) {
            object.savePreviousState();
        }
    }

    public void fixedUpdate(GraphicsContext gc, TextField textField) {
        Editor.keyCheck(gc, player, objects);
        Tiles.editor(gc, textField, player, objects);
        if (!player.isDead()) {
            updateObjects();
            resolveObjectSensors();
        } else {
            player.playDead(objects);
        }
    }

    private void updateObjects() {
        player.fixedUpdate(objects);
        for (PhysicsObject object : objects) {
            object.fixedUpdate(objects);
        }
    }

    private void resolveObjectSensors() {
        if (Editor.isEditMode()) return;

        toRemove.clear();
        player.sensor(objects, toRemove);
        for (PhysicsObject object : objects) {
            object.sensor(objects, toRemove);
        }
        objects.removeAll(toRemove);
    }
}
