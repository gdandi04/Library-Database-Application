package libraryDB;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchMedia {

  private InitializeConnection db;
  private Connection conn;

  public SearchMedia(InitializeConnection init) {
    this.db = init;
    try {
      this.conn = db.getConnection();
    } catch (SQLException e) {
      System.out.println("Cannot connect to database");
      e.printStackTrace();
    }
  }

  public void searchBook(String item) throws SQLException {
    CallableStatement search = db.getConnection().prepareCall("{CALL search_book(?)}");
    search.setString("search_query", item);
    ResultSet rs = search.executeQuery();

    ArrayList<ArrayList<String>> results = new ArrayList<>();

    while (rs.next()) {
      ArrayList<String> row = new ArrayList<>();
      row.add("Book ID: " + rs.getString("book_id"));
      row.add("Title: " + rs.getString("title"));
      row.add("Author: " + rs.getString("author"));
      row.add("Genre: " + rs.getString("genre"));
      row.add("Plot: " + rs.getString("plot"));
      row.add("Year of Publication: " + rs.getString("pubYear"));
      row.add("Page Count: " + rs.getString("pageCount"));
      row.add("Number of Copies Available: " + rs.getString("numCopies"));
      results.add(row);
    }

    if (results.isEmpty()) {
      System.out.println("No items found that meet this criteria");
    } else {
      for (ArrayList<String> list : results) {
        for (String s : list) {
          System.out.println(s);
        }
      }
    }
  }

  public void searchEBook(String item) throws SQLException {
    CallableStatement search = db.getConnection().prepareCall("{CALL search_ebook(?)}");
    search.setString("search_query", item);
    ResultSet rs = search.executeQuery();

    ArrayList<ArrayList<String>> results = new ArrayList<>();

    while (rs.next()) {
      ArrayList<String> row = new ArrayList<>();
      row.add("Book ID: " + rs.getString("book_id"));
      row.add("Title: " + rs.getString("title"));
      row.add("Author: " + rs.getString("author"));
      row.add("Genre: " + rs.getString("genre"));
      row.add("Plot: " + rs.getString("plot"));
      row.add("Year of Publication (print): " + rs.getString("printPubYear"));
      row.add("Year of Publication (online): " + rs.getString("ePubYear"));
      row.add("Page Count: " + rs.getString("pageCount"));
      row.add("Number of Copies Available: " + rs.getString("num_copies"));
      results.add(row);
    }

    if (results.isEmpty()) {
      System.out.println("No items found that meet this criteria");
    } else {
      for (ArrayList<String> list : results) {
        for (String s : list) {
          System.out.println(s);
        }
      }
    }
  }

  public void searchVideo(String item) throws SQLException {
    CallableStatement search = db.getConnection().prepareCall("{CALL search_video(?)}");
    search.setString("search_query", item);
    ResultSet rs = search.executeQuery();

    ArrayList<ArrayList<String>> results = new ArrayList<>();

    while (rs.next()) {
      ArrayList<String> row = new ArrayList<>();
      row.add("Video ID: " + rs.getString("video_id"));
      row.add("Title: " + rs.getString("title"));
      row.add("Director: " + rs.getString("director"));
      row.add("Actors: " + rs.getString("actors"));
      row.add("Genre: " + rs.getString("genre"));
      row.add("Plot: " + rs.getString("plot"));
      row.add("Runtime: " + rs.getString("runtime"));
      row.add("Year of Release: " + rs.getString("year_released"));
      row.add("Number of Copies Available: " + rs.getString("num_copies"));
      results.add(row);
    }

    if (results.isEmpty()) {
      System.out.println("No items found that meet this criteria");
    } else {
      for (ArrayList<String> list : results) {
        for (String s : list) {
          System.out.println(s);
        }
      }
    }
  }

  public void searchCD(String item) throws SQLException {
    CallableStatement search = db.getConnection().prepareCall("{CALL search_cd(?)}");
    search.setString("search_query", item);
    ResultSet rs = search.executeQuery();

    ArrayList<ArrayList<String>> results = new ArrayList<>();

    while (rs.next()) {
      ArrayList<String> row = new ArrayList<>();
      row.add("CD ID: " + rs.getString("cd_id"));
      row.add("Album Name: " + rs.getString("album_name"));
      row.add("Artist: " + rs.getString("artist"));
      row.add("Genre: " + rs.getString("genre"));
      row.add("Song List: " + rs.getString("song_list"));
      row.add("Year of Release: " + rs.getString("year_released"));
      row.add("Number of Copies Available: " + rs.getString("num_copies"));
      results.add(row);
    }

    if (results.isEmpty()) {
      System.out.println("No items found that meet this criteria");
    } else {
      for (ArrayList<String> list : results) {
        for (String s : list) {
          System.out.println(s);
        }
      }
    }
  }

  private boolean searchHelp(String oneWord) throws SQLException {
    Statement search = conn.createStatement();
    return search.execute("SELECT * FROM media WHERE media_name LIKE %" + oneWord + "%");
  }

}