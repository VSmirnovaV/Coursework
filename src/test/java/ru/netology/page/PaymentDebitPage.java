package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;


import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentDebitPage {
    private final SelenideElement payForm = $(byText("Оплата по карте"));
    private final SelenideElement cardNumber = $("[placeholder='0000 0000 0000 0000']");
    private final SelenideElement wrongFormat = $(byText("Неверный формат"));
    private final SelenideElement monthField = $("[placeholder='08']");
    private final SelenideElement invalidValidity = $(byText("Неверно указан срок действия карты"));
    private final SelenideElement yearField = $("[placeholder='22']");
    private final SelenideElement lustYear = $(byText("Истёк срок действия карты"));
    private final SelenideElement ownerField = $$(".input__control").get(3);
    private final SelenideElement obligatoryField = $(byText("Поле обязательно для заполнения"));
    private final SelenideElement cvcField = $("[placeholder='999']");
    private final SelenideElement continueButton = $(byText("Продолжить"));
    private final SelenideElement error = $(byText("Ошибка! Банк отказал в проведении операции."));
    private final SelenideElement successMessage = $(byText("Операция одобрена Банком."));


    public PaymentDebitPage () {
        payForm.shouldHave(exactText("Оплата по карте"));
    }

    public void payDebitCard(String number, String month, String year, String owner, String cvc) {
        cardNumber.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        cardNumber.sendKeys(number);
        monthField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        monthField.sendKeys(month);
        yearField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        yearField.sendKeys(year);
        ownerField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        ownerField.sendKeys(owner);
        cvcField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        cvcField.sendKeys(cvc);
        continueButton.click();
    }

    public void paySuccessfully() {
        successMessage.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void payNoSuccessfullyDebitCard() {
        error.shouldBe(visible, Duration.ofSeconds(4));
    }

    public void emptyFieldCardNumber() {
        wrongFormat.shouldBe(visible);
    }
    public void invalidCard() {
        wrongFormat.shouldBe(visible);
    }

    public void emptyFieldMonth() {
        wrongFormat.shouldBe(visible);
    }

    public void emptyFieldYear() {
        wrongFormat.shouldBe(visible);
    }

    public void emptyFieldOwner() {
        obligatoryField.shouldBe(visible);
    }

    public void emptyFieldCvc() {
        wrongFormat.shouldBe(visible);
    }

    public void wrongFormatMonth() {
        wrongFormat.shouldBe(visible);
    }

    public void invalidMonth() {
        invalidValidity.shouldBe(visible);
    }

    public void invalidYear() {
        invalidValidity.shouldBe(visible);
    }

    public void invalidLastYear() {
        lustYear.shouldBe(visible);
    }

    public void spacesWithFieldOwner() {
        obligatoryField.shouldBe(visible);
    }

    public void invalidCvc() {
        wrongFormat.shouldBe(visible);
    }

    public void invalidOwner() {
        wrongFormat.shouldBe(visible);
    }

    public void invalidSpecialSign() {
        wrongFormat.shouldBe(visible);
    }
}
