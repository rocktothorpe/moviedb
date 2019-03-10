import java.sql.*;
import java.util.ArrayList;
import java.util.*;

class Movie {

//	public Connection connect;

	public static void main(String[] args) {
		try {
			
			ConnectSingleton.makeConnection(); // do this before stuff
			String name = "Nate Macfarlane";
			
			// query for some dudes ticket
			String q = "SELECT S.movie, S.mDate, S.startTime\n" + 
					"FROM Ticket as T, Customer as C, Showtime as S\n" + 
					"WHERE C.name= '" + name + "' and T.cid=C.cid and T.sid=S.sid;";
			
			queryTicketsByCust(q);
			
			System.out.println("\n\n");
			
			String movie = "Jaws";
			q = "SELECT S.sid, S.mDate, S.startTime, T.seats\n" 
			+ "FROM Showtime as S, Theater as T\n"
			 + "WHERE S.movie='" + movie + "' and S.thid=T.thid;";
			
			String mov = "45";
			ArrayList<OutputMovieByDate> out = queryMovieByDate("2019-03-15");
			
			for(int i = 0; i < out.size(); i++) {
				System.out.println(out.get(i).movie + " " + out.get(i).price);
			}
			
//			String addTick = "INSERT INTO Ticket VALUES(8, 3, 419);"; // wont work twice cuz prim key
					
//			buyTicket(addTick); +

		} catch (Exception e) {
			System.out.println("fail");
			e.printStackTrace();
		
		} 
	}
	
public static ArrayList<OutputMovieByDate> queryMovieByDate(String d) {
		
		ArrayList<OutputMovieByDate> out = new ArrayList<OutputMovieByDate>();
		
		try { //public String movie, starT, price;
			String q = "SELECT  movie, startTime, price\n"
					+ "FROM Showtime\n"
					+ "WHERE mDate='" + d + "';";
			
			ResultSet res = movieQuery(q);
			while (res.next()) { 
				OutputMovieByDate temp = new OutputMovieByDate();
				temp.movie = (res.getString(1));
				temp.startT = (res.getString(2));
				temp.price = (res.getString(3));
				out.add(temp);
				
			}
			
			return out;
			
		} catch (Exception e) {
			System.out.println("fail");
			e.printStackTrace();
		} 
		return null;
	}
	
public static ArrayList<OutputSeatsByMovie> queryRemainSeatsBySID(String sid) {
		
		ArrayList<OutputSeatsByMovie> out = new ArrayList<OutputSeatsByMovie>();
		
		
		try {
			ConnectSingleton.connect.setAutoCommit(false);
			String query = "CREATE VIEW SIDSeats as \n"
					 + "SELECT S.sid, TH.seats, S.mDate, S.startTime\n"
					+ "FROM Showtime as S, Theater as TH\n"
					+ "WHERE S.thid=TH.thid and S.sid='" + sid + "';"; 
			
			Statement stmn = ConnectSingleton.connect.createStatement();
			stmn.executeUpdate(query);
			ConnectSingleton.connect.commit(); 
			
			query = "CREATE VIEW SIDTickets as\n" 
					 + "SELECT S.sid, COUNT(S.sid) as Tickets\n"
					 + "FROM Showtime as S, Ticket as T\n"
					 + "WHERE T.sid=S.sid\n"
					 + "GROUP BY S.sid;";
			stmn.executeUpdate(query);
			ConnectSingleton.connect.commit(); 
			
			query = "SELECT SIDSeats.sid, SIDSeats.mDate, "
					+ "SIDSeats.startTime, seats - COALESCE(Tickets, 0) as remaining\n"
					 + "FROM SIDSeats\n"
					 + "LEFT JOIN SIDTickets ON SIDSeats.sid=SIDTickets.sid;";
			
		
			ResultSet res = movieQuery(query);
			while (res.next()) { 
				OutputSeatsByMovie temp = new OutputSeatsByMovie();
				temp.showId = (res.getString(1));
				temp.date = (res.getString(2));
				temp.sTime = (res.getString(3));
				temp.seats = (res.getString(4));
				//System.out.println(temp.seats);
				out.add(temp);
				
			}
			
			stmn.executeUpdate("DROP VIEW SIDSeats, SIDTickets;");
			ConnectSingleton.connect.commit(); 
			
			return out;
			
		} catch (Exception e) {
			System.out.println("fail");
			e.printStackTrace();
		} 
		return null;
	}
	
