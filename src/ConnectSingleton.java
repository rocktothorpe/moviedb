import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConnectSingleton {
	
	static Connection connect;
	static Statement statement;
	
	public static void main(String[] args) {
	
	}
	
	public static void  makeConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection
					( "jdbc:mysql://ambari-head.csc.calpoly.edu/moviedb", "moviedb", "movie1213");
			statement = connect.createStatement();
			}
			
			catch (Exception e) {
				System.out.println("fail");
				e.printStackTrace();
			
			}
	}

}