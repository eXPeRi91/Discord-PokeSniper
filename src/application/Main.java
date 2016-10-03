package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import resources.DPSUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/MyView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("DiscordPokeSniper " + DPSUtils.getVersion());
			primaryStage.show();
			primaryStage.setMinWidth(677.0);        
			primaryStage.setMinHeight(540.0);
			primaryStage.setMaxWidth(677.0);        
			primaryStage.setMaxHeight(540.0);
			primaryStage.setOnCloseRequest(e -> {
				Platform.exit();
				System.exit(0);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DPSUtils.setCurrentDirectoryLocation();
		DPSUtils.loadSnipingPokemon();
		launch(args);
	}

}
