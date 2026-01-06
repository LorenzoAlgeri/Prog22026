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

import java.util.Scanner;
import macchinette.Importo;
import macchinette.Moneta;

/** Client che legge importi e stampa il valore della moneta corrispondente o "invalid". */
public class RiconosciMonete {

  /**
   * Metodo principale.
   *
   * @param args argomenti della linea di comando (non utilizzati)
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine().trim();
      if (line.isEmpty()) continue;

      Importo importo;
      try {
        importo = Importo.parse(line);
      } catch (IllegalArgumentException e) {
        System.out.println("invalid");
        continue;
      }

      Moneta moneta = Moneta.fromImporto(importo);
      if (moneta == null) {
        System.out.println("invalid");
      } else {
        System.out.println(moneta);
      }
    }

    scanner.close();
  }
}
