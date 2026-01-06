/*

Copyright 2024 Massimo Santini

This file is part of "Programmazione 2 @ UniMI" teaching material.

This is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This material is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this file.  If not, see <https://www.gnu.org/licenses/>.

*/

package clients;

import java.util.Optional;
import java.util.Scanner;
import macchinette.Aggregato;
import macchinette.Importo;
import macchinette.StrategiaResto;
import macchinette.StrategiaRestoH;
import macchinette.StrategiaRestoL;

/** Client che calcola il resto utilizzando diverse strategie. */
public class CalcolaResti {

  /**
   * Metodo principale.
   *
   * @param args argomenti della linea di comando: strategia (H o L) e importo del resto
   */
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Uso: CalcolaResti <H|L> <importo>");
      return;
    }

    // Determina la strategia
    StrategiaResto strategia;
    String strategiaStr = args[0].trim().toUpperCase();
    if (strategiaStr.equals("H")) {
      strategia = StrategiaRestoH.INSTANCE;
    } else if (strategiaStr.equals("L")) {
      strategia = StrategiaRestoL.INSTANCE;
    } else {
      System.err.println("Strategia non valida: " + args[0]);
      return;
    }

    // Parse dell'importo del resto
    Importo resto;
    try {
      resto = Importo.parse(args[1]);
    } catch (IllegalArgumentException e) {
      System.err.println("Importo non valido: " + args[1]);
      return;
    }

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine().trim();
      if (line.isEmpty()) continue;

      Aggregato disponibile;
      try {
        disponibile = Aggregato.parse(line);
      } catch (IllegalArgumentException e) {
        continue;
      }

      // Verifica se il valore totale è sufficiente
      if (disponibile.valoreTotale().minoreDi(resto)) {
        System.out.println("value");
        continue;
      }

      // Calcola il resto
      Optional<Aggregato> risultato = strategia.calcola(resto, disponibile);
      if (risultato.isEmpty()) {
        System.out.println("change");
      } else {
        System.out.println(risultato.get());
      }
    }

    scanner.close();
  }
}
