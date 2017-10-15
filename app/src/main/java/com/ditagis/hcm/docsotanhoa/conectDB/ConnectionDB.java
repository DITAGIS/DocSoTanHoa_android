package com.ditagis.hcm.docsotanhoa.conectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.ditagis.hcm.docsotanhoa.sql.SQLServerConnUtils.database;

public class ConnectionDB {
	private static final String PROTOCOL = "jdbc:jtds:sqlserver://";
	private static final String SERVER = "103.74.117.51"; // Tadu
	private static final String INSTANCT_NAME = "MSSQLSERVER";
	private static final int PORT = 1433;
	private static final String DB = "HCM_DocSoTanHoa";
	private static final String USER = "docsotanhoa";
	private static final String PASSWORD = "Docso123";

	public Connection getConnect() {
//		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		StrictMode.setThreadPolicy(policy);
		String url = "jdbc:jtds:sqlserver://" + SERVER + ":1433/"
				+ database + ";instance=" + INSTANCT_NAME;
		Connection cnn = null;
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			cnn = DriverManager.getConnection(url, USER, PASSWORD);
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
