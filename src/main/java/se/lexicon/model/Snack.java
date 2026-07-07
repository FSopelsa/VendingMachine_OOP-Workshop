package se.lexicon.model;

/**
 * A snack product with weight in grams as its category-specific detail.
 */
public final class Snack extends Product {

    private final int weightGrams;

    /**
     * Constructs a snack product.
     *
     * @param id           the unique product identifier (must be positive)
     * @param name         the snack name (must not be blank)
     * @param price        the price in SEK (must be positive)
     * @param quantity     the initial stock quantity (must be zero or positive)
     * @param weightGrams  the weight in grams (must be positive)
     * @throws IllegalArgumentException if any field is invalid
     */
    public Snack(int id, String name, int price, int quantity, int weightGrams) {
        super(id, name, price, quantity);
        this.weightGrams = requirePositive(weightGrams, "weightGrams");
    }

    /**
     * Gets the snack's weight in grams.
     *
     * @return the weight
     */
    public int getWeightGrams() {
        return weightGrams;
    }

    @Override
    public String getType() {
        return "Snack";
    }

    @Override
    protected String getSpecificDetail() {
        return weightGrams + "g";
    }
}
