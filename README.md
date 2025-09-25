# Quiz Application

A JavaFX-based quiz application developed as part of the Java Fundamentals course at Inholland University of Applied Sciences.

## Features

- **Interactive Quiz Interface**: Clean, user-friendly interface for taking quizzes
- **Multiple Question Types**: Support for multiple-choice (radiogroup) and true/false (boolean) questions
- **Timer Functionality**: Each question has a configurable time limit with visual countdown
- **Score Tracking**: Real-time score updates using JavaFX property binding
- **Results Management**: Save and view high scores for each quiz
- **JSON-based Quiz Format**: Load quizzes from JSON files with flexible question configuration

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

1. **Start the Application**: Launch the application to see the welcome screen
2. **Load a Quiz**: Click "Load Quiz" and select a JSON quiz file (use `sample-quiz.json` for testing)
3. **Start Playing**: Click "Start Quiz" to begin
4. **Enter Your Name**: Provide your name before starting the quiz
5. **Answer Questions**: Select answers within the time limit for each question
6. **View Results**: See your score and compare with previous attempts

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