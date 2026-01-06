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
 * Rappresenta un prodotto vendibile in un distributore automatico.
 *
 * <p>Un prodotto è caratterizzato da un nome, un prezzo e una taglia. Questa classe è immutabile.
 *
 * <p><strong>Invariante di rappresentazione:</strong> nome != null &amp;&amp; !nome.isBlank()
 * &amp;&amp; prezzo != null &amp;&amp; taglia != null
 *
 * <p><strong>Funzione di astrazione:</strong> AF(nome, prezzo, taglia) rappresenta il prodotto con
 * il nome, prezzo e taglia specificati.
 */
public final class Prodotto implements Comparable<Prodotto> {

  /** Il nome del prodotto. */
  private final String nome;

  /** Il prezzo del prodotto. */
  private final Importo prezzo;

  /** La taglia del prodotto. */
  private final Taglia taglia;

  /**
   * Costruisce un prodotto con i dati specificati.
   *
   * @param nome il nome del prodotto, non deve essere null né vuoto
   * @param prezzo il prezzo del prodotto, non deve essere null
   * @param taglia la taglia del prodotto, non deve essere null
   * @throws NullPointerException se uno dei parametri è null
   * @throws IllegalArgumentException se il nome è vuoto o composto solo da spazi
   */
  public Prodotto(String nome, Importo prezzo, Taglia taglia) {
    if (nome == null) {
      throw new NullPointerException("Il nome non può essere null");
    }
    if (nome.isBlank()) {
      throw new IllegalArgumentException("Il nome non può essere vuoto");
    }
    Objects.requireNonNull(prezzo, "Il prezzo non può essere null");
    Objects.requireNonNull(taglia, "La taglia non può essere null");

    this.nome = nome;
    this.prezzo = prezzo;
    this.taglia = taglia;
  }

  /**
   * Restituisce il nome del prodotto.
   *
   * @return il nome del prodotto
   */
  public String nome() {
    return nome;
  }

  /**
   * Restituisce il prezzo del prodotto.
   *
   * @return il prezzo del prodotto
   */
  public Importo prezzo() {
    return prezzo;
  }

  /**
   * Restituisce la taglia del prodotto.
   *
   * @return la taglia del prodotto
   */
  public Taglia taglia() {
    return taglia;
  }

  /**
   * Crea un prodotto a partire da una stringa nel formato "nome|prezzo|taglia".
   *
   * @param s la stringa da convertire, non deve essere null
   * @return il prodotto corrispondente
   * @throws NullPointerException se s è null
   * @throws IllegalArgumentException se s non è nel formato corretto
   */
  public static Prodotto parse(String s) {
    if (s == null) {
      throw new NullPointerException("La stringa non può essere null");
    }

    String[] parti = s.split("\\|");
    if (parti.length != 3) {
      throw new IllegalArgumentException(
          "Formato non valido, atteso 'nome|prezzo|taglia': " + s);
    }

    String nome = parti[0].trim();
    if (nome.isEmpty()) {
      throw new IllegalArgumentException("Il nome non può essere vuoto: " + s);
    }

    Importo prezzo;
    try {
      prezzo = Importo.parse(parti[1]);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Prezzo non valido in: " + s, e);
    }

    Taglia taglia;
    try {
      taglia = Taglia.parse(parti[2]);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Taglia non valida in: " + s, e);
    }

    return new Prodotto(nome, prezzo, taglia);
  }

  /**
   * Confronta questo prodotto con un altro secondo l'ordinamento naturale.
   *
   * <p>L'ordinamento è: prima per taglia, poi per nome, infine per prezzo.
   *
   * @param altro il prodotto da confrontare, non deve essere null
   * @return un valore negativo, zero o positivo
   * @throws NullPointerException se altro è null
   */
  @Override
  public int compareTo(Prodotto altro) {
    Objects.requireNonNull(altro, "Il prodotto da confrontare non può essere null");

    int cmp = this.taglia.compareTo(altro.taglia);
    if (cmp != 0) return cmp;

    cmp = this.nome.compareTo(altro.nome);
    if (cmp != 0) return cmp;

    return this.prezzo.compareTo(altro.prezzo);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Prodotto other)) return false;
    return this.nome.equals(other.nome)
        && this.prezzo.equals(other.prezzo)
        && this.taglia.equals(other.taglia);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, prezzo, taglia);
  }

  /**
   * Restituisce la rappresentazione testuale del prodotto.
   *
   * <p>Il formato è: "&lt;nome, prezzo, taglia&gt;"
   *
   * @return la rappresentazione testuale
   */
  @Override
  public String toString() {
    return "<" + nome + ", " + prezzo + ", " + taglia + ">";
  }
}
