package libraryDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class DeleteHold {

  private InitializeConnection db;
  private Connection conn;

  public DeleteHold(InitializeConnection init) {
    this.db = init;
    try {
      this.conn = db.getConnection();
    } catch (SQLException e) {
      System.out.println("Cannot connect to database");
      e.printStackTrace();
    }
  }

  public ArrayList<ArrayList<String>> printBooksOnHold() throws SQLException {
    Statement mediaHolds = conn.createStatement();
    ResultSet rsMediaHolds =
        mediaHolds.executeQuery("SELECT * FROM media_holds WHERE member_id =" + db.getUserID());

    Statement mediaName = conn.createStatement();
    ResultSet rsMediaName =
        mediaName.executeQuery("SELECT media_title FROM media JOIN media_holds WHERE media.media_id = media_holds.media_id AND member_id =" + db.getUserID());

    ArrayList<ArrayList<String>> media = new ArrayList<>();


    while (rsMediaHolds.next() && rsMediaName.next()) {
      ArrayList<String> row = new ArrayList<String>();

      row.add("Media ID: " + rsMediaHolds.getString("media_id"));
      row.add("Media Name: " + rsMediaName.getString("media_title"));
      media.add(row);
//      System.out.println(row);
    }

    if (media.size() == 0) {
      System.out.println("You have no holds in your account.");
      this.db.run();
    }

    return media;
  }

  public void deleteHoldItem(String item) throws SQLException {
    if (item.equals("cancel")) {
      this.db.run();
    }
    int itemID = Integer.parseInt(item);
    if (containsHoldItem(itemID)) {
      CallableStatement delete = db.getConnection().prepareCall("{CALL delete_hold(?)}");
      delete.setInt("media_id", itemID);
      delete.execute();
    } else {
      Scanner scanner = new Scanner(System.in);
      System.out.println("No such media item found in your holds. Try again or type \"exit\" to return to all commands.");
      String input = scanner.next();
      if (input.equals("exit")) {
        System.out.println("end this command");
      } else {
        try {
          deleteHoldItem(input);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private boolean containsHoldItem(int item) throws SQLException {
    ArrayList<ArrayList<String>> media = this.printBooksOnHold();

    boolean containsMediaItem = false;
    for (ArrayList<String> l : media) {
      if (l.get(0).substring(10, 11).equals(Integer.toString(item))) {
        containsMediaItem = true;
      }
    }

    return containsMediaItem;
  }
}