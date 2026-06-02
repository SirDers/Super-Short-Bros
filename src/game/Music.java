package game;

import world.Tiles;

import java.nio.file.Paths;
import javafx.animation.AnimationTimer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import game.state.Editor;

public class Music {

    private static MediaPlayer currentPlayer;
    private static MediaPlayer nextPlayer;

    private static String currentSongPath;
    private static String editSongPath;

    public static void setup(Stage stage) {
        try {
    		Music.setMusic(Tiles.getTheme());

            new Thread(() -> {
                boolean previousEditMode = Editor.isEditMode();
                while (stage.isShowing()) {
                    if (Editor.isEditMode() != previousEditMode) {
                        previousEditMode = Editor.isEditMode();

                        double currentTime = currentPlayer.getCurrentTime().toSeconds();

                        String newSongPath = Editor.isEditMode() ? editSongPath : currentSongPath;
                		if (Tiles.getTheme() != 4 && Tiles.getTheme() != 2)
                			fadeTransitionSimultaneous(newSongPath, currentTime);
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMusic(int type) {
    	
        currentSongPath = Paths.get("resources/music/level" + type + ".mp3").toUri().toString();
        editSongPath = Paths.get("resources/music/level" + type +"edit.mp3").toUri().toString();

        if (currentPlayer != null) {
            currentPlayer.stop();
        }
        if (type == 4 || type == 2) {
            playSong(currentSongPath, 0);
            return;
        }
        playSong(Editor.isEditMode() ? editSongPath : currentSongPath, 0);
    }

    private static void playSong(String songPath, double startTime) {
        Media media = new Media(songPath);
        currentPlayer = new MediaPlayer(media);

        currentPlayer.setOnEndOfMedia(() -> currentPlayer.seek(javafx.util.Duration.seconds(5)));

        currentPlayer.setOnReady(() -> {
            currentPlayer.seek(javafx.util.Duration.seconds(startTime));
            currentPlayer.play();
        });

        currentPlayer.setVolume(1.0);
    }

    private static void fadeTransitionSimultaneous(String newSongPath, double startTime) {
        Media media = new Media(newSongPath);
        nextPlayer = new MediaPlayer(media);
        nextPlayer.setVolume(0);
        nextPlayer.setOnEndOfMedia(() -> nextPlayer.seek(javafx.util.Duration.seconds(5)));
        nextPlayer.setOnReady(() -> {
            nextPlayer.seek(javafx.util.Duration.seconds(startTime));
            nextPlayer.play();

            AnimationTimer fadeTimer = new AnimationTimer() {
                private double volume = 1.0;
                private double nextVolume = 0.0;

                @Override
                public void handle(long now) {
                    volume -= 0.05;
                    nextVolume += 0.05;

                    if (volume <= 0) {
                        currentPlayer.stop();
                        currentPlayer = nextPlayer;
                        this.stop();
                    } else {
                        currentPlayer.setVolume(Math.max(0, volume));
                        nextPlayer.setVolume(Math.min(1.0, nextVolume));
                    }
                }
            };
            fadeTimer.start();
        });
    }
}
