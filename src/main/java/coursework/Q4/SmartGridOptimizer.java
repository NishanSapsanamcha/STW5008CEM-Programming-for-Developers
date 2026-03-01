package coursework.Q4;

import java.util.*;

/**
 * @author Nishan Rai
 * This class implements a simplified Smart Grid optimization algorithm.
 *
 * Problem summary:
 * - Each hour has electricity demand per district (A/B/C etc.).
 * - We have multiple energy sources (Solar/Hydro/Diesel), each with:
 *   capacity, availability window, and unit cost.
 * - We must allocate energy to districts so that demand is met within ±10%.
 * - We should prioritize cheaper sources first (greedy).
 * - If greedy distribution violates ±10% bounds, we attempt a redistribution
 *   of already-used energy to satisfy the constraints.
 *
 * Notes:
 * - This is a practical, assignment-friendly approach:
 *   Greedy (min-cost) + feasibility adjustment.
 *
 * Time Complexity per hour:
 *   Sorting sources: O(S log S)
 *   Allocation: O(S * D)
 * Space Complexity:
 *   O(S + D + allocations)
 */
public final class SmartGridOptimizer {

    private SmartGridOptimizer() {
    }

    /**
     * Represents one energy source.
     */
    public static class Source {
        public final String id;
        public final String type;        // Solar, Hydro, Diesel
        public final double capacity;    // kWh available per hour
        public final int startHour;      // inclusive
        public final int endHour;        // inclusive
        public final double cost;        // per kWh

        public Source(String id, String type, double capacity, int startHour, int endHour, double cost) {
            this.id = id;
            this.type = type;
            this.capacity = capacity;
            this.startHour = startHour;
            this.endHour = endHour;
            this.cost = cost;
        }

        public boolean available(int hour) {
            return startHour <= hour && hour <= endHour;
        }
    }

    /**
     * Result object for one hour allocation.
     */
    public static class HourResult {
        public final int hour;
        public final Map<String, Map<String, Double>> districtAlloc; // district -> (sourceId -> kWh)
        public final double totalCost;
        public final double renewablePct;
        public final double dieselKwh;

        public HourResult(int hour,
                          Map<String, Map<String, Double>> districtAlloc,
                          double totalCost,
                          double renewablePct,
                          double dieselKwh) {
            this.hour = hour;
            this.districtAlloc = districtAlloc;
            this.totalCost = totalCost;
            this.renewablePct = renewablePct;
            this.dieselKwh = dieselKwh;
        }
    }

    /**
     * Allocate energy for one hour.
     *
     * @param hour    hour of the day
     * @param demand  district -> demand(kWh)
     * @param sources list of energy sources
     * @param flex    allowed flexibility (e.g. 0.10 means ±10%)
     * @return HourResult with allocations and summary stats
     */
    public static HourResult allocateHour(int hour,
                                          Map<String, Double> demand,
                                          List<Source> sources,
                                          double flex) {

        // Sort districts for stable output
        List<String> districts = new ArrayList<>(demand.keySet());
        Collections.sort(districts);

        // Lower/upper demand bounds
        Map<String, Double> lo = new HashMap<>();
        Map<String, Double> hi = new HashMap<>();
        for (String d : districts) {
            lo.put(d, demand.get(d) * (1.0 - flex));
            hi.put(d, demand.get(d) * (1.0 + flex));
        }

        // allocation[district][sourceId] = kWh
        Map<String, Map<String, Double>> alloc = new LinkedHashMap<>();
        for (String d : districts) alloc.put(d, new LinkedHashMap<>());

        // Filter available sources and sort by cost (cheapest first)
        List<Source> available = new ArrayList<>();
        for (Source s : sources) if (s.available(hour)) available.add(s);
        available.sort(Comparator.comparingDouble(s -> s.cost));

        // -------------------------
        // Step 1: Greedy allocation
        // -------------------------
        for (Source s : available) {
            double remaining = s.capacity;

            for (String d : districts) {
                if (remaining <= 0) break;

                double used = sum(alloc.get(d));
                double need = demand.get(d) - used;

                if (need <= 0) continue;

                double take = Math.min(need, remaining);
                if (take > 0) {
                    alloc.get(d).put(s.id, alloc.get(d).getOrDefault(s.id, 0.0) + take);
                    remaining -= take;
                }
            }
        }

        // --------------------------------------------------------
        // Step 2: If outside ±10%, attempt redistribution of used energy
        // --------------------------------------------------------
        if (!withinBounds(districts, alloc, lo, hi)) {
            // totalFromSource[sourceId] = total kWh used from that source (across all districts)
            Map<String, Double> totalFromSource = new LinkedHashMap<>();
            for (String d : districts) {
                for (Map.Entry<String, Double> e : alloc.get(d).entrySet()) {
                    totalFromSource.put(e.getKey(), totalFromSource.getOrDefault(e.getKey(), 0.0) + e.getValue());
                }
            }

            // Clear allocations and redistribute totals more carefully
            for (String d : districts) alloc.get(d).clear();

            for (Map.Entry<String, Double> entry : totalFromSource.entrySet()) {
                String sid = entry.getKey();
                double remaining = entry.getValue();

                // First pass: fill towards exact demand
                for (String d : districts) {
                    if (remaining <= 0) break;

                    double used = sum(alloc.get(d));
                    double need = demand.get(d) - used;
                    double take = Math.min(Math.max(0.0, need), remaining);

                    if (take > 0) {
                        alloc.get(d).put(sid, alloc.get(d).getOrDefault(sid, 0.0) + take);
                        remaining -= take;
                    }
                }

                // Second pass: fill up to upper bound
                for (String d : districts) {
                    if (remaining <= 0) break;

                    double used = sum(alloc.get(d));
                    double room = hi.get(d) - used;
                    double take = Math.min(Math.max(0.0, room), remaining);

                    if (take > 0) {
                        alloc.get(d).put(sid, alloc.get(d).getOrDefault(sid, 0.0) + take);
                        remaining -= take;
                    }
                }
            }
        }

        // -------------------------
        // Step 3: Compute summary
        // -------------------------
        Map<String, Double> costMap = new HashMap<>();
        Map<String, String> typeMap = new HashMap<>();
        for (Source s : available) {
            costMap.put(s.id, s.cost);
            typeMap.put(s.id, s.type);
        }

        double totalCost = 0.0;
        double totalUsed = 0.0;
        double dieselUsed = 0.0;
        double renewableUsed = 0.0;

        for (String d : districts) {
            for (Map.Entry<String, Double> e : alloc.get(d).entrySet()) {
                String sid = e.getKey();
                double v = e.getValue();

                totalCost += v * costMap.getOrDefault(sid, 0.0);
                totalUsed += v;

                if ("Diesel".equalsIgnoreCase(typeMap.getOrDefault(sid, ""))) dieselUsed += v;
                else renewableUsed += v;
            }
        }

        double renewablePct = (totalUsed == 0) ? 0.0 : (renewableUsed / totalUsed) * 100.0;

        return new HourResult(hour, alloc, totalCost, renewablePct, dieselUsed);
    }

    private static double sum(Map<String, Double> m) {
        double s = 0;
        for (double v : m.values()) s += v;
        return s;
    }

    private static boolean withinBounds(List<String> districts,
                                        Map<String, Map<String, Double>> alloc,
                                        Map<String, Double> lo,
                                        Map<String, Double> hi) {
        for (String d : districts) {
            double used = sum(alloc.get(d));
            if (used < lo.get(d) - 1e-9 || used > hi.get(d) + 1e-9) return false;
        }
        return true;
    }
}