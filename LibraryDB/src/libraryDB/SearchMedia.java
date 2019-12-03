package libraryDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchMedia {

  private InitializeConnection db;
  private String book;
  private Connection conn;

  public SearchMedia(InitializeConnection db) {
    this.db = db;
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
   // while (scan.hasNext()) {
    String next = scan.nextLine();
    Scanner nextItem = new Scanner(next);
    while (nextItem.hasNext()) {
      String query = nextItem.next();
      System.out.println(query);
      if (next.equals("exitSearch")) {
        System.out.println("Exited search command, back to homepage");
      } else {
        if (this.searchHelp(query)) {
          Statement search = conn.createStatement();
          ResultSet searchResult =
                search.executeQuery("SELECT * FROM media WHERE media_title LIKE '%"
                      + next + "%'");
          while (searchResult.next()) {
            ArrayList<String> row = new ArrayList<String>();
            row.add("Media ID: " + searchResult.getString("media_id"));
            row.add("Media Name: " + searchResult.getString("media_title"));
            media.add(row);
            System.out.println(row);
          }
        }
      }
    }
//    for (ArrayList<String> list : media) {
//      for (String s : list) {
//        System.out.println(s);
//      }
//    }
  }

  private boolean searchHelp(String oneWord) throws SQLException {
    Statement search = conn.createStatement();
    return search.execute("SELECT * FROM media WHERE media_title LIKE '%" + oneWord + "%'");
  }

}