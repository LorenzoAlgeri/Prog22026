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

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Rappresenta un importo monetario non negativo.
 *
 * <p>Un importo è composto da unità e centesimi. Questa classe è immutabile e tutte le operazioni
 * aritmetiche restituiscono nuove istanze.
 *
 * <p><strong>Invariante di rappresentazione:</strong> centesimiTotali &gt;= 0
 *
 * <p><strong>Funzione di astrazione:</strong> AF(centesimiTotali) rappresenta l'importo monetario
 * dove le unità sono centesimiTotali / 100 e i centesimi sono centesimiTotali % 100.
 */
public final class Importo implements Comparable<Importo> {

  /** Importo nullo (zero). */
  public static final Importo ZERO = new Importo(0);

  /** Il valore dell'importo espresso in centesimi. */
  private final int centesimiTotali;

  /**
   * Costruisce un importo a partire da unità e centesimi.
   *
   * @param unita il numero di unità, deve essere &gt;= 0
   * @param centesimi il numero di centesimi, deve essere compreso tra 0 e 99 inclusi
   * @throws IllegalArgumentException se unita &lt; 0 o centesimi non è in [0, 99]
   */
  public Importo(int unita, int centesimi) {
    if (unita < 0) {
      throw new IllegalArgumentException("Le unità non possono essere negative: " + unita);
    }
    if (centesimi < 0 || centesimi > 99) {
      throw new IllegalArgumentException(
          "I centesimi devono essere compresi tra 0 e 99: " + centesimi);
    }
    this.centesimiTotali = unita * 100 + centesimi;
  }

  /**
   * Costruisce un importo a partire dal totale in centesimi.
   *
   * <p>Costruttore privato per uso interno.
   *
   * @param centesimiTotali il valore totale in centesimi
   */
  private Importo(int centesimiTotali) {
    assert centesimiTotali >= 0 : "centesimiTotali deve essere >= 0";
    this.centesimiTotali = centesimiTotali;
  }

  /**
   * Restituisce il numero di unità di questo importo.
   *
   * @return il numero di unità (&gt;= 0)
   */
  public int unita() {
    return centesimiTotali / 100;
  }

  /**
   * Restituisce il numero di centesimi di questo importo (parte frazionaria).
   *
   * @return il numero di centesimi (0-99)
   */
  public int centesimi() {
    return centesimiTotali % 100;
  }

  /**
   * Restituisce il valore totale in centesimi.
   *
   * @return il valore in centesimi
   */
  public int inCentesimi() {
    return centesimiTotali;
  }

  /**
   * Restituisce un nuovo importo pari alla somma di questo e dell'altro.
   *
   * @param altro l'importo da sommare, non deve essere null
   * @return un nuovo importo pari alla somma
   * @throws NullPointerException se altro è null
   */
  public Importo somma(Importo altro) {
    Objects.requireNonNull(altro, "L'importo da sommare non può essere null");
    return new Importo(this.centesimiTotali + altro.centesimiTotali);
  }

  /**
   * Restituisce un nuovo importo pari alla differenza tra questo e l'altro.
   *
   * @param altro l'importo da sottrarre, non deve essere null
   * @return un nuovo importo pari alla differenza
   * @throws NullPointerException se altro è null
   * @throws IllegalArgumentException se il risultato sarebbe negativo
   */
  public Importo sottrai(Importo altro) {
    Objects.requireNonNull(altro, "L'importo da sottrarre non può essere null");
    if (this.centesimiTotali < altro.centesimiTotali) {
      throw new IllegalArgumentException(
          "La sottrazione produrrebbe un importo negativo: " + this + " - " + altro);
    }
    return new Importo(this.centesimiTotali - altro.centesimiTotali);
  }

