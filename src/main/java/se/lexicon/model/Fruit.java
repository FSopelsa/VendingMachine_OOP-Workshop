package se.lexicon.model;

// Fruit product with origin country as its category-specific detail.
public final class Fruit extends Product {

    private final String origin;

    public Fruit(int id, String name, int price, int quantity, String origin) {
        super(id, name, price, quantity);
        this.origin = requireText(origin, "origin");
    }

    public String getOrigin() {
        return origin;
    }

    @Override
    public String getType() {
        return "Fruit";
    }

    @Override
    protected String getSpecificDetail() {
        // Used by Product.describe() without any instanceof checks.
        return origin;
    }
}
