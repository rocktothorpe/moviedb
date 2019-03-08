import java.sql.*;
import java.*;

class Movie {

	static Connection connect;

	public static void main(String[] args) {
	
		try {
		
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection
				( "jdbc:mysql://ambari-head.csc.calpoly.edu/moviedb", "moviedb", "movie1213");
		Statement statement = connect.createStatement();

		ResultSet rs = statement.executeQuery(

		"SELECT * FROM Showtime");

		while (rs.next()) {

		String movie = rs.getString(2);

		System.out.println("movie = " +

		movie);

		}
		
		} catch (Exception e) {
			System.out.println("fail");
			e.printStackTrace();
		
		}
	
	}
}
