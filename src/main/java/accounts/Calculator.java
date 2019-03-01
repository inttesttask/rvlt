package accounts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Calculator {
    private static Logger log = LoggerFactory.getLogger(Calculator.class);

    private HashMap<Integer, Account> accounts = new HashMap<>();

    public static final Calculator INSTANCE = new Calculator();

    private Calculator() {
    }

    /**
     * Creates new account
     * @param id account id
     * @param balance starting balance
     * @return true if success, false if account already exists
     */
    public boolean addAccount(int id, long balance) {
        if (accounts.containsKey(id)) {
            return false;
        } else {
            accounts.put(id, new Account(id, balance));
            return true;
        }
    }

    public void transfer(int sourceId, int targetId, long amount) {
        if (!accounts.containsKey(sourceId)) {
            throw new TransferException("sourceId is not exist");
        }
        if (!accounts.containsKey(targetId)) {
            throw new TransferException("targetId is not exist");
        }
        if (amount <= 0) {
            throw new TransferException("Illegal amount " + amount);
        }

        int lock1 = sourceId < targetId ? sourceId : targetId;
        int lock2 = sourceId < targetId ? targetId : sourceId;

        synchronized (accounts.get(lock1)) {
            synchronized (accounts.get(lock2)) {
                log.info("TRANSFER STARTED FROM " + sourceId + " TO " + targetId + ", AMOUNT " + amount);
                if (accounts.get(sourceId).withdraw(amount)) {
                    accounts.get(targetId).deposit(amount);
                    log.info("Transfer completed");
                }else{
                    log.warn("Insufficient funds");
                    throw new TransferException("Insufficient funds");
                }
            }
        }
    }

    public long getAccountBalance(int accountId) {
        if (!accounts.containsKey(accountId)) {
            throw new TransferException("Account with id " + accountId + " does not exist");
        }
        return accounts.get(accountId).getBalance();
    }
}
