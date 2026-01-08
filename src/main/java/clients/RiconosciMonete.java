package clients;
import java.util.Optional;
import java.util.Scanner;
import macchinette.Importo;
import macchinette.Moneta;

public class RiconosciMonete {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      try {
        Optional<Moneta> moneta = Moneta.fromImporto(Importo.parse(scanner.nextLine()));
        if (moneta.isEmpty()) {
          System.out.println("invalid");
        } else {
          System.out.println(moneta.get());
        }
      } catch (IllegalArgumentException e) {
        System.out.println("invalid");
      }
    }
    scanner.close();
  }
}
