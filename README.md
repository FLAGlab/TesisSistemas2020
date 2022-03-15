# Evaluation of Work Stealing Algorithms

This repository contains a Benchmark for the evaluations of different work stealing algorithms.
Currently, the system can supports
  - FIFO
  - LIFO
  - Priorityes (developped for this work)

For each algorithm we execute 30 Iterations with a varying number of cores (1..96).
Our algorithm is tested on generated task DAGs of sizes 50..1600 with a varying density in the set {0.2, 0.5, 0.8} for each iteration and number of cores combination. The results of benchmarks are summarized at our [online webpage](https://flaglab.github.io/WorkStealingAlgorithms/)
