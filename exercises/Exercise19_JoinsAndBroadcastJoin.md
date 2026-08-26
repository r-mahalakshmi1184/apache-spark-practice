# Exercise 19: DataFrame Joins and Broadcast Join

## Code
See `src/main/scala/exercises/Exercise19_JoinsAndBroadcastJoin.scala`

## Join types demonstrated
- **Inner join**: Returns only rows with matching keys on both sides.
  Eve (department_id 999) is excluded since no department matches.
- **Left join**: Returns all rows from the left (Employees) table, with
  nulls filled in for unmatched columns from the right table. Eve appears
  with `department_name = null`.
- **Broadcast join**: Explicitly hints Spark to broadcast the smaller
  DataFrame (Departments) to every executor, avoiding a shuffle.

## Shuffle sort merge join vs broadcast join

| | Shuffle sort merge join | Broadcast join |
|---|---|---|
| How it works | Both DataFrames are shuffled/sorted by join key, then merged | Small DataFrame is sent whole to every executor; large DataFrame stays in place |
| Network cost | High - both sides move across the network | Low - only the small table is sent once per executor |
| Best for | Two large DataFrames of similar size | One large + one small DataFrame |
| Trigger | Spark's default for large-large joins | Automatic if small table < broadcast threshold (default 10MB), or forced with `broadcast()` hint |

## When broadcast join is appropriate
Use a broadcast join when one side of the join is **small enough to fit
comfortably in memory on every executor** (like a lookup/dimension table -
Departments, country codes, currency rates). This avoids the expensive
shuffle and sort needed for a regular join, since the small table doesn't
need to be partitioned by key at all - it's just replicated everywhere.
For two large, similarly-sized DataFrames, a shuffle sort merge join is
unavoidable and more efficient than trying to broadcast a large table.
