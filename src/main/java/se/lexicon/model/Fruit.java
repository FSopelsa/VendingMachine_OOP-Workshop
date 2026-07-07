package se.lexicon.model;

/**
 * A fruit product with origin country as its category-specific detail.
 */
public final class Fruit extends Product {

    private final String origin;

    /**
     * Constructs a fruit product.
     *
     * @param id       the unique product identifier (must be positive)
     * @param name     the fruit name (must not be blank)
     * @param price    the price in SEK (must be positive)
     * @param quantity the initial stock quantity (must be zero or positive)
     * @param origin   the country of origin (must not be blank)
     * @throws IllegalArgumentException if any field is invalid
     */
    public Fruit(int id, String name, int price, int quantity, String origin) {
        super(id, name, price, quantity);
        this.origin = requireText(origin, "origin");
    }

    /**
     * Gets the fruit's origin country.
     *
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    @Override
    public String getType() {
        return "Fruit";
    }

    @Override
    protected String getSpecificDetail() {
        return origin;
    }
}
