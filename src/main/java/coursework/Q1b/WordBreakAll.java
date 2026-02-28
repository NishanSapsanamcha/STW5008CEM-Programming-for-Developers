package coursework.Q1b;

import java.util.*;

/**
 * @author Nishan Rai
 * This class solves the Word Break II problem.
 *
 * The goal is to return ALL possible sentences that can
 * be formed by breaking the string using words from dictionary.
 *
 * Approach:
 * Use DFS + Memoization.
 * For each starting index, recursively build valid sentences.
 *
 * Time Complexity: Exponential (due to multiple combinations)
 * Space Complexity: O(n * m)
 */
public final class WordBreakAll {

    private WordBreakAll() { }

    public static List<String> wordBreak(String s, List<String> wordDict) {
        return dfs(s, 0, new HashSet<>(wordDict), new HashMap<>());
    }

    private static List<String> dfs(String s, int start,
                                    Set<String> dict,
                                    Map<Integer, List<String>> memo) {

        if (memo.containsKey(start)) return memo.get(start);

        List<String> result = new ArrayList<>();

        if (start == s.length()) {
            result.add("");
            return result;
        }

        for (String word : dict) {
            if (s.startsWith(word, start)) {
                List<String> sub = dfs(s, start + word.length(), dict, memo);
                for (String tail : sub) {
                    result.add(tail.isEmpty() ? word : word + " " + tail);
                }
            }
        }

        memo.put(start, result);
        return result;
    }
}