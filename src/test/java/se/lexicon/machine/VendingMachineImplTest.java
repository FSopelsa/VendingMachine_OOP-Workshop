package se.lexicon.machine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.lexicon.machine.PurchaseResult.Status;
import se.lexicon.model.Beverage;
import se.lexicon.model.Fruit;
import se.lexicon.model.Product;
import se.lexicon.model.Snack;

import static org.junit.jupiter.api.Assertions.*;

class VendingMachineImplTest {

    private VendingMachineImpl machine;

    @BeforeEach
    void setUp() {
        machine = new VendingMachineImpl();
    }

    @Test
    void insertCoin_shouldIncreaseBalance_whenCoinIsValid() {
        boolean accepted = machine.insertCoin(10);

        assertTrue(accepted);
        assertEquals(10, machine.getBalance());
    }

    @Test
    void insertCoin_shouldRejectInvalidCoin_andLeaveBalanceUnchanged() {
        boolean accepted = machine.insertCoin(7);

        assertFalse(accepted);
        assertEquals(0, machine.getBalance());
    }

    @Test
    void purchaseProduct_shouldReturnProductResetBalanceAndReduceQuantity_whenPurchaseSucceeds() {
        Product cola = new Beverage(1, "Cola", 20, 3, 330);
        machine.addProduct(cola);
        machine.insertCoin(20);

        PurchaseResult result = machine.purchaseProduct(1);

        assertEquals(Status.SUCCESS, result.getStatus());
        assertTrue(result.isSuccess());
        assertSame(cola, result.getProduct().orElseThrow());
        assertEquals(0, machine.getBalance());
        assertEquals(2, cola.getQuantity());
        assertEquals(0, result.getChangeReturned());
    }

    @Test
    void purchaseProduct_shouldFailAndKeepBalanceAndQuantity_whenBalanceIsInsufficient() {
        Product cola = new Beverage(1, "Cola", 20, 3, 330);
        machine.addProduct(cola);
        machine.insertCoin(10);

        PurchaseResult result = machine.purchaseProduct(1);

        assertEquals(Status.INSUFFICIENT_BALANCE, result.getStatus());
        assertFalse(result.isSuccess());
        assertSame(cola, result.getProduct().orElseThrow());
        assertEquals(10, result.getMissingAmount());
        assertEquals(10, machine.getBalance());
        assertEquals(3, cola.getQuantity());
    }

    @Test
    void purchaseProduct_shouldFailAndKeepBalanceAndQuantity_whenProductIsOutOfStock() {
        Product cola = new Beverage(1, "Cola", 20, 0, 330);
        machine.addProduct(cola);
        machine.insertCoin(20);

        PurchaseResult result = machine.purchaseProduct(1);

        assertEquals(Status.OUT_OF_STOCK, result.getStatus());
        assertFalse(result.isSuccess());
        assertSame(cola, result.getProduct().orElseThrow());
        assertEquals(20, machine.getBalance());
        assertEquals(0, cola.getQuantity());
    }

    @Test
    void returnChange_shouldReturnCurrentBalanceAndResetBalanceToZero() {
        machine.insertCoin(50);

        int change = machine.returnChange();

        assertEquals(50, change);
        assertEquals(0, machine.getBalance());
    }

    @Test
    void getProducts_shouldReturnAllAddedProducts() {
        machine.addProduct(new Snack(1, "Chips", 15, 5, 130));
        machine.addProduct(new Beverage(2, "Cola", 20, 3, 330));
        machine.addProduct(new Fruit(3, "Apple", 10, 8, "Sweden"));

        assertEquals(3, machine.getProducts().size());
    }

    @Test
    void purchaseProduct_shouldReturnProductNotFound_whenIdDoesNotExist() {
        machine.insertCoin(20);

        PurchaseResult result = machine.purchaseProduct(99);

        assertEquals(Status.PRODUCT_NOT_FOUND, result.getStatus());
        assertFalse(result.isSuccess());
        assertTrue(result.getProduct().isEmpty());
        assertEquals(99, result.getProductId());
        assertEquals(20, machine.getBalance());
    }

    @Test
    void purchaseProduct_shouldReturnChangeAutomatically_whenBalanceExceedsPrice() {
        Product chips = new Snack(1, "Chips", 15, 5, 130);
        machine.addProduct(chips);
        machine.insertCoin(20);

        PurchaseResult result = machine.purchaseProduct(1);

        assertEquals(Status.SUCCESS, result.getStatus());
        assertEquals(5, result.getChangeReturned());
        assertEquals(0, machine.getBalance());
        assertEquals(4, chips.getQuantity());
    }

    @Test
    void addProduct_shouldRejectDuplicateProductId() {
        machine.addProduct(new Snack(1, "Chips", 15, 5, 130));

        assertThrows(
                IllegalArgumentException.class,
                () -> machine.addProduct(new Fruit(1, "Apple", 10, 8, "Sweden"))
        );
    }
}
