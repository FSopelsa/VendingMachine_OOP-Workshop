package se.lexicon.model;

/**
 * A beverage product with volume in milliliters as its category-specific detail.
 */
public final class Beverage extends Product {

    private final int volumeMl;

    /**
     * Constructs a beverage product.
     *
     * @param id       the unique product identifier (must be positive)
     * @param name     the beverage name (must not be blank)
     * @param price    the price in SEK (must be positive)
     * @param quantity the initial stock quantity (must be zero or positive)
     * @param volumeMl the volume in milliliters (must be positive)
     * @throws IllegalArgumentException if any field is invalid
     */
    public Beverage(int id, String name, int price, int quantity, int volumeMl) {
        super(id, name, price, quantity);
        this.volumeMl = requirePositive(volumeMl, "volumeMl");
    }

    /**
     * Gets the beverage's volume in milliliters.
     *
     * @return the volume
     */
    public int getVolumeMl() {
        return volumeMl;
    }

    @Override
    public String getType() {
        return "Beverage";
    }

    @Override
    protected String getSpecificDetail() {
        return volumeMl + "ml";
    }
}
