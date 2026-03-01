package coursework.Q3;

/**
 * This class solves the stock buy-sell problem
 * with at most K transactions.
 *
 * @author Nishan Rai
 * Approach:
 * Dynamic Programming using two arrays:
 * buy[t] and sell[t]
 *
 * Time Complexity: O(n * k)
 * Space Complexity: O(k)
 */
public final class MaxProfitKTransactions {

    private MaxProfitKTransactions() { }

    public static int maxProfit(int k, int[] prices) {

        if (prices.length == 0 || k == 0) return 0;

        int n = prices.length;

        if (k >= n / 2) {
            int profit = 0;
            for (int i = 1; i < n; i++)
                if (prices[i] > prices[i - 1])
                    profit += prices[i] - prices[i - 1];
            return profit;
        }

        int[] buy = new int[k + 1];
        int[] sell = new int[k + 1];

        for (int i = 0; i <= k; i++)
            buy[i] = Integer.MIN_VALUE;

        for (int price : prices) {
            for (int t = 1; t <= k; t++) {
                buy[t] = Math.max(buy[t], sell[t - 1] - price);
                sell[t] = Math.max(sell[t], buy[t] + price);
            }
        }

        return sell[k];
    }
}