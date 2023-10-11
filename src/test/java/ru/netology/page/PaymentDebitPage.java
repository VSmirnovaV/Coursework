package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;


import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentDebitPage {
    private final SelenideElement cardNumber = $("[placeholder='0000 0000 0000 0000']");
    private final SelenideElement emptyFieldCardNumber = $(byText("Неверный формат"));
    private final SelenideElement monthField = $("[placeholder='08']");
    private final SelenideElement invalidMonthField = $(byText("Неверно указан срок действия карты"));
    private final SelenideElement wrongFormat = $(byText("Неверный формат"));
    private final SelenideElement emptyFieldMonth = $(byText("Неверный формат"));
    private final SelenideElement yearField = $("[placeholder='22']");
    private final SelenideElement lustYear = $(byText("Истёк срок действия карты"));
    private final SelenideElement invalidYear = $(byText("Неверно указан срок действия карты"));
    private final SelenideElement emptyFieldYear = $(byText("Неверный формат"));
    private final SelenideElement ownerField = $$(".input__control").get(3);
    private final SelenideElement emptyFieldOwner = $(byText("Поле обязательно для заполнения"));
    private final SelenideElement emptyOwnerError = $(byText("Неверный формат"));
    private final SelenideElement spaceWithFieldOwner = $(byText("Поле обязательно для заполнения"));
    private final SelenideElement cvcField = $("[placeholder='999']");
    private final SelenideElement invalidCvc = $(byText("Неверный формат"));
    private final SelenideElement emptyFieldCvc = $(byText("Неверный формат"));
    private final SelenideElement continueButton = $(byText("Продолжить"));
    private final SelenideElement error = $(byText("Ошибка! Банк отказал в проведении операции."));
    private final SelenideElement successMessage = $(byText("Операция одобрена Банком."));

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
        error.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void EmptyFieldCardNumber() {
        emptyFieldCardNumber.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void EmptyFieldMonth() {
        emptyFieldMonth.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void EmptyFieldYear() {
        emptyFieldYear.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void EmptyFieldOwner() {
        emptyFieldOwner.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void EmptyFieldCvc() {
        emptyFieldCvc.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void wrongFormatMonth() {
        wrongFormat.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void invalidMonth() {
        invalidMonthField.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void invalidYear() {
        invalidYear.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void invalidLastYear() {
        lustYear.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void spacesWithFieldOwner() {
        spaceWithFieldOwner.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void invalidCvc() {
        invalidCvc.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void invalidOwner() {
        emptyOwnerError.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void invalidSpecialSign() {
        emptyOwnerError.shouldBe(visible, Duration.ofSeconds(15));
    }
}
