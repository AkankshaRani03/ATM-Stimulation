import java.util.ArrayList;

public class UserAccount {
    private String accountNumber;
    private String pin;
    private double balance;
    private ArrayList<String> transactionHistory;

    public UserAccount(String accountNumber, String pin, double initialBalance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public boolean checkPin(String pin) {
        return this.pin.equals(pin);
    }

    public double checkBalance() {
        return this.balance;
    }

    public void deposit(double amount) {
        balance += amount;
        addTransaction("Deposited: " + amount);
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            addTransaction("Withdrew: " + amount);
            return true;
        }
        return false;
    }

    private void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public ArrayList<String> getTransactionHistory() {
        return transactionHistory;
    }
}
