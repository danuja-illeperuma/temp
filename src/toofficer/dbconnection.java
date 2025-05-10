package toofficer;

import java.sql.*;


// Database connection helper class
class dbconnection {
    private static final String URL = "jdbc:mysql://localhost:3307/fotdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {   //static method that we can access by simply call class name and method name.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
}