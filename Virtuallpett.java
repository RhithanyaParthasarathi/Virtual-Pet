package virtuallpett;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;


public class Virtuallpett {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VirtPetFrame());
    }
}

class VirtPetFrame extends JFrame {
    private String petType;
    private String variety;
    private String petName;
    private Pet pet;

    public VirtPetFrame() {
        setTitle("Virtual Pet Game");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        // Confirm before closing the application
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        VirtPetFrame.this,
                        "Do you want to exit from this game?",
                        "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION
                );
                if (response == JOptionPane.YES_OPTION) {
                    
                    System.exit(0);
                    
                }
            }
        });

        // Initial pet selection screen
        showPetSelectionScreen();
    }

    private void showPetSelectionScreen() {
        removeAllComponents();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255)); // Light blue background

        JLabel label = new JLabel("Choose your pet type:");
        label.setFont(new Font("Arial", Font.BOLD, 20));

        panel.add(label, createConstraints(0, 0, 1, 1, 10));

        String[] petOptions = {"Dog", "Cat", "Bird"};
        int row = 1;
        for (String option : petOptions) {
            JButton button = createStyledButton(option);
            button.addActionListener(e -> handlePetChoice(option));
            panel.add(button, createConstraints(0, row++, 1, 1, 10));
        }

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 50));
        button.setBackground(new Color(173, 216, 230)); // Light blue
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        return button;
    }

    private GridBagConstraints createConstraints(int x, int y, int width, int height, int padding) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.insets = new Insets(padding, padding, padding, padding);
        gbc.anchor = GridBagConstraints.CENTER;
        return gbc;
    }

    private void handlePetChoice(String choice) {
        this.petType = choice;
        if (petType.equals("Dog")) {
            showVarietySelectionScreen("Dog", new String[]{"Golden Retriever", "Labrador", "Pomeranian", "Pug", "Beagle"});
        } else if (petType.equals("Cat")) {
            showVarietySelectionScreen("Cat", new String[]{"Persian", "Siamese", "Scottish Fold", "Bombay", "Maine Coon"});
        } else if (petType.equals("Bird")) {
            showVarietySelectionScreen("Bird", new String[]{"Parrot", "Cockatoo", "Finch", "Cockatiel", "Parakeet"});
        }
    }

    private void showVarietySelectionScreen(String petType, String[] varieties) {
        removeAllComponents();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255)); // Light blue background

        JLabel label = new JLabel("Select your " + petType + " variety:");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(label, createConstraints(0, 0, 1, 1, 10));

        int row = 1;
        for (String variety : varieties) {
            JButton button = createStyledButton(variety);
            button.addActionListener(e -> handleVarietySelection(petType, variety)); // Pass petType and variety
            panel.add(button, createConstraints(0, row++, 1, 1, 10));
        }

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void handleVarietySelection(String petType, String variety) {
        this.variety = variety;
        showPetTraits(petType, variety); // Show traits after variety is selected
    }

    private void showPetTraits(String petType, String variety) {
        String traits = getTraits(petType, variety);
        JOptionPane.showMessageDialog(this, "Traits of the selected " + petType + " variety (" + variety + "):\n" + traits);
        showNameInputScreen(); // Proceed to name input screen after displaying traits
    }

    private String getTraits(String petType, String variety) {
        if (petType.equals("Dog")) {
            return Dog.getTraits(variety);
        } else if (petType.equals("Cat")) {
            return Cat.getTraits(variety);
        } else if (petType.equals("Bird")) {
            return Bird.getTraits(variety);
        }
        return "Unknown Traits.";
    }

    private void showNameInputScreen() {
        removeAllComponents();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255)); // Light blue background

        JLabel label = new JLabel("Name your " + variety + " " + petType + ":");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        JTextField nameField = new JTextField(20);
        JButton submitButton = createStyledButton("Submit");

        submitButton.addActionListener(e -> {
            petName = nameField.getText();
            if (petName == null || petName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty!");
                return;
            }
            createPet(petType, petName, variety);
            showMainGameScreen();
        });

        panel.add(label, createConstraints(0, 0, 1, 1, 10));
        panel.add(nameField, createConstraints(0, 1, 1, 1, 10));
        panel.add(submitButton, createConstraints(0, 2, 1, 1, 10));

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void createPet(String petType, String petName, String variety) {
        switch (petType) {
            case "Dog" -> pet = new Dog(petName, variety);
            case "Cat" -> pet = new Cat(petName, variety);
            case "Bird" -> pet = new Bird(petName, variety);
        }
    }

    private void showMainGameScreen() {
        removeAllComponents();

        JLabel statusLabel = new JLabel("What would you like to do with " + petName + "?");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(statusLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(new Color(240, 248, 255));

        String[] options = {"Feed", "Entertain", "Check Status", "Put to Sleep","Mini-Games", "Exit"};
        for (String option : options) {
            JButton button = createStyledButton(option);
            button.addActionListener(e -> handleMainOption(option));
            panel.add(button);
        }

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void handleMainOption(String option) {
        switch (option) {
            case "Feed" -> pet.feedInteractive();
            case "Entertain" -> pet.entertainInteractive();
            case "Check Status" -> pet.showStatus();
            case "Put to Sleep" -> pet.putToSleepInteractive();
            case "Mini-Games" -> {
                MiniGames miniGames = new MiniGames(); // Create a MiniGames object
                miniGames.chooseGame(pet); // Pass the pet object to the chooseGame method
            }
            case "Exit" -> {
                int response = JOptionPane.showConfirmDialog(
                        this,
                        "Do you want to exit from this game?",
                        "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION
                );
                if (response == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Thanks for playing with " + pet.name + ".\n" + pet.name+" says See you again later!");
                    System.exit(0);
                }
            }
        }
    }

    private void removeAllComponents() {
        getContentPane().removeAll();
        revalidate();
        repaint();
    }
}

abstract class Pet {
    protected String name;
    protected String variety;
    protected int happiness = 50;
    protected int hunger = 50;
    protected int sleepiness = 50;

    public Pet(String name, String variety) {
        this.name = name;
        this.variety = variety;
    }

    public void feedInteractive() {
        String foodAmountStr;
    String foodType;
    String feedingMessage;

    // Determine the food type and feeding message based on the pet type
    if (this instanceof Dog) {
        foodType = "biscuits";
        feedingMessage = "How many biscuits do you want to feed " + name + "?";
    } else if (this instanceof Cat) {
        foodType = "bowls of milk";
        feedingMessage = "How many bowls of milk do you want to feed " + name + "?";
    } else {
        foodType = "portions of grains";
        feedingMessage = "How many portions of grains do you want to feed " + name + "?";
    }

    // Loop to repeatedly prompt for valid input if invalid is entered
    while (true) {
        // Input dialog to get the number of food items to feed
        foodAmountStr = JOptionPane.showInputDialog(feedingMessage);

        // If the user presses Cancel or provides no input
        if (foodAmountStr == null || foodAmountStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No input provided. " + name + " didn't get fed.");
            return;
        }

        try {
            int foodAmount = Integer.parseInt(foodAmountStr);

            // Check if the number of food items is positive
            if (foodAmount <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter a positive number of " + foodType + ".");
                continue; // Ask again for input
            }

            // Update hunger and happiness based on the food amount
            hunger -= foodAmount;
            if (hunger < 0) hunger = 0;  // Ensure hunger doesn't go below 0
            happiness += 10;  // Increase happiness by a fixed value

            // Show confirmation message
            JOptionPane.showMessageDialog(null, name + " says: Yum! That was tasty!");
            break; // Exit the loop after valid input

        } catch (NumberFormatException e) {
            // Handle invalid input (non-numeric values)
            JOptionPane.showMessageDialog(null, "Please enter a valid number of " + foodType + ".");
        }
    }

    // Show the pet's updated status (e.g., hunger, happiness, etc.)
    showStatus();
    }

    public void entertainInteractive() {
        String[] playOptions = {"Play Frisbee", "Chase Laser", "Fly Through Hoops"};
    int choice = JOptionPane.showOptionDialog(null, "Choose a game for " + name + ":",
            "Entertain " + name, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, playOptions, playOptions[0]);

    if (choice == -1) {
        JOptionPane.showMessageDialog(null, name + " says: Maybe next time!");
        return;
    }

    String playItem = playOptions[choice];
    int numThrows = 0;

    // Input validation for number of throws
    while (true) {
        try {
            String input = JOptionPane.showInputDialog("How many times do you want to play with the " + playItem + "?");
            if (input == null || input.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number!");
                continue;
            }

            numThrows = Integer.parseInt(input);

            if (numThrows <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter a positive number greater than 0!");
                continue;
            }

            break;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid number.");
        }
    }

    int successfulPlays = 0;
    Random rand = new Random();
    for (int i = 0; i < numThrows; i++) {
        if (rand.nextBoolean()) {
            successfulPlays++;
            JOptionPane.showMessageDialog(null, name + " says: I got it!");
        } else {
            JOptionPane.showMessageDialog(null, name + " says: Oops, I missed!");
        }
    }

    happiness += successfulPlays * 5;
    hunger += numThrows * 2;
    JOptionPane.showMessageDialog(null, name + " played with the " + playItem + " " + numThrows + " times and succeeded " + successfulPlays + " times!" + "\nScore: " + successfulPlays * 10 + "/" + numThrows * 10);
    showStatus();
    }

    public void showStatus() {
        String statusMessage = name + "'s Status:\n" +
                "Happiness: " + happiness + "\n" +
                "Hunger: " + hunger + "\n" +
                "Sleepiness: " + sleepiness;
        JOptionPane.showMessageDialog(null, statusMessage);
    }

    public void putToSleepInteractive() {
        int response = JOptionPane.showConfirmDialog(null, name + " seems tired. Is " + name + " ready to sleep?", "Sleep", JOptionPane.YES_NO_OPTION);

    if (response == JOptionPane.YES_OPTION) {
        int hoursToSleep = 0;

        // Input validation for hours of sleep
        while (true) {
            try {
                String input = JOptionPane.showInputDialog("How many hours do you want " + name + " to sleep?");
                if (input == null || input.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number!");
                    continue;
                }

                hoursToSleep = Integer.parseInt(input);

                if (hoursToSleep <= 0) {
                    JOptionPane.showMessageDialog(null, "Please enter a positive number greater than 0!");
                    continue;
                }

                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid number.");
            }
        }

        // Update happiness and hunger based on sleep
        happiness += hoursToSleep * 3;
        hunger += hoursToSleep * 1;
        JOptionPane.showMessageDialog(null, name + " slept for " + hoursToSleep + " hours.");
        showStatus();
    } else {
        JOptionPane.showMessageDialog(null, name + " is not ready to sleep.");
    }
}
}

class Dog extends Pet {
    public Dog(String name, String variety) {
        super(name, variety);
    }

    public static String getTraits(String variety) {
        switch (variety) {
            case "Golden Retriever": return "Golden Retrievers are friendly and loyal. They love to play!";
            case "Labrador": return "Labradors are energetic and love water.";
            case "Pomeranian": return "Pomeranians are small, fluffy, and full of personality.";
            case "Pug": return "Pugs are playful, charming, and affectionate.";
            case "Beagle": return "Beagles are friendly and love to explore!";
            default: return "Unknown breed.";
        }
    }
}

class Cat extends Pet {
    public Cat(String name, String variety) {
        super(name, variety);
    }

    public static String getTraits(String variety) {
        switch (variety) {
            case "Persian": return "Persian cats are gentle and love lounging.";
            case "Siamese": return "Siamese cats are vocal and curious.";
            case "Scottish Fold": return "Scottish Folds are known for their round faces and loving nature.";
            case "Bombay": return "Bombay cats are sleek and affectionate.";
            case "Maine Coon": return "Maine Coons are large, friendly, and playful.";
            default: return "Unknown breed.";
        }
    }
}

class Bird extends Pet {
    public Bird(String name, String variety) {
        super(name, variety);
    }

    public static String getTraits(String variety) {
        switch (variety) {
            case "Parrot": return "Parrots are colorful, smart, and can mimic sounds.";
            case "Cockatoo": return "Cockatoos are known for their crests and sociable nature.";
            case "Finch": return "Finches are intelligent, social, active, and are good singers.";
            case "Cockatiel": return "Cockatiels are friendly and love to whistle.";
            case "Parakeet": return "Parakeets are active, playful, and enjoy being social.";
            default: return "Unknown breed.";
        }
    }
}

class MiniGames {
    private final Map<String, String[]> wordBank = new LinkedHashMap<>();
    private final List<String[]> triviaQuestions = new ArrayList<>();
    
    public MiniGames() {
        // Word Bank for different levels
        wordBank.put("Easy", new String[]{"cat:Small furry pet", "dog:Man's best friend", "fish:Lives in water", 
                                          "bird:Can fly", "ant:Tiny insect", "cow:Produces milk"});
        wordBank.put("Medium", new String[]{"guitar:Instrument with strings", "pencil:Used for writing", 
                                            "planet:Earth is one", "flower:Blooms in spring", 
                                            "garden:Place to grow plants", "bottle:Holds liquids"});
        wordBank.put("Hard", new String[]{"elephant:Large animal with trunk", "mountain:Large natural elevation", 
                                          "dolphin:Intelligent sea mammal", "universe:All of space", 
                                          "chocolate:Sweet treat", "microscope:Used to see small things"});

        // Trivia Questions
        triviaQuestions.add(new String[]{"What is the largest planet?", "Jupiter"});
        triviaQuestions.add(new String[]{"Who wrote 'Hamlet'?", "Shakespeare"});
        triviaQuestions.add(new String[]{"What is the capital of France?", "Paris"});
        triviaQuestions.add(new String[]{"Which organ pumps blood in the body?", "Heart"});
        triviaQuestions.add(new String[]{"Which element has the symbol O?", "Oxygen"});
        triviaQuestions.add(new String[]{"What gas do plants produce during photosynthesis?", "Oxygen"});
        triviaQuestions.add(new String[]{"What is the capital of Japan?", "Tokyo"});
        triviaQuestions.add(new String[]{"What is the tallest mountain in the world?", "Mount Everest"});
        triviaQuestions.add(new String[]{"Who painted the Mona Lisa?", "Leonardo da Vinci"});
        triviaQuestions.add(new String[]{"What is the chemical symbol for gold?", "Au"});
        triviaQuestions.add(new String[]{"Which animal is known as the king of the jungle?", "Lion"});
    }
    
    

    public void chooseGame(Pet pet) {
        String[] options = {"Word gusessing Game", "Trivia Game", "PinPoint Game", "Exit"};
        boolean exitGame = false;

        while (!exitGame) {
            // Create a new frame for mini-games
            JFrame frame = new JFrame("Mini-Games");
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            // Add window listener for close button
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    int confirmExit = JOptionPane.showConfirmDialog(frame, 
                        "Do you want to exit mini-games?", "Exit Mini-Games", JOptionPane.YES_NO_OPTION);
                    if (confirmExit == JOptionPane.YES_OPTION) {
                        frame.dispose();  // Close the frame if Yes
                    }
                }
            });

            int choice = JOptionPane.showOptionDialog(frame, "Choose a game:", "Mini-Games",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == JOptionPane.CLOSED_OPTION) {
                // Handle window close action
                int confirmExit = JOptionPane.showConfirmDialog(frame, 
                        "Do you want to exit mini-games?", "Exit Mini-Games", JOptionPane.YES_NO_OPTION);
                if (confirmExit == JOptionPane.YES_OPTION) {
                    frame.dispose(); // Close the frame if Yes
                    break; // Exit the loop if Yes
                }
            }

            switch (choice) {
                case 0 -> playWordGame(pet); // Word Game
                case 1 -> playTriviaGame(pet); // Trivia Game
                case 2 -> playPinpointGame(pet); // pinpoint Game
                case 3 -> exitGame = true; // Exit
                default -> JOptionPane.showMessageDialog(frame, "Invalid choice. Try again.");
            }
        }
    }

    public void playWordGame(Pet pet) {
        String[] levels = {"Easy", "Medium", "Hard"};
        int levelChoice = JOptionPane.showOptionDialog(null, "Choose a difficulty level:", "Word Game",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, levels, levels[0]);

        if (levelChoice == -1) return;

        String selectedLevel = levels[levelChoice];
        String[] words = wordBank.get(selectedLevel);
        Random random = new Random();
        int attempts = 0, score = 0;

        JOptionPane.showMessageDialog(null, "In this game, you will guess a word letter by letter.\n"
                + "You will get a hint after your 3rd attempt if needed.\n"
                + "Try to guess the word in fewer attempts to earn more points.");

        while (true) {
            String[] wordAndHint = words[random.nextInt(words.length)].split(":");
            String word = wordAndHint[0];
            String hint = wordAndHint[1];
            StringBuilder displayWord = new StringBuilder("_".repeat(word.length()));
            Set<Character> guessed = new HashSet<>();

            boolean guessedCorrectly = false;
            attempts = 0;

            while (attempts < 10 && !guessedCorrectly) {
                String guess = JOptionPane.showInputDialog(
                        "Word: " + displayWord + "\nAttempts left: " + (10 - attempts) +
                                "\nThe word has " + word.length() + " letters." +
                                (attempts >= 3 ? "\nHint: " + hint : ""));
                
                if (guess == null || guess.isEmpty()) {
                    int exitChoice = JOptionPane.showConfirmDialog(null, "Do you want to exit the Word Game?", 
                                                                   "Exit Word Game", JOptionPane.YES_NO_OPTION);
                    if (exitChoice == JOptionPane.YES_OPTION) {
                        return; // Exit the game if Yes
                    } else {
                        continue; // Continue the game if No
                    }
                }

                char guessedChar = guess.charAt(0);
                if (guessed.contains(guessedChar)) {
                    JOptionPane.showMessageDialog(null, "You already guessed this letter.");
                    continue;
                }
                guessed.add(guessedChar);

                boolean letterFound = false;
                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) == guessedChar) {
                        displayWord.setCharAt(i, guessedChar);
                        letterFound = true;
                    }
                }

                if (!letterFound) {
                    JOptionPane.showMessageDialog(null, "Sorry, that letter is not in the word.");
                }

                if (displayWord.toString().equals(word)) {
                    guessedCorrectly = true;
                    score += 10;
                    JOptionPane.showMessageDialog(null, "Correct! The word was: " + word);
                }
                attempts++;
                if (!guessedCorrectly && attempts == 10) {
                    JOptionPane.showMessageDialog(null, "Out of attempts! The word was: " + word);
                    pet.happiness -= 5;
                }
            }

            pet.happiness += score;
            int continueChoice = JOptionPane.showConfirmDialog(null, "Do you want to play another word?", 
                                                               "Word Game", JOptionPane.YES_NO_OPTION);
            if (continueChoice == JOptionPane.NO_OPTION) break;
        }

        JOptionPane.showMessageDialog(null, "Your final score: " + score);
    }

    public void playTriviaGame(Pet pet) {
        int score = 0;
        int correctAnswers = 0;
        int wrongAnswers = 0;
        int totalQuestionsAnswered = 0;
        Random random = new Random();

        while (true) {
            Collections.shuffle(triviaQuestions);

            for (int i = 0; i < triviaQuestions.size(); i++) {
                String[] questionAndAnswer = triviaQuestions.get(i);
                String question = questionAndAnswer[0];
                String correctAnswer = questionAndAnswer[1];

                String userAnswer = JOptionPane.showInputDialog(question);

                if (userAnswer == null) {
                    int exitChoice = JOptionPane.showConfirmDialog(null, "Do you want to exit the Trivia Game?", 
                                                                   "Exit Trivia Game", JOptionPane.YES_NO_OPTION);
                    if (exitChoice == JOptionPane.YES_OPTION) {
                        return; // Exit the trivia game
                    }
                }

                if (userAnswer != null && userAnswer.equalsIgnoreCase(correctAnswer)) {
                    correctAnswers++;
                    JOptionPane.showMessageDialog(null, "Correct!");
                } else {
                    wrongAnswers++;
                    JOptionPane.showMessageDialog(null, "Wrong! The correct answer was: " + correctAnswer);
                }

                totalQuestionsAnswered++;

                if (totalQuestionsAnswered % 3 == 0) {
                    int continueChoice = JOptionPane.showConfirmDialog(null, 
                        "You've answered " + totalQuestionsAnswered + " questions! Do you want to continue playing trivia?", 
                        "Trivia Game", JOptionPane.YES_NO_OPTION);

                    if (continueChoice == JOptionPane.NO_OPTION) {
                        break;
                    }
                }
            }

            score = correctAnswers * 10;
            pet.happiness += score;

            JOptionPane.showMessageDialog(null, "You answered " + correctAnswers + " questions correctly and " +
                    wrongAnswers + " incorrectly. Your final score is: " + score);

            int playAgain = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Trivia Game", JOptionPane.YES_NO_OPTION);
            if (playAgain != JOptionPane.YES_OPTION) {
                break;
            } else {
                correctAnswers = 0;
                wrongAnswers = 0;
                score = 0;
                totalQuestionsAnswered = 0;
            }
        }
    }
    
     public void playPinpointGame(Pet pet) {
        final int GRID_SIZE = 5; // 5x5 grid
        final int TARGET_X = (int) (Math.random() * GRID_SIZE); // Random X-coordinate of the target
        final int TARGET_Y = (int) (Math.random() * GRID_SIZE); // Random Y-coordinate of the target

        // Create a new JFrame for the Pinpoint game
        JFrame frame = new JFrame("Pinpoint Game");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Keep mini-games open
        frame.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        JButton[][] buttons = new JButton[GRID_SIZE][GRID_SIZE];

        // Initialize the grid of buttons
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setPreferredSize(new Dimension(60, 60));
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j, buttons, TARGET_X, TARGET_Y, pet, frame));
                frame.add(buttons[i][j]);
            }
        }

        // Display the instructions
        showInstructions(frame);

        frame.pack();
        frame.setVisible(true);
    }

    // Show instructions for playing the game
    private void showInstructions(JFrame frame) {
        String instructions = "Welcome to the Pinpoint Game!\n\n"
                + "Objective: Find the hidden object in the grid.\n"
                + "Click on a cell to make a guess. The object is hidden in a random cell.\n"
                + "If you get close, you'll be told you're 'warmer'. If you move away, you're 'colder'.\n"
                + "Keep guessing until you find it! Good luck!";
        JOptionPane.showMessageDialog(frame, instructions, "Game Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    // Action listener for button clicks
    private class ButtonClickListener implements ActionListener {
        private int row, col;
        private JButton[][] buttons;
        private int targetX, targetY;
        private Pet pet;
        private JFrame frame;

        public ButtonClickListener(int row, int col, JButton[][] buttons, int targetX, int targetY, Pet pet, JFrame frame) {
            this.row = row;
            this.col = col;
            this.buttons = buttons;
            this.targetX = targetX;
            this.targetY = targetY;
            this.pet = pet;
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Check if the player guessed the correct spot
            if (row == targetX && col == targetY) {
                // Mark the correct cell with an "X"
                buttons[row][col].setText("X");
                buttons[row][col].setBackground(Color.GREEN);

                // Increase pet's happiness
                pet.happiness += 10;

                // Show win message
                JOptionPane.showMessageDialog(null, "Congratulations! You found the hidden object!");

                // Ask the user if they want to play again
                int choice = JOptionPane.showConfirmDialog(frame, "Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    // Reset the game
                    resetGame();
                } else {
                    frame.dispose(); // Close the game window
                }
            } else {
                // Give feedback based on distance
                int distance = Math.abs(targetX - row) + Math.abs(targetY - col);
                if (distance == 1) {
                    JOptionPane.showMessageDialog(null, "You're getting warmer!");
                } else if (distance == 2) {
                    JOptionPane.showMessageDialog(null, "You're getting colder!");
                } else {
                    JOptionPane.showMessageDialog(null, "You're far away!");
                }
            }
        }

        // Reset the game when the player chooses to play again
        private void resetGame() {
            // Randomly set the target location again
            targetX = (int) (Math.random() * 5);
            targetY = (int) (Math.random() * 5);

            // Reset button texts and backgrounds
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    buttons[i][j].setText("");
                    buttons[i][j].setBackground(Color.LIGHT_GRAY);
                }
            }
        }
    }

}