import java.io.*;
import java.util.*;


public class QuestionBank {
    private Map<String, List<Question>> questionsByDomain;
    private static final String QUESTION_DIR = "questions";
    
    public QuestionBank() {
        questionsByDomain = new HashMap<>();
        loadDefaultQuestions();
    }
    
    private void loadDefaultQuestions() {
 
        File directory = new File(QUESTION_DIR);
        if (!directory.exists()) {
            directory.mkdir();
        }
        
        createDefaultQuestionsIfNeeded("Java", createDefaultJavaQuestions());
        createDefaultQuestionsIfNeeded("Math", createDefaultMathQuestions());
        createDefaultQuestionsIfNeeded("Science", createDefaultScienceQuestions());
        createDefaultQuestionsIfNeeded("General Knowledge", createDefaultGeneralKnowledgeQuestions());
        
  
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".dat"));
        if (files != null) {
            for (File file : files) {
                String domain = file.getName().replace(".dat", "");
                questionsByDomain.put(domain, loadQuestionsFromFile(domain));
            }
        }
    }
    
    private void createDefaultQuestionsIfNeeded(String domain, List<Question> questions) {
        File file = new File(QUESTION_DIR + "/" + domain + ".dat");
        if (!file.exists()) {
            saveQuestionsToFile(domain, questions);
        }
    }
    
    private List<Question> loadQuestionsFromFile(String domain) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(QUESTION_DIR + "/" + domain + ".dat"))) {
            return (List<Question>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading questions for " + domain + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    private void saveQuestionsToFile(String domain, List<Question> questions) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(QUESTION_DIR + "/" + domain + ".dat"))) {
            oos.writeObject(questions);
        } catch (IOException e) {
            System.err.println("Error saving questions for " + domain + ": " + e.getMessage());
        }
    }
    
    public List<String> getAvailableDomains() {
        return new ArrayList<>(questionsByDomain.keySet());
    }
    
    public List<Question> getQuestionsForDomain(String domain, String difficulty, int count) {
        List<Question> allQuestions = questionsByDomain.getOrDefault(domain, new ArrayList<>());
        List<Question> filteredQuestions = new ArrayList<>();
        
        for (Question q : allQuestions) {
            if (q.getDifficulty().equals(difficulty)) {
                filteredQuestions.add(q);
            }
        }
        
        
        if (filteredQuestions.size() < count) {
            for (Question q : allQuestions) {
                if (!q.getDifficulty().equals(difficulty) && !filteredQuestions.contains(q)) {
                    filteredQuestions.add(q);
                }
            }
        }
        
     
        Collections.shuffle(filteredQuestions);
        return filteredQuestions.size() <= count ? 
               filteredQuestions : 
               filteredQuestions.subList(0, count);
    }
    
    public void importQuestionsFromCsv(String domain, String filePath) {
        List<Question> questions = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            if ((line = br.readLine()) != null && line.startsWith("Question,")) {
               
            } else {
                
                processQuestionLine(line, questions);
            }
            
       
            while ((line = br.readLine()) != null) {
                processQuestionLine(line, questions);
            }
            
          
            List<Question> existingQuestions = questionsByDomain.getOrDefault(domain, new ArrayList<>());
            existingQuestions.addAll(questions);
            questionsByDomain.put(domain, existingQuestions);
            
            
            saveQuestionsToFile(domain, existingQuestions);
            
        } catch (IOException e) {
            System.err.println("Error importing questions: " + e.getMessage());
        }
    }
    
    private void processQuestionLine(String line, List<Question> questions) {
        
        String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        
        if (parts.length >= 7) {
            String questionText = parts[0].trim().replace("\"", "");
            String correctAnswer = parts[1].trim().replace("\"", "");
            
            List<String> options = new ArrayList<>();
            options.add(correctAnswer); 
            
   
            for (int i = 2; i < 6 && i < parts.length; i++) {
                if (!parts[i].trim().isEmpty()) {
                    options.add(parts[i].trim().replace("\"", ""));
                }
            }
            
            Collections.shuffle(options); 
            
            String difficulty = parts.length > 6 ? parts[6].trim().replace("\"", "") : "Medium";
            String type = parts.length > 7 ? parts[7].trim().replace("\"", "") : "MULTIPLE_CHOICE";
            String explanation = parts.length > 8 ? parts[8].trim().replace("\"", "") : "";
            
            Question question = new Question(
                questionText,
                options,
                options.indexOf(correctAnswer),
                difficulty,
                QuestionType.valueOf(type),
                explanation
            );
            
            questions.add(question);
        }
    }
    
    private List<Question> createDefaultJavaQuestions() {
        List<Question> javaQuestions = new ArrayList<>();
      
        javaQuestions.add(new Question(
            "What is the correct way to declare a variable in Java?",
            Arrays.asList("int x = 10;", "x = 10;", "variable x = 10;", "x: int = 10;"),
            0, "Easy", QuestionType.MULTIPLE_CHOICE,
            "In Java, variables must be declared with their data type before use."
        ));
        
        javaQuestions.add(new Question(
            "Which keyword is used to create an object in Java?",
            Arrays.asList("new", "this", "create", "object"),
            0, "Easy", QuestionType.MULTIPLE_CHOICE,
            "The 'new' keyword is used to instantiate objects in Java."
        ));
        
        javaQuestions.add(new Question(
            "Java is a platform-independent language.",
            Arrays.asList("True", "False"),
            0, "Easy", QuestionType.TRUE_FALSE,
            "Java is platform-independent because it compiles to bytecode that can run on any system with a JVM."
        ));
        
      
        javaQuestions.add(new Question(
            "What is the purpose of the 'static' keyword in Java?",
            Arrays.asList(
                "It makes the member belong to the class rather than an instance",
                "It makes the variable value unchangeable",
                "It creates a new instance of a class",
                "It restricts class inheritance"
            ),
            0, "Medium", QuestionType.MULTIPLE_CHOICE,
            "Static members belong to the class itself, not to instances of the class."
        ));
        
        javaQuestions.add(new Question(
            "Which collection interface provides guaranteed order of iteration?",
            Arrays.asList("List", "Set", "Map", "Queue"),
            0, "Medium", QuestionType.MULTIPLE_CHOICE,
            "The List interface maintains elements in insertion order."
        ));
        
       
        javaQuestions.add(new Question(
            "What is the output of: System.out.println(\"5\" + 2 + 3);",
            Arrays.asList("523", "10", "5 + 2 + 3", "Error"),
            0, "Hard", QuestionType.MULTIPLE_CHOICE,
            "Since the expression starts with a string, the '+' operator performs string concatenation."
        ));
        
        javaQuestions.add(new Question(
            "Which statement about Java interfaces is true?",
            Arrays.asList(
                "Since Java 8, interfaces can have default and static methods",
                "Interfaces can contain concrete methods",
                "A class can extend multiple interfaces",
                "Interface methods are by default protected"
            ),
            0, "Hard", QuestionType.MULTIPLE_CHOICE,
            "Java 8 introduced default and static methods in interfaces."
        ));
        javaQuestions.add(new Question(
    "Which of the following is not a Java primitive data type?",
    Arrays.asList("int", "boolean", "String", "double"),
    2, "Easy", QuestionType.MULTIPLE_CHOICE,
    "`String` is not a primitive type in Java; it is a class."
));

javaQuestions.add(new Question(
    "What will be the output of: System.out.println(10 > 5 && 3 < 1);",
    Arrays.asList("true", "false", "Compilation Error", "Runtime Error"),
    1, "Medium", QuestionType.MULTIPLE_CHOICE,
    "The expression `10 > 5 && 3 < 1` evaluates to `true && false`, which is `false`."
));

javaQuestions.add(new Question(
    "Which method is called when an object is created in Java?",
    Arrays.asList("main()", "init()", "constructor", "finalize()"),
    2, "Easy", QuestionType.MULTIPLE_CHOICE,
    "A constructor is called when a new object is instantiated."
));

javaQuestions.add(new Question(
    "Java uses which memory area for storing objects?",
    Arrays.asList("Stack", "Heap", "Register", "Static"),
    1, "Medium", QuestionType.MULTIPLE_CHOICE,
    "Objects in Java are stored in the Heap memory."
));

javaQuestions.add(new Question(
    "Which of the following is used to handle exceptions in Java?",
    Arrays.asList("try-catch", "if-else", "switch-case", "throw-catch"),
    0, "Medium", QuestionType.MULTIPLE_CHOICE,
    "`try-catch` is the correct block used to handle exceptions in Java."
));

        
        return javaQuestions;
    }
    
    private List<Question> createDefaultMathQuestions() {
        List<Question> mathQuestions = new ArrayList<>();
        
        
        mathQuestions.add(new Question(
            "What is 12 × 9?",
            Arrays.asList("108", "98", "112", "121"),
            0, "Easy", QuestionType.MULTIPLE_CHOICE,
            "12 × 9 = 108"
        ));
        
        mathQuestions.add(new Question(
            "What is the next number in the sequence: 2, 4, 8, 16, ...?",
            Arrays.asList("32", "24", "20", "30"),
            0, "Easy", QuestionType.MULTIPLE_CHOICE,
            "Each number is multiplied by 2 to get the next number. 16 × 2 = 32."
        ));
        
        mathQuestions.add(new Question(
            "The sum of the angles in a triangle is 180 degrees.",
            Arrays.asList("True", "False"),
            0, "Easy", QuestionType.TRUE_FALSE,
            "The sum of the angles in any triangle is always 180 degrees."
        ));
   
        mathQuestions.add(new Question(
            "Solve for x: 3x + 7 = 22",
            Arrays.asList("5", "6", "7", "4"),
            0, "Medium", QuestionType.MULTIPLE_CHOICE,
            "3x + 7 = 22\n3x = 15\nx = 5"
        ));
        
        mathQuestions.add(new Question(
            "What is the value of sin(30°)?",
            Arrays.asList("0.5", "1", "0", "√3/2"),
            0, "Medium", QuestionType.MULTIPLE_CHOICE,
            "sin(30°) = 0.5 or 1/2"
        ));
        
      
        mathQuestions.add(new Question(
            "If f(x) = x² - 3x + 2, what is f'(x)?",
            Arrays.asList("2x - 3", "2x + 3", "x² - 3", "2x"),
            0, "Hard", QuestionType.MULTIPLE_CHOICE,
            "The derivative of f(x) = x² - 3x + 2 is f'(x) = 2x - 3"
        ));
        
        mathQuestions.add(new Question(
            "What is the value of the integral ∫(2x + 3)dx from x=0 to x=2?",
            Arrays.asList("10", "7", "8", "12"),
            0, "Hard", QuestionType.MULTIPLE_CHOICE,
            "∫(2x + 3)dx = x² + 3x + C\nEvaluating from 0 to 2: (4 + 6) - (0 + 0) = 10"
        ));
        
        return mathQuestions;
    }
    
    private List<Question> createDefaultScienceQuestions() {
        List<Question> scienceQuestions = new ArrayList<>();
        
        
        scienceQuestions.add(new Question(
            "What is the chemical symbol for oxygen?",
            Arrays.asList("O", "Ox", "Om", "Og"),
            0, "Easy", QuestionType.MULTIPLE_CHOICE,
            "Oxygen's chemical symbol is O."
        ));
        
        scienceQuestions.add(new Question(
            "Which planet is closest to the Sun?",
            Arrays.asList("Mercury", "Venus", "Earth", "Mars"),
            0, "Easy", QuestionType.MULTIPLE_CHOICE,
            "Mercury is the closest planet to the Sun."
        ));

        scienceQuestions.add(new Question(
            "What is the main function of mitochondria in cells?",
            Arrays.asList(
                "Energy production",
                "Protein synthesis",
                "Cell division",
                "Waste disposal"
            ),
            0, "Medium", QuestionType.MULTIPLE_CHOICE,
            "Mitochondria are known as the powerhouse of the cell because they produce ATP."
        ));
        
        scienceQuestions.add(new Question(
            "Newton's Second Law of Motion states that F = ma. What does 'a' represent?",
            Arrays.asList("Acceleration", "Area", "Amplitude", "Altitude"),
            0, "Medium", QuestionType.MULTIPLE_CHOICE,
            "In F = ma, 'a' represents acceleration."
        ));
        
     
        scienceQuestions.add(new Question(
            "What is the Heisenberg Uncertainty Principle?",
            Arrays.asList(
                "It's impossible to know both the position and momentum of a particle precisely",
                "Energy cannot be created or destroyed",
                "Every action has an equal and opposite reaction",
                "Matter cannot travel faster than light"
            ),
            0, "Hard", QuestionType.MULTIPLE_CHOICE,
            "The Heisenberg Uncertainty Principle states that we cannot simultaneously know both the position and momentum of a particle with perfect precision."
        ));
        
        scienceQuestions.add(new Question(
            "Which of these elements is a noble gas?",
            Arrays.asList("Xenon", "Chlorine", "Sodium", "Calcium"),
            0, "Hard", QuestionType.MULTIPLE_CHOICE,
            "Xenon (Xe) is a noble gas. Noble gases are in Group 18 of the periodic table."
        ));
        
        return scienceQuestions;
    }
    
    private List<Question> createDefaultGeneralKnowledgeQuestions() {
        List<Question> generalQuestions = new ArrayList<>();
    
        generalQuestions.add(new Question(
            "Which country is known as the Land of the Rising Sun?",
            Arrays.asList("Japan", "China", "Thailand", "Vietnam"),
            0, "Easy", QuestionType.MULTIPLE_CHOICE,
            "Japan is known as the Land of the Rising Sun."
        ));
        
        generalQuestions.add(new Question(
            "Who painted the Mona Lisa?",
            Arrays.asList("Leonardo da Vinci", "Pablo Picasso", "Vincent van Gogh", "Michelangelo"),
            0, "Easy", QuestionType.MULTIPLE_CHOICE,
            "The Mona Lisa was painted by Leonardo da Vinci in the early 16th century."
        ));
        

        generalQuestions.add(new Question(
            "What is the capital of Australia?",
            Arrays.asList("Canberra", "Sydney", "Melbourne", "Perth"),
            0, "Medium", QuestionType.MULTIPLE_CHOICE,
            "Canberra is the capital city of Australia."
        ));
        
        generalQuestions.add(new Question(
            "In which year did World War II end?",
            Arrays.asList("1945", "1939", "1944", "1941"),
            0, "Medium", QuestionType.MULTIPLE_CHOICE,
            "World War II ended in 1945."
        ));
        
  
        generalQuestions.add(new Question(
            "Who wrote the book 'One Hundred Years of Solitude'?",
            Arrays.asList(
                "Gabriel García Márquez",
                "Isabel Allende",
                "Jorge Luis Borges",
                "Pablo Neruda"
            ),
            0, "Hard", QuestionType.MULTIPLE_CHOICE,
            "'One Hundred Years of Solitude' was written by Gabriel García Márquez."
        ));
        
        generalQuestions.add(new Question(
            "What is the name of the longest river in Africa?",
            Arrays.asList("Nile", "Congo", "Niger", "Zambezi"),
            0, "Hard", QuestionType.MULTIPLE_CHOICE,
            "The Nile is the longest river in Africa."
        ));
        
        return generalQuestions;
    }
    
    public void addQuestion(String domain, Question question) {
        List<Question> questions = questionsByDomain.getOrDefault(domain, new ArrayList<>());
        questions.add(question);
        questionsByDomain.put(domain, questions);
        saveQuestionsToFile(domain, questions);
    }
}
