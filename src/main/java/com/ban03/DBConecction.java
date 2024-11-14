package com.ban03;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DBConecction {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/sexshop?";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public Connection DBC() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public int getLen(String tabla) throws SQLException {
        Connection conn = DBC();
        Statement state = conn.createStatement();
        ResultSet total = conn.getMetaData().getColumns(null, null, tabla, null);
        ResultSet res = state.executeQuery("describe " + tabla);

        int count = 0;
        while (total.next()) {
            count++;
        }

        // System.out.println("Total de columnas: " + count);
        // while (res.next()) {
        // for (int i = 1; i <= count; i++) {
        // System.out.print(res.getString(i) + " | ");
        // }
        // System.out.println();
        // }

        res.close();
        total.close();
        state.close();
        conn.close();
        return count;
    }

}

class Main {
    public static void main(String[] args) throws Exception {
        System.out.println(new DBConecction().getLen("categorias"));

    }

}
