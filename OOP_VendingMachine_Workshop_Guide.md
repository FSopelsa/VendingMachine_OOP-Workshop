# OOP Vending Machine Workshop Guide
---

### 1. Single Responsibility Principle

Each class should have one clear job.

Example:

```text
Product          -> represents shared product data
Snack            -> represents snack-specific data
Beverage         -> represents beverage-specific data
Fruit            -> represents fruit-specific data
VendingMachine   -> handles balance, coins, stock, and purchase rules
ConsoleUI        -> handles user input and printed output
```

Do not put menu or input code inside the product classes or vending machine logic.

---

### 2. Encapsulation

Fields should be private. Control access through constructors and methods.

Example:

```java
private String name;
private int price;
private int quantity;
```

Avoid making fields public.

Validate important values:

```java
if (price <= 0) {
    throw new IllegalArgumentException("Price must be positive");
}
```

---

### 3. Inheritance

Use inheritance when several classes share common data or behavior.

In this project, all products have:

```text
id
name
price
quantity
```

So these belong in an abstract `Product` class.

Then create child classes:

```text
Snack extends Product
Beverage extends Product
Fruit extends Product
```

---

### 4. Abstraction

A plain product should not exist because every real product must be a snack, beverage, or fruit.

So `Product` should be abstract:

```java
public abstract class Product {
    public abstract String describe();
}
```

---

### 5. Polymorphism

The vending machine should treat all products as `Product`.

Do this:

```java
List<Product> products;
```

Then call:

```java
product.describe();
```

Avoid this:

```java
if (product instanceof Snack) {
    ...
}
```

Each product type should know how to describe itself.

---

## How To Categorize Java Files

A clean beginner-friendly structure could be:

```text
src/main/java/se/lexicon/vending/
|
+-- Main.java
|
+-- model/
|   +-- Product.java
|   +-- Snack.java
|   +-- Beverage.java
|   +-- Fruit.java
|
+-- service/
|   +-- VendingMachine.java
|   +-- VendingMachineImpl.java
|
+-- ui/
    +-- ConsoleUI.java
```

And tests:

```text
src/test/java/se/lexicon/vending/
+-- service/
    +-- VendingMachineImplTest.java
```

---

## Package Meaning

### `model`

Contains classes that represent the data or domain objects.

Examples:

```text
Product
Snack
Beverage
Fruit
```

### `service`

Contains business logic.

Examples:

```text
insert coin
validate coin
purchase product
return change
check stock
```

### `ui`

Contains console interaction only.

Examples:

```text
print menu
read user input
show messages
```

### `Main.java`

Starts the application and connects the pieces together.

---

## Simple Rule

Ask this question when creating a class:

> What is this class responsible for?

If the answer contains "and", the class may be doing too much.

Bad:

```text
VendingMachine reads input and prints menu and handles purchases
```

Better:

```text
ConsoleUI reads input and prints menu
VendingMachine handles purchases
```

That separation is one of the most important lessons in this workshop.
