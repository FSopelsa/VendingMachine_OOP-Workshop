package se.lexicon.payment;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Immutable representation of returned change, stored as coin value -> coin count.
public final class Change {

    private final Map<Integer, Integer> coins;

    public Change(Map<Integer, Integer> coins) {
        // Sort coins for stable display regardless of the input map implementation.
        List<Integer> coinValues = coins.keySet().stream()
                .sorted(Collections.reverseOrder())
                .toList();

        Map<Integer, Integer> sortedCoins = new LinkedHashMap<>();
        for (int coinValue : coinValues) {
            int count = coins.get(coinValue);
            if (count <= 0) {
                throw new IllegalArgumentException("coin count must be greater than 0.");
            }
            sortedCoins.put(coinValue, count);
        }

        this.coins = Collections.unmodifiableMap(sortedCoins);
    }

    public Map<Integer, Integer> getCoins() {
        return coins;
    }

    public int getTotalAmount() {
        // Total is derived from the coin breakdown instead of stored separately.
        int total = 0;
        for (Map.Entry<Integer, Integer> entry : coins.entrySet()) {
            total += entry.getKey() * entry.getValue();
        }
        return total;
    }

    public boolean isEmpty() {
        return coins.isEmpty();
    }

    public String format() {
        if (isEmpty()) {
            return "No change";
        }

        // Example: "20 kr x 1, 10 kr x 1".
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : coins.entrySet()) {
            if (!builder.isEmpty()) {
                builder.append(", ");
            }
            builder.append(entry.getKey())
                    .append(" kr x ")
                    .append(entry.getValue());
        }

        return builder.toString();
    }
}
