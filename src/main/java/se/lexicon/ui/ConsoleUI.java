package se.lexicon.ui;

import java.util.Scanner;
import se.lexicon.machine.PurchaseResult;
import se.lexicon.machine.VendingMachine;
import se.lexicon.model.Product;
import se.lexicon.payment.Change;

/**
 * Handles user input and printed messages for the vending machine console interface.
 * <p>
 * The console UI is responsible for:
 * <ul>
 *     <li>Displaying products with price and stock information</li>
 *     <li>Accepting user commands (insert coin, select product, return change, exit)</li>
 *     <li>Translating business results into human-readable messages</li>
 *     <li>Maintaining a consistent interaction loop until the user chooses to exit</li>
 * </ul>
 * All business rules and state remain in the VendingMachine implementation.
 * </p>
 */
public class ConsoleUI {

    private final VendingMachine machine;
    private final Scanner scanner;

    /**
     * Constructs a console UI with the specified machine and input source.
     *
     * @param machine the vending machine to interact with
     * @param scanner the input source (typically System.in or a mock for testing)
     */
    public ConsoleUI(VendingMachine machine, Scanner scanner) {
        this.machine = machine;
        this.scanner = scanner;
    }

    /**
     * Starts the main interactive loop until the user chooses to exit.
     * <p>
     * Returns any leftover balance to the user before exiting.
     * </p>
     */
    public void start() {
        System.out.println("Welcome to Lexicon Vending Machine");
        System.out.println();

        boolean running = true;
        while (running) {
            displayProducts();
            displayBalance();
            printMenu();

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    insertCoin();
                    break;
                case "2":
                    purchaseProduct();
                    break;
                case "3":
                    returnChange();
                    break;
                case "4":
                    displayProducts();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Unknown option.");
                    break;
            }
        }

        printChange(machine.returnChange());
        System.out.println("Goodbye.");
    }

    /**
     * Displays all available products with price, type, and stock information.
     */
    private void displayProducts() {
        System.out.println("------------------------------------");
        for (Product product : machine.getProducts()) {
            System.out.printf(
                    "[%d] %-12s - %d kr  (%s)  Stock: %d%n",
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getTypeDetails(),
                    product.getQuantity()
            );
        }
        System.out.println("------------------------------------");
    }

    /**
     * Displays the current customer balance.
     */
    private void displayBalance() {
        System.out.println("Balance: " + machine.getBalance() + " kr");
    }

    /**
     * Displays the main menu options.
     */
    private void printMenu() {
        System.out.println("1. Insert coin");
        System.out.println("2. Select product");
        System.out.println("3. Return change");
        System.out.println("4. Show products");
        System.out.println("0. Exit");
        System.out.print("> Choose action: ");
    }

    /**
     * Prompts the user to insert a coin and adds it to their balance if valid.
     */
    private void insertCoin() {
        int coin = readInt("> Insert coin: ");
        boolean accepted = machine.insertCoin(coin);

        if (!accepted) {
            System.out.println("Invalid coin. Only 1, 2, 5, 10, 20, 50 kr accepted.");
        }

        displayBalance();
    }

    /**
     * Prompts the user to select a product and processes the purchase attempt.
     */
    private void purchaseProduct() {
        int productId = readInt("> Select product: ");
        PurchaseResult result = machine.purchaseProduct(productId);

        switch (result.getStatus()) {
            case SUCCESS:
                Product product = result.getProduct().orElseThrow();
                System.out.println("Dispensing: " + product.describe());
                printChange(result.getChangeReturned());
                break;
            case PRODUCT_NOT_FOUND:
                System.out.println("No product found with id " + result.getProductId() + ".");
                break;
            case INSUFFICIENT_BALANCE:
                result.getProduct().ifPresent(productWithInsufficientBalance ->
                        System.out.println(
                                "Insufficient balance. "
                                        + productWithInsufficientBalance.getName()
                                        + " costs "
                                        + productWithInsufficientBalance.getPrice()
                                        + " kr. Missing "
                                        + result.getMissingAmount()
                                        + " kr."
                        )
                );
                break;
            case OUT_OF_STOCK:
                result.getProduct().ifPresent(outOfStockProduct ->
                        System.out.println(outOfStockProduct.getName() + " is out of stock.")
                );
                break;
            default:
                throw new IllegalStateException("Unexpected purchase status: " + result.getStatus());
        }

        displayBalance();
    }

    /**
     * Prompts the user to return their current balance as change.
     */
    private void returnChange() {
        Change change = machine.returnChange();

        if (change.isEmpty()) {
            System.out.println("No balance to return.");
        } else {
            printChange(change);
        }

        displayBalance();
    }

    /**
     * Prints a formatted representation of the change.
     *
     * @param change the change to display
     */
    private void printChange(Change change) {
        if (!change.isEmpty()) {
            System.out.println("Change returned: " + change.getTotalAmount() + " kr");
            System.out.println("Coins: " + change.format());
        }
    }

    /**
     * Repeatedly prompts the user until a valid integer is entered.
     *
     * @param prompt the prompt to display
     * @return the parsed integer value
     */
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a whole number.");
            }
        }
    }
}
