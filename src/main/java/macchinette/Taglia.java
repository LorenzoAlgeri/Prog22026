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
 * Rappresenta la taglia di un prodotto o di un binario.
 *
 * <p>Le taglie sono ordinate in modo naturale: S &lt; M &lt; L.
 *
 * <p><strong>Invariante di rappresentazione:</strong> essendo un enum, l'invariante è
 * garantito dalla definizione stessa dei valori.
 *
 * <p><strong>Funzione di astrazione:</strong> ogni valore dell'enum rappresenta una taglia
 * fisica: S (small/piccola), M (medium/media), L (large/grande).
 */
public enum Taglia {
  /** Taglia piccola. */
  S,
  /** Taglia media. */
  M,
  /** Taglia grande. */
  L;

  /**
   * Verifica se questa taglia può contenere un oggetto della taglia specificata.
   *
   * <p>Una taglia può contenere un'altra se è maggiore o uguale nell'ordinamento naturale.
   *
   * @param altra la taglia da verificare, non deve essere null
   * @return true se questa taglia può contenere l'altra
   * @throws NullPointerException se altra è null
   */
  public boolean contiene(Taglia altra) {
    if (altra == null) {
      throw new NullPointerException("La taglia da confrontare non può essere null");
    }
    return this.ordinal() >= altra.ordinal();
  }

  /**
   * Restituisce la taglia corrispondente alla stringa specificata.
   *
   * <p>La stringa può contenere spazi iniziali o finali e può essere in qualsiasi case.
   *
   * @param s la stringa da convertire, non deve essere null
   * @return la taglia corrispondente
   * @throws NullPointerException se s è null
   * @throws IllegalArgumentException se s non corrisponde a nessuna taglia valida
   */
  public static Taglia parse(String s) {
    if (s == null) {
      throw new NullPointerException("La stringa non può essere null");
    }
    String trimmed = s.trim().toUpperCase();
    return switch (trimmed) {
      case "S" -> S;
      case "M" -> M;
      case "L" -> L;
      default -> throw new IllegalArgumentException("Taglia non valida: " + s);
    };
  }
}
