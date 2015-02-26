package openLauncher;

import javafx.application.Application;
import javafx.stage.Stage;
import openLauncher.launcher.SpalshScreen;

public class Main extends Application {

    public static void main(String[] args) throws Exception {
       // LauncherGui.main(args);
        System.out.println("Starting");
        Main.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        SpalshScreen.start(stage, this);
    }
}
