# Exercise 20: End-to-End Real-Time Spark Project

## Code
See `src/main/scala/exercises/Exercise20_EndToEndProject.scala`

## Pipeline steps
1. **Read data**: transaction_id, customer_id, amount, transaction_type, timestamp
2. **Handle nulls**: `na.fill(0.0, Seq("amount"))` replaces null amounts with 0
3. **Derived column via UDF**: categorizes each transaction as small/medium/large
4. **Aggregate by customer**: total amount and transaction count per customer_id
5. **Window aggregation**: running total per customer, ordered by timestamp,
   using `Window.partitionBy("customer_id").orderBy("timestamp")`
6. **Join with lookup**: enriches transactions with customer_name using a
   **broadcast join** (customerLookup is small, so it's sent to every executor)
7. **Repartition**: `repartition(4, $"customer_id")` redistributes data
   evenly by customer_id before writing, to avoid skewed output files
8. **Write output**: saved as Parquet for efficient downstream analytics

## Concepts covered

| Concept | Where it appears |
|---|---|
| DAG | Built from combining all the above transformations |
| Stages | New stage boundaries at each shuffle (groupBy, window, join, repartition) |
| Partitions | Explicitly set to 4 via repartition, keyed by customer_id |
| Transformations | withColumn, filter (implicit in na.fill), groupBy, join |
| Actions | show(), write.parquet() |
| Shuffle points | groupBy (aggregate), Window partitionBy/orderBy, repartition |
| Lineage-based resiliency | If any partition is lost, Spark recomputes it by replaying transformations from the original DataFrame, rather than needing stored copies of every intermediate result |

## Why each design choice matters
- **UDF** demonstrates custom business logic that doesn't fit built-in
  functions (though built-in `when/otherwise` could also work here -
  UDFs are for cases too complex for expression-based logic).
- **Window function** enables per-group running calculations without a
  full groupBy collapse - each row keeps its own line vs the aggregate.
- **Broadcast join** avoids shuffling the (potentially large) transactions
  table just to attach customer names from a small lookup table.
- **Repartition by customer_id** ensures the write step produces
  reasonably balanced output files and that customer data lands together,
  useful if downstream processing also partitions by customer.
