package se.lexicon.machine;

import java.util.List;
import java.util.Optional;
import se.lexicon.model.Product;
import se.lexicon.payment.Change;

/**
 * Public contract for vending machine business logic.
 * <p>
 * This interface defines all operations supported by a vending machine, including coin insertion,
 * product purchase, change return, and inventory management.
 * </p>
 */
public interface VendingMachine {

    /**
     * Inserts a coin into the machine.
     *
     * @param coin the coin value in SEK
     * @return false when the coin value is not accepted, true otherwise
     */
    boolean insertCoin(int coin);

    /**
     * Attempts to purchase a product by its ID.
     *
     * @param productId the ID of the product to purchase
     * @return a PurchaseResult describing the outcome of the attempt
     */
    PurchaseResult purchaseProduct(int productId);

    /**
     * Returns the current balance as coin breakdown and resets balance to zero.
     *
     * @return a Change object representing the returned balance
     */
    Change returnChange();

    /**
     * Gets the current balance without resetting it.
     *
     * @return the balance in SEK
     */
    int getBalance();

    /**
     * Gets all products currently in inventory.
     *
     * @return a list of available products
     */
    List<Product> getProducts();

    /**
     * Finds a product by its ID.
     *
     * @param productId the product ID to search for
     * @return an Optional containing the product if found, empty otherwise
     */
    Optional<Product> findProductById(int productId);

    /**
     * Adds a product to the machine's inventory.
     *
     * @param product the product to add
     */
    void addProduct(Product product);
}
