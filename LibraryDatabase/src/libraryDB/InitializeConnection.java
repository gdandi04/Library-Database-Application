package libraryDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author kath
 */
public class InitializeConnection {

  /**
   * The name of the MySQL account to use (or empty for anonymous)
   */
  private final String userName;

  /**
   * The password for the MySQL account (or empty for anonymous)
   */
  private final String password;

  /**
   * The name of the computer running MySQL
   */
  private final String serverName = "localhost";

  /**
   * The port of the MySQL server (default is 3306)
   */
  private final int portNumber = 3306;

  /**
   * The name of the database we are testing with (this default is installed with MySQL)
   */
  private final String dbName = "universityLibrarydb";

  private int userID;

  public enum Commands {
    DELETE, HOLD, CHECKOUT, SEARCH,
    VIEW_HOLDS, VIEW_RENTED, SIGNOUT;
  }

  /**
   * The name of the table we are testing with
   */
  private final boolean useSSL = false;

  public InitializeConnection() {
    Scanner in = new Scanner(System.in);
    System.out.println("Username: ");
    this.userName = in.next();
    System.out.println("Password: ");
    this.password = in.next();
    System.out.println("User id: ");
    this.setUserId();
  }

  private void setUserId() {
    Scanner in = new Scanner(System.in);
    try {
      this.userID = Integer.parseInt(in.next());
    } catch (NumberFormatException e) {
      System.out.println("Please enter a valid user id");
      this.setUserId();
    }
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
   * Connect to MySQL and do some stuff.
   */
  public void run() {
    // Connect to MySQL
    Connection conn = null;
    try {
      conn = this.getConnection();
      this.runCommand(conn);
    } catch (SQLException e) {
      System.out.println("ERROR: Could not connect to the database");
      InitializeConnection retry = new InitializeConnection();
      retry.run();
    }
  }

  public void runCommand(Connection conn) {
    this.possibleCommands();

    Scanner scan = new Scanner(System.in);
    String command = scan.next();

    switch (command.toUpperCase()) {
      case "DELETE":
        try {
          DeleteHold dh = new DeleteHold(this);
          dh.deleteItem();
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
        try {
          CheckOut ch = new CheckOut(this, conn);
          ch.showAllMedia();
          ch.rentMedia();

        } catch (SQLException e) {
          e.printStackTrace();

        }
        break;

      case "SEARCH":
        SearchMedia sm = new SearchMedia(this);
        SearchMedia searchMedia = new SearchMedia(this);
        try {
          String mediaType = this.userChoice();

          String mediaQuery = this.searchQuery();
          switch (mediaType) {
            case "1":
              System.out.println("Searching Books");
              searchMedia.searchBook(mediaQuery);
              this.exitOut(mediaType);
              mediaQuery = this.searchQuery();
              break;
            case "2":
              System.out.println("Searching E-Books");
              searchMedia.searchEBook(mediaQuery);
              this.exitOut(mediaType);
              mediaQuery = this.searchQuery();
              break;
            case "3":
              System.out.println("Searching Videos");
              searchMedia.searchVideo(mediaQuery);
              this.exitOut(mediaType);
              mediaQuery = this.searchQuery();
              break;
            case "4":
              System.out.println("Searching CDs");
              searchMedia.searchCD(mediaQuery);
              this.exitOut(mediaType);
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

      case "VIEW_HOLDS":
        PlaceHold ph = new PlaceHold(this, conn);
        System.out.println(ph.printBooksOnHold() + "\n\n\n");
        this.runCommand(conn);
        break;

      case "VIEW_RENTED":
        CheckOut ch = new CheckOut(this, conn);
        System.out.println(ch.printRentedMedia() + "\n\n\n");
        this.runCommand(conn);
        break;

      case "SIGNOUT":
        System.out.println("Would you like to sign out?\n\'Yes\' or \'No\'");
        String s = scan.next();
        if (s.equalsIgnoreCase("Yes")) {
          System.out.println("Goodbye");
          scan.close();
          break;
        } else if (s.equalsIgnoreCase("No")) {
          this.runCommand(conn);
        }
        break;
      default:
        System.out.println("Invalid command\n");
        this.runCommand(conn);
    }
  }

  int getUserID() {
    return this.userID;
  }

  private boolean checkIfExit(String choice) {
    if (choice.equalsIgnoreCase("exit")) {
      return true;
    }
    return false;
  }

  private void possibleCommands() {
    System.out.println("Please enter one of the following possible commands: ");
    System.out.println(Arrays.asList(Commands.values()));
  }

  private void exitOut(String mediaType) throws SQLException {
    mediaType = this.userChoice();
    if (this.checkIfExit(mediaType)) {
      this.runCommand(this.getConnection());
    }
  }

  private void displayMediaTypes() {
    System.out.println(
        "Choose the type of media you would like to search for by typing the corresponding number.");
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