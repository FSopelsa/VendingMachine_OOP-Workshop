package se.lexicon.payment;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * An immutable representation of change returned from the vending machine.
 * <p>
 * Change is stored as a map of coin value to coin count and is always sorted in descending order
 * for consistent display. Each coin count must be positive; zero-count entries are rejected.
 * </p>
 */
public final class Change {

    private final Map<Integer, Integer> coins;

    /**
     * Factory method to create an empty change object (no coins to return).
     *
     * @return an empty Change instance
     */
    public static Change empty() {
        return new Change(Map.of());
    }

    /**
     * Constructs a Change object with the specified coins.
     * <p>
     * Coins are automatically sorted in descending order for stable display.
     * </p>
     *
     * @param coins a map of coin value to count (all counts must be positive)
     * @throws IllegalArgumentException if any coin count is not positive
     */
    public Change(Map<Integer, Integer> coins) {
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

    /**
     * Gets the coin breakdown as an immutable map.
     *
     * @return the coins map
     */
    public Map<Integer, Integer> getCoins() {
        return coins;
    }

    /**
     * Calculates the total amount of change.
     *
     * @return the sum of all coin values times their counts
     */
    public int getTotalAmount() {
        int total = 0;
        for (Map.Entry<Integer, Integer> entry : coins.entrySet()) {
            total += entry.getKey() * entry.getValue();
        }
        return total;
    }

    /**
     * Checks if this Change object contains no coins.
     *
     * @return true if the coin map is empty, false otherwise
     */
    public boolean isEmpty() {
        return coins.isEmpty();
    }

    /**
     * Formats the coin breakdown for display in the UI.
     * <p>
     * Returns "No change" if empty, otherwise formats as "20 kr x 1, 10 kr x 1".
     * </p>
     *
     * @return a formatted string representation
     */
    public String format() {
        if (isEmpty()) {
            return "No change";
        }

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
