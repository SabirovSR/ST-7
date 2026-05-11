package ru.unn.cs.st7;

import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

public final class Task2 {

    private static final String IPIFY_URL = "https://api.ipify.org/?format=json";

    private Task2() {
    }

    public static void run(WebDriver driver) throws Exception {
        System.out.println("--- Задание 2: ipify ---");
        JSONObject json = JsonFromPage.fetchJson(driver, IPIFY_URL);
        String ip = String.valueOf(json.get("ip"));
        System.out.println(ip);
    }
}
