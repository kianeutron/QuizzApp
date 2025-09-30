package org.example.quizzapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import org.example.quizzapp.QuizApplication;
import org.example.quizzapp.model.Quiz;
import org.example.quizzapp.service.QuizService;

import java.io.File;
import java.io.IOException;

/**
 * Controller for the main menu screen.
 * Handles quiz loading and navigation to game screen.
 */
public class MenuController {
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private Button loadQuizButton;
    
    @FXML
    private Button startQuizButton;
    
    @FXML
    private Button viewResultsButton;
    
    @FXML
    private Label quizInfoLabel;
    
    private Quiz loadedQuiz;
    private final QuizService quizService = new QuizService();
    
    /**
     * Initializes the menu screen.
     */
    @FXML
    private void initialize() {
        startQuizButton.setDisable(true);
        quizInfoLabel.setText("No quiz loaded");
    }
    
    /**
     * Handles loading a quiz from JSON file.
     */
    @FXML
    private void handleLoadQuiz() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Quiz File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        
        File selectedFile = fileChooser.showOpenDialog(QuizApplication.getPrimaryStage());
        
        if (selectedFile != null) {
            try {
                loadedQuiz = quizService.loadQuiz(selectedFile);
                
                // Update UI
                startQuizButton.setDisable(false);
                quizInfoLabel.setText(String.format("Loaded: %s (%d questions)", 
                    loadedQuiz.getTitle(), loadedQuiz.getTotalQuestions()));
                
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                    "Quiz loaded successfully!", 
                    String.format("Quiz: %s\nQuestions: %d", 
                        loadedQuiz.getTitle(), loadedQuiz.getTotalQuestions()));
                
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", 
                    "Failed to load quiz", 
                    "Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Handles starting the quiz.
     */
    @FXML
    private void handleStartQuiz() {
        if (loadedQuiz != null) {
            try {
                // Pass the quiz to GameManager and switch to game view
                org.example.quizzapp.service.GameManager.getInstance().loadQuiz(loadedQuiz);
                QuizApplication.switchScene("view/game-view.fxml", "Quiz Application - Game");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", 
                    "Failed to start quiz", 
                    "Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Handles viewing results.
     */
    @FXML
    private void handleViewResults() {
        try {
            QuizApplication.switchScene("view/results-view.fxml", "Quiz Application - Results");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", 
                "Failed to load results view", 
                "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Shows an alert dialog.
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
