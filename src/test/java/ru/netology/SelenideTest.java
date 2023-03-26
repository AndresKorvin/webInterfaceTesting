package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;


class SelenideTest {

    @Test
    void testForm() {

        open("http://localhost:9999/");
        $("[data-test-id=name] .input__control").setValue("ВасилийТркин");
        $("[data-test-id=phone] .input__control").setValue("+79996665544");
        $("[data-test-id=agreement]").click();
        $("button").click();
        $("[data-test-id=order-success]").shouldHave(exactText("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время."));

    }
}