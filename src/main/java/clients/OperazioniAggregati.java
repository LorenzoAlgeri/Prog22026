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
import macchinette.Aggregato;
import macchinette.AggregatoInsufficienteException;

/** Client che esegue operazioni su aggregati di monete. */
public class OperazioniAggregati {

  /**
   * Metodo principale.
   *
   * @param args argomenti della linea di comando (non utilizzati)
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Aggregato corrente = new Aggregato();

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine().trim();
      if (line.isEmpty()) continue;

      char operazione = line.charAt(0);
      if (operazione != '+' && operazione != '-') {
        continue;
      }

      String aggregatoStr = line.substring(1).trim();

      Aggregato operando;
      try {
        operando = Aggregato.parse(aggregatoStr);
      } catch (IllegalArgumentException e) {
        continue;
      }

      if (operazione == '+') {
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
