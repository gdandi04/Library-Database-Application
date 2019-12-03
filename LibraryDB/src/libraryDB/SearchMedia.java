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
<<<<<<< HEAD
    System.out.println("Enter a search query:");
   // while (scan.hasNext()) {
    String next = scan.nextLine();
    Scanner nextItem = new Scanner(next);
    while (nextItem.hasNext()) {
      String query = nextItem.next();
      System.out.println(query);
      if (next.equals("exitSearch")) {
=======
//    System.out.println("Enter a search query or type \"exitSearch\" to go back to the homepage.");
    while (scan.hasNext()) {
      if (scan.next().equals("exitSearch")) {
>>>>>>> e43ca9ea7988b2a96527eca2c99f03b3d63f2160
        System.out.println("Exited search command, back to homepage");
        this.db.run();
      } else {
<<<<<<< HEAD
        if (this.searchHelp(query)) {
          Statement search = conn.createStatement();
          ResultSet searchResult =
                search.executeQuery("SELECT * FROM media WHERE media_title LIKE '%"
                      + next + "%'");
=======
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

>>>>>>> e43ca9ea7988b2a96527eca2c99f03b3d63f2160
          while (searchResult.next()) {
            ArrayList<String> row = new ArrayList<String>();
            row.add("Media ID: " + searchResult.getString("media_id"));
            row.add("Media Name: " + searchResult.getString("media_title"));
            media.add(row);
<<<<<<< HEAD
            System.out.println(row);
=======
//          }
>>>>>>> e43ca9ea7988b2a96527eca2c99f03b3d63f2160
          }
          break;
        } catch (SQLException e) {
          System.out.println("Unable to execute query");
          e.printStackTrace();
        }
      }
<<<<<<< HEAD
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
=======
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
>>>>>>> e43ca9ea7988b2a96527eca2c99f03b3d63f2160

}