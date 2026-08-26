# Exercise 12: Broadcast Variable

## Code
See `src/main/scala/exercises/Exercise12_BroadcastVariable.scala`

## What a broadcast variable does
A broadcast variable lets you efficiently distribute a **read-only** copy
of a variable (like a small lookup table) to every executor **once**,
instead of it being shipped along with every task that needs it.

## Why broadcasting is more efficient
Without broadcasting, if a small lookup Map is referenced inside a
`map`/`filter` closure, Spark serializes and sends a **copy of that Map
with every single task** - which can mean sending it thousands of times
if there are many partitions/tasks. With `sc.broadcast()`, the data is
sent to each executor **once** and cached there, then reused by every
task running on that executor. This dramatically reduces network traffic
and memory duplication, especially useful when:
- The lookup table is small enough to fit in memory on each executor
- It's reused across many tasks/partitions (like joining a large dataset
  with department names, country codes, currency rates, etc.)

## When to use it
Broadcast variables are ideal for **small, static reference data** used
repeatedly across a large dataset - a classic pattern being a large
"fact" table joined against a small "dimension" table (also related to
broadcast joins, covered in Exercise 19).
