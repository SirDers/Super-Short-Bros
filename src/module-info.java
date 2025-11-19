module JavaFXProject {
	requires javafx.controls;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.media;
	
	opens game to javafx.graphics, javafx.fxml;
}
