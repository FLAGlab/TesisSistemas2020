# Evaluation of Work Stealing Algorithms

We evaluate three work stealing algorithms. The classic algorithms, as well as an alternative proposal using priorities:


# Experiment Design

Each validation scenario uses a different computation size (i.e., number of tasks), generating DAGs to represent the computation containing 50, 100, 200, 400, 800, and 1600 nodes. Additionally, for each DAG size we evaluate three different graph density values (i.e., the ratio of outgoing edges to nodes) 0.2, 0.5, and 0.8


# Experiment Results

## Performance evaluation

  Density | 1 processor | 8 processos | 32 processors | 96 processors 
 -------------- | -------------- | -------------- | -------------- | -------------- 
 0.2  | <img src="https://github.com/FLAGlab/WorkStealingAlgorithms/blob/gh-pages/img/all_02_1.png" width="200" height="200"> |   <img src="https://github.com/FLAGlab/WorkStealingAlgorithms/blob/gh-pages/img/all_02_8.png" width="200" height="200"> | <img src="https://github.com/FLAGlab/WorkStealingAlgorithms/blob/gh-pages/img/all_02_32.png" width="200" height="200"> | <img src="https://github.com/FLAGlab/WorkStealingAlgorithms/blob/gh-pages/img/all_02_96.png" width="200" height="200">
 0.5  | <img src="https://github.com/FLAGlab/WorkStealingAlgorithms/blob/gh-pages/img/all_05_1.png" width="200" height="200">  | <img src="https://github.com/FLAGlab/WorkStealingAlgorithms/blob/gh-pages/img/all_05_8.png" width="200" height="200"> | <img src="https://github.com/FLAGlab/WorkStealingAlgorithms/blob/gh-pages/img/all_05_32.png" width="200" height="200"> | <img src="https://github.com/FLAGlab/WorkStealingAlgorithms/blob/gh-pages/img/all_05_96.png" width="200" height="200">
 0.8  | <img src="https://github.com/FLAGlab/WorkStealingAlgorithms/blob/gh-pages/img/all_08_1.png" width="200" height="200"> |  <img src="https://github.com/FLAGlab/WorkStealingAlgorithms/blob/gh-pages/img/all_08_8.png" width="200" height="200">| <img src="https://github.com/FLAGlab/WorkStealingAlgorithms/blob/gh-pages/img/all_08_32.png" width="200" height="200"> | <img src="https://github.com/FLAGlab/WorkStealingAlgorithms/blob/gh-pages/img/all_08_96.png" width="200" height="200">


## Load evaluation

### Desnsity = 0.2

No. of  Nodes | 1 processor | 8 processos | 32 processors | 96 processors
 -------------- | -------------- | -------------- | -------------- | --------------  
 50 |  |  |  |  
200 |  | <img src="https://github.com/FLAGlab/WorkStealingAlgorithms/blob/gh-pages/img/load_02_8_200.png" width="300" height="100"> | <img src="https://github.com/FLAGlab/WorkStealingAlgorithms/blob/gh-pages/img/load_02_32_200.png" width="300" height="100"> |


### Desnsity = 0.5

No. of  Nodes | 1 processor | 8 processos | 32 processors | 96 processors
 -------------- | -------------- | -------------- | -------------- | --------------  
 50 |  |  |  |  
 200 |  |  | ![05_32_200]("img/load_02_32_200.png") |  

### Desnsity = 0.8

No. of  Nodes | 1 processor | 8 processos | 32 processors | 96 processors
 -------------- | -------------- | -------------- | -------------- | --------------  
 50 |  |  |  |  
 200 |  |  |  |  


# Evaluation Configuration

All our benchmarks ran on a XeonSP G291-281 GPU Server with two RTX2080 CPUs, each with 48 physical cores with a 2.2GHz frequency and a 128GB NUMA enabled memory architecture, running the Ubuntu 20.04.2 LTS OS. We use version 1.8 of the JVM for our experiments.

