package se.lexicon.machine;

import java.util.List;
import java.util.Optional;
import se.lexicon.model.Product;

public interface VendingMachine {

    boolean insertCoin(int coin);

    PurchaseResult purchaseProduct(int productId);

    int returnChange();

    int getBalance();

    List<Product> getProducts();

    Optional<Product> findProductById(int productId);

    void addProduct(Product product);
}
