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
  private String book;
  private Connection conn;

  public DeleteHold() {
    this.db = new InitializeConnection(15);
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

    ArrayList<ArrayList<String>> media = new ArrayList<>();

    while (rsMediaHolds.next()) {
      ArrayList<String> row = new ArrayList<String>();

      Statement mediaName = conn.createStatement();
      ResultSet rsMediaName =
          mediaName.executeQuery("SELECT media_title FROM media WHERE media_id = " +
              rsMediaHolds.getNString(2));

      row.add("Media ID: " + rsMediaHolds.getNString("media_id"));
      row.add("Media Name: " + rsMediaName.getNString("media_title"));

      media.add(row);
    }

    for (ArrayList<String> list : media) {
      for (String s : list) {
        System.out.println(s);
      }
    }

    return media;
  }

  public void deleteHoldItem(String item) throws SQLException {
    if (containsHoldItem(item)) {
      CallableStatement delete = db.getConnection().prepareCall("{CALL delete_hold(?)}");
      delete.setString("media_id", item);
    } else {
      Scanner scanner = new Scanner(System.in);
      System.out.println("No such media item found in your holds. Try again or type \"exit\" to return to all commands.");
      String input = scanner.next();
      if (input.equals("exit")) {
        System.out.println("end this command");
      } else {
        deleteHoldItem(input);
      }
    }
  }

  private boolean containsHoldItem(String item) throws SQLException {
    ArrayList<ArrayList<String>> media = this.printBooksOnHold();

    boolean containsMediaItem = false;
    for (ArrayList<String> l : media) {
      if (l.contains(item)) {
        containsMediaItem = true;
      }
    }
    return containsMediaItem;
  }

}
