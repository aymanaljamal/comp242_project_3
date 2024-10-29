module comp242_project_3 {
	requires javafx.controls;
	requires javafx.graphics;
	requires java.base;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml,javafx.base;
}
