package coursework;

import coursework.Q1.MaxPointsOnLine;
import coursework.Q1b.WordBreakAll;
import coursework.Q2.MaxPathSumTree;
import coursework.Q3.MaxProfitKTransactions;
import coursework.Q4.SmartGridOptimizer;
import coursework.Q4.SmartGridOptimizer.Source;
import coursework.Q4.SmartGridOptimizer.HourResult;
import coursework.Task6.SafestPathReliability;
import coursework.Task6b.EdmondsKarpMaxFlow;

import java.util.*;

/**
 * Main entry point for the coursework project.
 *
 * This class demonstrates running:
 * - Q1: Max Points on a Line
 * - Q1(b): Word Break
 * - Q2: Max Path Sum (Binary Tree)
 * - Q3: Max Profit with K Transactions
 * - Q4: Smart Grid Optimization
 * - Task 6(a): Safest Path
 * - Task 6(b): Maximum Flow
 **/
public class Main {

    public static void main(String[] args) {

        // ==================================================
        // Q1: Max Points on a Line
        // ==================================================
        System.out.println("========== Q1: Max Points on a Line ==========");

        int[][] points2 = {
                {1, 1},
                {3, 2},
                {5, 3},
                {4, 1},
                {2, 3},
                {1, 4}
        };

        int ans2 = MaxPointsOnLine.maxPoints(points2);

        System.out.println("Input: " + deepToString(points2));
        System.out.println("Max points on same line = " + ans2); // Expected: 4


        // ==================================================
        // Q1(b): Word Break (All ways)
        // ==================================================
        System.out.println("\n========== Q1(b): Word Break ==========");

        String s1 = "nepaltrekkingguide";
        List<String> dict1 = Arrays.asList("nepal", "trekking", "guide", "nepaltrekking");

        List<String> sentences1 = WordBreakAll.wordBreak(s1, dict1);

        System.out.println("String: " + s1);
        System.out.println("All possible sentences:");
        for (String sentence : sentences1) {
            System.out.println(" - " + sentence);
        }


        // ==================================================
        // Q2: Maximum Path Sum in Binary Tree
        // ==================================================
        System.out.println("\n========== Q2: Maximum Path Sum ==========");

        MaxPathSumTree.TreeNode root =
                new MaxPathSumTree.TreeNode(-10);
        root.left = new MaxPathSumTree.TreeNode(9);
        root.right = new MaxPathSumTree.TreeNode(20);
        root.right.left = new MaxPathSumTree.TreeNode(15);
        root.right.right = new MaxPathSumTree.TreeNode(7);

        int maxPath = MaxPathSumTree.maxPathSum(root);
        System.out.println("Maximum Path Sum = " + maxPath); // Expected: 42


        // ==================================================
        // Q3: Max Profit with K Transactions
        // ==================================================
        System.out.println("\n========== Q3: Max Profit with K Transactions ==========");

        int[] prices = {3, 2, 6, 5, 0, 3};
        int k = 2;

        int profit = MaxProfitKTransactions.maxProfit(k, prices);

        System.out.println("Prices: " + Arrays.toString(prices));
        System.out.println("K = " + k);
        System.out.println("Maximum Profit = " + profit); // Expected: 7


        // ==================================================
        // Q4: Smart Grid Optimization
        // ==================================================
        System.out.println("\n========== Q4: Smart Grid Optimization ==========");

        Map<String, Double> demand = new LinkedHashMap<>();
        demand.put("A", 20.0);
        demand.put("B", 15.0);
        demand.put("C", 25.0);

        List<Source> sources = List.of(
                new Source("S1", "Solar", 50, 6, 18, 1.0),
                new Source("S2", "Hydro", 40, 0, 24, 1.5),
                new Source("S3", "Diesel", 60, 17, 23, 3.0)
        );

        HourResult result = SmartGridOptimizer.allocateHour(6, demand, sources, 0.10);

        System.out.println("Hour: " + result.hour);
        System.out.println("Total Cost: " + result.totalCost);
        System.out.println("Renewable %: " + String.format("%.2f", result.renewablePct));
        System.out.println("Diesel Used: " + result.dieselKwh);
        System.out.println("Allocations: " + result.districtAlloc);


        // ==================================================
        // Task 6(a): Safest Path
        // ==================================================
        System.out.println("\n========== Task 6(a): Safest Path ==========");

        int n = 5;
        List<List<SafestPathReliability.Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());

        addUndirected(graph, 0, 1, 0.9);
        addUndirected(graph, 1, 2, 0.8);
        addUndirected(graph, 2, 3, 0.9);
        addUndirected(graph, 3, 4, 0.7);

        SafestPathReliability.Result safest =
                SafestPathReliability.safestPath(n, graph, 0, 4);

        System.out.println("Safest Path Reliability = " + safest.reliability);
        System.out.println("Path = " + safest.path);


        // ==================================================
        // Task 6(b): Maximum Flow
        // ==================================================
        System.out.println("\n========== Task 6(b): Maximum Flow ==========");

        int[][] capacity = new int[6][6];
        capacity[0][1] = 16;
        capacity[0][2] = 13;
        capacity[1][3] = 12;
        capacity[2][4] = 14;
        capacity[3][5] = 20;
        capacity[4][5] = 4;

        int maxFlow = EdmondsKarpMaxFlow.maxFlow(6, capacity, 0, 5);

        System.out.println("Maximum Flow = " + maxFlow);


        System.out.println("\n===== All Questions Executed Successfully =====");
    }

    private static void addUndirected(List<List<SafestPathReliability.Edge>> g,
                                      int a, int b, double p) {
        g.get(a).add(new SafestPathReliability.Edge(b, p));
        g.get(b).add(new SafestPathReliability.Edge(a, p));
    }

    private static String deepToString(int[][] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(Arrays.toString(arr[i]));
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}