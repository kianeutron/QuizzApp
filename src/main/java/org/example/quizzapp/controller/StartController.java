package org.example.quizzapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.quizzapp.QuizApplication;

import java.io.IOException;

/**
 * Controller for the start screen of the Quiz Application.
 * Provides navigation to the main menu.
 */
public class StartController {
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private Label descriptionLabel;
    
    @FXML
    private Button startButton;
    
    /**
     * Initializes the start screen.
     */
    @FXML
    private void initialize() {
        titleLabel.setText("Quiz Application");
        descriptionLabel.setText("Test your knowledge with interactive quizzes!");
    }
    
    /**
     * Handles the start button click to navigate to the main menu.
     */
    @FXML
    private void handleStartButton() {
        try {
            QuizApplication.switchScene("view/menu-view.fxml", "Quiz Application - Menu");
        } catch (IOException e) {
            System.err.println("Error loading menu view: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
