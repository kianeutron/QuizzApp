package org.example.quizzapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class QuizApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(QuizApplication.class.getResource("view/start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // Add CSS styling
        scene.getStylesheets().add(QuizApplication.class.getResource("styles/application.css").toExternalForm());

        stage.setTitle("Quiz Application");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();
    }

    /**
     * Gets the primary stage for scene switching.
     *
     * @return The primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Switches to a new scene.
     *
     * @param fxmlPath Path to the FXML file
     * @param title Window title
     * @throws IOException If FXML file cannot be loaded
     */
    public static void switchScene(String fxmlPath, String title) throws IOException {
        // Try to load resource with absolute path first
        URL resourceUrl = QuizApplication.class.getResource("/org/example/quizzapp/" + fxmlPath);
        if (resourceUrl == null) {
            // Fallback to relative path
            resourceUrl = QuizApplication.class.getResource(fxmlPath);
        }
        if (resourceUrl == null) {
            throw new IOException("Could not find FXML file: " + fxmlPath);
        }
        FXMLLoader loader = new FXMLLoader(resourceUrl);
        Scene scene = new Scene(loader.load());

        // Add CSS styling to new scene
        scene.getStylesheets().add(QuizApplication.class.getResource("styles/application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
    }
}
