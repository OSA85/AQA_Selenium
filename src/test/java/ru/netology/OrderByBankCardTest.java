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
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSendForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Малыш Барбоскин");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+77777777777");
        driver.findElement(By.className("checkbox__box")).click();//согласие на обработку данных
        driver.findElement(By.tagName("button")).click(); //отправить
        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void shouldSendFormTwoSecondName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Малыш Барбоскин-Аршавкин");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+77777777777");
        driver.findElement(By.className("checkbox__box")).click();//согласие на обработку данных
        driver.findElement(By.tagName("button")).click(); //отправить
        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void shouldSendFormAllFieldsEmpty() { //задача 2 все поля пустые
        driver.findElement(By.tagName("button")).click(); //отправить
        List<WebElement> textFieldsInputSub = driver.findElements(By.className("input__sub"));
        String expectedText = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector
                ("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expectedText, actual);
    }

    @Test
    public void shouldSendFormWithoutName() { //задача 2 поле без имени
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+77777777777");
        driver.findElement(By.className("checkbox__box")).click();//согласие на обработку данных
        driver.findElement(By.tagName("button")).click(); //отправить
        String expectedText = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector
                ("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expectedText, actual);
    }

    @Test
    public void shouldSendFormNameOnEnglish() { //задача 2 поле имя на английском
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Sherlock Holmes");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+77777777777");
        driver.findElement(By.className("checkbox__box")).click();//согласие на обработку данных
        driver.findElement(By.tagName("button")).click(); //отправить
        List<WebElement> textFieldsInputSub = driver.findElements(By.className("input__sub"));
        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector
                ("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expectedText, actual);
    }

    @Test
    public void shouldSendFormWithoutPhone() { //задача 2 поле без телефона
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Малыш Барбоскин");
        driver.findElement(By.className("checkbox__box")).click();//согласие на обработку данных
        driver.findElement(By.tagName("button")).click(); //отправить
        List<WebElement> textFieldsInputSub = driver.findElements(By.className("input__sub"));
        String expectedText = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector
                ("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expectedText, actual);
    }

    @Test
    public void shouldSendFormWrongNumber() { //задача 2 поле телефона неправильный номер

        List<WebElement> textFields = driver.findElements(By.className("input__control")); //поиск всех полей с именем таким класса
        textFields.get(0).sendKeys("Малыш Барбоскин");
        textFields.get(1).sendKeys("+777777777");
        driver.findElement(By.className("checkbox__box")).click();//согласие на обработку данных
        driver.findElement(By.tagName("button")).click(); //отправить
        List<WebElement> textFieldsInputSub = driver.findElements(By.className("input__sub"));
        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector
                ("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expectedText, actual);
    }

    @Test
    public void shouldSendFormWithoutCheckbox() {   //задача 2 без подтверждения обработки данных
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Малыш Барбоскин");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+77777777777");
        driver.findElement(By.tagName("button")).click(); //отправить
        String expectedText = "Я соглашаюсь с условиями обработки и использования" +
                " моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='agreement']")).getText().trim();
        assertEquals(expectedText, actualText);
    }


}
