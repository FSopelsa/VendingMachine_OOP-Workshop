package se.lexicon;

import java.util.Scanner;
import se.lexicon.machine.VendingMachine;
import se.lexicon.machine.VendingMachineImpl;
import se.lexicon.model.Beverage;
import se.lexicon.model.Fruit;
import se.lexicon.model.Snack;
import se.lexicon.ui.ConsoleUI;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        VendingMachine machine = new VendingMachineImpl();
        seedProducts(machine);

        ConsoleUI consoleUI = new ConsoleUI(machine, new Scanner(System.in));
        consoleUI.start();
    }

    private static void seedProducts(VendingMachine machine) {
        machine.addProduct(new Snack(1, "Chips", 15, 5, 130));
        machine.addProduct(new Beverage(2, "Cola", 20, 3, 330));
        machine.addProduct(new Fruit(3, "Apple", 10, 8, "Sweden"));
    }
}
