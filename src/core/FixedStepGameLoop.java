package core;

import input.Controls;
import javafx.animation.AnimationTimer;

public class FixedStepGameLoop extends AnimationTimer {
    public static final double FIXED_DT = 1.0 / 60.0;
    private static final double MAX_FRAME_TIME = 0.25;

    private final Runnable fixedUpdate;
    private final FrameRenderer renderer;
    private long previousTime = 0;
    private double accumulator = 0.0;

    public FixedStepGameLoop(Runnable fixedUpdate, FrameRenderer renderer) {
        this.fixedUpdate = fixedUpdate;
        this.renderer = renderer;
    }

    @Override
    public void handle(long now) {
        if (previousTime == 0) {
            previousTime = now;
            return;
        }

        double deltaTime = (now - previousTime) / 1_000_000_000.0;
        previousTime = now;
        deltaTime = Math.min(deltaTime, MAX_FRAME_TIME);
        accumulator += deltaTime;

        while (accumulator >= FIXED_DT) {
            fixedUpdate.run();
            accumulator -= FIXED_DT;
            Controls.endFrame();
        }

        double alpha = Math.min(accumulator / FIXED_DT, 1.0);
        renderer.render(alpha, deltaTime);
    }

    @FunctionalInterface
    public interface FrameRenderer {
        void render(double alpha, double deltaTime);
    }
}
