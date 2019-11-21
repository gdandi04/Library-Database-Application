package libraryDB;

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
//    System.out.println("Enter a search query or type \"exitSearch\" to go back to the homepage.");
    while (scan.hasNext()) {
      if (scan.next().equals("exitSearch")) {
        System.out.println("Exited search command, back to homepage");
        this.db.run();
      } else {
//        String searchCriteria = scan.next();
//        if (this.searchHelp(next)) {
//        String query = "SELECT media_id, media_title FROM media WHERE UPPER(media_title) LIKE ?";
        String query = "SELECT * FROM media";
        try {
          // going to do a search using "upper"
//          searchCriteria = searchCriteria.toUpperCase();

          // create the preparedstatement and add the criteria
          PreparedStatement ps = conn.prepareStatement(query);
//          ps.setString(1, "%" + searchCriteria + "%");

          ResultSet searchResult = ps.executeQuery();

          while (searchResult.next()) {
            ArrayList<String> row = new ArrayList<String>();
            row.add("Media ID: " + searchResult.getNString("media_id"));
            row.add("Media Name: " + searchResult.getNString("media_title"));
            media.add(row);
//          }
          }
          break;
        } catch (SQLException e) {
          System.out.println("Unable to execute query");
          e.printStackTrace();
        }
      }
      for (ArrayList<String> list : media) {
        for (String s : list) {
          System.out.println(s);
        }
      }
    }
  }
//
//  private boolean searchHelp(String oneWord) throws SQLException {
//    Statement search = conn.createStatement();
//    return search.execute("SELECT * FROM media WHERE media_title LIKE '%" + oneWord + "%'");
//  }

}
