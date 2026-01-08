package clients;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import macchinette.Prodotto;

public class OrdinaProdotti {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    List<Prodotto> prodotti = new ArrayList<>();

    while (scanner.hasNextLine()) {
      prodotti.add(Prodotto.parse(scanner.nextLine()));
    }

    Collections.sort(prodotti);

    for (Prodotto p : prodotti) {
      System.out.println(p);
    }
    scanner.close();
  }
}
