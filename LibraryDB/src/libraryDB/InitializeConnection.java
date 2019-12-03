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
import java.util.ArrayList;
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
    DELETE, HOLD, CHECKOUT, SEARCH, EXIT;
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
    System.out.println("User id: ");
    this.userID = Integer.parseInt(in.next());
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
            + this.dbName + "?characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true",
        connectionProps);

    return conn;
  }

  /**
   * Run a SQL command which does not return a result set:
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

    Scanner commandType = new Scanner(System.in);
    String command = commandType.next();

    switch (Commands.valueOf(command)) {
      case DELETE:
        DeleteHold dh = new DeleteHold(this);
        try {

          Scanner deleteScanner = new Scanner(System.in);
          System.out.println("Enter the ID of the media item you'd like to delete from your holds list or type cancel to return to home.");
          System.out.println(dh.printBooksOnHold());
          String item = deleteScanner.next();
          dh.deleteHoldItem(item);
          System.out.println(dh.printBooksOnHold());

        } catch (SQLException e) {
          System.out.println("Unable to execute command.");
          e.printStackTrace();
        }

      case HOLD:
        System.out.println(Commands.HOLD.name());
        break;

      case CHECKOUT:
        System.out.println(Commands.CHECKOUT.name());
        break;

      case SEARCH:
        SearchMedia searchMedia = new SearchMedia(this);
        try {
            String mediaType = this.userChoice();
          if (mediaType.equals("exit")) {
            this.run();
          }
            String mediaQuery = this.searchQuery();
//          while (!mediaType.equals("exit")) {
            switch (mediaType) {
              case "1":
                System.out.println("Searching Books");
                searchMedia.searchBook(mediaQuery);
                mediaType = this.userChoice();
                mediaQuery = this.searchQuery();
                break;
              case "2":
                System.out.println("Searching E-Books");
                searchMedia.searchEBook(mediaQuery);
                mediaType = this.userChoice();
                mediaQuery = this.searchQuery();
                break;
              case "3":
                System.out.println("Searching Videos");
                searchMedia.searchVideo(mediaQuery);
                mediaType = this.userChoice();
                mediaQuery = this.searchQuery();
                break;
              case "4":
                System.out.println("Searching CDs");
                searchMedia.searchCD(mediaQuery);
                mediaType = this.userChoice();
                mediaQuery = this.searchQuery();
                break;
              case "exit":
                this.run();
                break;
              default:
                System.out.println("The library doesn't carry this media type");
            }
        } catch (SQLException e) {
          System.out.println("Unable to execute command.");
          e.printStackTrace();
        }
        break;

      case EXIT:
        System.out.println("Ended session");
        break;
        
      default:
        System.out.println("Invalid command");
    }
  }

  int getUserID() {
    return this.userID;
  }

  private void possibleCommands() {
    System.out.println("Please enter one of the following possible commands: ");
    System.out.println(Arrays.asList(Commands.values()));
  }

  private void displayMediaTypes() {
      System.out.println("Choose the type of media you would like to search for by typing the corresponding number.");
      System.out.println("1: Book\n"
          + "2: E-Book\n"
          + "3: Video\n"
          + "4: CD");
  }

  private String userChoice() {
    Scanner choice = new Scanner(System.in);
    this.displayMediaTypes();
    return choice.next();
  }

  private String searchQuery() {
    Scanner media = new Scanner(System.in);
    System.out.println("Enter your search query:");
    return media.next();
  }

}