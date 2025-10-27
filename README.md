# Quiz Application

A JavaFX-based quiz application developed as part of the Java Fundamentals course at Inholland University of Applied Sciences.

## Features

- **Interactive Quiz Interface**: Clean, user-friendly interface for taking quizzes
- **Multiple Question Types**: Support for multiple-choice (radiogroup) and true/false (boolean) questions
- **Timer Functionality**: Each question has a configurable time limit with visual countdown
- **Score Tracking**: Real-time score updates using JavaFX property binding
- **Results Management**: Save and view high scores for each quiz
- **JSON-based Quiz Format**: Load quizzes from JSON files with flexible question configuration
- **Leaderboard**: View all previous quiz results sorted by score in descending order with:
  - Player names
  - Score as a percentage (without decimals)
  - Completion dates
- **CSV Export**: Export leaderboard data to CSV format with the following structure:
  - `quizId;quizName;playerName;totalQuestions;correctQuestions;date`
  - Configurable export path and filename with .csv extension
- **Practice Mode**: Alternative quiz mode where:
  - Timer does not run (unlimited time per question)
  - Scores are not saved to the leaderboard
  - Perfect for learning and practice without pressure
- **Responsive UI**: Works seamlessly on any screen size:
  - Scrollable pages when not in fullscreen
  - Adaptive layout for different window sizes
  - Text wrapping for smaller displays

## Design Patterns Implemented

- **Singleton Pattern**: GameManager for centralized game state management
- **Factory Pattern**: QuestionFactory for creating different question types
- **Observer Pattern**: JavaFX property binding for automatic UI updates
- **MVC Pattern**: Clear separation of Model, View, and Controller components

## Project Structure

```
src/
├── main/
│   ├── java/org/example/quizzapp/
│   │   ├── controller/          # JavaFX controllers
│   │   ├── model/              # Data model classes
│   │   ├── service/            # Business logic and services
│   │   ├── QuizApplication.java # Main application class
│   │   └── Launcher.java       # Application launcher
│   └── resources/org/example/quizzapp/
│       ├── view/               # FXML view files
│       └── styles/             # CSS styling
├── test/                       # JUnit tests
└── sample-quiz.json           # Example quiz file
```

## Requirements

- Java 21 LTS
- JavaFX 21
- Maven 3.6+
- IntelliJ IDEA 2025.2 (recommended)

## Running the Application

### From IDE
1. Open the project in IntelliJ IDEA
2. Run the `Launcher.java` class

### From Command Line
```bash
mvn clean javafx:run
```

### Building
```bash
mvn clean compile
```

### Running Tests
```bash
mvn test
```

## Usage

### Starting a Quiz

1. **Start the Application**: Launch the application to see the welcome screen
2. **Load a Quiz**: Click "Load Quiz" and select a JSON quiz file (use `sample-quiz.json` for testing)
3. **Choose Game Mode**:
   - **Normal Mode**: Timed quiz with score saved to leaderboard
   - **Practice Mode**: Untimed quiz without leaderboard tracking
4. **Enter Your Name**: Provide your name before starting the quiz
5. **Answer Questions**: Select answers and submit within the time limit (Normal mode only)
6. **View Results**: See your score and the leaderboard

### Viewing Results and Leaderboard

- After completing a quiz, the results screen displays:
  - Your score as a percentage (without decimals)
  - A completion message based on your performance
  - **Leaderboard**: All previous attempts sorted by score in descending order
    - Shows player name, score percentage, and completion date
  - Action buttons:
    - **Export to CSV**: Download leaderboard data in CSV format
    - **Play Again**: Take the quiz again
    - **Back to Menu**: Return to the main menu

### Exporting Leaderboard

1. Click the **"Export to CSV"** button on the results screen
2. Choose the location and filename (defaults to `.csv` extension)
3. The file will be created with the following format:
   ```
   quizId;quizName;playerName;totalQuestions;correctQuestions;date
   ```
4. Example output:
   ```
   javabasicsquiz;Java Basics Quiz;Alice;3;3;2025-10-27T12:49:29
   javabasicsquiz;Java Basics Quiz;Bob;3;2;2025-10-27T12:40:32
   javabasicsquiz;Java Basics Quiz;Charlie;3;1;2025-10-27T12:34:06
   ```

## Quiz JSON Format

The application supports JSON quiz files with the following structure:

```json
{
  "title": "Quiz Title",
  "description": "Quiz description",
  "pages": [
    {
      "timeLimit": 30,
      "elements": [
        {
          "type": "radiogroup",
          "name": "q1",
          "title": "Question text",
          "choices": ["Option 1", "Option 2", "Option 3"],
          "correctAnswer": "Option 2",
          "isRequired": true
        }
      ]
    }
  ]
}
```

## Authentication

No specific usernames or passwords are required for this application.

## Development Notes

- All code follows Java coding conventions
- Methods are kept concise (under 30 lines)
- Comprehensive error handling for file operations and user input
- Unit tests cover model classes and core business logic
- CSS styling provides a professional appearance

## Authors

Developed for the Java Fundamentals course at Inholland University of Applied Sciences.

Teachers: Mark de Haan & Dan Breczinski