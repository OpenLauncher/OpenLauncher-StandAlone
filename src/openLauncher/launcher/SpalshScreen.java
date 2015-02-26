package openLauncher.launcher;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import openLauncher.Main;

public class SpalshScreen {

	public static void start(Stage stage, Main main) throws Exception {
		Parent root = FXMLLoader.load(main.getClass().getResource("launcher/SplashScreen.fxml"));

		Scene scene = new Scene(root, 400, 250);
		stage.setTitle("Open Launcher");
		stage.setScene(scene);
		stage.show();
	}
}
