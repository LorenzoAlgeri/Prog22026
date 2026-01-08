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
 * Strategia greedy che parte dalle monete di valore maggiore.
 *
 * <p>Non garantisce di trovare soluzione anche quando esiste.
 */
public class StrategiaRestoH implements StrategiaResto {

  public static final StrategiaRestoH INSTANCE = new StrategiaRestoH();

  private StrategiaRestoH() {}

  @Override
  public Optional<Aggregato> calcola(Importo resto, Aggregato disponibile) {
    Objects.requireNonNull(resto);
    Objects.requireNonNull(disponibile);

    if (resto.equals(Importo.ZERO)) return Optional.of(new Aggregato());
    if (disponibile.valoreTotale().minoreDi(resto)) return Optional.empty();

    Aggregato risultato = new Aggregato();
    int rimanente = resto.inCentesimi();

    Moneta[] monete = Moneta.values();
    for (int i = monete.length - 1; i >= 0 && rimanente > 0; i--) {
      Moneta m = monete[i];
      int valore = m.valore().inCentesimi();
      int disp = disponibile.quantita(m);

      if (disp > 0 && valore <= rimanente) {
        int daUsare = Math.min(rimanente / valore, disp);
        if (daUsare > 0) {
          risultato.aggiungi(m, daUsare);
          rimanente -= daUsare * valore;
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
