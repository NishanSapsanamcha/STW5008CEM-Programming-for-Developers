# Proof of Correctness (Safest Path)

## Claim
Running Dijkstra on transformed weights w(e) = -log(p(e)) returns a path with maximum safety product.

---

## Proof
Let P be any path from source to destination.

Original objective:
Maximize Safety(P) = ∏ p(e)

Define transformed cost:
Cost(P) = Σ w(e) = Σ (-log(p(e)))

Now compute:

Cost(P) = Σ (-log(p(e)))
= - Σ log(p(e))
= - log(∏ p(e))
= - log(Safety(P))

Because log is strictly increasing, Safety(P1) > Safety(P2)
⇔ log(Safety(P1)) > log(Safety(P2))
⇔ -log(Safety(P1)) < -log(Safety(P2))
⇔ Cost(P1) < Cost(P2)

So the path with the maximum product of probabilities is EXACTLY the same path
that has the minimum transformed sum cost.

Dijkstra is correct for shortest paths when all weights are non-negative.
Here, w(e) = -log(p(e)) is always ≥ 0 because:
0 < p(e) ≤ 1 ⇒ log(p(e)) ≤ 0 ⇒ -log(p(e)) ≥ 0

Therefore Dijkstra correctly finds the minimum Cost(P), which corresponds to
the maximum Safety(P).

Hence, the adapted algorithm is correct.