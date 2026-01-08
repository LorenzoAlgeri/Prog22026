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
 * <p><strong>RI:</strong> garantito dall'enum; il valore è sempre uno tra S, M, L.
 *
 * <p><strong>AF:</strong> rappresenta la taglia fisica di un prodotto o la capacità dimensionale
 * di un binario; S rappresenta la taglia piccola, M rappresenta la taglia media,
 * L rappresenta la taglia grande.
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
    String t = s.trim().toUpperCase();
    if (t.equals("S")) return S;
    if (t.equals("M")) return M;
    if (t.equals("L")) return L;
    throw new IllegalArgumentException("taglia non valida: " + s);
  }
}
