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

/**
 * Taglia di un prodotto o binario. Ordinamento naturale: S &lt; M &lt; L.
 *
 * <p><strong>RI:</strong> garantito dall'enum
 *
 * <p><strong>AF:</strong> S = piccola, M = media, L = grande
 */
public enum Taglia {
  S,
  M,
  L;

  /**
   * Verifica se questa taglia può contenere l'altra.
   *
   * @return true se this &gt;= altra
   */
  public boolean contiene(Taglia altra) {
    if (altra == null) throw new NullPointerException();
    return this.ordinal() >= altra.ordinal();
  }

  /**
   * Parsing di una stringa (S, M, L, case-insensitive).
   *
   * @throws IllegalArgumentException se non valida
   */
  public static Taglia parse(String s) {
    if (s == null) throw new NullPointerException();
    return switch (s.trim().toUpperCase()) {
      case "S" -> S;
      case "M" -> M;
      case "L" -> L;
      default -> throw new IllegalArgumentException("taglia non valida: " + s);
    };
  }
}
