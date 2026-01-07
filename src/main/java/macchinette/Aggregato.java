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

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Multi-insieme di monete.
 *
 * <p><strong>RI:</strong> monete != null, ogni entry ha chiave != null e valore &gt; 0
 *
 * <p><strong>AF:</strong> AF(monete) = multi-insieme dove ogni moneta m appare monete.get(m) volte
 */
public class Aggregato implements Iterable<Map.Entry<Moneta, Integer>> {

  private final Map<Moneta, Integer> monete;

  /** Crea un aggregato vuoto. */
  public Aggregato() {
    this.monete = new TreeMap<>();
  }

  /**
   * Crea una copia di un aggregato.
   *
   * @param altro aggregato da copiare
   */
  public Aggregato(Aggregato altro) {
    Objects.requireNonNull(altro);
    this.monete = new TreeMap<>(altro.monete);
  }

  /** Verifica se l'aggregato è vuoto. */
  public boolean vuoto() {
    return monete.isEmpty();
  }

  /** Restituisce la quantità di una moneta (0 se assente). */
  public int quantita(Moneta moneta) {
    Objects.requireNonNull(moneta);
    return monete.getOrDefault(moneta, 0);
  }

  /** Calcola il valore totale dell'aggregato. */
  public Importo valoreTotale() {
    int totale = 0;
    for (Map.Entry<Moneta, Integer> e : monete.entrySet()) {
      totale += e.getKey().valore().inCentesimi() * e.getValue();
    }
    return new Importo(totale / 100, totale % 100);
  }

  /**
   * Aggiunge monete all'aggregato.
   *
   * @param moneta tipo di moneta
   * @param quantita quantità da aggiungere (&gt; 0)
   */
  public void aggiungi(Moneta moneta, int quantita) {
    if (moneta == null) throw new NullPointerException("moneta null");
    if (quantita <= 0) throw new IllegalArgumentException("quantità non positiva");
    // aggiunge alla mappa
    int vecchio = monete.getOrDefault(moneta, 0);
    monete.put(moneta, vecchio + quantita);
  }

  /** Aggiunge tutte le monete di un altro aggregato. */
  public void aggiungi(Aggregato altro) {
    Objects.requireNonNull(altro);
    for (Map.Entry<Moneta, Integer> e : altro.monete.entrySet()) {
      Moneta m = e.getKey();
      int q = e.getValue();
      int old = monete.getOrDefault(m, 0);
      monete.put(m, old + q);
    }
  }

  /**
   * Rimuove le monete di un altro aggregato da questo.
   *
   * @param altro aggregato da rimuovere
   * @throws AggregatoInsufficienteException se impossibile
   */
  public void rimuovi(Aggregato altro) throws AggregatoInsufficienteException {
    Objects.requireNonNull(altro);

    // Verifica valore
    if (this.valoreTotale().minoreDi(altro.valoreTotale())) {
      throw new AggregatoInsufficienteException(AggregatoInsufficienteException.Motivo.VALUE);
    }

    // Verifica monete
    for (Map.Entry<Moneta, Integer> e : altro.monete.entrySet()) {
      if (this.quantita(e.getKey()) < e.getValue()) {
        throw new AggregatoInsufficienteException(AggregatoInsufficienteException.Motivo.COINS);
      }
    }

    // Rimozione
    for (Map.Entry<Moneta, Integer> e : altro.monete.entrySet()) {
      Moneta m = e.getKey();
      int nuova = monete.get(m) - e.getValue();
      if (nuova == 0) monete.remove(m);
      else monete.put(m, nuova);
    }
  }

  /** Verifica se questo aggregato contiene almeno le monete dell'altro. */
  public boolean contiene(Aggregato altro) {
    Objects.requireNonNull(altro);
    for (Map.Entry<Moneta, Integer> e : altro.monete.entrySet()) {
      if (this.quantita(e.getKey()) < e.getValue()) return false;
    }
    return true;
  }

  @Override
  public Iterator<Map.Entry<Moneta, Integer>> iterator() {
    return new TreeMap<>(monete).entrySet().iterator();
  }

  /**
   * Parsing di una stringa nel formato "n x valore, m x valore, ...".
   *
   * @param s stringa da parsare
   * @return aggregato corrispondente
   */
  public static Aggregato parse(String s) {
    if (s == null) throw new NullPointerException();
    Aggregato result = new Aggregato();
    String str = s.trim();
    if (str.isEmpty()) return result;

    for (String parte : str.split(",")) {
      String p = parte.trim();
      if (p.isEmpty()) continue;

      String[] tokens = p.split("\\s*x\\s*", 2);
      if (tokens.length != 2)
        throw new IllegalArgumentException("formato errato: " + p);

      int q;
      try {
        q = Integer.parseInt(tokens[0].trim());
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("quantità non valida: " + tokens[0], e);
      }
      if (q <= 0) throw new IllegalArgumentException("quantità non positiva");

      Moneta m = Moneta.parse(tokens[1].trim());
      if (m == null) throw new IllegalArgumentException("moneta non valida: " + tokens[1]);

      result.aggiungi(m, q);
    }
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Aggregato other)) return false;
    return this.monete.equals(other.monete);
  }

  @Override
  public int hashCode() {
    return monete.hashCode();
  }

  @Override
  public String toString() {
    if (monete.isEmpty()) return "<>";
    StringBuilder sb = new StringBuilder("<");
    boolean first = true;
    for (Map.Entry<Moneta, Integer> e : monete.entrySet()) {
      if (!first) sb.append(", ");
      first = false;
      sb.append(e.getValue()).append(" x ").append(e.getKey());
    }
    return sb.append(">").toString();
  }
}
