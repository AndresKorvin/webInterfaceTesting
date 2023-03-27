package ru.netology;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeleniumNegativeTest {

    private WebDriver driver;


    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }
    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }
    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @ParameterizedTest
    @CsvSource({
            "Alex, +79996665544", // латиница

            "Турчинский~, +79996665544", // спецсимволы
            "Турчинский!, +79996665544", // спецсимволы
            "Турчинский№, +79996665544", // спецсимволы
            "Турчинский;, +79996665544", // спецсимволы
            "Турчинский%, +79996665544", // спецсимволы
            "Турчинский:, +79996665544", // спецсимволы
            "Турчинский?, +79996665544", // спецсимволы
            "Турчинский*, +79996665544", // спецсимволы
            "Турчинский+, +79996665544", // спецсимволы
            "Турчинский=, +79996665544", // спецсимволы
            "Турчинский/, +79996665544", // спецсимволы
            "Турчинский., +79996665544", // спецсимволы

            "123, +79996665544" // цифры
    })
    void shouldNotTakeName(String person, String phone) {

        driver.get("http://localhost:9999/");
        driver.findElement (By.cssSelector("[data-test-id=name] .input__control")).sendKeys(person);
        driver.findElement (By.cssSelector("[data-test-id=phone] .input__control")).sendKeys(phone);
        driver.findElement (By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement (By.cssSelector("button")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "Турчинский, 1", // 1 цифра, без +
            "Турчинский, +1", // 1 цифра

            "Турчинский, 9996665544", // 10 цифр, без +
            "Турчинский, +9996665544", // 10 цифр

            "Турчинский, 799966655443", // 12 цифр, без +
            "Турчинский, +799966655443", // 12 цифр

            "Турчинский, +абвгдежзикл", // кириллица 11 букв с +
            "Турчинский, +qwertyuiopa", // латиница 11 букв с +

            "Турчинский, йцукен", // кириллица
            "Турчинский, qwerty", // латиница

            "Турчинский, +~", // спецсимволы
            "Турчинский, +`", // спецсимволы
            "Турчинский, +!", // спецсимволы
            "Турчинский, +@", // спецсимволы
            "Турчинский, +7 999 999 9999", // пробелы

    })
    void shouldNotTakePhone(String person, String phone) {

        driver.get("http://localhost:9999/");
        driver.findElement (By.cssSelector("[data-test-id=name] .input__control")).sendKeys(person);
        driver.findElement (By.cssSelector("[data-test-id=phone] .input__control")).sendKeys(phone);
        driver.findElement (By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement (By.cssSelector("button")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void emptyFieldPhone() {

        driver.get("http://localhost:9999/");
        driver.findElement (By.cssSelector("[data-test-id=name] .input__control")).sendKeys("Андрей");
        driver.findElement (By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement (By.cssSelector("button")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void emptyFieldName() {

        driver.get("http://localhost:9999/");
        driver.findElement (By.cssSelector("[data-test-id=phone] .input__control")).sendKeys("+79999999999");
        driver.findElement (By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement (By.cssSelector("button")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void notCheckBox() {

        driver.get("http://localhost:9999/");
        driver.findElement (By.cssSelector("[data-test-id=name] .input__control")).sendKeys("Андрей");
        driver.findElement (By.cssSelector("[data-test-id=phone] .input__control")).sendKeys("+79999999999");
        driver.findElement (By.cssSelector("button")).click();

        String expected = "agreement";
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid")).isDisplayed());
    }
}
