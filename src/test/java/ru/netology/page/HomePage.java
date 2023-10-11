package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class HomePage {
    private final SelenideElement heading = $(byText("Путешествие дня"));
    private final SelenideElement payForm = $(byText("Оплата по карте"));
    private final SelenideElement buttonBuy = $(byText("Купить"));
    private final SelenideElement buttonBuyInCredit = $(byText("Купить в кредит"));


    public HomePage() {
        heading.shouldBe(visible);
    }

    public PaymentDebitPage payDebitCard() {
        buttonBuy.click();
        payForm.shouldHave(exactText("Оплата по карте"));
        return new PaymentDebitPage();
    }

    public PaymentCreditPage PayCreditCard() {
        heading.shouldBe(exactText("Путешествие дня"));
        buttonBuyInCredit.click();
        return new PaymentCreditPage();
    }


}
