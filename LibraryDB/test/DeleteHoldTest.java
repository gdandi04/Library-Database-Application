import java.sql.SQLException;
import libraryDB.DeleteHold;
import org.junit.Test;

class DeleteHoldTest {

  @Test
  public void printBooksOnHold() throws SQLException {
    DeleteHold dh = new DeleteHold();
    dh.printBooksOnHold();
  }
}