# Restaurant Rush

## SC2002-MACS2-Group6
## Team Members
| Name | Matric No |
| --- | ---|
| LEE XING YI SARAH | U2540546B |
| QIN YUXI EILEEN | U2540685L |
| TAN JIE SHENG | U2540156A|
| XAVIER TAN YI-EN | U2540425G |

---
# Restaurant Rush

Restaurant Rush is a turn-based restaurant management simulation developed for SC2002 Object-Oriented Design & Programming.

Players manage restaurant operations across 18 turns by controlling staff, prioritizing customers, processing orders, and maximizing revenue while maintaining customer satisfaction. The project evolves over three development stages, introducing new customer types, staff members, and gameplay mechanics.

---

# Table of Contents
* [Stage Evolution](#stage-evolution)
* [Project Structure](#project-structure)
* [Running the Program](#running-the-program)
* [Assumptions](#assumptions)
* [Design Considerations](#design-considerations)
* [Recorded Video Timestamps](#recorded-video-timestamps)

# Stage Evolution

## Stage 1 → Stage 2

### New Files Added

| File | Reason |
|--------|--------|
| VIPCustomer.java | Introduced VIP customers with 10% off on bill and satisfaction characteristics. |
| CriticCustomer.java | Introduced critic customers with higher satisfaction loss per round and lower threshold. |

### Existing Files Modified

| File | Changes | Reason |
|--------|--------|--------|
| Customer.java | Added abstract methods and customer-specific behaviour to support VIPCustomer and CriticCustomer implementations.| Enabled polymorphic handling of different customer types through inheritance.|
| Restaurant.java | Updated payment and satisfaction evaluation logic to account for customer-specific behaviours.| Support VIP bill discounts and Critic satisfaction thresholds through polymorphism.|
| RegularCustomer.java | Implemented customer type identification through method overriding.| Allows customer-specific information to be displayed without explicit type checking.|
| TurnController.java | Support arrival and processing of VIP and Critic customers.| Allow generation of VIP and Critic Customer in the queue.|
| RestaurantCLI.java | Updated exiting prints to identify Customer type. | Provide customer-type information to support managerial decision-making.|

### Files Removed

None.

### Design Rationale
Stage 2 extends the customer hierarchy through inheritance by introducing VIPCustomer and CriticCustomer. Customer.java was enhanced with common customer behaviours, allowing restaurant operations to interact with customers through a shared abstraction. This reduces customer-type conditionals and improves extensibility for future customer categories.


### Relevant Git History

- Stage 1 Commit: `ff72d61aabda1aa9b9aba284b8770a25117007fc`
- Stage 2 Commit: `1f6c6e6ed4ba495685d7a13aea50fac3753a83bf`
- Compare Link: `https://github.com/JieShengNP/SC2002_MACS2_Group6/compare/stage-1...stage-2`

---

## Stage 2 → Stage 3

### New Files Added

| File | Reason |
|--------|--------|
| Host.java | Added a new staff role responsible for seating customers. |
| ComboMeal.java | Introduced ComboMeal menu item composed from two randomly selected menu items.|

### Existing Files Modified

| File | Changes | Reason |
|--------|--------|--------|
| Order.java | Added boolean attribute for Happy Hour. | Allow restaurant to check if it was placed during Happy Hour for discount.|
| Customer.java | Updated menu selection and satisfaction-related behaviours.| Allow customers to order ComboMeals and receive satisfaction bonuses during Happy Hour.|
| Restaurant.java | Extended restaurant operations to support Host actions and Happy Hour state management.| Support Stage 3 gameplay mechanics while maintaining existing restaurant operations.|
| TurnController.java | Updated turn flow to add Happy Hour.| Allow manager to trigger Happy Hour, if it is available.|
| RestaurantCLI.java | Updated exiting prints to include Host actions and option to enable Happy Hour.| Allow manager to have control Host as well as enable Happy Hour.|

### Files Removed

None.

### Design Rationale

Stage 3 introduces new gameplay features through extension of the existing design. Host encapsulates customer seating responsibilities, improving separation of concerns among staff roles. ComboMeal extends the menu system while remaining compatible with existing ordering, preparation and payment workflows through polymorphism.

### Relevant Git History

- Stage 2 Commit: `1f6c6e6ed4ba495685d7a13aea50fac3753a83bf`
- Stage 3 Commit: `6686c08cadb39b2b08f9cb75dfa9f86b3a1ffd83`
- Compare Link: `https://github.com/JieShengNP/SC2002_MACS2_Group6/compare/stage-2...stage-3`

---

# Project Structure

src/
- controller/
  - GameController.java
  - TurnController.java

- entity/
  - customer/
    - Customer.java
    - RegularCustomer.java
    - VIPCustomer.java
    - CriticCustomer.java

  - menu/
    - MenuItem.java
    - Salad.java
    - Burger.java
    - Pasta.java
    - ComboMeal.java

  - staff/
    - Staff.java
    - Waiter.java
    - Chef.java
    - Cashier.java
    - Host.java

  - restaurant/
    - Restaurant.java
    - Table.java
    - Order.java
    - WaitingQueue.java

- enums/
  - CustomerStatus.java
  - OrderStatus.java
- view/
  - RestaurantCLI.java
- Main.java

---

# Running the Program

## Prerequisites

- Java 21
- Git

## 1. Clone the Repository

```bash
git clone https://github.com/JieShengNP/SC2002_MACS2_Group6.git
cd SC2002_MACS2_Group6
```

## 2. View a Specific Development Stage

The repository preserves the software evolution through Git tags.

View the Stage 1 implementation:

```bash
git checkout stage-1
```

View the Stage 2 implementation:

```bash
git checkout stage-2
```

View the Stage 3 implementation:

```bash
git checkout stage-3
```

Return to the latest version:

```bash
git checkout main
```

## 3. Compile the Project

From the project root directory, run:

```bash
javac -d out -sourcepath src src/restaurantrush/Main.java
```

## 4. Run the Program

```bash
java -cp out restaurantrush.Main
```

Follow the command-line prompts to manage the restaurant.

## 5. Automated Test Runs

Instructions for executing the reproducible test cases are provided in `test-runs/test.md`.

The `test-runs` folder contains:
- Victory scenarios for Stage 1, Stage 2 and Stage 3.
- Defeat scenarios for Stage 1, Stage 2 and Stage 3.
- Corresponding expected output files.

---

# Assumptions


1. Customers seated during a turn are immediately eligible for subsequent actions in later phases of the same turn. For example, a customer seated by the Host may have their order taken by the Waiter during the same turn.
2. The waiting queue allows seating priority to be determined by the player. Although customers are displayed in a queue, the Waiter or Host may choose any waiting customer to be seated.
3. ComboMeal orders are generated using two distinct menu items selected at random from the available menu items. For example, (Pasta + Burger) or (Pasta + Salad) or (Burger + Salad).
4. Revenue is only recorded when payment is successfully processed by the Cashier.
5. Discounts from VIP customers, Combo Meal and Happy Hour are applied during payment calculation and are reflected in the final bill amount. If calculation results in decimal cents, it will be rounded down to the nearest integer.
6. Customers who have completed payment leave the restaurant immediately and do not occupy tables in subsequent turns.

---

# Design Considerations
1. Customers served in the end-of-turn summary means customers who have paid. Served customers who are eating or waiting to pay are excluded. The same paid population determines the victory count and satisfaction average. No paid customers is displayed as N/A.

2. Stages 2 and 3 replace the turn-1 Regular arrival with a Critic and the turn-4 arrival with a VIP. Arrivals remain on turns 1, 4, 7, 10, 13 and 16. Stage 1 contains only Regular customers.

3. A Regular loses 8 satisfaction per unserved turn, a VIP loses 5 and a Critic loses 12. A Critic leaves if still unserved and satisfaction is below 50 (inclusive) after the turn-end deduction. At 51 the Critic remains. Other customers leave at zero. Departures cancel the outstanding order, remove the waiting entry or release the table, and add no revenue.

4. We initially used a FIFO queue but changed to a list so managers could choose whom to seat. This allows them to prioritise customers with higher satisfaction to improve the average among paid customers, as departures incur no direct penalty. The trade-off is reduced first-come, first-served fairness and potentially fewer paying customers.

5. Stage 3 uses a 25% combo probability. With probability 75%, a single base item is selected. Combo preparation units are summed. Combo price is 90% of the component total.

6. We require two distinct items so that a ComboMeal offers variety and takes only two to three preparation units. Allowing duplicates would offer more combinations, but two Pastas would increase the Chef’s workload, while two Salads would generate less revenue for the same preparation time as Salad and Burger. We therefore trade some flexibility for manageable preparation time and better revenue potential.

7. VIP discount is applied first, followed by the Happy Hour. Price is locked when the order is taken. A VIP Burger during Happy Hour costs 1500 x 0.9 x 0.8 = 1080 cents, displayed as $10.80.

8. Happy Hour can be activated once at turn start and lasts for that turn and the next. During each active turn, `WAITING`, `SEATED` and `ORDERED` customers gain 15 satisfaction at the start of the turn, capped at 100 (customer satisfaction still decreases as per normal at the end of the turn if still `WAITING`, `SEATED` or `ORDERED`). This includes customers whose food is READY but has not been served. `SERVED`, `EATING`, `READY_TO_PAY`, `PAID` and `LEFT` customers are excluded. Existing order prices remain fixed.  <br>-   Activating Happy Hour when several eligible customers have low satisfaction provides greater recovery, while those already at 100 gain nothing. However, new orders receive a 20% discount, reducing revenue per order. The player must therefore balance satisfaction recovery against the revenue target when choosing when to activate it.

9. The Host acts before the Waiter in Stage 3. This lets the Host seat a customer and the Waiter take that customer's order in the same turn. The Waiter still retains the option to seat. Each staff object gets one performTask call per turn, and the input loops allow retries without spending a task.

10. The Waiter acts before the Chef, so food completed by the Chef can be served on the next turn the earliest. A customer served in turn `t` transitions to `EATING` at its end, `READY_TO_PAY` at the end of `t+1`, and can be charged in `t+2`. The Cashier runs before new `READY_TO_PAY` customers join the FIFO payment queue. This preserves a full eating turn and prevents same-turn payment.
---

# Recorded Video Timestamps

---
| Timestamp	|Segment |
|---|---|
|02:12|The game launching from the submitted project|
|02:12|Meaningful manager choices, including a decision where staff are constrained|
|03:52|A dish going READY → SERVED → payment|
|06:46|A VIP or Critic behaviour|
|07:40|A Stage 3 feature|
|04:10|One victory and one defeat (separate reproducible runs, or an accelerated replay)|
|09:09|The stage tag comparisons and the diagrams|


