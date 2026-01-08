package clients;
import java.util.Scanner;
import macchinette.Importo;

public class OperazioniImporti {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      try {
        String line = scanner.nextLine();
        String[] p;
        if (line.contains(" + ")) {
          p = line.split("\\s*\\+\\s*", 2);
          System.out.println(Importo.parse(p[0]).somma(Importo.parse(p[1])));
        } else if (line.contains(" - ")) {
          p = line.split("\\s*-\\s*", 2);
          try {
            System.out.println(Importo.parse(p[0]).sottrai(Importo.parse(p[1])));
          } catch (IllegalArgumentException e) {
            System.out.println("negative");
          }
        } else if (line.contains(" * ")) {
          p = line.split("\\s*\\*\\s*", 2);
          int n = Integer.parseInt(p[1].trim());
          if (n < 0) {
            System.out.println("negative");
          } else {
            System.out.println(Importo.parse(p[0]).moltiplica(n));
          }
        } else {
          p = line.split("\\s*/\\s*", 2);
          Importo divisore = Importo.parse(p[1]);
          if (divisore.equals(Importo.ZERO)) {
            System.out.println("invalid");
          } else {
            System.out.println(Importo.parse(p[0]).dividi(divisore));
          }
        }
      } catch (Exception e) {
        System.out.println("invalid");
      }
    }
    scanner.close();
  }
}
