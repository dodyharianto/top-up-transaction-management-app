package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector 
{
	private static DBConnector instance;
	public static DBConnector get_instance ()
	{
		if (instance == null)
		{
			instance = new DBConnector();
		}
		return instance;
	}
	
	public Connection connect ()
	{
		final String database_name = "uas";
		final String url = "jdbc:mysql://localhost:3306/" + database_name;
		final String username = "root";
		final String password = "";
		final String driver_class = "com.mysql.cj.jdbc.Driver";
		
		try 
		{
			Class.forName(driver_class);
			Connection connection = DriverManager.getConnection(url, username, password);
			
			if (connection != null)
			{
				System.out.println("Berhasil connect ke database");
				return connection;
			}
		} 
		catch (ClassNotFoundException | SQLException e) 
		{
			e.printStackTrace();
		}	
		
		return null;
	}
}
