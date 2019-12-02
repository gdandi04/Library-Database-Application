package libraryDB;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchMedia {

  private InitializeConnection db;
  private String book;
  private Connection conn;

  public SearchMedia() {
    this.db = new InitializeConnection(15);
    try {
      this.conn = db.getConnection();
    } catch (SQLException e) {
      System.out.println("Cannot connect to database");
      e.printStackTrace();
    }
  }

  public void searchResults() throws SQLException {
    ArrayList<ArrayList<String>> media = new ArrayList<>();
    Scanner scan = new Scanner(System.in);
    System.out.println("Enter a search query:");
    while (scan.hasNext()) {
      String next = scan.next();
      if (next.equals("exitSearch")) {
        System.out.println("Exited search command, back to homepage");
      } else {
        if (this.searchHelp(next)) {
          String sql ="Select name from tablename where like %";
          PreparedStatement statement = conn.prepareStatement(sql);
          statement.setString(1, next + "%");

//          Statement search = conn.createStatement();
//          ResultSet searchResult =
//              search.executeQuery("SELECT * FROM media WHERE media_name LIKE '%"
//                  + next + "%'");
          ResultSet searchResult = statement.executeQuery();
          while (searchResult.next()) {
            ArrayList<String> row = new ArrayList<String>();
            row.add("Media ID: " + searchResult.getNString("media_id"));
            row.add("Media Name: " + searchResult.getNString("media_title"));
            media.add(row);
          }
        }
      }
    }
    for (ArrayList<String> list : media) {
      for (String s : list) {
        System.out.println(s);
      }
    }
  }

  private boolean searchHelp(String oneWord) throws SQLException {
    Statement search = conn.createStatement();
    return search.execute("SELECT * FROM media WHERE media_name LIKE %" + oneWord + "%");
  }

}