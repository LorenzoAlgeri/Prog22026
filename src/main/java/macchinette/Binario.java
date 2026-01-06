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
 * Rappresenta un binario (slot) di un distributore automatico.
 *
 * <p>Un binario ha una taglia e una capacità massima. Può contenere zero o più unità di un unico
 * tipo di prodotto, purché la taglia del prodotto non ecceda quella del binario.
 *
 * <p><strong>Invariante di rappresentazione:</strong> taglia != null &amp;&amp; capacita &gt; 0
 * &amp;&amp; quantita &gt;= 0 &amp;&amp; quantita &lt;= capacita &amp;&amp; (prodotto == null) ==
 * (quantita == 0) &amp;&amp; (prodotto != null ==&gt; taglia.contiene(prodotto.taglia()))
 *
 * <p><strong>Funzione di astrazione:</strong> AF(taglia, capacita, prodotto, quantita) rappresenta
 * un binario di taglia e capacità date, contenente 'quantita' unità del prodotto (se non null).
 */
public class Binario {

  /** La taglia del binario. */
  private final Taglia taglia;

  /** La capacità massima del binario. */
  private final int capacita;

  /** Il prodotto contenuto, null se vuoto. */
  private Prodotto prodotto;

  /** La quantità di prodotti contenuti. */
  private int quantita;

  /**
   * Costruisce un binario vuoto con la taglia e capacità specificate.
   *
   * @param taglia la taglia del binario, non deve essere null
   * @param capacita la capacità massima, deve essere &gt; 0
   * @throws NullPointerException se taglia è null
   * @throws IllegalArgumentException se capacita &lt;= 0
   */
  public Binario(Taglia taglia, int capacita) {
    Objects.requireNonNull(taglia, "La taglia non può essere null");
    if (capacita <= 0) {
      throw new IllegalArgumentException("La capacità deve essere positiva: " + capacita);
    }
    this.taglia = taglia;
    this.capacita = capacita;
    this.prodotto = null;
    this.quantita = 0;
  }

  /**
   * Restituisce la taglia del binario.
   *
   * @return la taglia
   */
  public Taglia taglia() {
    return taglia;
  }

  /**
   * Restituisce la capacità massima del binario.
   *
   * @return la capacità
   */
  public int capacita() {
    return capacita;
  }

  /**
   * Restituisce la quantità di prodotti attualmente contenuti.
   *
   * @return la quantità
   */
  public int quantita() {
    return quantita;
  }

  /**
   * Verifica se il binario è vuoto.
   *
   * @return true se il binario non contiene prodotti
   */
  public boolean vuoto() {
    return quantita == 0;
  }

  /**
   * Restituisce lo spazio disponibile nel binario.
   *
   * @return il numero di prodotti che possono ancora essere caricati
   */
  public int spazioDisponibile() {
    return capacita - quantita;
  }

  /**
   * Restituisce il prodotto contenuto nel binario.
   *
   * @return il prodotto, o null se il binario è vuoto
   */
  public Prodotto prodotto() {
    return prodotto;
  }

  /**
   * Verifica se il binario può accettare il prodotto specificato.
   *
   * <p>Un binario può accettare un prodotto se:
   *
   * <ul>
   *   <li>la taglia del prodotto non eccede quella del binario
   *   <li>il binario è vuoto, oppure contiene lo stesso prodotto
   * </ul>
   *
   * @param p il prodotto da verificare, non deve essere null
   * @return true se il prodotto può essere caricato (senza considerare la capacità)
   * @throws NullPointerException se p è null
   */
  public boolean accetta(Prodotto p) {
    Objects.requireNonNull(p, "Il prodotto non può essere null");
    if (!taglia.contiene(p.taglia())) {
      return false;
    }
    return vuoto() || prodotto.equals(p);
  }

  /**
   * Carica una quantità di prodotti nel binario.
   *
   * @param p il prodotto da caricare, non deve essere null
   * @param q la quantità da caricare, deve essere &gt; 0
   * @throws NullPointerException se p è null
   * @throws IllegalArgumentException se q &lt;= 0
   * @throws BinarioException se il caricamento non è possibile (SIZE, CAPACITY, ITEM)
   */
  public void carica(Prodotto p, int q) throws BinarioException {
    Objects.requireNonNull(p, "Il prodotto non può essere null");
    if (q <= 0) {
      throw new IllegalArgumentException("La quantità deve essere positiva: " + q);
    }

    // Verifica taglia
    if (!taglia.contiene(p.taglia())) {
      throw new BinarioException(BinarioException.Motivo.SIZE);
    }

    // Verifica prodotto (se non vuoto)
    if (!vuoto() && !prodotto.equals(p)) {
      throw new BinarioException(BinarioException.Motivo.ITEM);
    }

    // Verifica capacità
    if (quantita + q > capacita) {
      throw new BinarioException(BinarioException.Motivo.CAPACITY);
    }

    // Esegue il caricamento
    if (vuoto()) {
      this.prodotto = p;
    }
    this.quantita += q;
  }

  /**
   * Dispensa un prodotto dal binario.
   *
   * @return il prodotto dispensato
   * @throws BinarioException se il binario è vuoto
   */
  public Prodotto dispensa() throws BinarioException {
    if (vuoto()) {
      throw new BinarioException(BinarioException.Motivo.EMPTY);
    }

    Prodotto p = this.prodotto;
    this.quantita--;
    if (this.quantita == 0) {
      this.prodotto = null;
    }
    return p;
  }

  /**
   * Crea un binario a partire da una stringa nel formato "capacità|taglia".
   *
   * @param s la stringa da convertire, non deve essere null
   * @return il binario corrispondente
   * @throws NullPointerException se s è null
   * @throws IllegalArgumentException se s non è nel formato corretto
   */
  public static Binario parse(String s) {
    if (s == null) {
      throw new NullPointerException("La stringa non può essere null");
    }

    String[] parti = s.split("\\|");
    if (parti.length != 2) {
      throw new IllegalArgumentException(
          "Formato non valido, atteso 'capacità|taglia': " + s);
    }

    int capacita;
    try {
      capacita = Integer.parseInt(parti[0].trim());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Capacità non valida: " + parti[0], e);
    }

    Taglia taglia;
    try {
      taglia = Taglia.parse(parti[1]);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Taglia non valida in: " + s, e);
    }

    return new Binario(taglia, capacita);
  }

  /**
   * Restituisce la rappresentazione testuale del binario.
   *
   * <p>Il formato è:
   *
   * <ul>
   *   <li>Vuoto: "&lt;-, taglia, 0, capacità&gt;"
   *   <li>Pieno: "&lt;&lt;prodotto&gt;, taglia, quantità, capacità&gt;"
   * </ul>
   *
   * @return la rappresentazione testuale
   */
  @Override
  public String toString() {
    if (vuoto()) {
      return "<-, " + taglia + ", 0, " + capacita + ">";
    }
    return "<" + prodotto + ", " + taglia + ", " + quantita + ", " + capacita + ">";
  }
}
