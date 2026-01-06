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

/** Client che esegue operazioni aritmetiche su importi monetari. */
public class OperazioniImporti {

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

      // Identifica l'operatore
      String[] parti;
      char operatore;

      if (line.contains(" + ")) {
        parti = line.split("\\s*\\+\\s*", 2);
        operatore = '+';
      } else if (line.contains(" - ")) {
        parti = line.split("\\s*-\\s*", 2);
        operatore = '-';
      } else if (line.contains(" * ")) {
        parti = line.split("\\s*\\*\\s*", 2);
        operatore = '*';
      } else if (line.contains(" / ")) {
        parti = line.split("\\s*/\\s*", 2);
        operatore = '/';
      } else {
        System.out.println("invalid");
        continue;
      }

      if (parti.length != 2) {
        System.out.println("invalid");
        continue;
      }

      try {
        Importo primoOperando = Importo.parse(parti[0]);

        if (operatore == '+') {
          Importo secondoOperando = Importo.parse(parti[1]);
          System.out.println(primoOperando.somma(secondoOperando));
        } else if (operatore == '-') {
          Importo secondoOperando = Importo.parse(parti[1]);
          try {
            System.out.println(primoOperando.sottrai(secondoOperando));
          } catch (IllegalArgumentException e) {
            System.out.println("negative");
          }
        } else if (operatore == '*') {
          int n;
          try {
            n = Integer.parseInt(parti[1].trim());
          } catch (NumberFormatException e) {
            System.out.println("invalid");
            continue;
          }
          if (n < 0) {
            System.out.println("negative");
          } else {
            System.out.println(primoOperando.moltiplica(n));
          }
        } else if (operatore == '/') {
          Importo secondoOperando = Importo.parse(parti[1]);
          if (secondoOperando.equals(Importo.ZERO)) {
            System.out.println("invalid");
          } else {
            System.out.println(primoOperando.dividi(secondoOperando));
          }
        }
      } catch (IllegalArgumentException e) {
        System.out.println("invalid");
      }
    }

    scanner.close();
  }
}
