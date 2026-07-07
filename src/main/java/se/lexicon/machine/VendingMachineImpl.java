package se.lexicon.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import se.lexicon.model.Product;
import se.lexicon.payment.Change;
import se.lexicon.payment.CoinBank;

// Default vending machine implementation containing the business rules from the workshop.
public class VendingMachineImpl implements VendingMachine {

    // LinkedHashMap keeps products in insertion order when they are displayed.
    private final Map<Integer, Product> products = new LinkedHashMap<>();
    private final CoinBank coinBank;

    public VendingMachineImpl() {
        this(new CoinBank());
    }

    public VendingMachineImpl(CoinBank coinBank) {
        this.coinBank = Objects.requireNonNull(coinBank, "coinBank must not be null.");
    }

    @Override
    public boolean insertCoin(int coin) {
        return coinBank.insertCoin(coin);
    }

    @Override
    public PurchaseResult purchaseProduct(int productId) {
        Product product = products.get(productId);

        // Each failure path returns early and leaves balance/stock unchanged.
        if (product == null) {
            return PurchaseResult.productNotFound(productId, coinBank.getBalance());
        }

        if (product.isOutOfStock()) {
            return PurchaseResult.outOfStock(product, coinBank.getBalance());
        }

        if (coinBank.getBalance() < product.getPrice()) {
            int missingAmount = product.getPrice() - coinBank.getBalance();
            return PurchaseResult.insufficientBalance(product, missingAmount, coinBank.getBalance());
        }

        // Only a successful purchase mutates both money and stock.
        coinBank.deduct(product.getPrice());
        product.decreaseQuantity();
        Change change = returnChange();

        return PurchaseResult.success(product, change);
    }

    @Override
    public Change returnChange() {
        return coinBank.returnChange();
    }

    @Override
    public int getBalance() {
        return coinBank.getBalance();
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
