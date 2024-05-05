package javaapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dao {
	// instance fields
	static Connection connect = null;
	Statement statement = null;

	// constructor
	public Dao() {

	}

	public Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	// CRUD implementation

	public void createTables() {
		// variables for SQL Query table creations
		final String createTicketsTable = "CREATE TABLE ypark_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200), start_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, end_date TIMESTAMP NULL, status VARCHAR(10) DEFAULT 'AVAILABLE')";
		final String createUsersTable = "CREATE TABLE ypark_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";

		try {

			// execute queries to create tables

			statement = getConnection().createStatement();

			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
	}

	public void addUsers() {
		// add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;

		Statement statement;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB

			statement = getConnection().createStatement();

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "INSERT INTO ypark_users(uname,upass,admin) " + "VALUES('" + rowData.get(0) + "'," + " '"
						+ rowData.get(1) + "','" + rowData.get(2) + "');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close statement object
			statement.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public int insertRecords(String ticketName, String ticketDesc) {
		int id = 0;
		try {
			statement = getConnection().createStatement();
			statement.executeUpdate("Insert into ypark_tickets" + "(ticket_issuer, ticket_description) VALUES(" + " '"
					+ ticketName + "','" + ticketDesc + "')", Statement.RETURN_GENERATED_KEYS);

			// retrieve ticket id number newly auto generated upon record insertion
			ResultSet resultSet = null;
			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				// retrieve first field in table
				id = resultSet.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;

	}

	public ResultSet readRecords() {

		ResultSet results = null;
		try {
			statement = connect.createStatement();
			results = statement.executeQuery("SELECT * FROM ypark_tickets");
			// connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}

	// continue coding for updateRecords implementation
	public void updateRecords(String id, String option, String updatedTicket) {
		try {
			statement = getConnection().createStatement();
			statement.executeUpdate(
					"UPDATE gchoi_tickets SET " + option + "='" + updatedTicket + "'WHERE ticket_id = " + id + ";");

			System.out.println("Ticket " + id + " has been updated");

			statement.close();
			
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("There was an error updating the ticket");
			System.out.println(se.getMessage());
		}
	}

	// continue coding for deleteRecords implementation
	public void deleteRecords(String id) {
		try {
			statement = getConnection().createStatement();

			statement.executeUpdate("DELETE FROM ypark_tickets WHERE ticket_id = " + id);

			System.out.println("Ticket " + id + "has been deleted");

			statement.close();
		}

		catch (SQLException se) {
			se.printStackTrace();
			System.out.println("There was an error deleting the ticket");
			System.out.println(se.getMessage());
		}
	}

	public void closeRecords(String id) {
		try {
			statement = getConnection().createStatement();

			statement.executeUpdate(
					"UPDATE tickets.ypark_tickets SET status = 'N/A', end_date = current_timestamp() WHERE ticket_id = "
							+ id + ";");

			System.out.println("Ticket " + id + "has been closed");
			statement.close();

		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("There was an error closing the ticket");
			System.out.println(se.getMessage());

		}
	}
}
