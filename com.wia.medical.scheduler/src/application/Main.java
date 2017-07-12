package application;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Model;

public class Main extends Application {

	private AnchorPane rootLayout;
	
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			
			this.primaryStage = primaryStage;
			
			loader.setLocation(getClass().getResource("MainScreen.fxml"));

			rootLayout = (AnchorPane) loader.load();
			Scene scene = new Scene(rootLayout, 700, 700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			this.primaryStage.setScene(scene);
			this.primaryStage.show();
			
			
			
			Model m = new Model();
			System.out.println(m.toString());
			
			
			

//			Parent root = (Parent)loader.load();          
			MainController controller = loader.<MainController>getController();
			controller.setData(m, this.primaryStage);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
