package com.zyoon.calendar.required;

import java.sql.Connection;
import java.sql.DriverManager;

class MySqlConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/calendar_db?serverTimezone=Asia/Seoul";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}