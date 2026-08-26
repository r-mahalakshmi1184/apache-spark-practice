# Exercise 17: Spark on YARN

## Components
- **ResourceManager**: Cluster-wide resource scheduler - decides which
  applications get resources
- **NodeManager**: Runs on each worker machine - manages containers on
  that machine
- **ApplicationMaster**: Per-application coordinator - negotiates
  containers with the ResourceManager for this specific Spark job
- **Container**: An allocated unit of resources (CPU + memory) on a
  NodeManager where a process (Driver or Executor) actually runs
- **Spark Driver**: Runs main(), builds the DAG, coordinates task execution
- **Spark Executors**: Run inside containers, execute tasks, report back
  to the Driver

## Lifecycle of submitting a Spark app to YARN
1. Client runs `spark-submit --master yarn`, contacting the ResourceManager
2. ResourceManager picks a NodeManager to launch the first container,
   starting the ApplicationMaster
3. In cluster mode: the Driver runs inside this ApplicationMaster's
   container. In client mode: the Driver stays on the submitting machine
4. ApplicationMaster negotiates with ResourceManager for additional
   containers to run Executors
5. ResourceManager instructs other NodeManagers to launch those containers
6. Executors start inside their containers, register with the Driver,
   and begin executing tasks
7. On completion, the Driver/ApplicationMaster releases all containers
   back to the ResourceManager
