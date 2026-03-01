package coursework.Task6;

import java.util.*;

/**
 * @author Nishan Rai
 * Safest Path in a Disaster Response Network.
 *
 * Problem:
 * - Graph edges represent roads/links with reliability probability p (0..1).
 * - We want a path from source to destination with maximum reliability:
 *     maximize product(p_i)
 *
 * Technique:
 * - Convert to shortest path problem by using:
 *     weight = -log(p)
 * - Maximizing product(p) becomes minimizing sum(-log(p)).
 *
 * Algorithm:
 * - Use Dijkstra on transformed weights.
 *
 * Time Complexity: O((V + E) log V)
 * Space Complexity: O(V + E)
 */
public final class SafestPathReliability {

    private SafestPathReliability() { }

    public static class Edge {
        public final int to;
        public final double prob;

        public Edge(int to, double prob) {
            this.to = to;
            this.prob = prob;
        }
    }

    public static class Result {
        public final double reliability;
        public final List<Integer> path;

        public Result(double reliability, List<Integer> path) {
            this.reliability = reliability;
            this.path = path;
        }
    }

    /**
     * Finds safest path by maximum reliability.
     *
     * @param n   number of nodes (0..n-1)
     * @param g   adjacency list
     * @param src start node
     * @param dst end node
     */
    public static Result safestPath(int n, List<List<Edge>> g, int src, int dst) {

        double[] dist = new double[n];
        int[] parent = new int[n];

        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);

        dist[src] = 0.0;

        // pq stores nodes, sorted by smallest dist
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingDouble(u -> dist[u]));
        pq.add(src);

        while (!pq.isEmpty()) {
            int u = pq.poll();
            if (u == dst) break;

            for (Edge e : g.get(u)) {
                if (e.prob <= 0) continue;

                double w = -Math.log(e.prob);
                if (dist[u] + w < dist[e.to]) {
                    dist[e.to] = dist[u] + w;
                    parent[e.to] = u;
                    pq.add(e.to);
                }
            }
        }

        if (Double.isInfinite(dist[dst])) {
            return new Result(0.0, List.of());
        }

        // reliability = exp(-sum(-log(p))) = exp(-dist)
        double reliability = Math.exp(-dist[dst]);

        LinkedList<Integer> path = new LinkedList<>();
        for (int v = dst; v != -1; v = parent[v]) path.addFirst(v);

        return new Result(reliability, path);
    }
    private static void addUndirected(List<List<Edge>> g, int a, int b, double p) {
        g.get(a).add(new Edge(b, p));
        g.get(b).add(new Edge(a, p));
    }
}