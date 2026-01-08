package clients;
import java.util.Scanner;
import macchinette.Binario;
import macchinette.BinarioException;
import macchinette.Prodotto;
import macchinette.Taglia;

public class CaricaBinario {

  public static void main(String[] args) {
    Binario binario = new Binario(Taglia.parse(args[1]), Integer.parseInt(args[0]));
    System.out.println(binario);
    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String[] parti = scanner.nextLine().split(",", 2);
      try {
        binario.carica(Prodotto.parse(parti[1].trim()), Integer.parseInt(parti[0].trim()));
        System.out.println(binario);
      } catch (BinarioException e) {
        System.out.println(e.getMessage());
      }
    }
    scanner.close();
  }
}
