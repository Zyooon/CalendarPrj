package com.zyoon.calendar.challenge;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySqlConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/calendar_db?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}