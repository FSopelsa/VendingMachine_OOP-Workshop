package se.lexicon.machine;

import java.util.Optional;
import se.lexicon.model.Product;

// Value object describing the outcome of a purchase attempt.
public final class PurchaseResult {

    // The UI can switch on this status to decide which message to print.
    public enum Status {
        SUCCESS,
        PRODUCT_NOT_FOUND,
        INSUFFICIENT_BALANCE,
        OUT_OF_STOCK
    }

    private final Status status;
    private final Product product;
    private final int productId;
    private final int missingAmount;
    private final int changeReturned;
    private final int remainingBalance;

    // Private constructor forces callers to use the named factory methods below.
    private PurchaseResult(
            Status status,
            Product product,
            int productId,
            int missingAmount,
            int changeReturned,
            int remainingBalance
    ) {
        this.status = status;
        this.product = product;
        this.productId = productId;
        this.missingAmount = missingAmount;
        this.changeReturned = changeReturned;
        this.remainingBalance = remainingBalance;
    }

    // Successful purchase: product was dispensed and leftover balance was returned as change.
    public static PurchaseResult success(Product product, int changeReturned) {
        return new PurchaseResult(Status.SUCCESS, product, product.getId(), 0, changeReturned, 0);
    }

    // Failure: selected id does not match any stocked product.
    public static PurchaseResult productNotFound(int productId, int remainingBalance) {
        return new PurchaseResult(Status.PRODUCT_NOT_FOUND, null, productId, 0, 0, remainingBalance);
    }

    // Failure: balance remains untouched so the customer can insert more coins.
    public static PurchaseResult insufficientBalance(Product product, int missingAmount, int remainingBalance) {
        return new PurchaseResult(Status.INSUFFICIENT_BALANCE, product, product.getId(), missingAmount, 0, remainingBalance);
    }

    // Failure: balance remains untouched because nothing was dispensed.
    public static PurchaseResult outOfStock(Product product, int remainingBalance) {
        return new PurchaseResult(Status.OUT_OF_STOCK, product, product.getId(), 0, 0, remainingBalance);
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public Status getStatus() {
        return status;
    }

    public Optional<Product> getProduct() {
        return Optional.ofNullable(product);
    }

    public int getProductId() {
        return productId;
    }

    public int getMissingAmount() {
        return missingAmount;
    }

    public int getChangeReturned() {
        return changeReturned;
    }

    public int getRemainingBalance() {
        return remainingBalance;
    }
}
