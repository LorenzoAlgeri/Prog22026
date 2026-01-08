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
 * Strategia greedy che parte dalle monete di valore minore.
 *
 * <p><strong>RI:</strong> garantito dal pattern Singleton; esiste una sola istanza accessibile
 * tramite INSTANCE.
 *
 * <p><strong>AF:</strong> rappresenta la strategia di calcolo del resto che utilizza un approccio
 * greedy partendo dalle monete di valore più basso; ad ogni passo seleziona il maggior numero
 * possibile di monete della denominazione più piccola disponibile che non supera il resto rimanente;
 * tende a utilizzare più monete rispetto alla strategia H, ma come quella non garantisce
 * di trovare sempre una soluzione anche quando esiste.
 *
 * <p>Usa più monete rispetto a H, ma non garantisce di trovare soluzione.
 */
public class StrategiaRestoL implements StrategiaResto {

  public static final StrategiaRestoL INSTANCE = new StrategiaRestoL();

  private StrategiaRestoL() {}

  @Override
  public Optional<Aggregato> calcola(Importo resto, Aggregato disponibile) {
    Objects.requireNonNull(resto);
    Objects.requireNonNull(disponibile);

    if (resto.equals(Importo.ZERO)) return Optional.of(new Aggregato());
    if (disponibile.valoreTotale().minoreDi(resto)) return Optional.empty();

    Aggregato risultato = new Aggregato();
    int rimanente = resto.inCentesimi();

    for (Moneta m : Moneta.values()) {
      if (rimanente <= 0) break;
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
    return "StrategiaRestoL";
  }
}
