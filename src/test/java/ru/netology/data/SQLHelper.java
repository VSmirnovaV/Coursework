package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();
    public SQLHelper() {
    }
    @SneakyThrows
    public static Connection getConnection() {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/app", "app", "pass");
    }

    @SneakyThrows
    public static void clearDB() {
        Connection conn = getConnection();
            runner.execute(conn, "DELETE FROM credit_request_entity");
            runner.execute(conn, "DELETE FROM order_entity");
            runner.execute(conn, "DELETE FROM payment_entity");
    }

    @SneakyThrows
    public static String getDebitPaymentStatus() {
        String SqlStatus = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        Connection conn = getConnection();
            String result = runner.query(conn, SqlStatus, new ScalarHandler<>());
            return result;
    }

    @SneakyThrows
    public static String getCreditPaymentStatus() {
        String SqlStatus = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        Connection conn = getConnection();
            String result = runner.query(conn, SqlStatus, new ScalarHandler<>());
            return result;
    }
}

