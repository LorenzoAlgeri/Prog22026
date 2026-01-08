
package clients;

import java.util.Optional;
import java.util.Scanner;
import macchinette.Aggregato;
import macchinette.Importo;
import macchinette.StrategiaResto;
import macchinette.StrategiaRestoH;
import macchinette.StrategiaRestoL;

public class CalcolaResti {
  public static void main(String[] args) {
    StrategiaResto strategia;
    if (args[0].equals("H")) {
      strategia = StrategiaRestoH.INSTANCE;
    } else {
      strategia = StrategiaRestoL.INSTANCE;
    }
    Importo resto = Importo.parse(args[1]);
    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      Aggregato disponibile = Aggregato.parse(line);
      
      if (disponibile.valoreTotale().minoreDi(resto)) {
        System.out.println("value");
      } else {
        Optional<Aggregato> risultato = strategia.calcola(resto, disponibile);
        System.out.println(risultato.isEmpty() ? "change" : risultato.get());
      }
    }
    scanner.close();
  }
}
