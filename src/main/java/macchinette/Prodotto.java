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
 * Prodotto vendibile, immutabile. Ordinamento: taglia, nome, prezzo.
 *
 * <p><strong>RI:</strong> nome non è null;
 * nome non è una stringa vuota o contenente solo spazi bianchi;
 * prezzo non è null;
 * taglia non è null.
 *
 * <p><strong>AF:</strong> rappresenta un prodotto vendibile con un nome identificativo,
 * un prezzo in formato Importo e una taglia che ne determina le dimensioni fisiche.
 */
public final class Prodotto implements Comparable<Prodotto> {

  private final String nome;
  private final Importo prezzo;
  private final Taglia taglia;

  /**
   * Crea un prodotto.
   *
   * @param nome nome non vuoto
   * @param prezzo prezzo
   * @param taglia taglia
   */
  public Prodotto(String nome, Importo prezzo, Taglia taglia) {
    if (nome == null) throw new NullPointerException("nome null");
    if (nome.isBlank()) throw new IllegalArgumentException("nome vuoto");
    Objects.requireNonNull(prezzo, "prezzo null");
    Objects.requireNonNull(taglia, "taglia null");
    this.nome = nome;
    this.prezzo = prezzo;
    this.taglia = taglia;
  }

  /** Restituisce il nome. */
  public String nome() {
    return nome;
  }

  /** Restituisce il prezzo. */
  public Importo prezzo() {
    return prezzo;
  }

  /** Restituisce la taglia. */
  public Taglia taglia() {
    return taglia;
  }

  /**
   * Parsing formato "nome|prezzo|taglia".
   *
   * @throws IllegalArgumentException se formato non valido
   */
  public static Prodotto parse(String s) {
    if (s == null) throw new NullPointerException();
    String[] parti = s.split("\\|");
    if (parti.length != 3)
      throw new IllegalArgumentException("formato errato: " + s);

    String nome = parti[0].trim();
    if (nome.isEmpty()) throw new IllegalArgumentException("nome vuoto");

    Importo prezzo;
    try {
      prezzo = Importo.parse(parti[1]);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("prezzo non valido: " + s, e);
    }

    Taglia taglia;
    try {
      taglia = Taglia.parse(parti[2]);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("taglia non valida: " + s, e);
    }

    return new Prodotto(nome, prezzo, taglia);
  }

  @Override
  public int compareTo(Prodotto altro) {
    Objects.requireNonNull(altro);
    int cmp = this.taglia.compareTo(altro.taglia);
    if (cmp != 0) return cmp;
    cmp = this.nome.compareTo(altro.nome);
    if (cmp != 0) return cmp;
    return this.prezzo.compareTo(altro.prezzo);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Prodotto)) return false;
    Prodotto other = (Prodotto) obj;
    return nome.equals(other.nome) && prezzo.equals(other.prezzo) && taglia.equals(other.taglia);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, prezzo, taglia);
  }

  @Override
  public String toString() {
    return "<" + nome + ", " + prezzo + ", " + taglia + ">";
  }
}
