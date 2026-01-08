package clients;
import java.util.Scanner;
import macchinette.Importo;
import macchinette.Moneta;

public class RiconosciMonete {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      try {
        Moneta moneta = Moneta.fromImporto(Importo.parse(scanner.nextLine()));
        if (moneta == null) {
          System.out.println("invalid");
        } else {
          System.out.println(moneta);
        }
      } catch (IllegalArgumentException e) {
        System.out.println("invalid");
      }
    }
    scanner.close();
  }
}
