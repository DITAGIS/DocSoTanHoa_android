package com.ditagis.hcm.docsotanhoa.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
public class SQLServerConnUtils {
    public static String HOST_NAME = "112.78.4.175";
    public static String INSTANCE_NAME = "MSSQLSERVER";
    public static String database = "DocSoTanHoa";
    public static String PASSWORD = "268@lTk";
    public static String USERNAME = "sa";

 public static Connection getSQLServerConnection()
         throws SQLException, ClassNotFoundException {
 
     return getSQLServerConnection(HOST_NAME, INSTANCE_NAME,
             database, USERNAME, PASSWORD);
 }
 
 // Trường hợp sử dụng SQLServer.
 // Và thư viện SQLJDBC.
 public static Connection getSQLServerConnection(String hostName,
         String sqlInstanceName, String database, String userName,
         String password) throws ClassNotFoundException, SQLException {
     Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
     String connectionURL = "jdbc:sqlserver://" + hostName + ":1433"
             + ";instance=" + sqlInstanceName + ";databaseName=" + database;
 
     Connection conn = DriverManager.getConnection(connectionURL, userName,
             password);
     return conn;
 }
 
}