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
import macchinette.Binario;
import macchinette.BinarioException;
import macchinette.Prodotto;
import macchinette.Taglia;

/** Client che simula il caricamento di prodotti in un binario. */
public class CaricaBinario {

  /**
   * Metodo principale.
   *
   * @param args argomenti della linea di comando: capacità e taglia del binario
   */
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Uso: CaricaBinario <capacità> <taglia>");
      return;
    }

    int capacita;
    try {
      capacita = Integer.parseInt(args[0].trim());
    } catch (NumberFormatException e) {
      System.err.println("Capacità non valida: " + args[0]);
      return;
    }

    Taglia taglia;
    try {
      taglia = Taglia.parse(args[1]);
    } catch (IllegalArgumentException e) {
      System.err.println("Taglia non valida: " + args[1]);
      return;
    }

    Binario binario = new Binario(taglia, capacita);
    System.out.println(binario);

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine().trim();
      if (line.isEmpty()) continue;

      // Formato: "quantità, prodotto"
      String[] parti = line.split(",", 2);
      if (parti.length != 2) {
        continue;
      }

      int quantita;
      try {
        quantita = Integer.parseInt(parti[0].trim());
      } catch (NumberFormatException e) {
        continue;
      }

      Prodotto prodotto;
      try {
        prodotto = Prodotto.parse(parti[1].trim());
      } catch (IllegalArgumentException e) {
        continue;
      }

      try {
        binario.carica(prodotto, quantita);
        System.out.println(binario);
      } catch (BinarioException e) {
        System.out.println(e.getMessage());
      }
    }

    scanner.close();
  }
}
