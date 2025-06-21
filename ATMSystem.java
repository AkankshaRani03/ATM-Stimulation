import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ATMSystem {
    private HashMap<String, Account> accounts;
    private HashMap<String, List<Transaction>> transactionHistory;
    private Random random;

    public ATMSystem() {
        accounts = new HashMap<>();
        transactionHistory = new HashMap<>();
        random = new Random();
        // Initialize with some test accounts
        accounts.put("12345", new Account("12345", "1234", 1000.0, "John Doe"));
        accounts.put("67890", new Account("67890", "5678", 2000.0, "Jane Smith"));
    }

    public boolean login(String accountNumber, String pin) {
        if (accountNumber == null || pin == null || accountNumber.isEmpty() || pin.isEmpty()) {
            return false;
        }
        
        Account account = accounts.get(accountNumber);
        return account != null && account.getPin() != null && account.getPin().equals(pin);
    }

    public double getBalance(String accountNumber) {
        Account account = accounts.get(accountNumber);
        return account != null ? account.getBalance() : 0.0;
    }

    public boolean withdraw(String accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        if (account != null && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            addTransaction(accountNumber, "WITHDRAW", amount);
            return true;
        }
        return false;
    }

    public void deposit(String accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        if (account != null) {
            account.setBalance(account.getBalance() + amount);
            addTransaction(accountNumber, "DEPOSIT", amount);
        }
    }

    public boolean transfer(String fromAccount, String toAccount, double amount) {
        Account source = accounts.get(fromAccount);
        Account target = accounts.get(toAccount);
        
        if (source != null && target != null && source.getBalance() >= amount) {
            source.setBalance(source.getBalance() - amount);
            target.setBalance(target.getBalance() + amount);
            addTransaction(fromAccount, "TRANSFER_OUT", amount);
            addTransaction(toAccount, "TRANSFER_IN", amount);
            return true;
        }
        return false;
    }

    public boolean changePin(String accountNumber, String newPin) {
        Account account = accounts.get(accountNumber);
        if (account != null) {
            account.setPin(newPin);
            return true;
        }
        return false;
    }

    public String getTransactionHistory(String accountNumber) {
        List<Transaction> transactions = transactionHistory.get(accountNumber);
        if (transactions == null || transactions.isEmpty()) {
            return "No transactions found.";
        }

        StringBuilder history = new StringBuilder();
        for (Transaction t : transactions) {
            history.append(t.toString()).append("\n");
        }
        return history.toString();
    }

    public String createAccount(String name, String pin, double initialDeposit) {
        if (name == null || pin == null || name.isEmpty() || pin.isEmpty() || initialDeposit < 0) {
            return null;
        }

        String accountNumber = generateAccountNumber();
        if (accounts.containsKey(accountNumber)) {
            return null;
        }

        Account newAccount = new Account(accountNumber, pin, initialDeposit, name);
        accounts.put(accountNumber, newAccount);
        addTransaction(accountNumber, "INITIAL_DEPOSIT", initialDeposit);
        return accountNumber;
    }

    public String generateReceipt(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            return "Account not found.";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder receipt = new StringBuilder();
        receipt.append("=== ATM RECEIPT ===\n");
        receipt.append("Date: ").append(dateFormat.format(new Date())).append("\n");
        receipt.append("Account: ").append(accountNumber).append("\n");
        receipt.append("Name: ").append(account.getName()).append("\n");
        receipt.append("Current Balance: $").append(String.format("%.2f", account.getBalance())).append("\n");
        receipt.append("Last 5 Transactions:\n");

        List<Transaction> transactions = transactionHistory.get(accountNumber);
        if (transactions != null && !transactions.isEmpty()) {
            int count = Math.min(5, transactions.size());
            for (int i = transactions.size() - 1; i >= transactions.size() - count; i--) {
                receipt.append(transactions.get(i).toString()).append("\n");
            }
        }

        receipt.append("===================");
        return receipt.toString();
    }

    private String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = String.format("%05d", random.nextInt(100000));
        } while (accounts.containsKey(accountNumber));
        return accountNumber;
    }

    private void addTransaction(String accountNumber, String type, double amount) {
        List<Transaction> transactions = transactionHistory.computeIfAbsent(accountNumber, k -> new ArrayList<>());
        transactions.add(new Transaction(type, amount));
    }

    private static class Account {
        private String accountNumber;
        private String pin;
        private double balance;
        private String name;

        public Account(String accountNumber, String pin, double balance, String name) {
            this.accountNumber = accountNumber;
            this.pin = pin;
            this.balance = balance;
            this.name = name;
        }

        public String getPin() {
            return pin;
        }

        public void setPin(String pin) {
            this.pin = pin;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public String getName() {
            return name;
        }
    }

    private static class Transaction {
        private String type;
        private double amount;
        private long timestamp;

        public Transaction(String type, double amount) {
            this.type = type;
            this.amount = amount;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            return String.format("%s: $%.2f at %s", type, amount, new java.util.Date(timestamp));
        }
    }
}

