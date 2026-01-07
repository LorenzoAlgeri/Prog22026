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
 * Strategia con backtracking: esplora tutte le combinazioni.
 *
 * <p>Garantisce di trovare soluzione se esiste, a differenza delle greedy.
 */
public class StrategiaRestoBacktrack implements StrategiaResto {

  public static final StrategiaRestoBacktrack INSTANCE = new StrategiaRestoBacktrack();

  private StrategiaRestoBacktrack() {}

  @Override
  public Optional<Aggregato> calcola(Importo resto, Aggregato disponibile) {
    Objects.requireNonNull(resto);
    Objects.requireNonNull(disponibile);

    if (resto.equals(Importo.ZERO)) return Optional.of(new Aggregato());
    if (disponibile.valoreTotale().minoreDi(resto)) return Optional.empty();

    Moneta[] monete = Moneta.values();
    int[] disp = new int[monete.length];
    int[] usate = new int[monete.length];

    for (int i = 0; i < monete.length; i++) {
      disp[i] = disponibile.quantita(monete[i]);
    }

    if (backtrack(monete, disp, usate, monete.length - 1, resto.inCentesimi())) {
      Aggregato result = new Aggregato();
      for (int i = 0; i < monete.length; i++) {
        if (usate[i] > 0) result.aggiungi(monete[i], usate[i]);
      }
      return Optional.of(result);
    }
    return Optional.empty();
  }

  // Backtracking ricorsivo
  private boolean backtrack(Moneta[] monete, int[] disp, int[] usate, int idx, int rimanente) {
    if (rimanente == 0) return true;
    if (idx < 0) return false;

    int valore = monete[idx].valore().inCentesimi();
    int maxQ = Math.min(disp[idx], rimanente / valore);

    for (int q = maxQ; q >= 0; q--) {
      usate[idx] = q;
      if (backtrack(monete, disp, usate, idx - 1, rimanente - q * valore)) return true;
    }
    usate[idx] = 0;
    return false;
  }

  @Override
  public String toString() {
    return "StrategiaRestoBacktrack";
  }
}
