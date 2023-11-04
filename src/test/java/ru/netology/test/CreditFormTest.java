package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.HomePage;
import ru.netology.page.PaymentCreditPage;
import ru.netology.page.PaymentDebitPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditFormTest {

    HomePage homePage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        homePage = open("http://localhost:8080", HomePage.class);
    }

    @AfterEach
    void cleanDB() {
        SQLHelper.clearDB();
    }

    @Test
    void shouldPayTripWithCreditCard() { //успешная покупка по кредитной карте
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.paySuccessfully();
        var expected = DataHelper.getApprovedCard().getStatus();
        var actual = SQLHelper.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldPayTripWithCreditUnnamedCard() { //успешная покупка по кредитной неименной карте
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.unnamedCard();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.paySuccessfully();
        var expected = DataHelper.getApprovedCard().getStatus();
        var actual = SQLHelper.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldPayTripWithCreditCardWithFutureDate() { //успешная покупка по кредитной карте, будущий год
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidFutureYear(3);
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.paySuccessfully();
        var expected = DataHelper.getApprovedCard().getStatus();
        var actual = SQLHelper.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldStayOwner20Sign() { // отправить заявку с 20 символами (проверка граничных значений)
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getSign20();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.paySuccessfully();
        var expected = DataHelper.getApprovedCard().getStatus();
        var actual = SQLHelper.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldPayTripWithCreditCardUnsuccessfully() { //неуспешная покупка по кредитной карте
        homePage.creditPage();
        var cardsInfo = DataHelper.getDeclinedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.payNoSuccessfullyCreditCard();
        var expected = DataHelper.getApprovedCard().getStatus();
        var actual = SQLHelper.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldStayEmptyFieldMonth() { //пустое поле "месяц"
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.emptyField();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.emptyFieldMonth();
    }

    @Test
    void shouldStayEmptyFieldYear() { //пустое поле "год"
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.emptyField();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.emptyFieldYear();
    }

    @Test
    void shouldStayEmptyFieldOwner() { //пустое поле "владелец"
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.emptyField();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.emptyFieldOwner();
    }

    @Test
    void shouldStayEmptyFieldCvc() { //пустое поле "CVC"
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.emptyField();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.emptyFieldCvc();
    }

    @Test
    void shouldStayEmptyAllField() { //оставить пустыми все поля
        homePage.creditPage();
        var card = DataHelper.emptyField();
        var month = DataHelper.emptyField();
        var year = DataHelper.emptyField();
        var owner = DataHelper.emptyField();
        var cvc = DataHelper.emptyField();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.emptyFieldCardNumber();
        creditPage.emptyFieldMonth();
        creditPage.emptyFieldYear();
        creditPage.emptyFieldOwner();
        creditPage.emptyFieldCvc();
    }

    @Test
    void shouldPayTripWithCreditCardRuOwner() { //покупка тура с указанием имени владельца на русском
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwnerRu();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.invalidOwner();
    }

    @Test
    void shouldPayTripWithCreditCardNumber() { //покупка тура с указанием с цифр вместо имени
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomNumber();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.invalidOwner();
    }

    @Test
    void shouldStayOwnerOneSign() { // отправить заявку с 1 символом (проверка граничных значений) поле владелец
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getSign1();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.emptyFieldOwner();
    }

    @Test
    void shouldStayOwner21Sign() { // отправить заявку с 21 символом (проверка граничных значений)
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getSign21();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.invalidOwner();
    }

    @Test
    void shouldPayTripWithCreditCardSpecialSign() { //покупка тура с указанием специальных знаков вместо имени
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.specialSign();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.invalidSpecialSign();
    }

    @Test
    void shouldStayCvc1Sign() { //отправить форму с одной цифрой в cvc (граничные значения)
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomNumber1();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.invalidCvc();
    }

    @Test
    void shouldStayCvc2Sign() { //отправить форму с двумя цифрами в cvc (граничные значения)
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomNumber2();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.invalidCvc();
    }

    @Test
    void shouldSpecifyMonthMinus1AndCurrentYear() { //следует указать прошлый месяц и текущий год
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonthMinus1(1);
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.invalidMonth();
    }

    @Test
    void shouldSpecifyMonth00() { //следует указать 00 в поле "месяц"
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.invalidMonthReturn00();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.invalidMonth();
    }

    @Test
    void shouldSpecifyInvalidMonth() { //следует указать невалидный месяц
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.invalidMonthReturn13();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.invalidMonth();
    }

    @Test
    void shouldSpecifyWrongFormatMonth() { //следует указать месяц в неверном формате
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.invalidMonthReturn3();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.wrongFormatMonth();
    }

    @Test
    void shouldSpecifyLastYear() { //следует указать прошлый год
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateInvalidLastYear(1);
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.invalidLastYear();
    }

    @Test
    void shouldSpecifyInvalidFutureYear() { //следует указать будущий неверный год
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateInvalidFutureYear(20);
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.invalidYear();
    }

    @Test
    void shouldSpecify00WithFieldYear() { //следует указать 00 в поле год
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.invalidMonthReturn00();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.invalidLastYear();
    }

    @Test
    void shouldSpecify1SignWithFieldYear() { //следует указать 1 знак в поле год
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.getRandomNumber1();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.emptyFieldYear();
    }

    @Test
    void shouldSpecifySpacesWithFieldOwner() { // следует указать пробелы в поле владелец
        homePage.creditPage();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.spacesWithField();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.spacesWithFieldOwner();
    }

    @Test
    void shouldSpecifyZerosWithCard() { // следует указать нули в поле карта
        homePage.creditPage();
        var card = DataHelper.getCardWithZeros();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.invalidCard();
    }

    @Test
    void shouldStayEmptyFieldWithCard() { // следует оставить пустым поле карта
        homePage.creditPage();
        var card = DataHelper.emptyField();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.emptyFieldCardNumber();
    }

    @Test
    void shouldSpecify15SignWithFieldCard() { // следует ввести 15 цифр в поле карта
        homePage.creditPage();
        var card = DataHelper.getCardWith15Sign();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.emptyFieldCardNumber();
    }

    @Test
    void shouldSpecify1SignWithFieldCard() { // следует ввести одну цифру в поле карта
        homePage.creditPage();
        var card = DataHelper.getRandomNumber1();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        var creditPage = homePage.creditPage();
        creditPage.payCreditCard(card, month, year, owner, cvc);
        creditPage.emptyFieldCardNumber();
    }
}