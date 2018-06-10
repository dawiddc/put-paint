package org.dawiddc.paint;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends javafx.application.Application {

    public static void Main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = new File("src/main/java/org/dawiddc/paint/view/MainPane.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("PUT Paint");
        primaryStage.setScene(new Scene(root, 1366.0, 768.0));
        primaryStage.getIcons().add(new Image("assets/logo.png"));
        primaryStage.show();
    }
}