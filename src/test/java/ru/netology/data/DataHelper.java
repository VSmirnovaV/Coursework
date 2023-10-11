package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private static Faker faker = new Faker(new Locale("en"));
    private static Faker fakerRu = new Faker(new Locale("ru"));

    DataHelper() {
    }

    @Value
    public static class CardsInfo {
        String cardsNumber;
        String status;
    }

    public static CardsInfo getApprovedCard() {
        return new CardsInfo("1111 2222 3333 4444", "APPROVED");
    }

    public static CardsInfo getDeclinedCard() {
        return new CardsInfo("5555 6666 7777 8888", "DECLINED");
    }

    public static String getRandomOwner() {
        String owner = faker.name().fullName();
        return owner;
    }

    public static String getRandomOwnerRu() {
        String owner = fakerRu.name().fullName();
        return owner;
    }

    public static String unnamedCard() {
        return "INSTANT CARD";
    }

    public static String getRandomNumber() {
        String number = fakerRu.number().digits(7);
        return number;
    }
    public static String getRandomNumber1() {
        String number = fakerRu.number().digits(1);
        return number;
    }

    public static String getRandomNumber2() {
        String number = fakerRu.number().digits(2);
        return number;
    }

    public static String getRandomNumber31() {
        String number = fakerRu.number().digits(31);
        return number;
    }

    public static String getRandomCVC() {
        String cvc = faker.number().digits(3);
        return cvc;
    }

    public static String specialSign() {
        return "@#$%&%$#";
    }

    public static String getInvalidRandomCVC() {
        String cvc = faker.number().digits(2);
        return cvc;
    }

    public static String generateValidMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }
    public static String generateValidMonthMinus1(int addMonth) {
        return LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("MM"));
    }
    public static String invalidMonthReturn13() {
        return String.valueOf(13);
    }

    public static String invalidMonthReturn3() {
        return String.valueOf(3);
    }

    public static String invalidMonthReturn00() {
        return String.valueOf("00");
    }

    public static String emptyField() {
        return "";
    }

    public static String spacesWithField() {
        return "        ";
    }

    public static String generateValidYear() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String generateValidFutureYear(int addYears) {
        return LocalDate.now().plusYears(3).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String generateInvalidLastYear(int addYears) {
        return LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String generateInvalidFutureYear(int addYears) {
        return LocalDate.now().plusYears(20).format(DateTimeFormatter.ofPattern("yy"));
    }

}
