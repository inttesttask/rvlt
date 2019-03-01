package accounts;

public class Account {
    private Integer id;
    private long balance;

    public Account() {//for json (de)serialization
    }

    public Account(int id, long balance) {
        this.id = id;
        this.balance = balance;
    }

    public void deposit(long amount) {
        balance += amount;
    }

    /**
     * decrease balance if possible
     *
     * @param amount money to withdraw
     * @return true is success, false if insufficient funds
     */
    public boolean withdraw(long amount) {
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }

    public int getId() {
        return id;
    }

    public long getBalance() {
        return balance;
    }
}
