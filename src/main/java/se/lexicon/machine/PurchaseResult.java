package se.lexicon.machine;

import java.util.Optional;
import se.lexicon.model.Product;
import se.lexicon.payment.Change;

/**
 * A value object describing the outcome of a purchase attempt.
 * <p>
 * PurchaseResult encodes all possible purchase outcomes (success, product not found, insufficient balance,
 * out of stock) and provides the necessary information for the UI to display appropriate messages and
 * confirm successful transactions.
 * </p>
 */
public final class PurchaseResult {

    /**
     * Enumeration of possible purchase outcomes.
     */
    public enum Status {
        /** Product was successfully dispensed and change was returned. */
        SUCCESS,
        /** The selected product ID was not found in inventory. */
        PRODUCT_NOT_FOUND,
        /** The inserted balance was insufficient for the product price. */
        INSUFFICIENT_BALANCE,
        /** The selected product is out of stock. */
        OUT_OF_STOCK
    }

    private final Status status;
    private final Product product;
    private final int productId;
    private final int missingAmount;
    private final Change changeReturned;
    private final int remainingBalance;

    /**
     * Private constructor to ensure instances are created via named factory methods.
     *
     * @param status           the purchase result status
     * @param product          the product involved (may be null for failure cases)
     * @param productId        the product ID (even if not found)
     * @param missingAmount    the amount needed if balance was insufficient
     * @param changeReturned   the change returned on success
     * @param remainingBalance the balance remaining after the transaction
     */
    private PurchaseResult(
            Status status,
            Product product,
            int productId,
            int missingAmount,
            Change changeReturned,
            int remainingBalance
    ) {
        this.status = status;
        this.product = product;
        this.productId = productId;
        this.missingAmount = missingAmount;
        this.changeReturned = changeReturned;
        this.remainingBalance = remainingBalance;
    }

    /**
     * Factory method for a successful purchase.
     *
     * @param product         the dispensed product
     * @param changeReturned  the leftover balance as change
     * @return a success PurchaseResult
     */
    public static PurchaseResult success(Product product, Change changeReturned) {
        return new PurchaseResult(Status.SUCCESS, product, product.getId(), 0, changeReturned, 0);
    }

    /**
     * Factory method for when a product ID does not match any stocked product.
     *
     * @param productId        the requested product ID
     * @param remainingBalance the unchanged customer balance
     * @return a product not found PurchaseResult
     */
    public static PurchaseResult productNotFound(int productId, int remainingBalance) {
        return new PurchaseResult(Status.PRODUCT_NOT_FOUND, null, productId, 0, Change.empty(), remainingBalance);
    }

    /**
     * Factory method for when the balance is insufficient for the product.
     *
     * @param product          the product that could not be purchased
     * @param missingAmount    the additional amount needed
     * @param remainingBalance the unchanged customer balance
     * @return an insufficient balance PurchaseResult
     */
    public static PurchaseResult insufficientBalance(Product product, int missingAmount, int remainingBalance) {
        return new PurchaseResult(Status.INSUFFICIENT_BALANCE, product, product.getId(), missingAmount, Change.empty(), remainingBalance);
    }

    /**
     * Factory method for when the product is out of stock.
     *
     * @param product          the out-of-stock product
     * @param remainingBalance the unchanged customer balance
     * @return an out of stock PurchaseResult
     */
    public static PurchaseResult outOfStock(Product product, int remainingBalance) {
        return new PurchaseResult(Status.OUT_OF_STOCK, product, product.getId(), 0, Change.empty(), remainingBalance);
    }

    /**
     * Checks if the purchase was successful.
     *
     * @return true if status is SUCCESS, false otherwise
     */
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    /**
     * Gets the purchase result status.
     *
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Gets the product involved in the purchase attempt (if any).
     *
     * @return an Optional containing the product, or empty if the product was not found
     */
    public Optional<Product> getProduct() {
        return Optional.ofNullable(product);
    }

    /**
     * Gets the product ID that was requested.
     *
     * @return the product ID
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Gets the amount that was missing (only relevant if status is INSUFFICIENT_BALANCE).
     *
     * @return the missing amount in SEK
     */
    public int getMissingAmount() {
        return missingAmount;
    }

    /**
     * Gets the change that was returned (only populated on successful purchase).
     *
     * @return the returned change
     */
    public Change getChangeReturned() {
        return changeReturned;
    }

    /**
     * Gets the customer's balance remaining after the transaction.
     *
     * @return the remaining balance in SEK
     */
    public int getRemainingBalance() {
        return remainingBalance;
    }
}
