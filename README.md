# Java Swing Quiz Application by Team Byte Us

This is a desktop-based Quiz Application developed using **Java** and **Swing**. The application allows users to log in, choose a domain (e.g., Java, Math, General Knowledge), attempt a timed quiz, and view results upon completion.

## 📂 Project Structure

QuizApp/
├── Login.java
├── Question.java
├── QuestionBank.java
├── QuizResult.java
├── Rules.java
├── UserProfile.java
├── question/
          ├── Java.dat
          ├── Math.dat
          ├── General Knowledge.dat
└── README.md

## ✅ Features

- User login and profile management.
- Multiple quiz domains (Java, Math, General Knowledge).
- Timer-based quizzes.
- Randomized questions per session.
- Displays quiz score and correct answers after submission.
- Simple and clean GUI built using Swing.

## 💻 Requirements

- Java JDK 8 or higher
- Java IDE (Eclipse, IntelliJ IDEA, NetBeans) or a basic text editor with terminal access

## 🔧 Setup Instructions

### 1. Clone the Repository

\\ git clone https://github.com/Divyansh-132006/quiz-app-java.git
cd quiz-app-java


2. Open the Project in IDE or run it in CMD
Open the folder in your preferred Java IDE.

Ensure all .java and .dat files are in the same project directory.

3. Compile the Project
You can compile the project manually using terminal or your IDE’s build tool.

javac *.java

java Login

This will start the application from the login screen.


📦 Data Files
This application uses binary .dat files to store questions for each domain:

Java.dat – Contains Java domain questions

Math.dat – Contains Math domain questions

General Knowledge.dat – Contains GK questions

Science.dat  - Contains Science question

Ensure these files are present in the project directory when you run the application.

🧠 Domain Categories
Each category contains a preloaded set of 15 questions:

Java

Math

General Knowledge

Science

More categories can be added by creating new .dat files and updating the QuestionBank class.

🛠️ Future Enhancements
Add a user registration system

Store user scores for analytics

Export results to PDF

Online multiplayer quiz

 LeaderBoard

 MultiPlayer at a time for compotition

 UI images are : 
 

🙋‍♂️ Author
Team Byte Us:
Feel free to reach out or contribute to the project!
