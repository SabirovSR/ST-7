package ru.unn.cs.st7;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public final class Task3 {

    public static final String METEO_URL =
            "https://api.open-meteo.com/v1/forecast?latitude=56&longitude=44&hourly=temperature_2m,rain"
                    + "&current=cloud_cover&timezone=Europe%2FMoscow&forecast_days=1&wind_speed_unit=ms";

    private Task3() {
    }

    public static void run(WebDriver driver) throws Exception {
        run(driver, Paths.get("result", "forecast.txt"));
    }

    public static void run(WebDriver driver, Path forecastFile) throws Exception {
        System.out.println("--- Задание 3: прогноз Open-Meteo (Нижний Новгород) ---");
        JSONObject root = JsonFromPage.fetchJson(driver, METEO_URL);
        JSONObject hourly = (JSONObject) root.get("hourly");
        JSONArray times = (JSONArray) hourly.get("time");
        JSONArray temps = (JSONArray) hourly.get("temperature_2m");
        JSONArray rains = (JSONArray) hourly.get("rain");

        String table = formatForecastTable(times, temps, rains);
        System.out.print(table);

        Path parent = forecastFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(forecastFile, table.getBytes(StandardCharsets.UTF_8));
    }

    private static String formatForecastTable(JSONArray times, JSONArray temps, JSONArray rains) {
        final String h0 = "№";
        final String h1 = "Дата/время";
        final String h2 = "Температура";
        final String h3 = "Осадки (мм)";

        int rows = times.size();
        int w0 = h0.length();
        int w1 = h1.length();
        int w2 = h2.length();
        int w3 = h3.length();

        for (int i = 0; i < rows; i++) {
            w0 = Math.max(w0, String.valueOf(i + 1).length());
            w1 = Math.max(w1, String.valueOf(times.get(i)).length());
            w2 = Math.max(w2, String.valueOf(temps.get(i)).length());
            w3 = Math.max(w3, String.valueOf(rains.get(i)).length());
        }

        String sep = horizontalRule(w0, w1, w2, w3);
        StringBuilder sb = new StringBuilder();
        sb.append(sep).append('\n');
        sb.append(dataRow(h0, h1, h2, h3, w0, w1, w2, w3, false, false, true, true)).append('\n');
        sb.append(sep).append('\n');
        for (int i = 0; i < rows; i++) {
            sb.append(dataRow(
                    String.valueOf(i + 1),
                    String.valueOf(times.get(i)),
                    String.valueOf(temps.get(i)),
                    String.valueOf(rains.get(i)),
                    w0, w1, w2, w3,
                    false, false, true, true)).append('\n');
        }
        sb.append(sep).append('\n');
        return sb.toString();
    }

    private static String horizontalRule(int w0, int w1, int w2, int w3) {
        return "+" + repeatDash(w0 + 2)
                + "+" + repeatDash(w1 + 2)
                + "+" + repeatDash(w2 + 2)
                + "+" + repeatDash(w3 + 2)
                + "+";
    }

    private static String repeatDash(int len) {
        StringBuilder b = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            b.append('-');
        }
        return b.toString();
    }

    /** Строка данных: пробел после «|», выравнивание чисел в столбцах температуры и осадков вправо. */
    private static String dataRow(
            String c0, String c1, String c2, String c3,
            int w0, int w1, int w2, int w3,
            boolean right0, boolean right1, boolean right2, boolean right3) {
        return "| " + cell(c0, w0, right0)
                + " | " + cell(c1, w1, right1)
                + " | " + cell(c2, w2, right2)
                + " | " + cell(c3, w3, right3)
                + " |";
    }

    private static String cell(String value, int width, boolean rightAlign) {
        if (rightAlign) {
            return String.format(Locale.US, "%" + width + "s", value);
        }
        return String.format(Locale.US, "%-" + width + "s", value);
    }
}
