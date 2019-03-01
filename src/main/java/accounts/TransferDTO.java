package accounts;

/**
 * Класс данных для перевода
 */
public class TransferDTO {
    private int sourceId;//источник денег
    private int targetId;//получатель денег
    private long amount;//количество денег для перевода

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
