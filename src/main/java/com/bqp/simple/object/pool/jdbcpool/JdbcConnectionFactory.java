package com.bqp.simple.object.pool.jdbcpool;

import com.bqp.simple.object.pool.ObjectFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionFactory implements ObjectFactory<Connection> {
    private String connectionURL;
    private String userName;
    private String password;
    private String driver;

    public JdbcConnectionFactory(String driver, String connectionURL, String userName, String password) {
        this.driver = driver;
        this.connectionURL = connectionURL;
        this.userName = userName;
        this.password = password;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ce) {
            throw new IllegalArgumentException("Unable to find driver in classpath", ce);
        }
    }

    public Connection createNew() {

        try {
            return DriverManager.getConnection(connectionURL, userName, password);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new IllegalArgumentException("Unable to create new connection", se);
        }
    }

    @Override
    public String toString() {
        return "JdbcConnectionFactory{" +
                "connectionURL='" + connectionURL + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", driver='" + driver + '\'' +
                '}';
    }


}
