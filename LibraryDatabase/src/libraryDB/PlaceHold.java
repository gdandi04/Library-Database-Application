package libraryDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PlaceHold {
  private InitializeConnection db;
  private Connection conn;

  public PlaceHold(InitializeConnection db, Connection conn) {
    this.db = db;
    this.conn = conn;
  }

  public ArrayList<ArrayList<String>> selectMedia() throws SQLException {
    Scanner mediaType = new Scanner(System.in);
    System.out.println("What type of media are you looking for?\nBook: 1\nEbook: 2\nVideo: 3\nCD: 4");
    int type = mediaType.nextInt();
    String query = "";
    switch (type) {
      case 1:
        query = "SELECT * FROM book";
        break;
      case 2:
        query = "SELECT * FROM ebook";
        break;
      case 3:
        query = "SELECT * FROM video";
        break;
      case 4:
        query = "SELECT * FROM cd";
        break;
      default:
        System.out.println("No media type available for this input.");
        // add try again
    }

    Statement s = conn.createStatement();
    ResultSet rsMediaType = s.executeQuery(query);

    ArrayList<ArrayList<String>> media = new ArrayList<>();


    while (rsMediaType.next()) {
      ArrayList<String> row = new ArrayList<String>();

      row.add("Media ID: " + rsMediaType.getString(1));
      row.add("Media Name: " + rsMediaType.getString(2));
      row.add("Author/Director/Artist: " + rsMediaType.getString(3));
      row.add("Number of Copies: " + rsMediaType.getString(5));
      row.add("Genre: " + rsMediaType.getString(6));
      media.add(row);
      System.out.println(row);
    }


    return media;
  }

  public void placeHoldItem(String item) {
    Scanner mediaHold = new Scanner(System.in);
    String item = mediaHold.next();

    if (item.equals("cancel")) {
      this.db.run();
    }
    int itemID = Integer.parseInt(item);
    if (containsHoldItem(itemID, media)) {
      CallableStatement placeHold = db.getConnection().prepareCall("{CALL place_hold(?, ?)}");
      placeHold.setInt("mediaID", itemID);
      placeHold.setInt("memberID", db.getUserID());
      placeHold.execute();
    } else {
      Scanner scanner = new Scanner(System.in);
      System.out.println("No such media item found. Try again or type \"exit\" to return to home.");
      String input = scanner.next();
      if (input.equals("exit")) {
        System.out.println("end this command");
      } else {
        try {
          selectMedia();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }    // print out the holds list after it's been updated, limit 5 books on hold per member id
  }

  private boolean containsHoldItem(int item, ArrayList<ArrayList<String>> books) throws SQLException {

    boolean containsMediaItem = false;
    for (ArrayList<String> l : books) {
      if (l.get(0).substring(10, 11).equals(Integer.toString(item))) {
        containsMediaItem = true;
      }
    }
    return containsMediaItem;
  }

}
