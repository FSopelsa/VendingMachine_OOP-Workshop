package se.lexicon.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Tests the optional payment component separately from the vending machine.
class CoinBankTest {

    private CoinBank coinBank;

    @BeforeEach
    void setUp() {
        // New bank per test keeps balances independent.
        coinBank = new CoinBank();
    }

    @Test
    void insertCoin_shouldIncreaseBalance_whenCoinIsAccepted() {
        boolean accepted = coinBank.insertCoin(20);

        assertTrue(accepted);
        assertEquals(20, coinBank.getBalance());
    }

    @Test
    void insertCoin_shouldRejectCoinAndKeepBalance_whenCoinIsNotAccepted() {
        boolean accepted = coinBank.insertCoin(7);

        assertFalse(accepted);
        assertEquals(0, coinBank.getBalance());
    }

    @Test
    void isAcceptedCoin_shouldReturnTrueOnlyForSwedishCoinValues() {
        assertTrue(coinBank.isAcceptedCoin(1));
        assertTrue(coinBank.isAcceptedCoin(2));
        assertTrue(coinBank.isAcceptedCoin(5));
        assertTrue(coinBank.isAcceptedCoin(10));
        assertTrue(coinBank.isAcceptedCoin(20));
        assertTrue(coinBank.isAcceptedCoin(50));

        assertFalse(coinBank.isAcceptedCoin(0));
        assertFalse(coinBank.isAcceptedCoin(7));
    }

    @Test
    void deduct_shouldReduceBalance_whenAmountIsAvailable() {
        coinBank.insertCoin(20);
        coinBank.insertCoin(10);

        coinBank.deduct(15);

        assertEquals(15, coinBank.getBalance());
    }

    @Test
    void deduct_shouldThrowException_whenAmountIsGreaterThanBalance() {
        coinBank.insertCoin(10);

        assertThrows(IllegalArgumentException.class, () -> coinBank.deduct(15));
        assertEquals(10, coinBank.getBalance());
    }

    @Test
    void deduct_shouldThrowException_whenAmountIsZeroOrNegative() {
        assertThrows(IllegalArgumentException.class, () -> coinBank.deduct(0));
        assertThrows(IllegalArgumentException.class, () -> coinBank.deduct(-1));
    }

    @Test
    void returnChange_shouldReturnCoinBreakdownAndResetBalance() {
        coinBank.insertCoin(50);
        coinBank.insertCoin(20);
        coinBank.insertCoin(10);
        coinBank.deduct(43);

        Change change = coinBank.returnChange();

        // 80 - 43 = 37, represented with the largest practical Swedish coins first.
        assertEquals(37, change.getTotalAmount());
        assertEquals(Map.of(20, 1, 10, 1, 5, 1, 2, 1), change.getCoins());
        assertEquals(0, coinBank.getBalance());
    }

    @Test
    void returnChange_shouldReturnEmptyChange_whenBalanceIsZero() {
        Change change = coinBank.returnChange();

        assertTrue(change.isEmpty());
        assertEquals(0, change.getTotalAmount());
        assertEquals("No change", change.format());
    }

    @Test
    void format_shouldDescribeCoinBreakdown() {
        Change change = new Change(Map.of(20, 1, 10, 1, 5, 1, 2, 1));

        assertEquals("20 kr x 1, 10 kr x 1, 5 kr x 1, 2 kr x 1", change.format());
    }
}
