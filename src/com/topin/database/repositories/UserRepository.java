package com.topin.database.repositories;

import com.topin.database.MysqlConn;
import com.topin.services.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private ResultSet resultSet;

    public void loginUser(String username, String password) {
        try {
            String sql = "SELECT * FROM users WHERE username = ? AND username = ?";
            PreparedStatement preparedStatement = MysqlConn.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            this.resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            Log.write(this).error("Error while send MySQL command: " + e.getMessage());
        }
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public boolean isEmpty() {
        try {
            resultSet.first();
            return false;
        } catch (SQLException e) {
            return true;
        }
    }

}
