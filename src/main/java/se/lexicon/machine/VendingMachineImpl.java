package se.lexicon.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import se.lexicon.model.Product;

// Default vending machine implementation containing the business rules from the workshop.
public class VendingMachineImpl implements VendingMachine {

    private static final Set<Integer> ACCEPTED_COINS = Set.of(1, 2, 5, 10, 20, 50);

    // LinkedHashMap keeps products in insertion order when they are displayed.
    private final Map<Integer, Product> products = new LinkedHashMap<>();
    private int balance;

    @Override
    public boolean insertCoin(int coin) {
        if (!ACCEPTED_COINS.contains(coin)) {
            return false;
        }

        balance += coin;
        return true;
    }

    @Override
    public PurchaseResult purchaseProduct(int productId) {
        Product product = products.get(productId);

        // Each failure path returns early and leaves balance/stock unchanged.
        if (product == null) {
            return PurchaseResult.productNotFound(productId, balance);
        }

        if (product.isOutOfStock()) {
            return PurchaseResult.outOfStock(product, balance);
        }

        if (balance < product.getPrice()) {
            int missingAmount = product.getPrice() - balance;
            return PurchaseResult.insufficientBalance(product, missingAmount, balance);
        }

        // Only a successful purchase mutates both money and stock.
        balance -= product.getPrice();
        product.decreaseQuantity();
        int change = returnChange();

        return PurchaseResult.success(product, change);
    }

    @Override
    public int returnChange() {
        int change = balance;
        balance = 0;
        return change;
    }

    @Override
    public int getBalance() {
        return balance;
    }

    @Override
    public List<Product> getProducts() {
        // Return a copy so callers cannot modify the machine inventory map directly.
        return Collections.unmodifiableList(new ArrayList<>(products.values()));
    }

    @Override
    public Optional<Product> findProductById(int productId) {
        return Optional.ofNullable(products.get(productId));
    }

    @Override
    public void addProduct(Product product) {
        Objects.requireNonNull(product, "product must not be null.");

        // Product ids are the selection numbers, so duplicates would make purchases ambiguous.
        if (products.containsKey(product.getId())) {
            throw new IllegalArgumentException("Product id already exists: " + product.getId());
        }

        products.put(product.getId(), product);
    }
}
