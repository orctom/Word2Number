import org.junit.Test;
import utils.Word2Number;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TxtTest {
  @Test
  public void test() throws IOException {
    Path path = Paths.get("/Users/xm180320/Downloads/labels.txt");
    Files.readAllLines(path).forEach(s -> System.out.println(Word2Number.replace(s.substring(0, s.indexOf('(')))));
  }
}
