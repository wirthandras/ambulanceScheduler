package application;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Model;

public class Main extends Application {

	private AnchorPane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			
			
			
			loader.setLocation(getClass().getResource("MainScreen.fxml"));

			rootLayout = (AnchorPane) loader.load();
			Scene scene = new Scene(rootLayout, 700, 700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
			
			Model m = new Model();
			System.out.println(m.toString());
			
			
			

//			Parent root = (Parent)loader.load();          
			MainController controller = loader.<MainController>getController();
			controller.setModel(m);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
