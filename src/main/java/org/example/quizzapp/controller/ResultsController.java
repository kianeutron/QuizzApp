package org.example.quizzapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.example.quizzapp.QuizApplication;
import org.example.quizzapp.model.PlayerResult;
import org.example.quizzapp.model.QuizResult;
import org.example.quizzapp.service.CsvExporter;
import org.example.quizzapp.service.GameManager;
import org.example.quizzapp.service.QuizService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
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
    private Button exportButton;
    
    @FXML
    private Button backToMenuButton;
    
    @FXML
    private Button playAgainButton;
    
    private final GameManager gameManager = GameManager.getInstance();
    private final QuizService quizService = new QuizService();
    private final CsvExporter csvExporter = new CsvExporter();
    private QuizResult currentQuizResult;
    
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
        // Show integer percentage without decimals per exam spec
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("scorePercentage"));
        // Show date only (yyyy-MM-dd) per example
        dateColumn.setCellValueFactory(cellData -> {
            PlayerResult result = cellData.getValue();
            if (result.getDate() != null) {
                String formattedDate = result.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
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
        
        // Calculate current player's result
        PlayerResult currentResult = gameManager.calculateFinalScore();
        
        // Save result only if not in practice mode
        if (!gameManager.isPracticeMode()) {
            try {
                quizService.saveResult(gameManager.getCurrentQuiz().getTitle(), currentResult);
            } catch (IOException e) {
                System.err.println("Error saving result: " + e.getMessage());
            }
        }
        
        // Set quiz name with mode indicator
        String quizTitle = gameManager.getCurrentQuiz().getTitle();
        String modeIndicator = gameManager.isPracticeMode() ? " (Practice Mode)" : "";
        titleLabel.setText(String.format("Quiz name: %s%s", quizTitle, modeIndicator));

        // Display completion message: prefer completedHtml if provided
        String completionMessage = (gameManager.getCurrentQuiz().getCompletedHtml() != null && !gameManager.getCurrentQuiz().getCompletedHtml().isBlank())
                ? processCompletionMessage(gameManager.getCurrentQuiz().getCompletedHtml(), currentResult)
                : generateCompletionMessage(currentResult);
        completionMessageLabel.setText(completionMessage);
        
        // Display current player's score as integer percentage without decimals
        playerScoreLabel.setText(String.format("Your score: %s", currentResult.getScorePercentage()));
        
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
     * Processes the completion message by replacing template variables and removing HTML tags.
     */
    private String processCompletionMessage(String htmlMessage, PlayerResult result) {
        // Replace template variables with actual values
        String processed = htmlMessage
                .replace("{correctAnswers}", String.valueOf(result.getCorrectQuestions()))
                .replace("{questionCount}", String.valueOf(result.getTotalQuestions()));
        
        // Remove HTML tags
        return processed.replaceAll("<[^>]*>", "").trim();
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

            currentQuizResult = quizService.loadResults(quizTitle);
            System.out.println("Loaded quiz result: " + currentQuizResult);
            System.out.println("Number of results: " + currentQuizResult.getResults().size());

            ObservableList<PlayerResult> results = FXCollections.observableArrayList(
                currentQuizResult.getResultsSortedByScore()
            );
            System.out.println("Setting " + results.size() + " results to table");

            // Debug: print each result
            for (PlayerResult result : results) {
                System.out.println("Result: " + result.getPlayerName() + " - " + result.getScoreString() + " - " + result.getDate());
            }

            resultsTable.setItems(results);
            System.out.println("Table items set. Table has " + resultsTable.getItems().size() + " items");
            
            if (gameManager.isPracticeMode()) {
                System.out.println("Practice mode: Results are not saved to leaderboard");
            }
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
     * Handles exporting leaderboard to CSV.
     */
    @FXML
    private void handleExport() {
        if (gameManager.isPracticeMode()) {
            showAlert(Alert.AlertType.WARNING, "Practice Mode", 
                "Cannot export in practice mode", "Results are not saved in practice mode, so there is nothing to export.");
            return;
        }
        
        if (currentQuizResult == null || currentQuizResult.getResults().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Data", 
                "No results to export", "There are no quiz results to export.");
            return;
        }
        
        try {
            String fileName = generateDefaultFileName();
            File file = showExportFileDialog(fileName);
            
            if (file != null) {
                csvExporter.exportToCsv(currentQuizResult, file.toPath());
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                    "Export successful", "Leaderboard exported to: " + file.getAbsolutePath());
            }
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid File", 
                "Invalid file selection", e.getMessage());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Export Error", 
                "Failed to export leaderboard", "Error: " + e.getMessage());
        }
    }
    
    /**
     * Shows file chooser dialog for CSV export.
     *
     * @param defaultFileName Default file name for the export
     * @return Selected file or null if cancelled
     */
    private File showExportFileDialog(String defaultFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Leaderboard to CSV");
        fileChooser.setInitialFileName(defaultFileName);
        
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(csvFilter);
        fileChooser.setSelectedExtensionFilter(csvFilter);
        
        return fileChooser.showSaveDialog(exportButton.getScene().getWindow());
    }
    
    /**
     * Generates a default file name for CSV export.
     *
     * @return Default file name with quiz title and current date
     */
    private String generateDefaultFileName() {
        String quizName = currentQuizResult.getName()
                                          .toLowerCase()
                                          .replaceAll("[^a-z0-9]", "-")
                                          .replaceAll("-+", "-")
                                          .replaceAll("^-|-$", "");
        String timestamp = java.time.LocalDate.now().toString();
        return quizName + "-" + timestamp + ".csv";
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
