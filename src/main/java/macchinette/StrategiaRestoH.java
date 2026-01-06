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
 * Strategia di calcolo del resto che preferisce le monete di valore maggiore.
 *
 * <p>Questa strategia (greedy) parte dalle monete di valore più alto e scende verso quelle di
 * valore più basso, usando il massimo numero possibile di ogni denominazione.
 *
 * <p>Non garantisce di trovare una soluzione anche quando esiste (es. resto di 6 cent con solo
 * monete da 5 e 2 cent: questa strategia proverebbe 5+? ma non troverebbe soluzione, mentre 2+2+2
 * funzionerebbe).
 */
public class StrategiaRestoH implements StrategiaResto {

  /** Istanza singleton della strategia. */
  public static final StrategiaRestoH INSTANCE = new StrategiaRestoH();

  /** Costruttore privato per il pattern singleton. */
  private StrategiaRestoH() {}

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

    // Itera sulle monete dall'alto verso il basso (ordine inverso dell'enum)
    Moneta[] monete = Moneta.values();
    for (int i = monete.length - 1; i >= 0 && rimanente > 0; i--) {
      Moneta m = monete[i];
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
    return "StrategiaRestoH";
  }
}
