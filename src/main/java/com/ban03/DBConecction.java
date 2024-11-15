package com.ban03;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConecction {
    private static final String DB_NAME = "CRUD";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/" + DB_NAME + "?";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public Connection DBC() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

}