  /**
   * Restituisce un nuovo importo pari a questo moltiplicato per un intero.
   *
   * @param n il moltiplicatore, deve essere &gt;= 0
   * @return un nuovo importo pari al prodotto
   * @throws IllegalArgumentException se n &lt; 0
   */
  public Importo moltiplica(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("Il moltiplicatore non può essere negativo: " + n);
    }
    return new Importo(this.centesimiTotali * n);
  }

  /**
   * Restituisce il quoziente intero della divisione di questo importo per l'altro.
   *
   * <p>Il risultato è il più grande intero n tale che altro.moltiplica(n) &lt;= this.
   *
   * @param divisore l'importo divisore, non deve essere null né zero
   * @return il quoziente intero
   * @throws NullPointerException se divisore è null
   * @throws IllegalArgumentException se divisore è zero
   */
  public int dividi(Importo divisore) {
    Objects.requireNonNull(divisore, "Il divisore non può essere null");
    if (divisore.centesimiTotali == 0) {
      throw new IllegalArgumentException("Divisione per zero");
    }
    return this.centesimiTotali / divisore.centesimiTotali;
  }

  /**
   * Verifica se questo importo è maggiore dell'altro.
   *
   * @param altro l'importo da confrontare, non deve essere null
   * @return true se questo importo è strettamente maggiore
   * @throws NullPointerException se altro è null
   */
  public boolean maggioreDi(Importo altro) {
    Objects.requireNonNull(altro, "L'importo da confrontare non può essere null");
    return this.centesimiTotali > altro.centesimiTotali;
  }

  /**
   * Verifica se questo importo è minore dell'altro.
   *
   * @param altro l'importo da confrontare, non deve essere null
   * @return true se questo importo è strettamente minore
   * @throws NullPointerException se altro è null
   */
  public boolean minoreDi(Importo altro) {
    Objects.requireNonNull(altro, "L'importo da confrontare non può essere null");
    return this.centesimiTotali < altro.centesimiTotali;
  }

  /**
   * Crea un importo a partire da una stringa nel formato decimale.
   *
   * <p>Formati accettati: ".50", "1", "2.30", "0.05", ecc.
   *
   * @param s la stringa da convertire, non deve essere null
   * @return l'importo corrispondente
   * @throws NullPointerException se s è null
   * @throws IllegalArgumentException se s non è nel formato corretto o rappresenta un valore
   *     negativo
   */
  public static Importo parse(String s) {
    if (s == null) {
      throw new NullPointerException("La stringa non può essere null");
    }
    String trimmed = s.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("La stringa non può essere vuota");
    }

    int centesimiTotali;
    try {
      BigDecimal bd = new BigDecimal(trimmed);
      centesimiTotali = bd.multiply(BigDecimal.valueOf(100)).intValueExact();
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Formato non valido: " + s, e);
    } catch (ArithmeticException e) {
      throw new IllegalArgumentException(
          "Il valore ha troppi decimali o è fuori range: " + s, e);
    }

    if (centesimiTotali < 0) {
      throw new IllegalArgumentException("L'importo non può essere negativo: " + s);
    }

    return new Importo(centesimiTotali);
  }

  @Override
  public int compareTo(Importo altro) {
    Objects.requireNonNull(altro, "L'importo da confrontare non può essere null");
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

  /**
   * Restituisce la rappresentazione testuale dell'importo.
   *
   * <p>Il formato è: "[u unit[s]] [c cent[s]]" dove le parti tra parentesi quadre sono opzionali a
   * seconda dei valori.
   *
   * <p>Esempi: "1 unit", "5 cents", "2 units 30 cents", "1 unit 1 cent"
   *
   * @return la rappresentazione testuale
   */
  @Override
  public String toString() {
    int u = unita();
    int c = centesimi();

    if (u == 0 && c == 0) {
      return "0 cents";
    }

    StringBuilder sb = new StringBuilder();

    if (u > 0) {
      sb.append(u).append(u == 1 ? " unit" : " units");
    }

    if (c > 0) {
      if (u > 0) {
        sb.append(" ");
      }
      sb.append(c).append(c == 1 ? " cent" : " cents");
    }

    return sb.toString();
  }
}
