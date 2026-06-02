package animation;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import world.Tiles;

public class PlayerAnimationController {
    private final Map<AnimationState, SpriteAnimation> animations = new HashMap<>();
    private final Map<String, Image> chessPieces = new HashMap<>();

    public void register(AnimationState state, SpriteAnimation animation) {
        animations.put(state, animation);
    }

    public void registerChessPiece(String piece, Image image) {
        chessPieces.put(piece, image);
    }

    public void drawState(GraphicsContext gc, AnimationState state, double spriteFrame, double drawX, double drawY, int flip) {
        SpriteAnimation animation = animations.get(state);
        if (animation == null) return;
        drawImage(gc, animation.frame(spriteFrame), drawX, drawY, flip);
    }

    public void drawChessPiece(GraphicsContext gc, String piece, double drawX, double drawY, int flip) {
        Image image = chessPieces.getOrDefault(piece, chessPieces.get("pawn"));
        drawImage(gc, image, drawX, drawY, flip);
    }

    private void drawImage(GraphicsContext gc, Image image, double drawX, double drawY, int flip) {
        double sizeFactor = Tiles.getSize() * 2.5;
        double spritePosX = sizeFactor * (0.375 - flip * 1.25) / 2.5;
        double spritePosY = sizeFactor / 5.25;
        gc.drawImage(image, drawX + spritePosX, drawY - spritePosY, flip * sizeFactor, sizeFactor);
    }
}
