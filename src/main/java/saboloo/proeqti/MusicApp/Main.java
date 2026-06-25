package saboloo.proeqti.MusicApp;

import saboloo.proeqti.MusicApp.ui.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        stage.setTitle("Music App");
        showScene(new Scene(new Login().getRoot(), 420, 360));
        stage.show();
    }


    public static void showScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    public static Stage getStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
