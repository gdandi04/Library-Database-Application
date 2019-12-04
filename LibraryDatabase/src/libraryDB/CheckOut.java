package libraryDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CheckOut {
  private InitializeConnection db;
  private Connection conn;
  ArrayList<ArrayList<String>> media;

  CheckOut(InitializeConnection db, Connection conn) {
    this.db = db;
    this.conn = conn;
  }

  public void showAllMedia() throws SQLException {
    System.out.println("What type of media are you looking for?\nBook: 1\nEbook: 2\nVideo: 3\nCD: 4\nor type \"EXIT\" to exit");
    Scanner scan = new Scanner(System.in);
    String command = scan.next();
    if (command.equals("EXIT")) {
      db.runCommand(conn);
    } else {
      this.selectMedia(command);
      System.out.println("Your Rented Media:\n" + this.printRentedMedia() + "\n\n");
    }
  }

  public void selectMedia(String item) throws SQLException {
    int itemID = Integer.parseInt(item);
    String query = "";
    String available = "WHERE num_copies > 0";
    switch (itemID) {
      case 1:
        query = "SELECT * FROM book " + available;
        break;
      case 2:
        query = "SELECT * FROM ebook " + available;
        break;
      case 3:
        query = "SELECT * FROM video " + available;
        break;
      case 4:
        query = "SELECT * FROM cd " + available;
        break;
      default:
        System.out.println("No media type available for this input. Select another media");
        Scanner scan = new Scanner(System.in);
        String newItem = scan.next();
        selectMedia(newItem);
    }
    Statement s = conn.createStatement();
    ResultSet rsMediaType = s.executeQuery(query);
    this.printAllMedia(rsMediaType);
  }

  public void printAllMedia(ResultSet rs) throws SQLException {
    ArrayList<ArrayList<String>> media = new ArrayList<>();

    while (rs.next()) {
      ArrayList<String> row = new ArrayList<String>();

      row.add("Media ID: " + rs.getString(1));
      row.add("Media Name: " + rs.getString(2));
      row.add("Author/Director/Artist: " + rs.getString(3));
      row.add("Genre " + rs.getString(5));
      row.add("Number of Copies: " + rs.getString(6));
      media.add(row);
      System.out.println(row);
    }
    this.media = media;

    if (media.size() < 1) {
      System.out.println("There are currently no media for this type");
      this.showAllMedia();
    } else {
      this.rentMedia();
    }
  }

  public void rentMedia() {
    System.out.println("Which item would you like to checkout? (or type \"EXIT\" to go back to homepage)");
    Scanner mediaHold = new Scanner(System.in);
    String item = mediaHold.next();
    if (item.equals("EXIT")) {
      db.runCommand(conn);
    }
    else if (this.containsHoldItem(item)) {
      this.checkout(item);
    } else {
      System.out.println("No such media item found. Try again or type \"EXIT\" to return to home.");
      this.exit();
    }
  }

  public void checkout(String item) {
    try {
      CallableStatement check = db.getConnection().prepareCall("{CALL add_rented_media(?, ?)}");
      check.setInt("mediaID", Integer.parseInt(item));
      check.setInt("memberID", db.getUserID());
      check.execute();
    } catch (SQLException e) {
      System.out.println("Error. Please try again.");
      this.rentMedia();
    }
  }

  public ArrayList<ArrayList<String>> printRentedMedia() {
    try {
      Statement mediaHolds = conn.createStatement();
      ResultSet rsMediaHolds =
            mediaHolds.executeQuery("SELECT * FROM member_rents_media WHERE member_id =" + db.getUserID());

      Statement mediaName = conn.createStatement();
      ResultSet rsMediaName =
            mediaName.executeQuery("SELECT media_title FROM media JOIN member_rents_media WHERE "
                  + "media.media_id = member_rents_media.media_id AND member_id =" + db.getUserID());

      ArrayList<ArrayList<String>> media = new ArrayList<>();


      while (rsMediaHolds.next() && rsMediaName.next()) {
        ArrayList<String> row = new ArrayList<String>();

        row.add("Media ID: " + rsMediaHolds.getString("media_id"));
        row.add("Media Name: " + rsMediaName.getString("media_title"));
        media.add(row);
      }

      if (media.size() == 0) {
        System.out.println("You have currently not rented out any media from this account.\n");
        this.db.runCommand(this.conn);
      }
      return media;
    } catch (SQLException e) {
      System.out.println("Client error");
      e.printStackTrace();
    }
   return null;
  }

  public void exit() {
    Scanner scanner = new Scanner(System.in);

    String input = scanner.next();
    if (input.equals("EXIT")) {
      db.runCommand(conn);
    } else {
      try {
        selectMedia(input);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  private boolean containsHoldItem(String item) {
    int itemID = Integer.parseInt(item);
    boolean containsMediaItem = false;
    for (ArrayList<String> l : this.media) {
      if (l.get(0).substring(10, 11).equals(Integer.toString(itemID))) {
        containsMediaItem = true;
      }
    }
    return containsMediaItem;
  }


}
