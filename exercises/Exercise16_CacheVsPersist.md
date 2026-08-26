# Exercise 16: Cache vs Persist

## Code
See `src/main/scala/exercises/Exercise16_CacheVsPersist.scala`

## The problem without caching
By default, RDDs are **not** stored after an action runs - Spark recomputes
the entire lineage from scratch every time a new action is called on the
same RDD. If multiple actions (sum, count, etc.) are run on the same
transformed RDD, the expensive transformation chain runs once per action,
wasting time and compute.

## cache()
`cache()` is shorthand for `persist(StorageLevel.MEMORY_ONLY)`. The first
action triggers computation and stores the result in memory; subsequent
actions reuse the cached data instead of recomputing it.

## persist() with a storage level
`persist()` allows choosing exactly how and where data should be stored:

| Storage Level | Description |
|---|---|
| MEMORY_ONLY | Store as deserialized objects in memory (default for cache()) - fastest, but data is lost if it doesn't fit and isn't recomputable cheaply |
| MEMORY_AND_DISK | Store in memory; spill to disk if it doesn't fit - safer, slightly slower on spill |
| MEMORY_ONLY_SER | Store serialized in memory - more compact, less memory used, extra CPU cost to deserialize |
| MEMORY_AND_DISK_SER | Serialized version of MEMORY_AND_DISK |
| DISK_ONLY | Store only on disk - useful when memory is very limited |

## Performance difference observed
Without caching, each action re-triggers the full transformation chain
from the original data. With `cache()`/`persist()`, the first action
computes and stores the result; every subsequent action reads directly
from the cached/persisted data, skipping recomputation entirely - this
becomes especially significant with expensive transformations or when an
RDD is reused across many actions (e.g. iterative ML algorithms).

## When to use which
Use `cache()` when the dataset comfortably fits in memory and will be
reused. Use `persist()` with `MEMORY_AND_DISK` (or a `_SER` variant) when
data might not fully fit in memory, to avoid losing the cached data and
falling back to full recomputation.
