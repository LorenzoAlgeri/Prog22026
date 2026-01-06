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
 * Strategia di calcolo del resto che preferisce le monete di valore minore.
 *
 * <p>Questa strategia (greedy) parte dalle monete di valore più basso e sale verso quelle di valore
 * più alto, usando il massimo numero possibile di ogni denominazione.
 *
 * <p>Tende a usare più monete rispetto alla strategia H.
 */
public class StrategiaRestoL implements StrategiaResto {

  /** Istanza singleton della strategia. */
  public static final StrategiaRestoL INSTANCE = new StrategiaRestoL();

  /** Costruttore privato per il pattern singleton. */
  private StrategiaRestoL() {}

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

    Aggregato risultato = new Aggregato();
    int rimanente = resto.inCentesimi();

    // Itera sulle monete dal basso verso l'alto (ordine naturale dell'enum)
    for (Moneta m : Moneta.values()) {
      if (rimanente <= 0) break;

      int valoreMoneta = m.valore().inCentesimi();
      int disponibili = disponibile.quantita(m);

      if (disponibili > 0 && valoreMoneta <= rimanente) {
        // Quante monete di questo tipo possiamo usare?
        int daUsare = Math.min(rimanente / valoreMoneta, disponibili);
        if (daUsare > 0) {
          risultato.aggiungi(m, daUsare);
          rimanente -= daUsare * valoreMoneta;
        }
      }
    }

    if (rimanente == 0) {
      return Optional.of(risultato);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public String toString() {
    return "StrategiaRestoL";
  }
}
