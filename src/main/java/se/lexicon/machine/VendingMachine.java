package se.lexicon.machine;

import java.util.List;
import java.util.Optional;
import se.lexicon.model.Product;

// Public contract for the vending machine business logic.
public interface VendingMachine {

    // Returns false when the coin value is not accepted.
    boolean insertCoin(int coin);

    // Attempts a purchase and returns a structured result instead of printing directly.
    PurchaseResult purchaseProduct(int productId);

    // Returns the current balance and resets it to zero.
    int returnChange();

    int getBalance();

    List<Product> getProducts();

    // Optional avoids returning null when an id is unknown.
    Optional<Product> findProductById(int productId);

    void addProduct(Product product);
}
