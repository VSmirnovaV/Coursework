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
        open("http://localhost:8080");
    }

    @AfterEach
    void cleanDB() {
        SQLHelper.clearDB();
    }

    @Test
    void shouldPayTripWithCreditCard() { //успешная покупка по кредитной карте
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.paySuccessfully();
        var expected = DataHelper.getApprovedCard().getStatus();
        var actual = SQLHelper.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldPayTripWithCreditUnnamedCard() { //успешная покупка по кредитной неименной карте
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.unnamedCard();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.paySuccessfully();
        var expected = DataHelper.getApprovedCard().getStatus();
        var actual = SQLHelper.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldPayTripWithCreditCardWithFutureDate() { //успешная покупка по кредитной карте, будущий год
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidFutureYear(3);
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.paySuccessfully();
        var expected = DataHelper.getApprovedCard().getStatus();
        var actual = SQLHelper.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldStayOwner20Sign() { // отправить заявку с 20 символами (проверка граничных значений)
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getSign20();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.paySuccessfully();
        var expected = DataHelper.getApprovedCard().getStatus();
        var actual = SQLHelper.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldPayTripWithCreditCardUnsuccessfully() { //неуспешная покупка по кредитной карте
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getDeclinedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.payNoSuccessfullyCreditCard();
        var expected = DataHelper.getApprovedCard().getStatus();
        var actual = SQLHelper.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldStayEmptyFieldMonth() { //пустое поле "месяц"
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.emptyField();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.EmptyFieldMonth();
    }

    @Test
    void shouldStayEmptyFieldYear() { //пустое поле "год"
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.emptyField();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.EmptyFieldYear();
    }

    @Test
    void shouldStayEmptyFieldOwner() { //пустое поле "владелец"
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.emptyField();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.EmptyFieldOwner();
    }

    @Test
    void shouldStayEmptyFieldCvc() { //пустое поле "CVC"
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.emptyField();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.EmptyFieldCvc();
    }

    @Test
    void shouldStayEmptyAllField() { //оставить пустыми все поля
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var card = DataHelper.emptyField();
        var month = DataHelper.emptyField();
        var year = DataHelper.emptyField();
        var owner = DataHelper.emptyField();
        var cvc = DataHelper.emptyField();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.EmptyFieldCardNumber();
        paymentCreditPage.EmptyFieldMonth();
        paymentCreditPage.EmptyFieldYear();
        paymentCreditPage.EmptyFieldOwner();
        paymentCreditPage.EmptyFieldCvc();
    }

    @Test
    void shouldPayTripWithCreditCardRuOwner() { //покупка тура с указанием имени владельца на русском
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwnerRu();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.invalidOwner();
    }

    @Test
    void shouldPayTripWithCreditCardNumber() { //покупка тура с указанием с цифр вместо имени
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomNumber();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.invalidOwner();
    }

    @Test
    void shouldStayOwnerOneSign() { // отправить заявку с 1 символом (проверка граничных значений) поле владелец
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getSign1();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.EmptyFieldOwner();
    }

    @Test
    void shouldStayOwner21Sign() { // отправить заявку с 21 символом (проверка граничных значений)
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getSign21();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.invalidOwner();
    }

    @Test
    void shouldPayTripWithCreditCardSpecialSign() { //покупка тура с указанием специальных знаков вместо имени
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.specialSign();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.invalidOwner();
    }

    @Test
    void shouldStayCvc1Sign() { //отправить форму с одной цифрой в cvc (граничные значения)
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomNumber1();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.invalidCvc();
    }

    @Test
    void shouldStayCvc2Sign() { //отправить форму с двумя цифрами в cvc (граничные значения)
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomNumber2();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.invalidCvc();
    }

    @Test
    void shouldSpecifyMonthMinus1AndCurrentYear() { //следует указать прошлый месяц и текущий год
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonthMinus1(1);
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.invalidMonth();
    }

    @Test
    void shouldSpecifyMonth00() { //следует указать 00 в поле "месяц"
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.invalidMonthReturn00();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.invalidMonth();
    }

    @Test
    void shouldSpecifyInvalidMonth() { //следует указать невалидный месяц
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.invalidMonthReturn13();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.invalidMonth();
    }

    @Test
    void shouldSpecifyWrongFormatMonth() { //следует указать месяц в неверном формате
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.invalidMonthReturn3();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.wrongFormatMonth();
    }

    @Test
    void shouldSpecifyLastYear() { //следует указать прошлый год
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateInvalidLastYear(1);
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.invalidLastYear();
    }

    @Test
    void shouldSpecifyInvalidFutureYear() { //следует указать будущий неверный год
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateInvalidFutureYear(20);
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.invalidYear();
    }

    @Test
    void shouldSpecify00WithFieldYear() { //следует указать 00 в поле год
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.invalidMonthReturn00();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.invalidLastYear();
    }

    @Test
    void shouldSpecify1SignWithFieldYear() { //следует указать 1 знак в поле год
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.getRandomNumber1();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.EmptyFieldYear();
    }

    @Test
    void shouldSpecifySpacesWithFieldOwner() { // следует указать пробелы в поле владелец
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.spacesWithField();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.spacesWithFieldOwner();
    }

    @Test
    void shouldSpecifyZerosWithCard() { // следует указать нули в поле карта
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var card = DataHelper.getCardWithZeros();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.payNoSuccessfullyCreditCard();
    }

    @Test
    void shouldStayEmptyFieldWithCard() { // следует оставить пустым поле карта
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var card = DataHelper.emptyField();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.EmptyFieldCardNumber();
    }

    @Test
    void shouldSpecify15SignWithFieldCard() { // следует ввести 15 цифр в поле карта
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var card = DataHelper.getCardWith15Sign();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.EmptyFieldCardNumber();
    }

    @Test
    void shouldSpecify1SignWithFieldCard() { // следует ввести одну цифру в поле карта
        HomePage homePage = new HomePage();
        homePage.PayCreditCard();
        var card = DataHelper.getRandomNumber1();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentCreditPage paymentCreditPage = new PaymentCreditPage();
        paymentCreditPage.payCreditCard(card, month, year, owner, cvc);
        paymentCreditPage.EmptyFieldCardNumber();
    }
}