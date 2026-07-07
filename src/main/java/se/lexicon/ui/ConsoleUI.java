package se.lexicon.ui;

import java.util.Scanner;
import se.lexicon.machine.PurchaseResult;
import se.lexicon.machine.VendingMachine;
import se.lexicon.model.Product;
import se.lexicon.payment.Change;

// Handles user input and printed messages. Business rules stay in VendingMachineImpl.
public class ConsoleUI {

    private final VendingMachine machine;
    private final Scanner scanner;

    public ConsoleUI(VendingMachine machine, Scanner scanner) {
        this.machine = machine;
        this.scanner = scanner;
    }

    public void start() {
        System.out.println("Welcome to Lexicon Vending Machine");
        System.out.println();

        // Main input loop runs until the user chooses exit.
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

        // Return any leftover balance when the user exits.
        printChange(machine.returnChange());
        System.out.println("Goodbye.");
    }

    private void displayProducts() {
        System.out.println("------------------------------------");
        for (Product product : machine.getProducts()) {
            // Product-specific details come from polymorphism in the Product hierarchy.
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

    private void displayBalance() {
        System.out.println("Balance: " + machine.getBalance() + " kr");
    }

    private void printMenu() {
        System.out.println("1. Insert coin");
        System.out.println("2. Select product");
        System.out.println("3. Return change");
        System.out.println("4. Show products");
        System.out.println("0. Exit");
        System.out.print("> Choose action: ");
    }

    private void insertCoin() {
        int coin = readInt("> Insert coin: ");
        boolean accepted = machine.insertCoin(coin);

        if (!accepted) {
            System.out.println("Invalid coin. Only 1, 2, 5, 10, 20, 50 kr accepted.");
        }

        displayBalance();
    }

    private void purchaseProduct() {
        int productId = readInt("> Select product: ");
        PurchaseResult result = machine.purchaseProduct(productId);

        // Translate the business result into console messages.
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

    private void returnChange() {
        Change change = machine.returnChange();

        if (change.isEmpty()) {
            System.out.println("No balance to return.");
        } else {
            printChange(change);
        }

        displayBalance();
    }

    private void printChange(Change change) {
        if (!change.isEmpty()) {
            System.out.println("Change returned: " + change.getTotalAmount() + " kr");
            System.out.println("Coins: " + change.format());
        }
    }

    private int readInt(String prompt) {
        // Keep asking until the user enters a whole number.
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
