package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class PaymentCreditPage {
    private SelenideElement heading = $(byText("Оплата по карте"));
    private SelenideElement cardNumber = $("[placeholder=0000 0000 0000 0000]");
    private SelenideElement monthField = $("[placeholder=08]");
    private SelenideElement yearField = $("[placeholder=22]");
    private SelenideElement ownerField = $(byText("Владелец"));
    private SelenideElement cvcField = $(byText("[placeholder=999]"));
    private SelenideElement continueButton = $(byText("Продолжить"));
    private SelenideElement error = $(".notification_status_error");
    private SelenideElement successMessage = $(".notification_status_ok");

    public void paySuccessfullyCreditCard(String number, String month, String year, String owner, String cvc) {
        cardNumber.sendKeys(number);
        monthField.sendKeys(month);
        yearField.sendKeys(year);
        ownerField.sendKeys(owner);
        cvcField.sendKeys(cvc);
        continueButton.click();
        successMessage.shouldHave(exactText("Операция одобрена Банком.")).shouldBe(visible, Duration.ofSeconds(15));
    }

    public void payNoSuccessfullyCreditCard(String number, String month, String year, String owner, String cvc) {
        cardNumber.sendKeys(number);
        monthField.sendKeys(month);
        yearField.sendKeys(year);
        ownerField.sendKeys(owner);
        cvcField.sendKeys(cvc);
        continueButton.click();
        error.shouldHave(exactText("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
    }
}
