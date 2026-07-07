package se.lexicon.model;

// Shared base class for all products. Abstract means a plain, undescribed product cannot be created.
public abstract class Product {

    private final int id;
    private final String name;
    private final int price;
    private int quantity;

    protected Product(int id, String name, int price, int quantity) {
        this.id = requirePositive(id, "id");
        this.name = requireText(name, "name");
        this.price = requirePositive(price, "price");
        this.quantity = requireZeroOrPositive(quantity, "quantity");
    }

    public final int getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final int getPrice() {
        return price;
    }

    public final int getQuantity() {
        return quantity;
    }

    public final boolean isOutOfStock() {
        return quantity == 0;
    }

    // Stock can only be reduced through this method, so the quantity cannot become negative.
    public final void decreaseQuantity() {
        if (isOutOfStock()) {
            throw new IllegalStateException(name + " is out of stock.");
        }
        quantity--;
    }

    // Implemented by each subtype, for example "Snack" or "Beverage".
    public abstract String getType();

    // Subtype-specific display detail, such as weight, volume, or origin.
    protected abstract String getSpecificDetail();

    public final String getTypeDetails() {
        return getType() + ", " + getSpecificDetail();
    }

    // Polymorphic product description used by the UI and purchase confirmation.
    public final String describe() {
        return name + " (" + getTypeDetails() + ")";
    }

    // Constructor validation helpers keep invalid product state out of the model.
    protected static int requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than 0.");
        }
        return value;
    }

    protected static int requireZeroOrPositive(int value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " must be 0 or greater.");
        }
        return value;
    }

    protected static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank.");
        }
        return value.trim();
    }
}
