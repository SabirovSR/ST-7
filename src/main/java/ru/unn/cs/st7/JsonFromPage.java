package ru.unn.cs.st7;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class JsonFromPage {

    private JsonFromPage() {
    }

    public static JSONObject fetchJson(WebDriver driver, String url) throws Exception {
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("pre")));
        String json = driver.findElement(By.tagName("pre")).getText();
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(json);
    }
}
