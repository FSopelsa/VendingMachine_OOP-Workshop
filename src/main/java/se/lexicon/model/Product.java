package se.lexicon.model;

/**
 * Shared base class for all vending machine products.
 * <p>
 * This abstract class enforces a consistent structure for product types (Snack, Beverage, Fruit).
 * All products have an immutable identity (id, name, price) and mutable quantity tracking.
 * Subclasses must implement {@link #getType()} and {@link #getSpecificDetail()} to provide
 * type-specific information.
 * </p>
 */
public abstract class Product {

    private final int id;
    private final String name;
    private final int price;
    private int quantity;

    /**
     * Constructs a product with the specified properties.
     *
     * @param id       the unique product identifier (must be positive)
     * @param name     the product name (must not be blank)
     * @param price    the product price in SEK (must be positive)
     * @param quantity the initial stock quantity (must be zero or positive)
     * @throws IllegalArgumentException if any required field is invalid
     */
    protected Product(int id, String name, int price, int quantity) {
        this.id = requirePositive(id, "id");
        this.name = requireText(name, "name");
        this.price = requirePositive(price, "price");
        this.quantity = requireZeroOrPositive(quantity, "quantity");
    }

    /**
     * Gets the product's unique identifier.
     *
     * @return the product ID
     */
    public final int getId() {
        return id;
    }

    /**
     * Gets the product's name.
     *
     * @return the product name
     */
    public final String getName() {
        return name;
    }

    /**
     * Gets the product's price.
     *
     * @return the price in SEK
     */
    public final int getPrice() {
        return price;
    }

    /**
     * Gets the current stock quantity.
     *
     * @return the quantity in stock
     */
    public final int getQuantity() {
        return quantity;
    }

    /**
     * Checks if the product is out of stock.
     *
     * @return true if quantity is zero, false otherwise
     */
    public final boolean isOutOfStock() {
        return quantity == 0;
    }

    /**
     * Decreases the stock quantity by one.
     * <p>
     * This is the only way to reduce quantity, ensuring it never becomes negative.
     * </p>
     *
     * @throws IllegalStateException if the product is out of stock
     */
    public final void decreaseQuantity() {
        if (isOutOfStock()) {
            throw new IllegalStateException(name + " is out of stock.");
        }
        quantity--;
    }

    /**
     * Gets the product type name (e.g., "Snack", "Beverage", "Fruit").
     * <p>
     * Implemented by each subclass to identify its category.
     * </p>
     *
     * @return the product type
     */
    public abstract String getType();

    /**
     * Gets the subtype-specific detail (e.g., weight for Snack, volume for Beverage, origin for Fruit).
     *
     * @return the specific detail as a formatted string
     */
    protected abstract String getSpecificDetail();

    /**
     * Gets a complete type description combining type and specific detail.
     *
     * @return the type details (e.g., "Snack, 130g")
     */
    public final String getTypeDetails() {
        return getType() + ", " + getSpecificDetail();
    }

    /**
     * Gets a polymorphic product description for display in the UI and purchase confirmations.
     *
     * @return a formatted product description
     */
    public final String describe() {
        return name + " (" + getTypeDetails() + ")";
    }

    /**
     * Validates that a value is positive.
     *
     * @param value     the value to validate
     * @param fieldName the name of the field (for error messages)
     * @return the validated value
     * @throws IllegalArgumentException if value is not positive
     */
    protected static int requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than 0.");
        }
        return value;
    }

    /**
     * Validates that a value is zero or positive.
     *
     * @param value     the value to validate
     * @param fieldName the name of the field (for error messages)
     * @return the validated value
     * @throws IllegalArgumentException if value is negative
     */
    protected static int requireZeroOrPositive(int value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " must be 0 or greater.");
        }
        return value;
    }

    /**
     * Validates that a string is not null and not blank.
     *
     * @param value     the value to validate
     * @param fieldName the name of the field (for error messages)
     * @return the trimmed, validated value
     * @throws IllegalArgumentException if value is null or blank
     */
    protected static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank.");
        }
        return value.trim();
    }
}
