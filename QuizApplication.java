import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class QuizApplication extends JFrame {
    private int questionsNumber = 3;
    private int[] selectedAnswers = new int[questionsNumber];
    private JLabel questionLabel;
    private JRadioButton[] options;
    private JButton nextButton;
    private ButtonGroup optionsGroup;
    private   JPanel buttonPanel;
    private JPanel mainPanel; // Main panel to switch between start page and questions



    // theme
    Color darkColor;
    Color primaryColor;
    Color secondaryColor;
    Color lightColor;


    // Array to store questions and their options
    private String[][][] questions = {
            {{"What is the capital of France?"}, {"Paris", "Rome", "Berlin", "Madrid",}, {"Paris"}},
            {{"Which planet is known as the Red Planet?"}, {"Mercury", "Jupiter", "Mars", "Venus",}, {"Mars"}},
            {{"What is the largest mammal?"}, {"Elephant", "Blue Whale", "Giraffe", "Horse"}, {"Blue Whale"}}
    };

    private int currentQuestion = 0;
    private int score = 0;


    /**
     * CustomButton is a subclass of JButton that customizes its appearance and behavior.
     * It provides a button with custom border colors and padding, and changes the border color
     * on mouse hover.
     */
    class CustomButton extends JButton {

        // Fields to store default and hover borders
        private Border defaultBorder;
        private Border hoverBorder;

        /**
         * Constructs a CustomButton with the specified text.
         *
         * @param text the text to be displayed on the button
         */
        public CustomButton(String text) {
            super(text); // Call superclass constructor

            // Create custom borders with specified colors
            defaultBorder = BorderFactory.createLineBorder(lightColor, 1); // lightColor and primaryColor are assumed to be defined elsewhere
            hoverBorder = BorderFactory.createLineBorder(primaryColor, 1);

            // Set the default border
            setBorder(defaultBorder);

            // Set preferred size
            setPreferredSize(new Dimension(70, 30));

            // Add mouse listener to handle hover effects
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBorder(hoverBorder); // Change border on mouse enter
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBorder(defaultBorder); // Change border back on mouse exit
                }
            });

            // Set button's foreground and background colors
            setForeground(primaryColor); // primaryColor assumed to be defined elsewhere
            setBackground(darkColor); // darkColor assumed to be defined elsewhere

            // Set padding (margin) for the button
            int topPadding,bottomPadding,rightPadding,leftPadding;
            bottomPadding = topPadding= 10;
            rightPadding =leftPadding = 20;
            super.setMargin(new Insets(topPadding, leftPadding, bottomPadding, rightPadding));

            // Set additional properties
            super.setOpaque(true); // Make button opaque
            super.setFocusPainted(false); // Disable focus painting
            super.setFocusable(false); // Make button not focusable
            super.setSize(20, 10); // Set the size (this might be overridden by layout managers)
        }
    }


    public QuizApplication() {
        setTitle("Simple Quiz App");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        setCustomLookAndFeel(); // Set custom look and feel

        initComponents(); // Initialize components
        resetAnswers();
        displayStartPage(); // Display the start page initially

//        displayQuestion(); // Display the first question

    }

    private void setCustomLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            primaryColor = new Color(0x00ADB5); // Primary color: #00ADB5
            secondaryColor = new Color(0x393E46); // Secondary color: #393E46
            lightColor = new Color(0xEEEEEE); // Light color: #EEEEEE
            darkColor = new Color(0x222831); // Dark color: #222831

            UIManager.put("Panel.background", darkColor);
            UIManager.put("Label.foreground", lightColor);
            UIManager.put("Label.background", darkColor);
            UIManager.put("Button.background", darkColor);
            UIManager.put("Button.foreground", primaryColor);
            UIManager.put("RadioButton.background", darkColor);
            UIManager.put("RadioButton.foreground", lightColor);

            UIManager.put("Label.font", new Font("Arial", Font.BOLD, 20)); // Set font size
            UIManager.put("RadioButton.font", new Font("Arial", Font.BOLD, 16)); // Set font size
            UIManager.put("Button.font", new Font("Arial", Font.BOLD, 16)); // Set font size


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOptionStyle() {
        for (int i = 0; i < 4; i++) {
            JRadioButton option = options[i];

            if (option.isSelected()) {
                option.setForeground(primaryColor);
            } else {
                option.setForeground(lightColor);
            }
        }


    }


    private void resetAnswers() {
        for (int questionIndex = 0; questionIndex < questionsNumber; questionIndex++) {
            selectedAnswers[questionIndex] = 5;
        }
    }

    private void setButtons() {
        // Create button to move to next question
        buttonPanel = new JPanel(new FlowLayout());


        JButton backButton = new CustomButton("Back");

        buttonPanel.add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuestion > 0) {
                    currentQuestion--;
                    displayQuestion();
                }

            }
        });
        nextButton = new CustomButton("Next");
        buttonPanel.add(nextButton);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedAnswers[currentQuestion] = getSelectedAnswer(); // Save  answer
                currentQuestion++; // Move to next question
                if (currentQuestion < questions.length)
                    displayQuestion(); // If there are more questions, display next question
                else
                    showResult(); // Otherwise, show the final score
            }
        });


        add(buttonPanel, BorderLayout.SOUTH);


    }


    private void initComponents() {
        setLayout(new BorderLayout());

        // Create main panel to switch between start page and questions
        mainPanel = new JPanel(new CardLayout());

        // Create start page
        JPanel startPanel = new JPanel(new BorderLayout());
        JLabel startLabel = new JLabel("Welcome to the Quiz App!");
        startLabel.setHorizontalAlignment(JLabel.CENTER);
        startPanel.add(startLabel, BorderLayout.CENTER);

        JButton startButton = new CustomButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
                cardLayout.next(mainPanel); // Switch to the next panel (questions)
                displayQuestion(); // Display the first question
            }
        });
        startPanel.add(startButton, BorderLayout.SOUTH);

        mainPanel.add(startPanel, "start");

        // Create panel to display questions
        JPanel questionsPanel = new JPanel(new BorderLayout());
        questionLabel = new JLabel();
        questionLabel.setOpaque(true);
        questionLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        questionsPanel.add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
        optionsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        options = new JRadioButton[4];
        optionsGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            JRadioButton option = new JRadioButton();
            option.setFocusable(false);
            option.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setOptionStyle();
                }
            });
            options[i] = option;
            optionsPanel.add(options[i]);
            optionsGroup.add(options[i]);
        }

        questionsPanel.add(optionsPanel, BorderLayout.CENTER);

        mainPanel.add(questionsPanel, "questions");
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));


        add(mainPanel, BorderLayout.CENTER);
    }

    private void displayStartPage() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "start"); // Show the start page
    }

    private void displayQuestion() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "questions"); // Show the questions panel

        questionLabel.setText(questions[currentQuestion][0][0]);
        for (int i = 0; i < 4; i++) {
            options[i].setText(questions[currentQuestion][1][i]);
            options[i].setSelected(false);
        }
        optionsGroup.clearSelection();

        try {
            if (!(selectedAnswers[currentQuestion] == 5)) {
                options[selectedAnswers[currentQuestion]].setSelected(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setOptionStyle();
        setButtons();

    }


    private void checkAnswers() {
        // Check if the selected option is correct
        for (int q = 0; q < questionsNumber; q++) {
            for (int i = 0; i < 4; i++) {
                if (options[i].isSelected() && options[i].getText().equals(questions[q][2][0])) {
                    score++; // Increment score if correct option is selected
                    break;
                }
            }
        }
    }

    private int getSelectedAnswer() {
        // Check if the selected option is correct
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                return i;
            }
        }
        return 5;
    }

    private void showResult() {
        checkAnswers(); // Check the selected answers

        // Display final score with the score number in a different color
        JLabel scoreLabel = new JLabel("Quiz Finished!\nYour Score: ");
        JLabel scoreNumberLabel = new JLabel(score + " / " + questions.length);
        scoreNumberLabel.setForeground(primaryColor); // Set the color of the score number

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Increase font size of the label
        scoreNumberLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Increase font size of the score number
        JPanel panel = new JPanel(new GridLayout(2, 1)); // Adjust layout for bigger window
        panel.add(scoreLabel);
        panel.add(scoreNumberLabel);

        // Create a custom OK button and disable focus
        JButton okButton = new CustomButton("OK");
        // Add action listener to handle OK button click
        okButton.addActionListener(e -> {
            // Close the dialog
            SwingUtilities.getWindowAncestor((Component) e.getSource()).dispose();
            resetApp();


//            System.exit(0); // Exit the application
        });

        // Create a panel to hold the OK button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(okButton);

        // Add button panel to main panel
        panel.add(buttonPanel);

        // Create the dialog without default OK button
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout());
        dialog.setTitle("Quiz Finished!");
        dialog.add(panel,BorderLayout.NORTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

    }
    private void resetApp(){
        currentQuestion =0;
        displayStartPage();
        remove(buttonPanel);
        resetAnswers();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuizApplication().setVisible(true); // Create and display the quiz app
            }
        });
    }
}
