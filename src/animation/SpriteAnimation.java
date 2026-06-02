package animation;

import javafx.scene.image.Image;

public class SpriteAnimation {
    private final Image[] frames;

    public SpriteAnimation(Image... frames) {
        this.frames = frames;
    }

    public Image frame(double spriteFrame) {
        if (frames.length == 1) return frames[0];
        int index = ((int) spriteFrame) % frames.length;
        return frames[index];
    }
}
