package coursework.Q1;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Nishan Rai
 * This class solves the "Max Points on a Line" problem.
 *
 * The goal is to determine the maximum number of points
 * that lie on the same straight line.
 *
 * Approach:
 * For each point, compute slope with every other point.
 * Store slope frequency using a HashMap.
 * The maximum frequency + duplicates gives result.
 *
 * Time Complexity: O(n^2)
 * Space Complexity: O(n)
 */
public final class MaxPointsOnLine {

    private MaxPointsOnLine() { }

    public static int maxPoints(int[][] points) {
        int n = points.length;
        if (n <= 2) return n;

        int result = 1;

        for (int i = 0; i < n; i++) {
            Map<Slope, Integer> map = new HashMap<>();
            int duplicates = 0;
            int best = 0;

            int x1 = points[i][0];
            int y1 = points[i][1];

            for (int j = i + 1; j < n; j++) {
                int dx = points[j][0] - x1;
                int dy = points[j][1] - y1;

                // same point
                if (dx == 0 && dy == 0) {
                    duplicates++;
                    continue;
                }

                // reduce fraction dy/dx
                int g = gcd(dx, dy);
                dx /= g;
                dy /= g;

                // --- NORMALIZE DIRECTION ---
                // Make dx positive OR if dx==0 then make dy positive
                if (dx < 0) {
                    dx = -dx;
                    dy = -dy;
                } else if (dx == 0) {
                    // vertical line: represent all as (0,1)
                    dy = 1;
                } else if (dy == 0) {
                    // horizontal line: represent all as (1,0)
                    dx = 1;
                }
                // ---------------------------

                Slope slope = new Slope(dx, dy);
                int count = map.getOrDefault(slope, 0) + 1;
                map.put(slope, count);
                best = Math.max(best, count);
            }

            result = Math.max(result, best + duplicates + 1);
        }

        return result;
    }

    private static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        if (a == 0 && b == 0) return 1;
        if (a == 0) return b;
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    private static final class Slope {
        final int dx, dy;

        Slope(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Slope)) return false;
            Slope s = (Slope) o;
            return dx == s.dx && dy == s.dy;
        }

        @Override
        public int hashCode() {
            return Objects.hash(dx, dy);
        }
    }
}