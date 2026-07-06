package se.lexicon.model;

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

    public final void decreaseQuantity() {
        if (isOutOfStock()) {
            throw new IllegalStateException(name + " is out of stock.");
        }
        quantity--;
    }

    public abstract String getType();

    protected abstract String getSpecificDetail();

    public final String getTypeDetails() {
        return getType() + ", " + getSpecificDetail();
    }

    public final String describe() {
        return name + " (" + getTypeDetails() + ")";
    }

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
