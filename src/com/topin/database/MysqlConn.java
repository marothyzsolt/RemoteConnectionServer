package com.topin.database;

import com.topin.services.Log;

import java.sql.*;

public class MysqlConn {
    private static Connection connection;

    public static void setup() {
        Log.write("MysqlConn").info("Start MySQL connection...");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://147.135.149.163:3306/remotelly",
                    "remotelly",
                    "DaxDgggdfR13@dsxxrgrhHHrdxt654"
            );
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet unsafeGet(String statement) {
        try {
            Statement stmt = connection.createStatement();

            return stmt.executeQuery(statement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close()
    {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
