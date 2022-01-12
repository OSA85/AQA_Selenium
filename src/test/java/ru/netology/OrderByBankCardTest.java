package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderByBankCardTest {


    private WebDriver driver;

    @BeforeAll
    public static void setUpAll(){
        WebDriverManager.chromedriver().setup();
//        System.setProperty("webdriver.chrome.driver","./driver/win/chromedriver.exe");
    }

    @BeforeEach
    public void setUp(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown(){
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSendForm (){
        driver.get("http://localhost:9999");
        List<WebElement> textFields = driver.findElements(By.className("input__control")); //поиск всех полей с именем таким класса
        textFields.get(0).sendKeys("Малыш Барбоскин");
        textFields.get(1).sendKeys("+77777777777");
        driver.findElement(By.className("checkbox__box")).click();//согласие на обработку данных
        driver.findElement(By.tagName("button")).click(); //отправить
        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals(expectedText, actualText);
    }



}
