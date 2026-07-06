![Lexicon Logo](https://lexicongruppen.se/media/wi5hphtd/lexicon-logo.svg)

# Workshop — OOP (Vending Machine)

---

## The Scenario

A new vending machine is being installed in the Lexicon break room and it needs software to run.

The machine stocks three categories of products: **snacks**, **beverages**, and **fruits**. Every product has a name, a price, and a stock quantity. But each category also carries its own detail — a snack has a weight in grams, a beverage has a volume in ml, and a fruit has a country of origin. When a product is displayed, it should describe itself including that specific detail.

The machine runs on coins. It accepts only the standard Swedish coin values: **1, 2, 5, 10, 20, and 50 kr**. Any other value is rejected immediately and the balance does not change.

Here is what a typical session looks like:

```
Welcome to Lexicon Vending Machine
------------------------------------
[1] Chips        - 15 kr  (Snack, 130g)     Stock: 5
[2] Cola         - 20 kr  (Beverage, 330ml)  Stock: 3
[3] Apple        - 10 kr  (Fruit, Sweden)    Stock: 8
------------------------------------
Balance: 0 kr

> Insert coin: 10
Balance: 10 kr

> Insert coin: 7
Invalid coin. Only 1, 2, 5, 10, 20, 50 kr accepted.
Balance: 10 kr

> Insert coin: 10
Balance: 20 kr

> Select product: 2
Dispensing: Cola (Beverage, 330ml)
Balance: 0 kr

> Select product: 1
Insufficient balance. Chips costs 15 kr.

> Insert coin: 20
Balance: 20 kr

> Select product: 1
Dispensing: Chips (Snack, 130g)
Change returned: 5 kr
Balance: 0 kr

> Return change
No balance to return.
```

The machine must also handle edge cases cleanly: if a product is out of stock, the user gets a clear message. If the user asks for change when the balance is already 0, the machine says so.

---

## Required Functionalities

### Products
- The machine stocks three product types: **Snack**, **Beverage**, and **Fruit**
- All products share: `id`, `name`, `price (int, in kr)`, `quantity`
- Each type adds its own specific field:
  - `Snack` → `weightGrams: int`
  - `Beverage` → `volumeMl: int`
  - `Fruit` → `origin: String`
- Every product must be able to describe itself — output format: `[name] ([type], [specific detail])`
- A product that cannot describe itself (plain product) should not be possible to create

### Payment
- Accepted coin values: `1, 2, 5, 10, 20, 50`
- Any other value is rejected — balance does not change, print a rejection message
- Each inserted valid coin adds to the current balance
- The user can request their remaining balance back at any time — balance resets to 0

### Purchase
- The user selects a product by its `id`
- If the product does not exist: print a clear message
- If the balance is less than the price: print how much is missing, do not deduct balance
- If the product is out of stock: print a clear message
- If the purchase succeeds: deduct the price from the balance, reduce stock by 1, print a confirmation
- If the balance exceeds the price after purchase: return the difference automatically

### Console UI
- On startup: display all products with their id, name, price, type detail, and stock
- Show the current balance after every action
- Accept user input in a loop until the user chooses to exit
- Keep the UI logic separate from the machine logic

---

## Your Task

### Step 1 — Model the System

Before writing any code, draw a class diagram.

Think about:
- What is shared across all product types? Where does that live?
- Can you ever create a plain `Product` with no type? What does that tell you?
- What does the vending machine need to do? Should that be a class or an interface — and why?
- Which classes know about which? Draw the relationships with the correct arrows and multiplicity.

Draw your diagram with all classes, fields, key methods, relationship arrows, and multiplicity.

### Step 2 — Implement the System

Build the application based on your model. Your implementation must:

- Use **inheritance** for the product hierarchy
- Use **polymorphism** when displaying or processing products — no `instanceof` checks
- Apply **encapsulation** — private fields, validated constructors
- Keep **console UI logic separate** from machine logic
- **Build a runnable JAR** that can be launched from the command line

---

## JUnit Tests

Write JUnit tests for the vending machine logic. Test the business rules — not the console output.

> Test `VendingMachineImpl` directly. Do not test `ConsoleUI`.

### Test Case 1 — Insert Valid Coin
**Given:** balance = 0
**When:** `insertCoin(10)`
**Then:** balance = 10

---

### Test Case 2 — Reject Invalid Coin
**Given:** balance = 0
**When:** `insertCoin(7)`
**Then:** balance stays 0

---

### Test Case 3 — Purchase Succeeds
**Given:** product price = 20, quantity = 3, balance = 0
**When:** `insertCoin(20)`, `purchaseProduct(id)`
**Then:** purchase returns the product, balance = 0, quantity = 2

---

### Test Case 4 — Purchase Fails — Insufficient Balance
**Given:** product price = 20, balance = 10
**When:** `purchaseProduct(id)`
**Then:** purchase fails, balance stays 10, quantity unchanged

---

### Test Case 5 — Purchase Fails — Out of Stock
**Given:** quantity = 0, balance >= price
**When:** `purchaseProduct(id)`
**Then:** purchase fails, balance unchanged, quantity stays 0

---

### Test Case 6 — Return Change
**Given:** balance = 50
**When:** `returnChange()`
**Then:** returns 50, balance = 0

---

### Test Case 7 — Product List
**Given:** machine has 3 products added
**When:** `getProducts()`
**Then:** list size = 3

---

## Part 2 — Optional Challenge

Extend your design with one meaningful improvement. Write a short explanation of:

- What you added
- Why it makes the system better
- What OOP concept it demonstrates

