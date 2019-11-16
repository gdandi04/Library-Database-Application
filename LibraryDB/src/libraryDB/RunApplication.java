package libraryDB;

public class RunApplication {

  /**
   * Connect to the DB and do some stuff
   * @param args
   */
  public static void main(String[] args) {
    InitializeConnection app = new InitializeConnection(10);
    app.run();
  }

}