# Max Flow Algorithm (Edmonds–Karp)

## Why Edmonds–Karp
Edmonds–Karp is a specific implementation of Ford–Fulkerson that:
- uses BFS to find augmenting paths
- guarantees polynomial time

This is suitable for coursework because it is easy to explain and trace.

---

## Residual Graph Concept
We build a residual capacity for every edge:

res(u,v) = c(u,v) - f(u,v)

If res(u,v) > 0, we can still send more flow on that edge.

Also, we maintain reverse edges to allow "undoing" flow:

res(v,u) = f(u,v)

---

## Algorithm Steps
1) Initialize all flows to 0.
2) While there exists an augmenting path from s to t in the residual graph (found using BFS):
   a) Find the bottleneck capacity (minimum residual edge capacity along the path).
   b) Add that bottleneck flow to every forward edge in the path.
   c) Subtract that bottleneck flow from every reverse edge in the path.
3) When BFS can no longer reach t, the current flow is maximum.

---

## Complexity
Edmonds–Karp Time Complexity: O(V * E^2)
Space Complexity: O(V^2) if using adjacency matrix, or O(V + E) with adjacency list.