	// get remaining seats for a movie
	public static ArrayList<OutputSeatsByMovie> queryRemainSeatsByMovie(String movie) {
		
		ArrayList<OutputSeatsByMovie> out = new ArrayList<OutputSeatsByMovie>();
		
		
		try {
			ConnectSingleton.connect.setAutoCommit(false);
			String query = "CREATE VIEW SIDSeats as \n"
					 + "SELECT S.sid, TH.seats, S.mDate, S.startTime\n"
					+ "FROM Showtime as S, Theater as TH\n"
					+ "WHERE S.thid=TH.thid and S.movie='" + movie + "';"; 
			
			Statement stmn = ConnectSingleton.connect.createStatement();
			stmn.executeUpdate(query);
			ConnectSingleton.connect.commit(); 
			
			query = "CREATE VIEW SIDTickets as\n" 
					 + "SELECT S.sid, COUNT(S.sid) as Tickets\n"
					 + "FROM Showtime as S, Ticket as T\n"
					 + "WHERE T.sid=S.sid\n"
					 + "GROUP BY S.sid;";
			stmn.executeUpdate(query);
			ConnectSingleton.connect.commit(); 
			
			query = "SELECT SIDSeats.sid, SIDSeats.mDate, "
					+ "SIDSeats.startTime, seats - COALESCE(Tickets, 0) as remaining\n"
					 + "FROM SIDSeats\n"
					 + "LEFT JOIN SIDTickets ON SIDSeats.sid=SIDTickets.sid;";
			
		
			ResultSet res = movieQuery(query);
			while (res.next()) { 
				OutputSeatsByMovie temp = new OutputSeatsByMovie();
				temp.showId = (res.getString(1));
				temp.date = (res.getString(2));
				temp.sTime = (res.getString(3));
				temp.seats = (res.getString(4));
				//System.out.println(temp.seats);
				out.add(temp);
				
			}
			
			stmn.executeUpdate("DROP VIEW SIDSeats, SIDTickets;");
			ConnectSingleton.connect.commit(); 
			
			return out;
			
		} catch (Exception e) {
			System.out.println("fail");
			e.printStackTrace();
		} 
		return null;
	}
	
	public static void buyTicket(String q) {
		
		try {
			
			ConnectSingleton.connect.setAutoCommit(false);
		
			Statement stmn = ConnectSingleton.connect.createStatement();
			stmn.executeUpdate(q);
		
			stmn.close();
			ConnectSingleton.connect.commit(); 
			ConnectSingleton.connect.close(); 
			
		} catch (Exception e) {
			System.out.println("fail");
			e.printStackTrace();
		} 
	}	
	
	// query for all tickets for a given name
	public static ArrayList<OutputTicketsByCust> queryTicketsByCust(String q) {
		
		ArrayList<OutputTicketsByCust> out = new ArrayList<OutputTicketsByCust>();
		OutputTicketsByCust temp = new OutputTicketsByCust();
		try {
			ResultSet res = movieQuery(q);
			while (res.next()) { 
				temp.movie = res.getString(1); 
				temp.date = res.getString(2);
				temp.sTime = res.getString(3);
				out.add(temp);
			} 
			return out;
		} catch (Exception e) {
			System.out.println("fail");
			e.printStackTrace();
		} 
		return null;
		
	}
	
	// get rs for any query
	public static ResultSet movieQuery(String query) {
		
		try {
			ResultSet rs = ConnectSingleton.statement.executeQuery(query);
			return rs;
				
		}
		
		catch (Exception e) {
			System.out.println("fail");
			e.printStackTrace();
		
		}
		return null; 
		
		
	}
}
