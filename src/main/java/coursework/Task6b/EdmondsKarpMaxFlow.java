package coursework.Task6b;

import java.util.*;

/**
 * @author Nishan Rai
 * Edmonds-Karp Algorithm for Maximum Flow.
 *
 * Problem:
 * - Given a directed graph with capacities,
 *   find maximum flow from source s to sink t.
 *
 * Algorithm:
 * - Edmonds-Karp is BFS-based Ford-Fulkerson.
 * - Repeatedly find shortest augmenting path (in edges) using BFS.
 * - Augment flow until no path exists.
 *
 * Time Complexity: O(V * E^2)
 * Space Complexity: O(V^2) for matrices
 */
public final class EdmondsKarpMaxFlow {

    private EdmondsKarpMaxFlow() { }

    /**
     * Computes max flow from s to t.
     *
     * @param n        number of nodes
     * @param capacity capacity[u][v] = capacity edge u->v
     * @param s        source
     * @param t        sink
     */
    public static int maxFlow(int n, int[][] capacity, int s, int t) {
        int[][] flow = new int[n][n];
        int total = 0;

        while (true) {
            int[] parent = new int[n];
            Arrays.fill(parent, -1);
            parent[s] = s;

            int[] minCap = new int[n];
            minCap[s] = Integer.MAX_VALUE;

            Queue<Integer> q = new ArrayDeque<>();
            q.add(s);

            // BFS to find augmenting path
            while (!q.isEmpty() && parent[t] == -1) {
                int u = q.poll();

                for (int v = 0; v < n; v++) {
                    int residual = capacity[u][v] - flow[u][v];

                    if (parent[v] == -1 && residual > 0) {
                        parent[v] = u;
                        minCap[v] = Math.min(minCap[u], residual);
                        q.add(v);
                    }
                }
            }

            // no augmenting path
            if (parent[t] == -1) break;

            int aug = minCap[t];
            total += aug;

            // update flow along path
            int v = t;
            while (v != s) {
                int u = parent[v];
                flow[u][v] += aug;
                flow[v][u] -= aug; // reverse edge
                v = u;
            }
        }
        return total;
    }
}