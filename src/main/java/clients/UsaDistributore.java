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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import macchinette.Aggregato;
import macchinette.Binario;
import macchinette.Distributore;
import macchinette.ErogazioneException;
import macchinette.Prodotto;
import macchinette.StrategiaRestoH;

/** Client che simula l'utilizzo completo di un distributore automatico. */
public class UsaDistributore {

  /**
   * Metodo principale.
   *
   * @param args argomenti della linea di comando (non utilizzati)
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // Prima riga: configurazione binari
    if (!scanner.hasNextLine()) {
      scanner.close();
      return;
    }
    String binariConfig = scanner.nextLine().trim();
    List<Binario> binari = parseBinari(binariConfig);

    // Seconda riga: fondo cassa iniziale
    if (!scanner.hasNextLine()) {
      scanner.close();
      return;
    }
    String fondoCassaStr = scanner.nextLine().trim();
    Aggregato fondoCassa;
    try {
      fondoCassa = Aggregato.parse(fondoCassaStr);
    } catch (IllegalArgumentException e) {
      fondoCassa = new Aggregato();
    }

    // Crea il distributore con strategia H
    Distributore distributore = new Distributore(binari, fondoCassa, StrategiaRestoH.INSTANCE);

    // Processa i comandi
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine().trim();
      if (line.isEmpty()) continue;

      char comando = line.charAt(0);

      if (comando == '+') {
        // Caricamento: + quantità, prodotto
        processaCaricamento(distributore, line.substring(1).trim());
      } else if (comando == '-') {
        // Erogazione: - indiceBinario, pagamento
        processaErogazione(distributore, line.substring(1).trim());
      } else if (comando == '?') {
        // Query: mostra binari non vuoti
        processaQuery(distributore);
      }
    }

    scanner.close();
  }

  /**
   * Parsa la configurazione dei binari.
   *
   * @param config stringa di configurazione
   * @return lista di binari
   */
  private static List<Binario> parseBinari(String config) {
    List<Binario> binari = new ArrayList<>();
    String[] parti = config.split(",");

    for (String parte : parti) {
      String p = parte.trim();
      if (p.isEmpty()) continue;

      try {
        Binario b = Binario.parse(p);
        binari.add(b);
      } catch (IllegalArgumentException e) {
        // Ignora configurazioni non valide
      }
    }

    return binari;
  }

  /**
   * Processa un comando di caricamento.
   *
   * @param distributore il distributore
   * @param params i parametri del comando
   */
  private static void processaCaricamento(Distributore distributore, String params) {
    // Formato: quantità, prodotto
    String[] parti = params.split(",", 2);
    if (parti.length != 2) {
      return;
    }

    int quantita;
    try {
      quantita = Integer.parseInt(parti[0].trim());
    } catch (NumberFormatException e) {
      return;
    }

    Prodotto prodotto;
    try {
      prodotto = Prodotto.parse(parti[1].trim());
    } catch (IllegalArgumentException e) {
      return;
    }

    int nonCaricati = distributore.carica(prodotto, quantita);
    System.out.println("+ " + nonCaricati);
  }

  /**
   * Processa un comando di erogazione.
   *
   * @param distributore il distributore
   * @param params i parametri del comando
   */
  private static void processaErogazione(Distributore distributore, String params) {
    // Formato: indiceBinario, pagamento
    String[] parti = params.split(",", 2);
    if (parti.length != 2) {
      return;
    }

    int indiceBinario;
    try {
      indiceBinario = Integer.parseInt(parti[0].trim());
    } catch (NumberFormatException e) {
      return;
    }

    Aggregato pagamento;
    try {
      pagamento = Aggregato.parse(parti[1].trim());
    } catch (IllegalArgumentException e) {
      return;
    }

    try {
      Aggregato resto = distributore.eroga(indiceBinario, pagamento);
      if (resto.vuoto()) {
        System.out.println("- <>");
      } else {
        System.out.println("- " + resto);
      }
    } catch (ErogazioneException e) {
      System.out.println("- " + e.getMessage());
    }
  }

  /**
   * Processa un comando di query.
   *
   * @param distributore il distributore
   */
  private static void processaQuery(Distributore distributore) {
    Iterator<Map.Entry<Integer, Binario>> it = distributore.binariNonVuoti();
    while (it.hasNext()) {
      Map.Entry<Integer, Binario> entry = it.next();
      int indice = entry.getKey();
      Binario binario = entry.getValue();
      Prodotto p = binario.prodotto();
      System.out.println("? " + indice + " | " + p.nome() + " | " + p.prezzo());
    }
  }
}
