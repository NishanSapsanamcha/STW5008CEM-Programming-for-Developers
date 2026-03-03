# Algorithm Adaptation for the Safest Path

## (a) Weight Transformation (Log Trick)
We want to maximize:

∏ p(e)

Take natural log:

log(∏ p(e)) = Σ log(p(e))

Because log is strictly increasing, maximizing the product is the same as maximizing Σ log(p(e)).

To convert to a shortest path (minimization), define a new weight:

w(e) = -log(p(e))

Then for a path:

Σ w(e) = Σ (-log(p(e))) = - Σ log(p(e)) = -log(∏ p(e))

Minimizing Σ w(e) is equivalent to maximizing ∏ p(e).

So we can use standard Dijkstra on w(e) = -log(p(e)).

---

## (b) Modified Dijkstra + RELAX
We run Dijkstra where:

dist[v] = minimum Σ (-log(p(e))) from source to v

RELAX(u, v):
If dist[u] + (-log(p(u,v))) < dist[v]
dist[v] = dist[u] + (-log(p(u,v)))
parent[v] = u

At the end, the maximum safety to v is:

Safety[v] = exp(-dist[v])

Because:
dist[v] = -log(bestProduct)
=> bestProduct = exp(-dist[v])

---

## Output
- dist[] gives shortest transformed distance
- parent[] reconstructs safest path
- exp(-dist[v]) gives safety probability of safest path