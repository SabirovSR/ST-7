package ru.unn.cs.st7;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class App {

    private static final String PASSWORD_PAGE = "https://www.calculator.net/password-generator.html";

    public static void main(String[] args) {
        WebDriver driver = WebDriverFactory.createChrome();
        try {
            runPasswordGenerator(driver);
            Task2.run(driver);
            Task3.run(driver);
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.toString());
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    static void runPasswordGenerator(WebDriver driver) throws Exception {
        System.out.println("--- Задание 1: генератор пароля ---");
        driver.get(PASSWORD_PAGE);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement generate = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[type='submit'][name='submit1']")));
        generate.click();
        String password = wait.until(d -> {
            try {
                WebElement bold = d.findElement(By.cssSelector("#resultid .verybigtext b"));
                String text = bold.getText();
                return text != null && !text.trim().isEmpty() ? text.trim() : null;
            } catch (StaleElementReferenceException | NoSuchElementException e) {
                return null;
            }
        });
        System.out.println(password);
    }
}
