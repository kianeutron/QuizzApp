package org.example.quizzapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.quizzapp.QuizApplication;
import org.example.quizzapp.model.PlayerResult;
import org.example.quizzapp.model.QuizResult;
import org.example.quizzapp.service.GameManager;
import org.example.quizzapp.service.QuizService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the results screen.
 * Displays quiz completion results and high scores.
 */
public class ResultsController {
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private Label completionMessageLabel;
    
    @FXML
    private Label playerScoreLabel;
    
    @FXML
    private TableView<PlayerResult> resultsTable;
    
    @FXML
    private TableColumn<PlayerResult, String> playerNameColumn;
    
    @FXML
    private TableColumn<PlayerResult, String> scoreColumn;
    
    @FXML
    private TableColumn<PlayerResult, String> dateColumn;
    
    @FXML
    private Button backToMenuButton;
    
    @FXML
    private Button playAgainButton;
    
    private final GameManager gameManager = GameManager.getInstance();
    private final QuizService quizService = new QuizService();
    
    /**
     * Initializes the results screen.
     */
    @FXML
    private void initialize() {
        try {
            System.out.println("ResultsController: Starting initialization");
            setupTable();
            System.out.println("ResultsController: Table setup complete");
            displayResults();
            System.out.println("ResultsController: Results display complete");
        } catch (Exception e) {
            System.err.println("Error in ResultsController initialization: " + e.getMessage());
            e.printStackTrace();
            // Set basic fallback content
            if (completionMessageLabel != null) {
                completionMessageLabel.setText("Error loading results");
            }
            if (playerScoreLabel != null) {
                playerScoreLabel.setText("Please try again");
            }
        }
    }
    
    /**
     * Sets up the results table columns.
     */
    private void setupTable() {
        // Set up table columns with proper cell value factories
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("scoreString"));
        dateColumn.setCellValueFactory(cellData -> {
            PlayerResult result = cellData.getValue();
            if (result.getDate() != null) {
                String formattedDate = result.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                return new javafx.beans.property.SimpleStringProperty(formattedDate);
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
    }
    
    /**
     * Displays the quiz results.
     */
    private void displayResults() {
        try {
            if (gameManager.getCurrentQuiz() == null) {
                completionMessageLabel.setText("No quiz completed");
                playerScoreLabel.setText("");
                return;
            }
        
        // Calculate and save current player's result
        PlayerResult currentResult = gameManager.calculateFinalScore();
        
        try {
            quizService.saveResult(gameManager.getCurrentQuiz().getTitle(), currentResult);
        } catch (IOException e) {
            System.err.println("Error saving result: " + e.getMessage());
        }
        
        // Display completion message
        String completionMessage = generateCompletionMessage(currentResult);
        completionMessageLabel.setText(completionMessage);
        
        // Display current player's score
        playerScoreLabel.setText(String.format("Your Score: %s", currentResult.getScoreString()));
        
        // Load and display all results
        loadHighScores();
        } catch (Exception e) {
            System.err.println("Error in displayResults: " + e.getMessage());
            e.printStackTrace();
            completionMessageLabel.setText("Error loading results");
            playerScoreLabel.setText("Please try again");
        }
    }
    
    /**
     * Generates a completion message based on the player's performance.
     */
    private String generateCompletionMessage(PlayerResult result) {
        int correct = result.getCorrectQuestions();
        int total = result.getTotalQuestions();
        
        if (correct == 0) {
            return "Unfortunately, none of your answers are correct. Please try again.";
        } else if (correct == total) {
            return "Excellent! You answered all questions correctly 🎉";
        } else {
            return String.format("You got %d out of %d correct.", correct, total);
        }
    }
    
    /**
     * Loads and displays high scores for the current quiz.
     */
    private void loadHighScores() {
        if (gameManager.getCurrentQuiz() == null) {
            return;
        }
        
        try {
            String quizTitle = gameManager.getCurrentQuiz().getTitle();
            System.out.println("Loading results for quiz: " + quizTitle);

            QuizResult quizResult = quizService.loadResults(quizTitle);
            System.out.println("Loaded quiz result: " + quizResult);
            System.out.println("Number of results: " + quizResult.getResults().size());

            ObservableList<PlayerResult> results = FXCollections.observableArrayList(
                quizResult.getResultsSortedByScore()
            );
            System.out.println("Setting " + results.size() + " results to table");

            // Debug: print each result
            for (PlayerResult result : results) {
                System.out.println("Result: " + result.getPlayerName() + " - " + result.getScoreString() + " - " + result.getDate());
            }

            resultsTable.setItems(results);
            System.out.println("Table items set. Table has " + resultsTable.getItems().size() + " items");
        } catch (Exception e) {
            System.err.println("Error loading high scores: " + e.getMessage());
            e.printStackTrace();
            resultsTable.setItems(FXCollections.observableArrayList());
        }
    }
    
    /**
     * Handles going back to the main menu.
     */
    @FXML
    private void handleBackToMenu() {
        try {
            QuizApplication.switchScene("view/menu-view.fxml", "Quiz Application - Menu");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", 
                "Failed to load menu view", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Handles playing the quiz again.
     */
    @FXML
    private void handlePlayAgain() {
        if (gameManager.getCurrentQuiz() != null) {
            try {
                // Reset the game state
                gameManager.resetGame();
                QuizApplication.switchScene("view/game-view.fxml", "Quiz Application - Game");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", 
                    "Failed to restart game", "Error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            handleBackToMenu();
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
