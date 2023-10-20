import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MonthlyExpenseOrganizerGUI2 {
    private final Map<String, Double> expenses = new HashMap<>();
    private double totalSavings = 0.0;  // Stores total savings
    private double savingsGoal = 0.0;   // Stores the savings goal
    private JFrame frame;
    private JComboBox<String> categoryComboBox;
    private JTextField amountField;
    private JTextField savingsGoalField; // New Field for Savings Goal
    private JTextArea resultArea;
    private String pinCode; 

    private final String[] categories = {"Utilities", "Food", "Transportation", "Rent", "Investments", "Entertainment"};

    public MonthlyExpenseOrganizerGUI2() {
        showPinSetup(); // Initiate the PIN setup before initializing the main app
    }

        private void showPinSetup() {
        // Create a new frame for the PIN setup
        JFrame pinFrame = new JFrame("Set Your PIN");
        pinFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pinFrame.setLayout(new FlowLayout());

        // Create the PIN setup components
        JLabel pinLabel = new JLabel("Set your 4-digit PIN:");
        JPasswordField pinField = new JPasswordField(4); // Limit to 4 characters for PIN
        JButton setPinButton = new JButton("Set PIN");

        // Add components to the PIN frame
        pinFrame.add(pinLabel);
        pinFrame.add(pinField);
        pinFrame.add(setPinButton);

        // Add functionality to handle the PIN setup process
        setPinButton.addActionListener(e -> {
            String enteredPin = new String(pinField.getPassword());
            if (isValidPin(enteredPin)) {
                pinCode = enteredPin;
                pinFrame.dispose();
                showLoginScreen(); // Proceed to the login screen after setting the PIN
            } else {
                JOptionPane.showMessageDialog(pinFrame, "Invalid PIN. Please enter a 4-digit PIN.");
            }
        });

        // Set the size of the PIN frame, finalize its configuration, and make it visible
        pinFrame.pack();
        pinFrame.setVisible(true);
    }

    private boolean isValidPin(String pin) {
        // The PIN is valid if it is 4 digits long and numeric
        return pin.length() == 4 && pin.matches("\\d+");
    }

    private void showLoginScreen() {
        // Create a new frame for logging in
        JFrame loginFrame = new JFrame("User Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new FlowLayout());

        // Create login components
        JLabel enterPinLabel = new JLabel("Enter your PIN:");
        JPasswordField enterPinField = new JPasswordField(4);
        JButton loginButton = new JButton("Login");

        // Add components to the login frame
        loginFrame.add(enterPinLabel);
        loginFrame.add(enterPinField);
        loginFrame.add(loginButton);

        // Add functionality to handle the login process
        loginButton.addActionListener(e -> {
            String enteredPin = new String(enterPinField.getPassword());
            if (enteredPin.equals(pinCode)) {
                loginFrame.dispose();
                initializeHomeScreen(); // Initialize the main application window after successful login
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Incorrect PIN. Please try again.");
            }
        });

        // Set the size of the login frame, finalize its configuration, and make it visible
        loginFrame.pack();
        loginFrame.setVisible(true);
    }



    private void initializeHomeScreen() {
        frame = new JFrame("Monthly Expense Organizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel entryPanel = new JPanel(new FlowLayout());
        categoryComboBox = new JComboBox<>(categories);
        amountField = new JTextField(10);
        savingsGoalField = new JTextField(10); // For entering Savings Goal
        JButton addExpenseButton = new JButton("Add Expense");
        JButton addSavingsButton = new JButton("Add Savings");
        JButton setSavingsGoalButton = new JButton("Set Savings Goal"); // New Button to set goal
        JButton generateSummaryButton = new JButton("Generate Monthly Summary"); // New Button for Monthly Summary
        JButton resetButton = new JButton("Reset");

        entryPanel.add(new JLabel("Category:"));
        entryPanel.add(categoryComboBox);
        entryPanel.add(new JLabel("Amount:"));
        entryPanel.add(amountField);
        entryPanel.add(addExpenseButton);
        entryPanel.add(addSavingsButton);
        entryPanel.add(new JLabel("Savings Goal:")); // New label and text field for Savings Goal
        entryPanel.add(savingsGoalField);
        entryPanel.add(setSavingsGoalButton); // Add the new button to the panel
        entryPanel.add(generateSummaryButton); // Add the Generate Monthly Summary button
        entryPanel.add(resetButton);

        resultArea = new JTextArea(15, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);           

        addExpenseButton.addActionListener(e -> {
            try {
                String category = (String) categoryComboBox.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText());
                expenses.put(category, expenses.getOrDefault(category, 0.0) + amount);
                updateResultArea();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount.");
            }
        });

        addSavingsButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                totalSavings += amount; // Simply add to total savings
                updateResultArea();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount.");
            }
        });

        setSavingsGoalButton.addActionListener(e -> {
            try {
                double goalAmount = Double.parseDouble(savingsGoalField.getText());
                savingsGoal = goalAmount; // Simply set the savings goal
                JOptionPane.showMessageDialog(frame, "Savings goal set: $" + goalAmount);
                updateResultArea();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid savings goal amount.");
            }
        });
        
        
        generateSummaryButton.addActionListener(e -> showMonthlySummary());
        
         resetButton.addActionListener(e -> {
            expenses.clear();
            totalSavings = 0.0;
            savingsGoal = 0.0; // Optional: You can decide whether to reset this as well
            amountField.setText("");
            savingsGoalField.setText("");
            updateResultArea();
        });

        
    

        frame.add(entryPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        updateResultArea(); // Initial update
    }
    
    
       private void showMonthlySummary() {
        // Calculate the summary details
        double totalExpenses = expenses.values().stream().mapToDouble(Double::doubleValue).sum();
        boolean isGoalMet = totalSavings >= savingsGoal;

        // Prepare the summary message
        StringBuilder summaryMessage = new StringBuilder();
        summaryMessage.append("Monthly Summary:\n\n");
        summaryMessage.append("Total Expenses: $").append(String.format("%.2f", totalExpenses)).append("\n");
        summaryMessage.append("Total Savings: $").append(String.format("%.2f", totalSavings)).append("\n");
        summaryMessage.append("Savings Goal: $").append(String.format("%.2f", savingsGoal)).append("\n");
        summaryMessage.append(isGoalMet ? "Congratulations! You've met your savings goal!\n" : "You did not meet your savings goal.\n");
        summaryMessage.append("\nExpenses Breakdown:\n");

        // Append the breakdown of expenses by category
        for (String category : categories) {
            double amount = expenses.getOrDefault(category, 0.0);
            summaryMessage.append(category).append(": $").append(String.format("%.2f", amount)).append("\n");
        }

        // Create and show a dialog containing the summary
        JTextArea summaryArea = new JTextArea(summaryMessage.toString());
        summaryArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(summaryArea);

        JDialog summaryDialog = new JDialog(frame, "Monthly Summary", true);
        summaryDialog.setLayout(new BorderLayout());
        summaryDialog.add(scrollPane, BorderLayout.CENTER); // Add summary text to the dialog

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> summaryDialog.dispose());
        summaryDialog.add(closeButton, BorderLayout.PAGE_END); // Add close button at the bottom

        summaryDialog.setSize(350, 300); // Adjust the size as needed
        summaryDialog.setLocationRelativeTo(frame);
        summaryDialog.setVisible(true);
    }
    
    private void updateResultArea() {
    StringBuilder resultText = new StringBuilder();
        double totalExpenses = 0.0;

        // Calculate total expenses
        for (double amount : expenses.values()) {
            totalExpenses += amount;
        }

        // Build result string for expenses and calculate the percentage
        resultText.append("Expenses:\n");
        for (String category : categories) {
            double amount = expenses.getOrDefault(category, 0.0);
            resultText.append(category).append(": $").append(String.format("%.2f", amount));
            if (totalExpenses > 0) { // To avoid dividing by zero
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
        if (totalSavings >= savingsGoal && savingsGoal > 0) { 
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
        SwingUtilities.invokeLater(() -> new MonthlyExpenseOrganizerGUI2());
    }
}



