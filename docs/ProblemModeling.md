# Problem Modeling (Safest Path)

## Goal
We are given a graph where each edge e has a safety probability p(e), 0 < p(e) ≤ 1.
The safety of a path is the PRODUCT of probabilities along the path:

Safety(path) = ∏ p(e)

We want the **safest path** from KTM to each relief center, meaning the path with **maximum product**.

---

## (a) Why standard Dijkstra (distance sum) is not suitable
Standard Dijkstra minimizes the SUM of edge weights:

Distance(path) = Σ w(e)

But our objective is to maximize a PRODUCT, not minimize a sum.
If we directly treat probabilities as weights, Dijkstra’s assumptions break because:
- Dijkstra is designed for additive costs (sum of edges).
- A path with a better product might not have a better sum, so Dijkstra’s “shortest so far is final” logic does not match the goal.

So using distance as weight does not correctly represent “safest path”.

---

## (b) Why maximizing probabilities directly is problematic for Dijkstra
Even if we try to "maximize" Σ p(e) or "maximize" a score directly with Dijkstra, it is still problematic because:

- Standard Dijkstra is based on **minimization** with **non-negative additive weights**.
- Maximization does not preserve the greedy property required by Dijkstra.
- Also, maximizing Σ p(e) is not the same as maximizing ∏ p(e).

Example idea:
Two paths may have the same total sum of probabilities but different products.
So directly maximizing path probability values in the standard Dijkstra framework does not guarantee correctness.