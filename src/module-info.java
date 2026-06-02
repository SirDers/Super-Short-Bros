module JavaFXProject {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.media;
	requires javafx.fxml;
	
	opens core to javafx.graphics, javafx.fxml;
	opens game to javafx.graphics, javafx.fxml;
}
