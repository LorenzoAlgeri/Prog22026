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

/**
 * Importo monetario non negativo, immutabile.
 *
 * <p><strong>RI:</strong> centesimiTotali &gt;= 0
 *
 * <p><strong>AF:</strong> AF(centesimiTotali) = importo di (centesimiTotali / 100) unità
 * e (centesimiTotali % 100) centesimi.
 */
public final class Importo implements Comparable<Importo> {

  /** Importo zero. */
  public static final Importo ZERO = new Importo(0);

  private final int centesimiTotali;

  /**
   * Crea un importo da unità e centesimi.
   *
   * @param unita numero di unità (&gt;= 0)
   * @param centesimi centesimi (0-99)
   * @throws IllegalArgumentException se i parametri non sono validi
   */
  public Importo(int unita, int centesimi) {
    if (unita < 0) throw new IllegalArgumentException("unità negative: " + unita);
    if (centesimi < 0 || centesimi > 99)
      throw new IllegalArgumentException("centesimi non in [0,99]: " + centesimi);
    this.centesimiTotali = unita * 100 + centesimi;
  }

  // Costruttore privato per uso interno
  private Importo(int centesimiTotali) {
    this.centesimiTotali = centesimiTotali;
  }

  /** Restituisce le unità. */
  public int unita() {
    return centesimiTotali / 100;
  }

  /** Restituisce i centesimi (0-99). */
  public int centesimi() {
    return centesimiTotali % 100;
  }

  /** Restituisce il valore totale in centesimi. */
  public int inCentesimi() {
    return centesimiTotali;
  }

  /**
   * Somma questo importo con un altro.
   *
   * @param altro importo da sommare
   * @return nuovo importo
   */
  public Importo somma(Importo altro) {
    Objects.requireNonNull(altro);
    return new Importo(this.centesimiTotali + altro.centesimiTotali);
  }

  /**
   * Sottrae un importo da questo.
   *
   * @param altro importo da sottrarre
   * @return nuovo importo
   * @throws IllegalArgumentException se il risultato sarebbe negativo
   */
  public Importo sottrai(Importo altro) {
    Objects.requireNonNull(altro, "altro non può essere null");
    if (this.centesimiTotali < altro.centesimiTotali)
      throw new IllegalArgumentException("risultato negativo");
    return new Importo(this.centesimiTotali - altro.centesimiTotali);
  }

  /**
   * Moltiplica questo importo per un intero.
   *
   * @param n moltiplicatore (&gt;= 0)
   * @return nuovo importo
   */
  public Importo moltiplica(int n) {
    if (n < 0) throw new IllegalArgumentException("moltiplicatore negativo");
    return new Importo(this.centesimiTotali * n);
  }

  /**
   * Divisione intera tra importi.
   *
   * @param divisore importo divisore (non zero)
   * @return quoziente intero
   */
  public int dividi(Importo divisore) {
    Objects.requireNonNull(divisore);
    if (divisore.centesimiTotali == 0) throw new IllegalArgumentException("divisione per zero");
    return this.centesimiTotali / divisore.centesimiTotali;
  }

  /** Verifica se questo importo è maggiore dell'altro. */
  public boolean maggioreDi(Importo altro) {
    Objects.requireNonNull(altro);
    return this.centesimiTotali > altro.centesimiTotali;
  }

  /** Verifica se questo importo è minore dell'altro. */
  public boolean minoreDi(Importo altro) {
    Objects.requireNonNull(altro);
    return this.centesimiTotali < altro.centesimiTotali;
  }

  /**
   * Parsing di una stringa decimale (es. ".50", "2.30", "1").
   *
   * @param s stringa da convertire
   * @return importo corrispondente
   * @throws IllegalArgumentException se formato non valido o valore negativo
   */
  public static Importo parse(String s) {
    if (s == null) throw new NullPointerException("stringa null");
    String str = s.trim();
    if (str.isEmpty()) throw new IllegalArgumentException("stringa vuota");

    int cents;
    try {
      if (str.contains(".")) {
        String[] parts = str.split("\\.");
        int unita = parts[0].isEmpty() ? 0 : Integer.parseInt(parts[0]);
        String centPart = parts.length > 1 ? parts[1] : "0";
        // Max 2 decimali
        if (centPart.length() > 2)
          throw new IllegalArgumentException("troppi decimali: " + s);
        // Padding: ".5" -> 50 cents, ".05" -> 5 cents
        if (centPart.length() == 1) centPart = centPart + "0";
        int centesimi = Integer.parseInt(centPart);
        cents = unita * 100 + centesimi;
      } else {
        cents = Integer.parseInt(str) * 100;
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("formato non valido: " + s, e);
    }

    if (cents < 0) throw new IllegalArgumentException("importo negativo: " + s);
    return new Importo(cents);
  }

  @Override
  public int compareTo(Importo altro) {
    Objects.requireNonNull(altro);
    return Integer.compare(this.centesimiTotali, altro.centesimiTotali);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Importo other)) return false;
    return this.centesimiTotali == other.centesimiTotali;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(centesimiTotali);
  }

  @Override
  public String toString() {
    int u = unita();
    int c = centesimi();

    if (u == 0 && c == 0) return "0 cents";

    StringBuilder sb = new StringBuilder();
    if (u > 0) sb.append(u).append(u == 1 ? " unit" : " units");
    if (c > 0) {
      if (u > 0) sb.append(" ");
      sb.append(c).append(c == 1 ? " cent" : " cents");
    }
    return sb.toString();
  }
}
