/*
 * Copyright 2025 Massimo Santini
 *
 * This file is part of "Programmazione 2 @ UniMI" teaching material.
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This material is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this file.  If not, see <https://www.gnu.org/licenses/>.
 */

package macchinette;

import java.util.Objects;
import java.util.Optional;

/**
 * Strategia di calcolo del resto con backtracking.
 *
 * <p>Questa strategia esplora tutte le possibili combinazioni di monete per trovare una soluzione.
 * Garantisce di trovare una soluzione se esiste, a differenza delle strategie greedy.
 *
 * <p>Parte dalle monete di valore maggiore per efficienza, ma esplora tutte le alternative se
 * necessario.
 */
public class StrategiaRestoBacktrack implements StrategiaResto {

  /** Istanza singleton della strategia. */
  public static final StrategiaRestoBacktrack INSTANCE = new StrategiaRestoBacktrack();

  /** Costruttore privato per il pattern singleton. */
  private StrategiaRestoBacktrack() {}

  @Override
  public Optional<Aggregato> calcola(Importo resto, Aggregato disponibile) {
    Objects.requireNonNull(resto, "L'importo del resto non può essere null");
    Objects.requireNonNull(disponibile, "L'aggregato disponibile non può essere null");

    if (resto.equals(Importo.ZERO)) {
      return Optional.of(new Aggregato());
    }

    // Verifica se il valore totale è sufficiente
    if (disponibile.valoreTotale().minoreDi(resto)) {
      return Optional.empty();
    }

    // Prepara gli array per il backtracking
    Moneta[] monete = Moneta.values();
    int[] disponibili = new int[monete.length];
    int[] usate = new int[monete.length];

    for (int i = 0; i < monete.length; i++) {
      disponibili[i] = disponibile.quantita(monete[i]);
    }

    // Esegue il backtracking partendo dall'ultima moneta (valore maggiore)
    if (backtrack(monete, disponibili, usate, monete.length - 1, resto.inCentesimi())) {
      Aggregato risultato = new Aggregato();
      for (int i = 0; i < monete.length; i++) {
        if (usate[i] > 0) {
          risultato.aggiungi(monete[i], usate[i]);
        }
      }
      return Optional.of(risultato);
    }

    return Optional.empty();
  }

  /**
   * Esegue il backtracking ricorsivo.
   *
   * @param monete l'array delle monete
   * @param disponibili le quantità disponibili per ogni moneta
   * @param usate le quantità usate per ogni moneta (output)
   * @param indice l'indice corrente della moneta da considerare
   * @param rimanente l'importo rimanente da coprire in centesimi
   * @return true se è stata trovata una soluzione
   */
  private boolean backtrack(
      Moneta[] monete, int[] disponibili, int[] usate, int indice, int rimanente) {
    // Caso base: abbiamo raggiunto l'importo esatto
    if (rimanente == 0) {
      return true;
    }

    // Caso base: abbiamo esaurito le monete
    if (indice < 0) {
      return false;
    }

    int valoreMoneta = monete[indice].valore().inCentesimi();
    int maxUsabili = Math.min(disponibili[indice], rimanente / valoreMoneta);

    // Prova a usare da maxUsabili a 0 monete di questo tipo
    for (int q = maxUsabili; q >= 0; q--) {
      usate[indice] = q;
      int nuovoRimanente = rimanente - q * valoreMoneta;

      if (backtrack(monete, disponibili, usate, indice - 1, nuovoRimanente)) {
        return true;
      }
    }

    usate[indice] = 0;
    return false;
  }

  @Override
  public String toString() {
    return "StrategiaRestoBacktrack";
  }
}
