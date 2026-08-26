# Exercise 13: Accumulator Exercise

## Code
See `src/main/scala/exercises/Exercise13_Accumulator.scala`

## How accumulators work on executors
An accumulator is a variable that executors can only **add to**, never
read from directly. Each executor keeps its own local copy of the
accumulator while processing its partition's tasks, and only sends the
partial result back to the **Driver**, which merges all partial values
into the final total. Executors never see each other's updates or the
running total - only the Driver has the complete, correct value, and
only after an action has fully executed.

## Result
Out of 5 transactions (100, -50, 200, -20, 300), 2 are negative
(-50 and -20) - matching the accumulator's final value.

## Why accumulators should NOT be used as a normal shared variable
- **They are write-only from the executors' side** - tasks can call
  `.add()` but cannot read `.value` reliably during execution; the
  correct total is only guaranteed once an action completes on the Driver.
- **They are not fault-tolerant in transformations** - if a task fails
  and Spark retries it (or if the same stage is recomputed due to lazy
  re-evaluation), the accumulator can be updated **more than once** for
  the same data, silently producing an incorrect count. This is why
  accumulators are recommended for use inside **actions** (like `foreach`)
  rather than transformations (like `map`), where retries are less
  likely to cause double-counting.
- They are meant purely for **monitoring/debugging aggregates** (like
  counting bad records, error counts, etc.), not as a substitute for
  proper distributed computation like `reduce` or `reduceByKey`.
