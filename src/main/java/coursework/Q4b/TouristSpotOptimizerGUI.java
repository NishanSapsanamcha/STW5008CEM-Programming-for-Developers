package coursework.Q4b;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Tourist Spot Optimizer GUI (Swing).
 *
 * Problem summary:
 * - A tourist has limited Budget and Max Time.
 * - Each spot has: cost, time needed, and rating.
 * - Select a set of spots that maximizes total rating while staying within constraints.
 *
 * Requirements (assignment style):
 * - Implement a heuristic approach (Greedy)
 * - Compare with brute force (exact) for validation
 *
 * Heuristic:
 * - Sort by rating/time ratio (best "value per hour") and pick if it fits.
 *
 * Brute force:
 * - Try all subsets (only feasible if number of spots is small).
 *
 * Time Complexity:
 * - Greedy: O(n log n)
 * - Brute force: O(2^n) (only for small n)
 */
public final class TouristSpotOptimizerGUI extends JFrame {

    /**
     * Data model for a tourist spot.
     */
    private static class Spot {
        String name;
        int cost;
        int time;
        double rating;

        Spot(String name, int cost, int time, double rating) {
            this.name = name;
            this.cost = cost;
            this.time = time;
            this.rating = rating;
        }
    }

    private final DefaultTableModel model =
            new DefaultTableModel(new Object[]{"Name", "Cost", "Time", "Rating"}, 0);
    private final JTable table = new JTable(model);

    private final JTextField nameF = new JTextField(10);
    private final JTextField costF = new JTextField(5);
    private final JTextField timeF = new JTextField(5);
    private final JTextField ratingF = new JTextField(5);

    private final JTextField budgetF = new JTextField("50", 6);
    private final JTextField maxTimeF = new JTextField("8", 6);

    private final JTextArea output = new JTextArea(12, 45);

    public TouristSpotOptimizerGUI() {
        super("Tourist Spot Optimizer (Heuristic vs Brute Force)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // --- Top panel: add new spot
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Name:")); top.add(nameF);
        top.add(new JLabel("Cost:")); top.add(costF);
        top.add(new JLabel("Time:")); top.add(timeF);
        top.add(new JLabel("Rating:")); top.add(ratingF);

        JButton addBtn = new JButton("Add Spot");
        addBtn.addActionListener(e -> addSpot());
        top.add(addBtn);

        // --- Middle panel: constraints + run
        JPanel mid = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mid.add(new JLabel("Budget:")); mid.add(budgetF);
        mid.add(new JLabel("Max Time:")); mid.add(maxTimeF);

        JButton runBtn = new JButton("Run Optimizer");
        runBtn.addActionListener(e -> runOptimizer());
        mid.add(runBtn);

        // --- Output area
        output.setEditable(false);

        // Layout
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(mid, BorderLayout.NORTH);
        bottom.add(new JScrollPane(output), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // Sample dataset (good for screenshots)
        model.addRow(new Object[]{"Pashupatinath", 10, 2, 4.7});
        model.addRow(new Object[]{"Swayambhunath", 8, 2, 4.6});
        model.addRow(new Object[]{"Boudhanath", 7, 2, 4.5});
        model.addRow(new Object[]{"Durbar Square", 12, 3, 4.4});

        pack();
        setLocationRelativeTo(null);
    }

    private void addSpot() {
        try {
            String n = nameF.getText().trim();
            int c = Integer.parseInt(costF.getText().trim());
            int t = Integer.parseInt(timeF.getText().trim());
            double r = Double.parseDouble(ratingF.getText().trim());

            if (n.isEmpty()) throw new IllegalArgumentException();

            model.addRow(new Object[]{n, c, t, r});

            nameF.setText("");
            costF.setText("");
            timeF.setText("");
            ratingF.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input! Enter: name, integer cost, integer time, double rating.");
        }
    }

    private List<Spot> readSpots() {
        List<Spot> spots = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            String n = model.getValueAt(i, 0).toString();
            int c = Integer.parseInt(model.getValueAt(i, 1).toString());
            int t = Integer.parseInt(model.getValueAt(i, 2).toString());
            double r = Double.parseDouble(model.getValueAt(i, 3).toString());
            spots.add(new Spot(n, c, t, r));
        }
        return spots;
    }

    private void runOptimizer() {
        try {
            int budget = Integer.parseInt(budgetF.getText().trim());
            int maxTime = Integer.parseInt(maxTimeF.getText().trim());

            List<Spot> spots = readSpots();

            List<Spot> greedy = greedyPick(spots, budget, maxTime);
            List<Spot> exact = bruteForcePick(spots, budget, maxTime);

            output.setText("");
            output.append("Heuristic (Greedy by rating/time):\n");
            printList(greedy, budget, maxTime);

            output.append("\nBrute Force (Optimal, for small n):\n");
            printList(exact, budget, maxTime);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid budget/maxTime values.");
        }
    }

    private void printList(List<Spot> list, int budget, int maxTime) {
        int totalCost = 0, totalTime = 0;
        double totalRating = 0;

        for (Spot s : list) {
            output.append(" - " + s.name + " (cost=" + s.cost + ", time=" + s.time + ", rating=" + s.rating + ")\n");
            totalCost += s.cost;
            totalTime += s.time;
            totalRating += s.rating;
        }

        output.append("Total cost=" + totalCost + "/" + budget +
                ", Total time=" + totalTime + "/" + maxTime +
                ", Total rating=" + String.format("%.2f", totalRating) + "\n");
    }

    /**
     * Greedy heuristic:
     * Sort spots by rating/time ratio (high to low), then pick if it fits.
     */
    private List<Spot> greedyPick(List<Spot> spots, int budget, int maxTime) {
        List<Spot> sorted = new ArrayList<>(spots);
        sorted.sort(Comparator.comparingDouble(s -> -(s.rating / s.time)));

        List<Spot> pick = new ArrayList<>();
        int cost = 0, time = 0;

        for (Spot s : sorted) {
            if (cost + s.cost <= budget && time + s.time <= maxTime) {
                pick.add(s);
                cost += s.cost;
                time += s.time;
            }
        }

        return pick;
    }

    /**
     * Brute force (exact):
     * Try all subsets and choose the one with max rating under constraints.
     * If n is too large, fallback to greedy to avoid explosion.
     */
    private List<Spot> bruteForcePick(List<Spot> spots, int budget, int maxTime) {
        int n = spots.size();
        if (n > 25) return greedyPick(spots, budget, maxTime);

        double bestRating = -1;
        int bestMask = 0;

        for (int mask = 0; mask < (1 << n); mask++) {
            int cost = 0, time = 0;
            double rating = 0;

            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    Spot s = spots.get(i);
                    cost += s.cost;
                    time += s.time;
                    rating += s.rating;

                    if (cost > budget || time > maxTime) break;
                }
            }

            if (cost <= budget && time <= maxTime && rating > bestRating) {
                bestRating = rating;
                bestMask = mask;
            }
        }

        List<Spot> pick = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if ((bestMask & (1 << i)) != 0) pick.add(spots.get(i));
        }
        return pick;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TouristSpotOptimizerGUI().setVisible(true));
    }
}