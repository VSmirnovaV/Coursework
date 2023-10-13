package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.HomePage;
import ru.netology.page.PaymentDebitPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormsTest {
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
    void shouldPayTripWithDebitCard() { //успешная покупка по дебетовой карте
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.paySuccessfully();
        var expected = DataHelper.getApprovedCard().getStatus();
        var actual = SQLHelper.getDebitPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldPayTripWithDebitUnnamedCard() { //успешная покупка по дебетовой неименной карте
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.unnamedCard();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.paySuccessfully();
        var expected = DataHelper.getApprovedCard().getStatus();
        var actual = SQLHelper.getDebitPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldPayTripWithDebitCardWithFutureDate() { //успешная покупка по дебетовой карте, будущий год
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidFutureYear(3);
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.paySuccessfully();
        var expected = DataHelper.getApprovedCard().getStatus();
        var actual = SQLHelper.getDebitPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldStayOwner20Sign() { // отправить заявку с 20 символами (проверка граничных значений)
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getSign20();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.paySuccessfully();
        var expected = DataHelper.getApprovedCard().getStatus();
        var actual = SQLHelper.getDebitPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldPayTripWithDebitCardUnsuccessfully() { //неуспешная покупка по дебетовой карте
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getDeclinedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.payNoSuccessfullyDebitCard();
        var expected = DataHelper.getDeclinedCard().getStatus();
        var actual = SQLHelper.getDebitPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void shouldStayEmptyFieldMonth() { //пустое поле "месяц"
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.emptyField();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.EmptyFieldMonth();
    }

    @Test
    void shouldStayEmptyFieldYear() { //пустое поле "год"
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.emptyField();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.EmptyFieldYear();
    }
    @Test
    void shouldStayEmptyFieldOwner() { //пустое поле "владелец"
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.emptyField();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.EmptyFieldOwner();
    }

    @Test
    void shouldStayEmptyFieldCvc() { //пустое поле "CVC"
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.emptyField();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.EmptyFieldCvc();
    }
    @Test
    void shouldStayEmptyAllField() { //оставить пустыми все поля
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var card = DataHelper.emptyField();
        var month = DataHelper.emptyField();
        var year = DataHelper.emptyField();
        var owner = DataHelper.emptyField();
        var cvc = DataHelper.emptyField();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.EmptyFieldCardNumber();
        paymentDebitPage.EmptyFieldMonth();
        paymentDebitPage.EmptyFieldYear();
        paymentDebitPage.EmptyFieldOwner();
        paymentDebitPage.EmptyFieldCvc();
    }

    @Test
    void shouldPayTripWithDebitCardRuOwner() { //покупка тура с указанием имени владельца на русском
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwnerRu();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.invalidOwner();
    }

    @Test
    void shouldPayTripWithDebitCardNumber() { //покупка тура с указанием с цифр вместо имени
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomNumber();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.invalidOwner();
    }

    @Test
    void shouldStayOwnerOneSign() { // отправить заявку с 1 символом (проверка граничных значений) поле владелец
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getSign1();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.EmptyFieldOwner();
    }

    @Test
    void shouldStayOwner21Sign() { // отправить заявку с 21 символом (проверка граничных значений)
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getSign21();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.invalidOwner();
    }

    @Test
    void shouldPayTripWithDebitCardSpecialSign() { //покупка тура с указанием специальных знаков вместо имени
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.specialSign();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.invalidOwner();
    }
    @Test
    void shouldStayCvc1Sign() { //отправить форму с одной цифрой в cvc (граничные значения)
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomNumber1();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.invalidCvc();
    }

    @Test
    void shouldStayCvc2Sign() { //отправить форму с двумя цифрами в cvc (граничные значения)
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomNumber2();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.invalidCvc();
    }

    @Test
    void shouldSpecifyMonthMinus1AndCurrentYear() { //следует указать прошлый месяц и текущий год
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonthMinus1(1);
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.invalidMonth();
    }
    @Test
    void shouldSpecifyMonth00() { //следует указать 00 в поле "месяц"
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.invalidMonthReturn00();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.invalidMonth();
    }

    @Test
    void shouldSpecifyInvalidMonth() { //следует указать невалидный месяц
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.invalidMonthReturn13();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.invalidMonth();
    }
    @Test
    void shouldSpecifyWrongFormatMonth() { //следует указать месяц в неверном формате
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.invalidMonthReturn3();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.wrongFormatMonth();
    }

    @Test
    void shouldSpecifyLastYear() { //следует указать прошлый год
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateInvalidLastYear(1);
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.invalidLastYear();
    }

    @Test
    void shouldSpecifyInvalidFutureYear() { //следует указать будущий неверный год
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateInvalidFutureYear(20);
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.invalidYear();
    }

    @Test
    void shouldSpecify00WithFieldYear() { //следует указать 00 в поле год
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.invalidMonthReturn00();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.invalidLastYear();
    }

    @Test
    void shouldSpecify1SignWithFieldYear() { //следует указать 1 знак в поле год
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.getRandomNumber1();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.EmptyFieldYear();
    }

    @Test
    void shouldSpecifySpacesWithFieldOwner() { // следует указать пробелы в поле владелец
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var cardsInfo = DataHelper.getApprovedCard();
        var card = cardsInfo.getCardsNumber();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.spacesWithField();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.spacesWithFieldOwner();
    }
    @Test
    void shouldSpecifyZerosWithCard() { // следует нули в поле карта
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var card = DataHelper.getCardWithZeros();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.payNoSuccessfullyDebitCard();
    }
    @Test
    void shouldStayEmptyFieldWithCard() { // следует оставить пустым поле карта
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var card = DataHelper.emptyField();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.EmptyFieldCardNumber();
    }
    @Test
    void shouldSpecify15SignWithFieldCard() { // следует ввести 15 цифр в поле карта
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var card = DataHelper.getCardWith15Sign();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.EmptyFieldCardNumber();
    }
    @Test
    void shouldSpecify1SignWithFieldCard() { // следует ввести одну цифру в поле карта
        HomePage homePage = new HomePage();
        homePage.payDebitCard();
        var card = DataHelper.getRandomNumber1();
        var month = DataHelper.generateValidMonth();
        var year = DataHelper.generateValidYear();
        var owner = DataHelper.getRandomOwner();
        var cvc = DataHelper.getRandomCVC();
        PaymentDebitPage paymentDebitPage = new PaymentDebitPage();
        paymentDebitPage.payDebitCard(card, month, year, owner, cvc);
        paymentDebitPage.EmptyFieldCardNumber();
    }
}




