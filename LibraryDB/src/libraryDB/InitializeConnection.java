/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

db.mysql.url="jdbc:mysql://localhost:3306/db?characterEncoding=UTF-8&useSSL=false"
 */

package libraryDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author kath
 */
public class InitializeConnection {

  /** The name of the MySQL account to use (or empty for anonymous) */
  private final String userName;

  /** The password for the MySQL account (or empty for anonymous) */
  private final String password;

  /** The name of the computer running MySQL */
  private final String serverName = "localhost";

  /** The port of the MySQL server (default is 3306) */
  private final int portNumber = 3306;

  /** The name of the database we are testing with (this default is installed with MySQL) */
  private final String dbName = "universityLibrarydb";

  private final int userID;

  public enum Commands {
    DELETE, HOLD, CHECKOUT, SEARCH;
  }

  /** The name of the table we are testing with */
//	private final String tableName = "JDBC_TEST";
  private final boolean useSSL = false;

  public InitializeConnection(int userID) {
    Scanner in = new Scanner(System.in);
    System.out.println("Username: ");
    this.userName = in.next();
    System.out.println("Password: ");
    this.password = in.next();
    this.userID = userID;
  }

  /**
   * Get a new database connection
   *
   * @return the connection to the database
   * @throws SQLException when unable to connect to the database
   */
  public Connection getConnection() throws SQLException {
    Connection conn = null;
    Properties connectionProps = new Properties();
    connectionProps.put("user", this.userName);
    connectionProps.put("password", this.password);

    conn = DriverManager.getConnection("jdbc:mysql://"
            + this.serverName + ":" + this.portNumber + "/"
            + this.dbName + "?characterEncoding=UTF-8&useSSL=false",
        connectionProps);

    return conn;
  }

  /**
   * Run a SQL command which does not return a recordset:
   * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
   *
   * @throws SQLException If something goes wrong
   */
  public boolean executeUpdate(Connection conn, String command) throws SQLException {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      stmt.executeUpdate(command); // This will throw a SQLException if it fails
      return true;
    } finally {

      // This will run whether we throw an exception or not
      if (stmt != null) { stmt.close(); }
    }
  }

  /**
   * Connect to MySQL and do some stuff.
   */
  public void run() {

    // Connect to MySQL
    Connection conn = null;
    try {
      conn = this.getConnection();
      System.out.println("Connected to database");
    } catch (SQLException e) {
      System.out.println("ERROR: Could not connect to the database");
      e.printStackTrace();
      return;
    }

    this.possibleCommands();

    Scanner scan = new Scanner(System.in);
    String command = scan.next();

    try {
      Commands country = Commands.valueOf(command);
    } catch ( IllegalArgumentException e ) {
      System.err.println( "No such country" );
    }

    switch (Commands.valueOf(command)) {
      case DELETE:
        System.out.println(Commands.DELETE.name());
        break;
      case HOLD:
        System.out.println(Commands.HOLD.name());
        break;
      case CHECKOUT:
        System.out.println(Commands.CHECKOUT.name());
        break;
      case SEARCH:
        System.out.println(Commands.SEARCH.name());
        break;
    }

  }

  int getUserID() {
    return this.userID;
  }

  private void possibleCommands() {
    System.out.println("Please enter one of the following possible commands: ");
    System.out.println(Arrays.asList(Commands.values()));
  }

}