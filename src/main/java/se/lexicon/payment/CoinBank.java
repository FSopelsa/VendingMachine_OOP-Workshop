package se.lexicon.payment;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

// Optional challenge class: owns coin validation, balance tracking, and change calculation.
public class CoinBank {

    private static final Set<Integer> ACCEPTED_COINS = Set.of(1, 2, 5, 10, 20, 50);
    // Largest-first order produces a simple, predictable change breakdown.
    private static final int[] CHANGE_ORDER = {50, 20, 10, 5, 2, 1};

    private int balance;

    // Adds only accepted Swedish coin values to the current balance.
    public boolean insertCoin(int coin) {
        if (!isAcceptedCoin(coin)) {
            return false;
        }

        balance += coin;
        return true;
    }

    public boolean isAcceptedCoin(int coin) {
        return ACCEPTED_COINS.contains(coin);
    }

    public int getBalance() {
        return balance;
    }

    // Used after a purchase to remove the product price from the inserted balance.
    public void deduct(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than 0.");
        }

        if (amount > balance) {
            throw new IllegalArgumentException("amount cannot be greater than current balance.");
        }

        balance -= amount;
    }

    // Converts the remaining balance into a Change object and empties the bank.
    public Change returnChange() {
        Map<Integer, Integer> coins = calculateCoins(balance);
        balance = 0;
        return new Change(coins);
    }

    // Greedy change calculation works here because every amount can be built from 1 kr coins.
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
