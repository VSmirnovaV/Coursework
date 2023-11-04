package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class HomePage {
    private final SelenideElement heading = $(byText("Путешествие дня"));
    private final SelenideElement buttonBuy = $(byText("Купить"));
    private final SelenideElement buttonBuyInCredit = $(byText("Купить в кредит"));
    public PaymentDebitPage debitPage() {
        buttonBuy.click();
        return new PaymentDebitPage();
    }
    public PaymentCreditPage creditPage() {
        buttonBuyInCredit.click();
        return new PaymentCreditPage();
    }

}
