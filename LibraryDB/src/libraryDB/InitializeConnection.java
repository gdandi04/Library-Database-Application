/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

db.mysql.url="jdbc:mysql://localhost:3306/db?characterEncoding=UTF-8&useSSL=false"
 */

package libraryDB;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    DELETE, HOLD, CHECKOUT, SEARCH, SIGNOUT;
  }

  /** The name of the table we are testing with */
//	private final String tableName = "JDBC_TEST";
  private final boolean useSSL = false;

  public InitializeConnection() {
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
      System.out.println("Connected to database\n\n\n");
      this.runCommand(conn);
    } catch (SQLException e) {
      System.out.println("ERROR: Could not connect to the database");
      e.printStackTrace();
      return;
    }
  }

    public void runCommand (Connection conn) {
      this.possibleCommands();

      Scanner scan = new Scanner(System.in);
      String command = scan.next();

      switch (command) {
        case "DELETE":
          try {
            this.deleteItem(conn);
          } catch (SQLException e) {
            System.out.println("Unable to execute command.");
            e.printStackTrace();
          }
          break;

        case "HOLD":
          try {
            PlaceHold ph = new PlaceHold(this, conn);
            ph.placeItemOnHold();
          } catch (SQLException e) {
            System.out.println("Unable to execute command");
            e.printStackTrace();
          }
          break;

        case "CHECKOUT":
          System.out.println(Commands.CHECKOUT.name());
          break;

        case "SEARCH":
          SearchMedia search = new SearchMedia(this);
          try {
            search.searchResults();
          } catch (SQLException e) {
            System.out.println("Unable to execute command.");
            e.printStackTrace();
          }
          break;

        case "SIGNOUT":
          System.out.println("Would you like to sign out?\n\'Yes\' or \'No\'");
          String s = scan.next();
            if (s.equals("Yes")) {
              System.out.println("Goodbye");
              scan.close();
              break;
            } else if (s.equals("No")) {
              this.runCommand(conn);
            }
            break;
        default:
          System.out.println("Invalid command\n");
          this.runCommand(conn);
      }
      scan.close();
    }

    private void deleteItem(Connection conn) throws SQLException {
      DeleteHold dh = new DeleteHold(this);
      System.out.println("Enter the ID of the media item you'd like to delete from your "
            + "holds list or type \"EXIT\" to return to home.");
      Scanner deleteScanner = new Scanner(System.in);
      System.out.println(dh.printBooksOnHold());
      if (deleteScanner.next().equals("EXIT")) {
       // deleteScanner.close();
        this.runCommand(conn);
      } else {
        String item = deleteScanner.next();
        dh.deleteHoldItem(item);
        this.deleteItem(conn);
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