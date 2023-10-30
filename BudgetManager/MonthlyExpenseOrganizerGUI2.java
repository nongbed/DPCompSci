package BudgetManager;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MonthlyExpenseOrganizerGUI {
	// Map to store each expense category and its corresponding amount
    private final Map<String, Double> expenses = new HashMap<>();
    private double totalSavings = 0.0; // Variable to track the total savings
    private double savingsGoal = 0.0; // Variable to store the savings goal
    private double expenseLimit = 0.0; // New field for the expense limit
    private boolean goalAchievedNotified = false; // Flag to check if the user has been notified about reaching the goal
    private JFrame frame; // Main window frame
    // GUI components for user interaction
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> monthComboBox;
    private JTextField amountField;
    private JTextField savingsGoalField;
    private JTextField expenseLimitField; // New field for inputting
    private JTextArea resultArea;
    private String pinCode; // User's PIN code for authentication
    private String currentMonth; // The current month selected by the user
    

    // Predefined categories and months
    private final String[] categories = {"Utilities", "Food", "Transportation", "Rent", "Investments", "Entertainment"};
    private final String[] months = {"January", "February", "March", "April", "May", "June",
                                     "July", "August", "September", "October", "November", "December"};
    private static final String PIN_FILE_PATH = "pincode.txt";


    public MonthlyExpenseOrganizerGUI() {
    	String savedPin = readPinFromFile();
        if (savedPin != null) {
            this.pinCode = savedPin; // Use the saved PIN
            showLoginScreen(); // Skip PIN setup and ask the user to log in
        } else {
            showPinSetup(); // Prompt the user to set up a new PIN
        }
    }
    
    private void savePinToFile(String pin) {
        try (FileWriter writer = new FileWriter(PIN_FILE_PATH)) {
            writer.write(pin);
        } catch (IOException e) {
            e.printStackTrace(); // Log an error if the file can't be written
            // Here, you might want to show a dialog to the user, or handle the error in another suitable way.
        }
    }
    
    private String readPinFromFile() {
        try {
            File pinFile = new File(PIN_FILE_PATH);
            if (pinFile.exists()) {
                return new String(Files.readAllBytes(Paths.get(PIN_FILE_PATH)));
            }
        } catch (IOException e) {
            e.printStackTrace(); // Log an error if the file can't be read
            // Similar to the save method, you might want to handle this error in a user-friendly way.
        }
        return null; // Return null if no PIN was found or an error occurred
    }


    

    private void showPinSetup() {
        JFrame pinFrame = new JFrame("Set Your PIN");
        pinFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pinFrame.setLayout(new FlowLayout());

        JLabel pinLabel = new JLabel("Set your 4-digit PIN:");
        JPasswordField pinField = new JPasswordField(4);
        JButton setPinButton = new JButton("Set PIN");

        pinFrame.add(pinLabel);
        pinFrame.add(pinField);
        pinFrame.add(setPinButton);

        setPinButton.addActionListener(e -> {
            String enteredPin = new String(pinField.getPassword());
            if (isValidPin(enteredPin)) {
                pinCode = enteredPin;
                savePinToFile(enteredPin); // Save the PIN when it's created
                pinFrame.dispose();
                showLoginScreen();
            } else {
                JOptionPane.showMessageDialog(pinFrame, "Invalid PIN. Please enter a 4-digit PIN.");
            }
        });


        pinFrame.pack();
        pinFrame.setVisible(true);
    }

    private boolean isValidPin(String pin) {
        return pin.length() == 4 && pin.matches("\\d+");
    }

    private void showLoginScreen() {
        JFrame loginFrame = new JFrame("User Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new FlowLayout());

        JLabel enterPinLabel = new JLabel("Enter your PIN:");
        JPasswordField enterPinField = new JPasswordField(4);
        JButton loginButton = new JButton("Login");

        loginFrame.add(enterPinLabel);
        loginFrame.add(enterPinField);
        loginFrame.add(loginButton);

        loginButton.addActionListener(e -> {
            String enteredPin = new String(enterPinField.getPassword());
            if (enteredPin.equals(pinCode)) {
                loginFrame.dispose();
                initializeHomeScreen();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Incorrect PIN. Please try again.");
            }
        });

        loginFrame.pack();
        loginFrame.setVisible(true);
    }

    private void initializeHomeScreen() {
        frame = new JFrame("Monthly Expense Organizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel entryPanel = new JPanel(new FlowLayout());
        categoryComboBox = new JComboBox<>(categories);
        monthComboBox = new JComboBox<>(months);
        monthComboBox.addActionListener(e -> currentMonth = (String) monthComboBox.getSelectedItem());

        amountField = new JTextField(10);
        savingsGoalField = new JTextField(10);
        expenseLimitField = new JTextField(10);
        JButton addExpenseButton = new JButton("Add Expense");
        JButton addSavingsButton = new JButton("Add Savings");
        JButton setSavingsGoalButton = new JButton("Set Savings Goal");
        JButton setExpenseLimitButton = new JButton("Set Expense Limit");
        JButton generateSummaryButton = new JButton("Generate Monthly Summary");
        JButton saveSummaryButton = new JButton("Save Summary");
        JButton resetButton = new JButton("Reset");

        entryPanel.add(new JLabel("Month:"));
        entryPanel.add(monthComboBox);
        entryPanel.add(new JLabel("Category:"));
        entryPanel.add(categoryComboBox);
        entryPanel.add(new JLabel("Amount:"));
        entryPanel.add(amountField);
        entryPanel.add(addExpenseButton);
        entryPanel.add(addSavingsButton);
        entryPanel.add(new JLabel("Expense Limit:"));
        entryPanel.add(expenseLimitField);
        entryPanel.add(setExpenseLimitButton);
        entryPanel.add(new JLabel("Savings Goal:"));
        entryPanel.add(savingsGoalField);
        entryPanel.add(setSavingsGoalButton);
        entryPanel.add(generateSummaryButton);
        entryPanel.add(saveSummaryButton);
        entryPanel.add(resetButton);

        resultArea = new JTextArea(15, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        currentMonth = (String) monthComboBox.getSelectedItem();

        addExpenseButton.addActionListener(e -> {
            try {
                String category = (String) categoryComboBox.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText());
                expenses.put(category, expenses.getOrDefault(category, 0.0) + amount);

                // Update the result area first
                updateResultArea();

                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount.");
            }
        });


        addSavingsButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                totalSavings += amount;
                updateResultArea();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount.");
            }
        });
        
        addExpenseButton.addActionListener(e -> {
            try {
                String category = (String) categoryComboBox.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText());
                double previousTotalExpenses = expenses.values().stream().mapToDouble(Double::doubleValue).sum();
                expenses.put(category, expenses.getOrDefault(category, 0.0) + amount);

                updateResultArea();  // Update the UI to reflect the new expense.

                // Check if the expense limit has been reached after adding the new expense.
                double newTotalExpenses = previousTotalExpenses + amount;
                if (newTotalExpenses >= expenseLimit && previousTotalExpenses < expenseLimit) {
                    showExpenseLimitReachedDialog();  // This is a new method you will create.
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount.");
            }
        });


        setSavingsGoalButton.addActionListener(e -> {
            try {
                double goalAmount = Double.parseDouble(savingsGoalField.getText());
                savingsGoal = goalAmount;
                JOptionPane.showMessageDialog(frame, "Savings goal set: $" + goalAmount);
                updateResultArea();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid savings goal amount.");
            }
        });
        
        setExpenseLimitButton.addActionListener(e -> {
            try {
                double limit = Double.parseDouble(expenseLimitField.getText());
                expenseLimit = limit;
                JOptionPane.showMessageDialog(frame, "Expense limit set: $" + limit);
                updateResultArea();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid expense limit amount.");
            }
        });


        generateSummaryButton.addActionListener(e -> showMonthlySummary());

        saveSummaryButton.addActionListener(e -> saveMonthlySummaryToFile());

        resetButton.addActionListener(e -> {
            expenses.clear();
            totalSavings = 0.0;
            savingsGoal = 0.0;
            amountField.setText("");
            savingsGoalField.setText("");
            updateResultArea();
        });

        frame.add(entryPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        updateResultArea();
    }
    
    
    private void showExpenseLimitReachedDialog() {
        JDialog limitReachedDialog = new JDialog(frame, "Limit Reached", true);
        limitReachedDialog.setLayout(new FlowLayout());
        JLabel messageLabel = new JLabel("YOU REACHED YOUR LIMIT!");
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> limitReachedDialog.dispose());
        limitReachedDialog.add(messageLabel);
        limitReachedDialog.add(closeButton);
        limitReachedDialog.pack();
        limitReachedDialog.setLocationRelativeTo(frame);
        limitReachedDialog.setVisible(true);
    }


    private void showMonthlySummary() {
        double totalExpenses = expenses.values().stream().mapToDouble(Double::doubleValue).sum();
        boolean isGoalMet = totalSavings >= savingsGoal;

        StringBuilder summaryMessage = new StringBuilder();
        summaryMessage.append("Monthly Summary for ").append(currentMonth).append(":\n\n");
        summaryMessage.append("Total Expenses: $").append(String.format("%.2f", totalExpenses)).append("\n");
        summaryMessage.append("Expense Limit: $").append(String.format("%.2f", expenseLimit)).append("\n");
        summaryMessage.append("Total Savings: $").append(String.format("%.2f", totalSavings)).append("\n");
        summaryMessage.append("Savings Goal: $").append(String.format("%.2f", savingsGoal)).append("\n");
        summaryMessage.append(isGoalMet ? "Congratulations! You've met your savings goal!\n" : "You did not meet your savings goal.\n");
        summaryMessage.append("\nExpenses Breakdown:\n");

        for (String category : categories) {
            double amount = expenses.getOrDefault(category, 0.0);
            summaryMessage.append(category).append(": $").append(String.format("%.2f", amount)).append("\n");
        }

        JTextArea summaryArea = new JTextArea(summaryMessage.toString());
        summaryArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(summaryArea);

        JDialog summaryDialog = new JDialog(frame, "Monthly Summary", true);
        summaryDialog.setLayout(new BorderLayout());
        summaryDialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> summaryDialog.dispose());
        summaryDialog.add(closeButton, BorderLayout.PAGE_END);

        summaryDialog.setSize(350, 300);
        summaryDialog.setLocationRelativeTo(frame);
        summaryDialog.setVisible(true);
    }

    private void saveMonthlySummaryToFile() {
        StringBuilder summaryContent = new StringBuilder();
        double totalExpenses = expenses.values().stream().mapToDouble(Double::doubleValue).sum();
        boolean isGoalMet = totalSavings >= savingsGoal;

        summaryContent.append("Monthly Summary for ").append(currentMonth).append(":\n\n");
        summaryContent.append("Total Expenses: $").append(String.format("%.2f", totalExpenses)).append("\n");
        summaryContent.append("Total Savings: $").append(String.format("%.2f", totalSavings)).append("\n");
        summaryContent.append("Savings Goal: $").append(String.format("%.2f", savingsGoal)).append("\n");
        summaryContent.append(isGoalMet ? "Congratulations! You've met your savings goal!\n" : "You did not meet your savings goal.\n");
        summaryContent.append("\nExpenses Breakdown:\n");

        for (String category : categories) {
            double amount = expenses.getOrDefault(category, 0.0);
            summaryContent.append(category).append(": $").append(String.format("%.2f", amount)).append("\n");
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Monthly Summary");

        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(summaryContent.toString());
                JOptionPane.showMessageDialog(frame, "Summary saved to file: " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "An error occurred while saving the file. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateResultArea() {
        StringBuilder resultText = new StringBuilder();
        double totalExpenses = 0.0;

        for (double amount : expenses.values()) {
            totalExpenses += amount;
        }

        resultText.append("Expenses:\n");
        for (String category : categories) {
            double amount = expenses.getOrDefault(category, 0.0);
            resultText.append(category).append(": $").append(String.format("%.2f", amount));
            if (totalExpenses > 0) {
                double percentage = (amount / totalExpenses) * 100;
                resultText.append(" (").append(String.format("%.2f", percentage)).append("%)");
            } else {
                resultText.append(" (").append("0.00%").append(")");
            }
            resultText.append("\n");
        }

        resultText.append("\nTotal Savings: $").append(String.format("%.2f", totalSavings));
        resultText.append("\nSavings Goal: $").append(String.format("%.2f", savingsGoal));
        resultText.append("\nTotal Expenses: $").append(String.format("%.2f", totalExpenses));

        resultArea.setText(resultText.toString());
        
        
        
        if (totalSavings >= savingsGoal && savingsGoal > 0 && !goalAchievedNotified) {
            goalAchievedNotified = true;

            JDialog goalMetDialog = new JDialog(frame, "Goal Achieved!", true);
            goalMetDialog.setLayout(new FlowLayout());

            JLabel messageLabel = new JLabel("Congratulations! You've met your goal!!!");
            JButton closeButton = new JButton("Close");

            closeButton.addActionListener(e -> goalMetDialog.dispose());

            goalMetDialog.add(messageLabel);
            goalMetDialog.add(closeButton);
            goalMetDialog.pack();
            goalMetDialog.setLocationRelativeTo(frame);
            goalMetDialog.setVisible(true);
        }
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MonthlyExpenseOrganizerGUI());
    }
}
