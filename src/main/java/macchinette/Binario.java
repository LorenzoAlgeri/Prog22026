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
 * Slot di un distributore con taglia e capacità fisse.
 *
 * <p><strong>RI:</strong> taglia != null, capacita &gt; 0, 0 &lt;= quantita &lt;= capacita,
 * (prodotto == null) == (quantita == 0), prodotto != null =&gt; taglia.contiene(prodotto.taglia())
 *
 * <p><strong>AF:</strong> AF(taglia, capacita, prodotto, quantita) = binario di quella taglia e
 * capacità contenente 'quantita' unità del prodotto
 */
public class Binario {

  private final Taglia taglia;
  private final int capacita;
  private Prodotto prodotto;
  private int quantita;

  /**
   * Crea un binario vuoto.
   *
   * @param taglia taglia del binario
   * @param capacita capacità massima (&gt; 0)
   */
  public Binario(Taglia taglia, int capacita) {
    Objects.requireNonNull(taglia);
    if (capacita <= 0) throw new IllegalArgumentException("capacità non positiva");
    this.taglia = taglia;
    this.capacita = capacita;
  }

  /** Restituisce la taglia. */
  public Taglia taglia() {
    return taglia;
  }

  /** Restituisce la capacità. */
  public int capacita() {
    return capacita;
  }

  /** Restituisce la quantità attuale. */
  public int quantita() {
    return quantita;
  }

  /** Verifica se vuoto. */
  public boolean vuoto() {
    return quantita == 0;
  }

  /** Restituisce lo spazio disponibile. */
  public int spazioDisponibile() {
    return capacita - quantita;
  }

  /** Restituisce il prodotto (null se vuoto). */
  public Prodotto prodotto() {
    return prodotto;
  }

  /** Verifica se il binario può accettare il prodotto (taglia e tipo). */
  public boolean accetta(Prodotto p) {
    Objects.requireNonNull(p);
    if (!taglia.contiene(p.taglia())) return false;
    return vuoto() || prodotto.equals(p);
  }

  /**
   * Carica prodotti nel binario.
   *
   * @param p prodotto da caricare
   * @param q quantità (&gt; 0)
   * @throws BinarioException se impossibile (SIZE, CAPACITY, ITEM)
   */
  public void carica(Prodotto p, int q) throws BinarioException {
    Objects.requireNonNull(p);
    if (q <= 0) throw new IllegalArgumentException("quantità non positiva");

    if (!taglia.contiene(p.taglia()))
      throw new BinarioException(BinarioException.Motivo.SIZE);
    if (!vuoto() && !prodotto.equals(p))
      throw new BinarioException(BinarioException.Motivo.ITEM);
    if (quantita + q > capacita)
      throw new BinarioException(BinarioException.Motivo.CAPACITY);

    if (vuoto()) this.prodotto = p;
    this.quantita += q;
  }

  /**
   * Dispensa un prodotto.
   *
   * @return il prodotto dispensato
   * @throws BinarioException se vuoto
   */
  public Prodotto dispensa() throws BinarioException {
    if (vuoto()) throw new BinarioException(BinarioException.Motivo.EMPTY);
    Prodotto p = this.prodotto;
    this.quantita--;
    if (this.quantita == 0) this.prodotto = null;
    return p;
  }

  /**
   * Parsing formato "capacità|taglia".
   *
   * @throws IllegalArgumentException se formato non valido
   */
  public static Binario parse(String s) {
    if (s == null) throw new NullPointerException();
    String[] parti = s.split("\\|");
    if (parti.length != 2)
      throw new IllegalArgumentException("formato errato: " + s);

    int cap;
    try {
      cap = Integer.parseInt(parti[0].trim());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("capacità non valida: " + parti[0], e);
    }

    Taglia t;
    try {
      t = Taglia.parse(parti[1]);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("taglia non valida: " + s, e);
    }

    return new Binario(t, cap);
  }

  @Override
  public String toString() {
    if (vuoto()) return "<-, " + taglia + ", 0, " + capacita + ">";
    return "<" + prodotto + ", " + taglia + ", " + quantita + ", " + capacita + ">";
  }
}
