package coursework;

import coursework.Q1.MaxPointsOnLine;
import coursework.Q1b.WordBreakAll;

import java.util.Arrays;
import java.util.List;

/**
 * Main entry point for the coursework project.
 *
 * This class demonstrates running:
 * - Q1: Max Points on a Line
 * - Q1(b): Word Break (All possible sentences)
 */
public class Main {

    public static void main(String[] args) {

        // -----------------------------
        // Q1: Max Points on a Line
        // -----------------------------
        System.out.println("========== Q1: Max Points on a Line ==========");

        int[][] points1 = {
                {1, 1},
                {2, 2},
                {3, 3}
        };

        int[][] points2 = {
                {1, 1},
                {3, 2},
                {5, 3},
                {4, 1},
                {2, 3},
                {1, 4}
        };

        int ans1 = MaxPointsOnLine.maxPoints(points1);
        int ans2 = MaxPointsOnLine.maxPoints(points2);

        System.out.println("Input 1: " + deepToString(points1));
        System.out.println("Max points on same line = " + ans1);

        System.out.println();

        System.out.println("Input 2: " + deepToString(points2));
        System.out.println("Max points on same line = " + ans2);

        // -----------------------------
        // Q1(b): Word Break (All ways)
        // -----------------------------
        System.out.println("\n========== Q1(b): Word Break (All Sentences) ==========");

        String s1 = "nepaltrekkingguide";
        List<String> dict1 = Arrays.asList("nepal", "trekking", "guide", "nepaltrekking");

        List<String> sentences1 = WordBreakAll.wordBreak(s1, dict1);

        System.out.println("String: " + s1);
        System.out.println("Dictionary: " + dict1);
        System.out.println("All possible sentences:");
        for (String sentence : sentences1) {
            System.out.println(" - " + sentence);
        }

        System.out.println();

        String s2 = "everesthikingtrail";
        List<String> dict2 = Arrays.asList("everest", "hiking", "trek");

        List<String> sentences2 = WordBreakAll.wordBreak(s2, dict2);

        System.out.println("String: " + s2);
        System.out.println("Dictionary: " + dict2);
        System.out.println("All possible sentences:");
        if (sentences2.isEmpty()) {
            System.out.println(" - No valid sentence can be formed.");
        } else {
            for (String sentence : sentences2) {
                System.out.println(" - " + sentence);
            }
        }

        System.out.println("\n===== Done all executed =====");
    }

    /**
     * Utility method to print 2D int array like [[1,1],[2,2]].
     */
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