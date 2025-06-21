import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ATMGUI {
    private JFrame frame;
    private JTextField accountField;
    private JPasswordField pinField;
    private ATMSystem atmSystem;
    private String currentAccount;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private Color primaryColor = new Color(0, 120, 215);
    private Color secondaryColor = new Color(245, 245, 245);
    private Color accentColor = new Color(255, 255, 255);

    public ATMGUI() {
        atmSystem = new ATMSystem();
        frame = new JFrame("ATM Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Create main panel with CardLayout for screen switching
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        // Add screens to main panel
        mainPanel.add(createLoginScreen(), "LOGIN");
        mainPanel.add(createMainMenu(), "MAIN_MENU");
        mainPanel.add(createAccountCreationScreen(), "CREATE_ACCOUNT");

        frame.add(mainPanel);
        frame.setVisible(true);
        showLoginScreen();
    }

    private JPanel createLoginScreen() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(secondaryColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Create a container panel for the login form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(accentColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Welcome to ATM System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(primaryColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        JLabel accountLabel = new JLabel("Account Number:");
        accountLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(accountLabel, gbc);

        accountField = new JTextField(20);
        accountField.setFont(new Font("Arial", Font.PLAIN, 16));
        accountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridx = 1;
        formPanel.add(accountField, gbc);

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(pinLabel, gbc);

        pinField = new JPasswordField(20);
        pinField.setFont(new Font("Arial", Font.PLAIN, 16));
        pinField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridx = 1;
        formPanel.add(pinField, gbc);

        JButton loginButton = createStyledButton("Login", 16, primaryColor);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(loginButton, gbc);

        JButton createAccountButton = createStyledButton("Create New Account", 14, new Color(100, 100, 100));
        gbc.gridy = 4;
        formPanel.add(createAccountButton, gbc);

        // Add the form panel to the login panel
        loginPanel.add(formPanel);

        loginButton.addActionListener(e -> {
            String accountNumber = accountField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();

            if (accountNumber.isEmpty() || pin.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both account number and PIN.");
                return;
            }

            if (atmSystem.login(accountNumber, pin)) {
                currentAccount = accountNumber;
                JOptionPane.showMessageDialog(frame, "Login successful!");
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid account or PIN.");
            }
        });

        createAccountButton.addActionListener(e -> showAccountCreationScreen());

        return loginPanel;
    }

    private JPanel createMainMenu() {
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(secondaryColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Create a container panel for the menu
        JPanel menuContainer = new JPanel(new GridBagLayout());
        menuContainer.setBackground(accentColor);
        menuContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome to ATM System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(primaryColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        menuContainer.add(welcomeLabel, gbc);

        String[] operations = {
            "Check Balance", "Withdraw Money", "Deposit Money",
            "Transfer Money", "Change PIN", "View History",
            "Print Receipt", "Logout"
        };

        for (int i = 0; i < operations.length; i++) {
            JButton button = createStyledButton(operations[i], 16, primaryColor);
            button.setPreferredSize(new Dimension(250, 50));
            
            gbc.gridx = i % 2;
            gbc.gridy = (i / 2) + 1;
            gbc.gridwidth = 1;
            menuContainer.add(button, gbc);

            final String operation = operations[i];
            button.addActionListener(e -> handleOperation(operation));
        }

        menuPanel.add(menuContainer);
        return menuPanel;
    }

    private JPanel createAccountCreationScreen() {
        JPanel createPanel = new JPanel(new GridBagLayout());
        createPanel.setBackground(secondaryColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Create a container panel for the form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(accentColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(primaryColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        JTextField nameField = new JTextField(20);
        JTextField initialDepositField = new JTextField(20);
        JPasswordField newPinField = new JPasswordField(20);
        JPasswordField confirmPinField = new JPasswordField(20);

        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        initialDepositField.setFont(new Font("Arial", Font.PLAIN, 16));
        newPinField.setFont(new Font("Arial", Font.PLAIN, 16));
        confirmPinField.setFont(new Font("Arial", Font.PLAIN, 16));

        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        initialDepositField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        newPinField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        confirmPinField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        addLabelAndField(formPanel, "Full Name:", nameField, 1, gbc);
        addLabelAndField(formPanel, "Initial Deposit:", initialDepositField, 2, gbc);
        addLabelAndField(formPanel, "New PIN:", newPinField, 3, gbc);
        addLabelAndField(formPanel, "Confirm PIN:", confirmPinField, 4, gbc);

        JButton createButton = createStyledButton("Create Account", 16, primaryColor);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(createButton, gbc);

        JButton backButton = createStyledButton("Back to Login", 14, new Color(100, 100, 100));
        gbc.gridy = 6;
        formPanel.add(backButton, gbc);

        createButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String initialDepositStr = initialDepositField.getText().trim();
                String pin = new String(newPinField.getPassword()).trim();
                String confirmPin = new String(confirmPinField.getPassword()).trim();

                if (name.isEmpty() || initialDepositStr.isEmpty() || pin.isEmpty() || confirmPin.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                    return;
                }

                if (!pin.equals(confirmPin)) {
                    JOptionPane.showMessageDialog(frame, "PINs do not match!");
                    return;
                }

                if (pin.length() < 4) {
                    JOptionPane.showMessageDialog(frame, "PIN must be at least 4 characters long.");
                    return;
                }

                double initialDeposit = Double.parseDouble(initialDepositStr);
                if (initialDeposit < 0) {
                    JOptionPane.showMessageDialog(frame, "Initial deposit cannot be negative.");
                    return;
                }

                String accountNumber = atmSystem.createAccount(name, pin, initialDeposit);
                if (accountNumber != null) {
                    String message = "Account created successfully!\n\n" +
                                   "Your account details:\n" +
                                   "Account Number: " + accountNumber + "\n" +
                                   "Name: " + name + "\n" +
                                   "Initial Deposit: $" + String.format("%.2f", initialDeposit) + "\n\n" +
                                   "Please remember your account number and PIN.";
                    JOptionPane.showMessageDialog(frame, message);
                    showLoginScreen();
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to create account!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid initial deposit amount!");
            }
        });

        backButton.addActionListener(e -> showLoginScreen());

        createPanel.add(formPanel);
        return createPanel;
    }

    private JButton createStyledButton(String text, int fontSize, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void addLabelAndField(JPanel panel, String labelText, JComponent field, int row, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        field.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void handleOperation(String operation) {
        switch (operation) {
            case "Check Balance":
                double balance = atmSystem.getBalance(currentAccount);
                JOptionPane.showMessageDialog(frame, "Your current balance is: $" + balance);
                break;
            case "Withdraw Money":
                handleWithdraw();
                break;
            case "Deposit Money":
                handleDeposit();
                break;
            case "Transfer Money":
                handleTransfer();
                break;
            case "Change PIN":
                handleChangePin();
                break;
            case "View History":
                String history = atmSystem.getTransactionHistory(currentAccount);
                JOptionPane.showMessageDialog(frame, "Transaction History:\n" + history);
                break;
            case "Print Receipt":
                handlePrintReceipt();
                break;
            case "Logout":
                currentAccount = null;
                showLoginScreen();
                break;
        }
    }

    private void handleWithdraw() {
        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
        try {
            double amount = Double.parseDouble(amountStr);
            if (atmSystem.withdraw(currentAccount, amount)) {
                JOptionPane.showMessageDialog(frame, "Withdrawal successful!");
            } else {
                JOptionPane.showMessageDialog(frame, "Insufficient funds!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid amount!");
        }
    }

    private void handleDeposit() {
        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
        try {
            double amount = Double.parseDouble(amountStr);
            atmSystem.deposit(currentAccount, amount);
            JOptionPane.showMessageDialog(frame, "Deposit successful!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid amount!");
        }
    }

    private void handleTransfer() {
        String targetAccount = JOptionPane.showInputDialog(frame, "Enter target account number:");
        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to transfer:");
        try {
            double amount = Double.parseDouble(amountStr);
            if (atmSystem.transfer(currentAccount, targetAccount, amount)) {
                JOptionPane.showMessageDialog(frame, "Transfer successful!");
            } else {
                JOptionPane.showMessageDialog(frame, "Transfer failed!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid amount!");
        }
    }

    private void handleChangePin() {
        String newPin = JOptionPane.showInputDialog(frame, "Enter new PIN:");
        if (atmSystem.changePin(currentAccount, newPin)) {
            JOptionPane.showMessageDialog(frame, "PIN changed successfully!");
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to change PIN!");
        }
    }

    private void handlePrintReceipt() {
        String receipt = atmSystem.generateReceipt(currentAccount);
        JOptionPane.showMessageDialog(frame, "Receipt:\n" + receipt);
    }

    private void showLoginScreen() {
        cardLayout.show(mainPanel, "LOGIN");
        accountField.setText("");
        pinField.setText("");
    }

    private void showMainMenu() {
        cardLayout.show(mainPanel, "MAIN_MENU");
    }

    private void showAccountCreationScreen() {
        cardLayout.show(mainPanel, "CREATE_ACCOUNT");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new ATMGUI();
            }
        });
    }
}
