package com.hillel.connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
    public static Connection provideConnection() {
        try {
            Context envContext = (Context) new InitialContext().lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/shop");
            return ds.getConnection();
        } catch (SQLException e) {
            System.err.println("Cannot get connection");
            return null;
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
