package org.example.quizzapp.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.quizzapp.QuizApplication;
import org.example.quizzapp.model.Question;
import org.example.quizzapp.model.RadioGroupQuestion;
import org.example.quizzapp.model.BooleanQuestion;
import org.example.quizzapp.service.GameManager;

import java.io.IOException;

/**
 * Controller for the game screen where players answer questions.
 * Handles player name input, question display, timer, and answer submission.
 */
public class GameController {
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private TextField playerNameField;
    
    @FXML
    private Button startGameButton;
    
    @FXML
    private VBox nameInputSection;
    
    @FXML
    private VBox gameSection;
    
    @FXML
    private Label questionNumberLabel;
    
    @FXML
    private Label questionTitleLabel;
    
    @FXML
    private VBox questionContainer;
    
    @FXML
    private Button submitAnswerButton;
    
    @FXML
    private Label scoreLabel;
    
    @FXML
    private ProgressBar timerProgressBar;
    
    @FXML
    private Label timerLabel;
    
    private final GameManager gameManager = GameManager.getInstance();
    private ToggleGroup answerToggleGroup;
    private Timeline timer;
    private int timeRemaining;
    private Object currentAnswer;
    
    /**
     * Initializes the game screen.
     */
    @FXML
    private void initialize() {
        // Initially show name input section
        gameSection.setVisible(false);
        gameSection.setManaged(false);
        
        // Bind score label to GameManager score property
        scoreLabel.textProperty().bind(gameManager.scoreProperty().asString("Score: %d"));
        
        // Set up submit button state
        submitAnswerButton.setDisable(true);
    }
    
    /**
     * Handles starting the game after player enters name.
     */
    @FXML
    private void handleStartGame() {
        String playerName = playerNameField.getText().trim();
        
        if (playerName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Name", 
                "Please enter your name", "A name is required to start the quiz.");
            return;
        }
        
        // Set player name in GameManager
        gameManager.setPlayerName(playerName);
        
        // Hide name input and show game section
        nameInputSection.setVisible(false);
        nameInputSection.setManaged(false);
        gameSection.setVisible(true);
        gameSection.setManaged(true);
        
        // Display first question
        displayCurrentQuestion();
    }
    
    /**
     * Displays the current question from GameManager.
     */
    private void displayCurrentQuestion() {
        Question currentQuestion = gameManager.getCurrentQuestion();
        
        if (currentQuestion == null) {
            // Quiz is complete
            finishQuiz();
            return;
        }
        
        // Update question info
        questionNumberLabel.setText(String.format("Question %d of %d", 
            gameManager.getCurrentQuestionIndex() + 1, gameManager.getTotalQuestions()));
        questionTitleLabel.setText(currentQuestion.getTitle());
        
        // Clear previous question UI
        questionContainer.getChildren().clear();
        currentAnswer = null;
        submitAnswerButton.setDisable(true);
        
        // Create question-specific UI
        if (currentQuestion instanceof RadioGroupQuestion) {
            createRadioGroupQuestion((RadioGroupQuestion) currentQuestion);
        } else if (currentQuestion instanceof BooleanQuestion) {
            createBooleanQuestion((BooleanQuestion) currentQuestion);
        }
        
        // Start timer
        startTimer();
    }
    
    /**
     * Creates UI for a radio group question.
     */
    private void createRadioGroupQuestion(RadioGroupQuestion question) {
        answerToggleGroup = new ToggleGroup();
        
        for (String choice : question.getShuffledChoices()) {
            RadioButton radioButton = new RadioButton(choice);
            radioButton.setToggleGroup(answerToggleGroup);
            radioButton.setOnAction(e -> {
                currentAnswer = choice;
                submitAnswerButton.setDisable(false);
            });
            questionContainer.getChildren().add(radioButton);
        }
    }
    
    /**
     * Creates UI for a boolean question.
     */
    private void createBooleanQuestion(BooleanQuestion question) {
        answerToggleGroup = new ToggleGroup();
        
        RadioButton trueButton = new RadioButton(question.getLabelTrue());
        RadioButton falseButton = new RadioButton(question.getLabelFalse());
        
        trueButton.setToggleGroup(answerToggleGroup);
        falseButton.setToggleGroup(answerToggleGroup);
        
        trueButton.setOnAction(e -> {
            currentAnswer = true;
            submitAnswerButton.setDisable(false);
        });
        
        falseButton.setOnAction(e -> {
            currentAnswer = false;
            submitAnswerButton.setDisable(false);
        });
        
        questionContainer.getChildren().addAll(trueButton, falseButton);
    }
    
    /**
     * Starts the countdown timer for the current question.
     */
    private void startTimer() {
        if (timer != null) {
            timer.stop();
        }
        
        timeRemaining = gameManager.getCurrentTimeLimit();
        updateTimerDisplay();
        
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeRemaining--;
            updateTimerDisplay();
            
            if (timeRemaining <= 0) {
                timer.stop();
                handleTimeUp();
            }
        }));
        
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }
    
    /**
     * Updates the timer display.
     */
    private void updateTimerDisplay() {
        int totalTime = gameManager.getCurrentTimeLimit();
        double progress = totalTime > 0 ? (double) timeRemaining / totalTime : 0;
        
        Platform.runLater(() -> {
            timerProgressBar.setProgress(progress);
            timerLabel.setText(String.format("Time: %d seconds", timeRemaining));
        });
    }
    
    /**
     * Handles time running out.
     */
    private void handleTimeUp() {
        // Submit null answer (incorrect)
        gameManager.submitAnswer(null);
        nextQuestion();
    }
    
    /**
     * Handles answer submission.
     */
    @FXML
    private void handleSubmitAnswer() {
        if (currentAnswer != null) {
            timer.stop();
            gameManager.submitAnswer(currentAnswer);
            nextQuestion();
        }
    }
    
    /**
     * Moves to the next question or finishes the quiz.
     */
    private void nextQuestion() {
        if (gameManager.nextQuestion()) {
            displayCurrentQuestion();
        } else {
            finishQuiz();
        }
    }
    
    /**
     * Finishes the quiz and navigates to results.
     */
    private void finishQuiz() {
        if (timer != null) {
            timer.stop();
        }
        
        try {
            QuizApplication.switchScene("view/results-view.fxml", "Quiz Application - Results");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", 
                "Failed to load results view", "Error: " + e.getMessage());
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
