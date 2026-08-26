# Exercise 21: E-commerce Order Processing (PySpark)

## Code
See `python_exercises/Exercise21_EcommerceOrderProcessing.py`

## Concepts used
- **Broadcast variable**: `discount_broadcast` distributes the small
  product-to-discount lookup Map to every executor once, avoiding
  re-sending it with every task.
- **Accumulator**: `invalid_orders` counts how many orders failed
  validation (negative amount or unknown product), aggregated safely
  across all partitions back to the driver.
- **filter (validate_order)**: a narrow transformation that also has the
  side effect of incrementing the accumulator for rejected orders.
- **map (apply discount)**: computes final price per valid order using
  the broadcasted discount lookup.
- **collect()**: the action that triggers the whole pipeline to execute.

## Result
| order_id | product_id | discounted_amount |
|---|---|---|
| 1 | P101 | 900.0 |
| 2 | P102 | 475.0 |
| 3 | P103 | 1200.0 |
| 4 | P101 | 450.0 |
| 5 | P104 | 1700.0 |

Invalid orders: 1 (order 6 - negative amount and unknown product P999)
