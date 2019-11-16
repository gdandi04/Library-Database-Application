package libraryDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

  private void printBooksOnHold() throws SQLException {
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

      row.add("Media ID: " + rsMediaHolds.getNString(2));
      row.add("Media Name: " + rsMediaName.getNString("media_title"));

      media.add(row);
    }

    for (ArrayList<String> list : media) {
      for (String s : list) {
        System.out.println(s);
      }
    }

  }

}
