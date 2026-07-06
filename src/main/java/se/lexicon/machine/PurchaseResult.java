package se.lexicon.machine;

import java.util.Optional;
import se.lexicon.model.Product;

public final class PurchaseResult {

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

    public static PurchaseResult success(Product product, int changeReturned) {
        return new PurchaseResult(Status.SUCCESS, product, product.getId(), 0, changeReturned, 0);
    }

    public static PurchaseResult productNotFound(int productId, int remainingBalance) {
        return new PurchaseResult(Status.PRODUCT_NOT_FOUND, null, productId, 0, 0, remainingBalance);
    }

    public static PurchaseResult insufficientBalance(Product product, int missingAmount, int remainingBalance) {
        return new PurchaseResult(Status.INSUFFICIENT_BALANCE, product, product.getId(), missingAmount, 0, remainingBalance);
    }

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
