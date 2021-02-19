# Evaluation of Work Stealing Algorithms

This repository contains a Benchmark for the evaluations of different work stealing algorithms.
Currently, the system can supports
  - FIFO
  - LIFO
  - Priorityes (developped for this work)

For each algorithm we execute k Iterations (30) with a varying number of cores (1..MAX_CORES).
Our algorithm is tested on generated task DAGs of sizes 50..1600 with a density in {0.2, 0.5, 0.5} for each iterations and number of course combination
