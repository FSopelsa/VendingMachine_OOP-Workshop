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

/**
 * Default implementation of the vending machine interface.
 * <p>
 * This class encapsulates all business rules from the workshop, including:
 * <ul>
 *     <li>Coin insertion and balance tracking via CoinBank</li>
 *     <li>Product purchase logic with comprehensive validation</li>
 *     <li>Inventory management with duplicate prevention</li>
 *     <li>Immutable product list exposure to prevent external modification</li>
 * </ul>
 * </p>
 */
public class VendingMachineImpl implements VendingMachine {

    private final Map<Integer, Product> products = new LinkedHashMap<>();
    private final CoinBank coinBank;

    /**
     * Constructs a vending machine with a default CoinBank.
     */
    public VendingMachineImpl() {
        this(new CoinBank());
    }

    /**
     * Constructs a vending machine with a specified CoinBank (useful for testing).
     *
     * @param coinBank the payment component
     * @throws NullPointerException if coinBank is null
     */
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
        return Collections.unmodifiableList(new ArrayList<>(products.values()));
    }

    @Override
    public Optional<Product> findProductById(int productId) {
        return Optional.ofNullable(products.get(productId));
    }

    @Override
    public void addProduct(Product product) {
        Objects.requireNonNull(product, "product must not be null.");

        if (products.containsKey(product.getId())) {
            throw new IllegalArgumentException("Product id already exists: " + product.getId());
        }

        products.put(product.getId(), product);
    }
}
