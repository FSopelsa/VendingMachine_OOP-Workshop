## Part 2 — Optional Challenge

Extend your design with one meaningful improvement. Write a short explanation of:

- What you added
- Why it makes the system better
- What OOP concept it demonstrates


---

```mermaid
classDiagram
class VendingMachineImpl {
  -CoinBank coinBank
  -Map~Integer, Product~ products
  +insertCoin(int) boolean
  +purchaseProduct(int) PurchaseResult
  +returnChange() Change
  +getBalance() int
}

class CoinBank {
  -Map~Integer, Integer~ coins
  +insertCoin(int) boolean
  +getBalance() int
  +returnChange() Change
  +isAcceptedCoin(int) boolean
}

class Change {
  -Map~Integer, Integer~ coins
  +getTotalAmount() int
  +isEmpty() boolean
  +format() String
}

VendingMachineImpl --> CoinBank
CoinBank --> Change
```

CoinBank owns accepted coin validation and balance tracking, and VendingMachineImpl.getBalance() delegates to coinBank.getBalance(). 
This makes the optional challenge a real design improvement, not just extra printing.
