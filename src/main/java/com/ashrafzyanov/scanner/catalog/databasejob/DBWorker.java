package com.ashrafzyanov.scanner.catalog.databasejob;

import java.sql.Connection;
import java.sql.DriverManager; 
import java.sql.SQLException; 

public class DBWorker {
  
    private Connection connection = null;
    private String URL = null;
    private String userName = null;
    private String password = null;
          
    public DBWorker(String URL, String userName, String password) {                                              
        this.URL = URL;
        this.userName = userName;
        this.password = password;
        try {
            Class.forName(driver);
        } catch(ClassNotFoundException exp) {}
    }
    
    public void openConnect() throws SQLException {
        connection = DriverManager.getConnection(URL, userName, password);      
    }

    public boolean isConnectionDataBase() throws SQLException {
        return (connection.isClosed());                                   
    }
    
    public void closeConnect() throws SQLException {
        if (!connection.isClosed()) {
            connection.close();     
        }
    }

    public Connection getConnection() {
        return connection;
    }
    
}
