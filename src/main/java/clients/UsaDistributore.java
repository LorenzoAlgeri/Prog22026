package clients;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import macchinette.Aggregato;
import macchinette.Binario;
import macchinette.Distributore;
import macchinette.ErogazioneException;
import macchinette.Prodotto;
import macchinette.StrategiaRestoH;

public class UsaDistributore {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    List<Binario> binari = new ArrayList<>();
    for (String parte : scanner.nextLine().split(",")) {
      try {
        binari.add(Binario.parse(parte.trim()));
      } catch (IllegalArgumentException e) {
      }
    }

    Aggregato fondoCassa;
    try {
      fondoCassa = Aggregato.parse(scanner.nextLine());
    } catch (IllegalArgumentException e) {
      fondoCassa = new Aggregato();
    }

    Distributore distributore = new Distributore(binari, fondoCassa, StrategiaRestoH.INSTANCE);

    while (scanner.hasNextLine()) {
      try {
        String line = scanner.nextLine();
        char comando = line.charAt(0);
        String[] p = line.substring(1).trim().split(",", 2);

        if (comando == '+') {
          System.out.println("+ " + distributore.carica(Prodotto.parse(p[1].trim()), Integer.parseInt(p[0].trim())));
        } else if (comando == '-') {
          try {
            Aggregato resto = distributore.eroga(Integer.parseInt(p[0].trim()), Aggregato.parse(p[1].trim()));
            System.out.println("- " + (resto.vuoto() ? "<>" : resto));
          } catch (ErogazioneException e) {
            System.out.println("- " + e.getMessage());
          }
        } else {
          Iterator<Distributore.BinarioConIndice> it = distributore.binariNonVuoti();
          while (it.hasNext()) {
            Distributore.BinarioConIndice bci = it.next();
            Prodotto prod = bci.binario().prodotto();
            System.out.println("? " + bci.indice() + " | " + prod.nome() + " | " + prod.prezzo());
          }
        }
      } catch (Exception e) {
      }
    }
    scanner.close();
  }
}
