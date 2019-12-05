package libraryDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PlaceHold {
  private InitializeConnection db;
  private Connection conn;
  private ArrayList<ArrayList<String>> media;

  public PlaceHold(InitializeConnection db, Connection conn) {
    this.db = db;
    this.conn = conn;
  }

  public void placeItemOnHold() throws SQLException {
    System.out.println("What type of media are you looking for?\nBook: 1\nEbook: 2\nVideo: 3\nCD: 4\nor type \"EXIT\" to exit");
    Scanner scan = new Scanner(System.in);
    String command = scan.next();
    if (command.equals("EXIT")) {
      db.runCommand(conn);
      scan.close();
    } else {
        this.selectMedia(command);
        this.placeHold();
        System.out.println(this.printBooksOnHold());
      }
    }

  public ArrayList<ArrayList<String>> selectMedia(String item) throws SQLException {
    String itemID = item;
      String query = "";
      switch (itemID.toUpperCase()) {
        case "1":
          query = "SELECT * FROM book";
          break;
        case "2":
          query = "SELECT * FROM ebook";
          break;
        case "3":
          query = "SELECT * FROM video";
          break;
        case "4":
          query = "SELECT * FROM cd";
          break;
        case "EXIT":
          this.db.runCommand(conn);
        default:
          System.out.println("No media type available for this input. Select another media");
           Scanner scan = new Scanner(System.in);
           String newItem = scan.next();
           selectMedia(newItem);
      }

      Statement s = conn.createStatement();
      ResultSet rsMediaType = s.executeQuery(query);

      ArrayList<ArrayList<String>> media = new ArrayList<>();


      while (rsMediaType.next()) {
        ArrayList<String> row = new ArrayList<String>();

        row.add("Media ID: " + rsMediaType.getString(1));
        row.add("Media Name: " + rsMediaType.getString(2));
        row.add("Author/Director/Artist: " + rsMediaType.getString(3));
        row.add("Genre " + rsMediaType.getString(5));
        row.add("Number of Copies: " + rsMediaType.getString(6));
        media.add(row);
        System.out.println(row);
      }


      this.media = media;
      return media;
    }

  public void placeHold() throws SQLException {
    System.out.println("Which item would you like to place on hold?");
    Scanner mediaHold = new Scanner(System.in);
    String item = mediaHold.next();
    ArrayList<ArrayList<String>> media = this.printBooksOnHold();
    if (this.containsHoldItem(item, media)) {
      System.out.println("You already have a hold on this item.");
      this.placeItemOnHold();
    } else {
      this.placeHoldItem(item);
      System.out.println("Hold placed successfully.");
      this.placeItemOnHold();
    }
  }

  public void placeHoldItem(String item) throws SQLException {
    if (containsHoldItem(item, media)) {
      CallableStatement placeHold = db.getConnection().prepareCall("{CALL place_hold(?, ?)}");
      placeHold.setInt("mediaID", Integer.parseInt(item));
      placeHold.setInt("memberID", db.getUserID());
      placeHold.execute();
    } else {
      Scanner scanner = new Scanner(System.in);
      System.out.println("No such media item found. Try again or type \"EXIT\" to return to home.");
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
  }

  public ArrayList<ArrayList<String>> printBooksOnHold() {
    try {
      Statement mediaHolds = conn.createStatement();
      ResultSet rsMediaHolds =
            mediaHolds.executeQuery("SELECT * FROM media_holds WHERE member_id =" + db.getUserID());

      Statement mediaName = conn.createStatement();
      ResultSet rsMediaName =
            mediaName.executeQuery("SELECT media_title FROM media JOIN media_holds WHERE "
                  + "media.media_id = media_holds.media_id AND member_id =" + db.getUserID());

      ArrayList<ArrayList<String>> media = new ArrayList<>();


      while (rsMediaHolds.next() && rsMediaName.next()) {
        ArrayList<String> row = new ArrayList<String>();

        row.add("Media ID: " + rsMediaHolds.getString("media_id"));
        row.add("Media Name: " + rsMediaName.getString("media_title"));
        media.add(row);
      }

      if (media.size() == 0) {
        System.out.println("You have no holds in your account.\n");
        this.db.runCommand(this.conn);

      }
      return media;
    } catch (SQLException e) {
      System.out.println("Client error");
      e.printStackTrace();
    }
    return media;
  }

  private boolean containsHoldItem(String item, ArrayList<ArrayList<String>> books) {
    int itemID = Integer.parseInt(item);
    boolean containsMediaItem = false;
    for (ArrayList<String> l : books) {
      if (l.get(0).substring(10, 11).equals(Integer.toString(itemID))) {
        containsMediaItem = true;
      }
    }
    return containsMediaItem;
  }

}
