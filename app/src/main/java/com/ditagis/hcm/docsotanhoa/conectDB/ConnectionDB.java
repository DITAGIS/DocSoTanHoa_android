package com.ditagis.hcm.docsotanhoa.conectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
	private static final String PROTOCOL = "jdbc:sqlserver://";
	private static final String SERVER = "112.78.4.175\\MSSQLSERVER";
	private static final int PORT = 1433;
	private static final String DB = "DocSoTanHoa";
	private static final String USER = "sa";
	private static final String PASSWORD = "268@lTk";

	public Connection getConnect() {
		String url = PROTOCOL + "" + SERVER + ":" + PORT + ";DatabaseName="
				+ DB + ";" + "user =" + USER + ";password=" + PASSWORD + ";";
		Connection cnn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			cnn = DriverManager.getConnection(url);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnn;
	}

	public static void main(String[] args) {
		ConnectionDB cndb = new ConnectionDB();
		System.out.println(cndb.getConnect());
	}

}
