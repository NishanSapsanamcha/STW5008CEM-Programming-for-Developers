# Question 3(a): Model as a Maximum Flow Problem

## Source and Sink
- Source (s): KTM
- Sink (t): BS

## Capacity Graph (Directed)
Each directed edge (u → v) has capacity c(u,v) in trucks/hour.

Relevant edges (from the provided table):
- KTM → JA : 10
- KTM → JB : 15

- JA → KTM : 10
- JA → PH  : 8
- JA → BS  : 5

- JB → KTM : 15
- JB → JA  : 4
- JB → BS  : 12

- PH → JA  : 8
- PH → BS  : 6

- BS → JA  : 5
- BS → JB  : 12
- BS → PH  : 6

## Objective
Find the maximum amount of flow (trucks/hour) that can be sent from KTM to BS,
subject to:
1) 0 ≤ f(u,v) ≤ c(u,v)
2) Flow conservation at all intermediate nodes (JA, JB, PH)
3) Maximize total flow into BS