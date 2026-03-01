package coursework.Q2;

/**
 * This class calculates the maximum path sum in a binary tree.
 *
 * @author Nishan Rai
 * A path can start and end at any node.
 *
 * Approach:
 * Use DFS.
 * At each node:
 * - compute max gain from left and right
 * - update global maximum
 *
 * Time Complexity: O(n)
 * Space Complexity: O(h) recursion stack
 */
public final class MaxPathSumTree {

    private MaxPathSumTree() { }

    public static class TreeNode {
        public int val;
        public TreeNode left, right;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    private static int maxSum;

    public static int maxPathSum(TreeNode root) {
        maxSum = Integer.MIN_VALUE;
        dfs(root);
        return maxSum;
    }

    private static int dfs(TreeNode node) {
        if (node == null) return 0;

        int left = Math.max(0, dfs(node.left));
        int right = Math.max(0, dfs(node.right));

        maxSum = Math.max(maxSum, node.val + left + right);

        return node.val + Math.max(left, right);
    }
}