package coursework.Q5;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Nishan Rai
 * Weather App GUI (Swing) - Nepal Cities
 *
 * Requirements:
 * - Fetch weather for multiple Nepal cities
 * - Compare Sequential approach vs Multithreaded approach
 * - Display results in a table
 * - Display timing in GUI
 *
 * API used:
 * - OpenWeatherMap Current Weather endpoint
 *
 * Time Complexity:
 * - Sequential: O(n) network calls in series
 * - Multithreaded: O(n) calls in parallel (faster wall-time)
 *
 * Note:
 * - Must update Swing UI on the Event Dispatch Thread (EDT).
 */
public final class WeatherAppGUI extends JFrame {

    // Please Put your OpenWeather API key here
    private static final String API_KEY = "Your_API_here";

    // 5 cities in Nepal
    private static final List<String> CITIES = List.of(
            "Kathmandu,NP", "Pokhara,NP", "Lalitpur,NP", "Biratnagar,NP", "Birgunj,NP"
    );

    private final DefaultTableModel model =
            new DefaultTableModel(new Object[]{"City", "Temp(C)", "Condition", "Humidity", "Wind(m/s)"}, 0);

    private final JTable table = new JTable(model);
    private final JTextArea log = new JTextArea(8, 55);

    public WeatherAppGUI() {
        super("Weather App (Sequential vs Multithreaded)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton seqBtn = new JButton("Fetch Sequential");
        JButton mtBtn = new JButton("Fetch Multi-threaded");

        seqBtn.addActionListener(e -> fetchSequential());
        mtBtn.addActionListener(e -> fetchMultithreaded());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(seqBtn);
        top.add(mtBtn);

        log.setEditable(false);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(new JScrollPane(log), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void clearTable() {
        while (model.getRowCount() > 0) model.removeRow(0);
    }

    /**
     * Sequential weather fetching (one city at a time).
     */
    private void fetchSequential() {
        clearTable();
        log.setText("");

        long start = System.nanoTime();

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        for (String city : CITIES) {
            try {
                WeatherRow row = fetchCity(client, city);
                model.addRow(row.toRow());
                log.append("OK: " + city + "\n");
            } catch (Exception ex) {
                log.append("FAIL: " + city + " -> " + ex.getMessage() + "\n");
            }
        }

        long end = System.nanoTime();
        log.append("\nSequential time: " + ((end - start) / 1_000_000) + " ms\n");
    }

    /**
     * Multithreaded weather fetching (parallel using thread pool).
     */
    private void fetchMultithreaded() {
        clearTable();
        log.setText("");

        long start = System.nanoTime();

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        ExecutorService pool = Executors.newFixedThreadPool(5);
        CompletionService<WeatherRow> cs = new ExecutorCompletionService<>(pool);

        // submit tasks
        for (String city : CITIES) {
            cs.submit(() -> fetchCity(client, city));
        }

        // collect results
        for (int i = 0; i < CITIES.size(); i++) {
            try {
                WeatherRow row = cs.take().get();

                // Swing must update table on EDT
                SwingUtilities.invokeLater(() -> model.addRow(row.toRow()));
                SwingUtilities.invokeLater(() -> log.append("OK: " + row.city + "\n"));

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> log.append("FAIL -> " + ex.getMessage() + "\n"));
            }
        }

        pool.shutdown();

        long end = System.nanoTime();
        log.append("\nMultithreaded time: " + ((end - start) / 1_000_000) + " ms\n");
    }

    /**
     * Holds one city's parsed weather result.
     */
    private static class WeatherRow {
        String city;
        double tempC;
        String condition;
        int humidity;
        double wind;

        Object[] toRow() {
            return new Object[]{city, tempC, condition, humidity, wind};
        }
    }

    /**
     * Fetch and parse weather for a city using OpenWeatherMap.
     */
    private WeatherRow fetchCity(HttpClient client, String city) throws Exception {

        if (API_KEY == null || API_KEY.isBlank() || API_KEY.equals("Your_API_here")) {
            throw new IllegalStateException("Set your OpenWeather API key in API_KEY.");
        }

        // Encode city safely (handles commas/spaces)
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);

        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + encodedCity + "&appid=" + API_KEY + "&units=metric";

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

        if (resp.statusCode() != 200) {
            throw new RuntimeException("HTTP " + resp.statusCode() + ": " + resp.body());
        }

        JsonObject obj = JsonParser.parseString(resp.body()).getAsJsonObject();

        WeatherRow row = new WeatherRow();
        row.city = obj.get("name").getAsString();

        JsonObject main = obj.getAsJsonObject("main");
        row.tempC = main.get("temp").getAsDouble();
        row.humidity = main.get("humidity").getAsInt();

        JsonArray weather = obj.getAsJsonArray("weather");
        row.condition = weather.get(0).getAsJsonObject().get("main").getAsString();

        JsonObject wind = obj.getAsJsonObject("wind");
        row.wind = wind.get("speed").getAsDouble();

        return row;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WeatherAppGUI().setVisible(true));
    }
}