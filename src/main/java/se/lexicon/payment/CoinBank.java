package se.lexicon.payment;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Manages payment logic for the vending machine, including coin acceptance and change calculation.
 * <p>
 * This class validates coin inputs against accepted Swedish coin denominations (1, 2, 5, 10, 20, 50 kr),
 * maintains a running balance, and calculates optimal change using a greedy algorithm.
 * </p>
 */
public class CoinBank {

    private static final Set<Integer> ACCEPTED_COINS = Set.of(1, 2, 5, 10, 20, 50);
    private static final int[] CHANGE_ORDER = {50, 20, 10, 5, 2, 1};

    private int balance;

    /**
     * Inserts a coin into the bank if it is an accepted denomination.
     *
     * @param coin the coin value in SEK
     * @return true if the coin was accepted and added to the balance, false otherwise
     */
    public boolean insertCoin(int coin) {
        if (!isAcceptedCoin(coin)) {
            return false;
        }

        balance += coin;
        return true;
    }

    /**
     * Checks whether a coin value is accepted by the machine.
     *
     * @param coin the coin value to validate
     * @return true if the coin is an accepted denomination, false otherwise
     */
    public boolean isAcceptedCoin(int coin) {
        return ACCEPTED_COINS.contains(coin);
    }

    /**
     * Gets the current balance in the bank.
     *
     * @return the balance in SEK
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Deducts an amount from the balance (typically the product price after a successful purchase).
     *
     * @param amount the amount to deduct (must be positive and not exceed the current balance)
     * @throws IllegalArgumentException if amount is invalid or exceeds balance
     */
    public void deduct(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than 0.");
        }

        if (amount > balance) {
            throw new IllegalArgumentException("amount cannot be greater than current balance.");
        }

        balance -= amount;
    }

    /**
     * Returns the remaining balance as a Change object and resets the balance to zero.
     *
     * @return a Change object representing the returned balance
     */
    public Change returnChange() {
        Map<Integer, Integer> coins = calculateCoins(balance);
        balance = 0;
        return new Change(coins);
    }

    /**
     * Calculates the optimal coin breakdown for a given amount using the greedy algorithm.
     * <p>
     * This works correctly for Swedish coins because every amount can be built from 1 kr coins.
     * Coins are allocated in descending order (50, 20, 10, 5, 2, 1) for predictable change.
     * </p>
     *
     * @param amount the amount to break down
     * @return a map of coin values to their counts
     */
    private Map<Integer, Integer> calculateCoins(int amount) {
        Map<Integer, Integer> coins = new LinkedHashMap<>();
        int remainingAmount = amount;

        for (int coin : CHANGE_ORDER) {
            int coinCount = remainingAmount / coin;
            if (coinCount > 0) {
                coins.put(coin, coinCount);
                remainingAmount %= coin;
            }
        }

        return coins;
    }
}
