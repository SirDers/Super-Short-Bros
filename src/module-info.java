module JavaFXProject {
	requires javafx.controls;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.media;
	requires javafx.fxml;
	
	opens game to javafx.graphics, javafx.fxml;
}
