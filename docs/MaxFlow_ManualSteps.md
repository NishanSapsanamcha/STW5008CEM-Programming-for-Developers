# Question 3(b): Edmonds–Karp Manual Execution (KTM → BS)

We run Edmonds–Karp (Ford–Fulkerson using BFS on the residual graph).

Notation:
- res(u→v) = residual capacity
- Each iteration:
    1) augmenting path
    2) pushed flow (bottleneck)
    3) residual updates

Initial residual capacities = original capacities.

------------------------------------------------------------

## Iteration 1 (BFS path)
Augmenting path found:
KTM → JA → BS

Residuals on path:
- res(KTM→JA) = 10
- res(JA→BS)  = 5

Bottleneck = min(10, 5) = 5

Push 5 units of flow.

Residual updates:
- res(KTM→JA): 10 → 5
- res(JA→BS):  5 → 0
  Add reverse residual edges (because flow can be cancelled later):
- res(JA→KTM) increases by 5
- res(BS→JA) increases by 5

Total flow now = 5

------------------------------------------------------------

## Iteration 2 (BFS path)
Augmenting path found:
KTM → JB → BS

Residuals on path:
- res(KTM→JB) = 15
- res(JB→BS)  = 12

Bottleneck = min(15, 12) = 12

Push 12 units of flow.

Residual updates:
- res(KTM→JB): 15 → 3
- res(JB→BS):  12 → 0
  Add reverse residual edges:
- res(JB→KTM) increases by 12
- res(BS→JB) increases by 12

Total flow now = 5 + 12 = 17

------------------------------------------------------------

## Iteration 3 (BFS path)
Now JA→BS and JB→BS are both saturated (0 residual),
so BFS reaches BS through PH.

Augmenting path found:
KTM → JA → PH → BS

Residuals on path:
- res(KTM→JA) = 5
- res(JA→PH)  = 8
- res(PH→BS)  = 6

Bottleneck = min(5, 8, 6) = 5

Push 5 units of flow.

Residual updates:
- res(KTM→JA): 5 → 0
- res(JA→PH):  8 → 3
- res(PH→BS):  6 → 1
  Add reverse residual edges:
- res(JA→KTM) increases by 5
- res(PH→JA) increases by 5
- res(BS→PH) increases by 5

Total flow now = 17 + 5 = 22

------------------------------------------------------------

## Iteration 4 (BFS path)
Only remaining outgoing residual from KTM is KTM→JB (3 residual).
To reach BS, we use JB→JA and JA→PH and the last PH→BS (1 residual).

Augmenting path found:
KTM → JB → JA → PH → BS

Residuals on path:
- res(KTM→JB) = 3
- res(JB→JA)  = 4
- res(JA→PH)  = 3
- res(PH→BS)  = 1

Bottleneck = min(3, 4, 3, 1) = 1

Push 1 unit of flow.

Residual updates:
- res(KTM→JB): 3 → 2
- res(JB→JA):  4 → 3
- res(JA→PH):  3 → 2
- res(PH→BS):  1 → 0
  Add reverse residual edges:
- res(JB→KTM) increases by 1
- res(JA→JB) increases by 1
- res(PH→JA) increases by 1
- res(BS→PH) increases by 1

Total flow now = 22 + 1 = 23

------------------------------------------------------------

## Stop Condition
Run BFS again:
All edges into BS from the source-side are saturated:
- res(JA→BS) = 0
- res(JB→BS) = 0
- res(PH→BS) = 0

So BS is not reachable in the residual graph.
Algorithm terminates.

Final Max Flow = 23