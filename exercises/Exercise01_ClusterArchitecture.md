# Exercise 1: Understanding the Spark Cluster

## Components
- **Driver program**: Runs main(), builds the DAG of transformations
- **Cluster Manager**: Allocates resources (in local mode, this role is implicit)
- **Worker Nodes**: Machines that host executors
- **Executors**: Processes that run tasks and cache data

## What happens when a Spark app is submitted
1. Driver starts, runs main(), builds logical execution plan (DAG)
2. Driver requests resources from the Cluster Manager
3. Cluster Manager allocates Worker Nodes
4. Executors are launched on Worker Nodes
5. Driver splits work into Tasks, sends to Executors
6. Executors run tasks in parallel, report results back to Driver

## Note on local[*] mode
In `.master("local[*]")`, a single machine plays all four roles at once —
Driver, Cluster Manager, Worker, and Executor — using threads instead of
separate physical machines. `local[*]` means "use all available CPU cores
as worker threads."
