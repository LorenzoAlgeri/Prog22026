package clients;
import java.util.Scanner;
import macchinette.Aggregato;
import macchinette.AggregatoInsufficienteException;

public class OperazioniAggregati {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Aggregato corrente = new Aggregato();

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      Aggregato operando = Aggregato.parse(line.substring(1).trim());
      
      if (line.charAt(0) == '+') {
        corrente.aggiungi(operando);
        System.out.println(corrente);
      } else {
        try {
          corrente.rimuovi(operando);
          System.out.println(corrente);
        } catch (AggregatoInsufficienteException e) {
          System.out.println(e.getMessage());
        }
      }
    }
    scanner.close();
  }
}
