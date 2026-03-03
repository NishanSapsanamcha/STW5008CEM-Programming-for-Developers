# Proof of Correctness (Edmonds–Karp)

## Claim
Edmonds–Karp returns a maximum flow.

---

## Key Ideas
Edmonds–Karp is Ford–Fulkerson where the augmenting path is always chosen using BFS
(shortest number of edges in the residual graph).

The algorithm repeatedly increases flow while preserving:
- capacity constraints (never exceeds residual)
- flow conservation (augmenting along a path keeps conservation at intermediate nodes)

When no augmenting path exists, the flow is maximum.

---

## Why "No Augmenting Path" ⇒ Maximum Flow
If there is no path from s to t in the residual graph, then:
- There is no way to push additional flow from s to t.

By the Max-Flow Min-Cut Theorem:
- When the residual graph has no augmenting path,
  the current flow equals the capacity of some s–t cut,
  and therefore the flow is maximum.

So Edmonds–Karp must stop exactly when a maximum flow is achieved.

---

## Complexity Guarantee
Unlike basic Ford–Fulkerson, Edmonds–Karp always terminates in polynomial time,
because each BFS increases the shortest-path distance to saturated edges a bounded number of times.
Time complexity is O(V * E^2).