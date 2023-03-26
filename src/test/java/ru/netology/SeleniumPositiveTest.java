package ru.netology;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;

public class SeleniumPositiveTest {

    private WebDriver driver;


    @BeforeAll
    static void setUpAll() {
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver_win32/chromedriver.exe");
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
            "Андрей, +79996665544", // только имя, кириллица
            "Турчинский Владимир, +79996665544", // пробел
            "Соколова-Сероглазова, +79996665544", // дефис
            "Ким-Ер Сэн, +79996665544", // дефис + пробел
            "Жёлудев Максим, +79996665544" // буква ё

    })
    void shouldRegistred(String person, String phone) {

        driver.get("http://localhost:9999/");
        driver.findElement (By.cssSelector("[data-test-id=name] .input__control")).sendKeys(person);
        driver.findElement (By.cssSelector("[data-test-id=phone] .input__control")).sendKeys(phone);
        driver.findElement (By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement (By.cssSelector("button")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.tagName("p")).getText().trim();
        Assertions.assertEquals(expected, actual);
    }
}
