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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Rappresenta un multi-insieme di monete (aggregato).
 *
 * <p>Un aggregato è una collezione di monete dove ogni tipo di moneta può apparire zero o più
 * volte. Supporta operazioni di aggiunta e rimozione di monete.
 *
 * <p><strong>Invariante di rappresentazione:</strong> monete != null &amp;&amp; per ogni entry
 * (m,q) in monete: m != null &amp;&amp; q &gt; 0 (nessuna entry con quantità zero o negativa)
 *
 * <p><strong>Funzione di astrazione:</strong> AF(monete) rappresenta il multi-insieme dove ogni
 * moneta m appare monete.get(m) volte (0 se assente dalla mappa).
 */
public class Aggregato implements Iterable<Map.Entry<Moneta, Integer>> {

  /** La mappa che associa ogni moneta alla sua quantità. */
  private final Map<Moneta, Integer> monete;

  /** Costruisce un aggregato vuoto. */
  public Aggregato() {
    this.monete = new TreeMap<>();
  }

  /**
   * Costruisce un aggregato come copia di un altro.
   *
   * @param altro l'aggregato da copiare, non deve essere null
   * @throws NullPointerException se altro è null
   */
  public Aggregato(Aggregato altro) {
    Objects.requireNonNull(altro, "L'aggregato da copiare non può essere null");
    this.monete = new TreeMap<>(altro.monete);
  }

  /**
   * Verifica se l'aggregato è vuoto.
   *
   * @return true se l'aggregato non contiene monete
   */
  public boolean vuoto() {
    return monete.isEmpty();
  }

  /**
   * Restituisce la quantità di una specifica moneta nell'aggregato.
   *
   * @param moneta la moneta da cercare, non deve essere null
   * @return la quantità di quella moneta (0 se assente)
   * @throws NullPointerException se moneta è null
   */
  public int quantita(Moneta moneta) {
    Objects.requireNonNull(moneta, "La moneta non può essere null");
    return monete.getOrDefault(moneta, 0);
  }

  /**
   * Calcola e restituisce il valore totale dell'aggregato.
   *
   * <p>Il valore viene calcolato ogni volta, non memorizzato.
   *
   * @return l'importo totale dell'aggregato
   */
  public Importo valoreTotale() {
    int totale = 0;
    for (Map.Entry<Moneta, Integer> entry : monete.entrySet()) {
      totale += entry.getKey().valore().inCentesimi() * entry.getValue();
    }
    return new Importo(totale / 100, totale % 100);
  }

  /**
   * Aggiunge una quantità di una moneta all'aggregato.
   *
   * @param moneta la moneta da aggiungere, non deve essere null
   * @param quantita la quantità da aggiungere, deve essere &gt; 0
   * @throws NullPointerException se moneta è null
   * @throws IllegalArgumentException se quantita &lt;= 0
   */
  public void aggiungi(Moneta moneta, int quantita) {
    Objects.requireNonNull(moneta, "La moneta non può essere null");
    if (quantita <= 0) {
      throw new IllegalArgumentException("La quantità deve essere positiva: " + quantita);
    }
    monete.merge(moneta, quantita, Integer::sum);
  }

  /**
   * Aggiunge tutte le monete di un altro aggregato a questo.
   *
   * @param altro l'aggregato da aggiungere, non deve essere null
   * @throws NullPointerException se altro è null
   */
  public void aggiungi(Aggregato altro) {
    Objects.requireNonNull(altro, "L'aggregato da aggiungere non può essere null");
    for (Map.Entry<Moneta, Integer> entry : altro.monete.entrySet()) {
      monete.merge(entry.getKey(), entry.getValue(), Integer::sum);
    }
  }

  /**
   * Rimuove tutte le monete di un altro aggregato da questo.
   *
   * @param altro l'aggregato da rimuovere, non deve essere null
   * @throws NullPointerException se altro è null
   * @throws AggregatoInsufficienteException se la rimozione non è possibile
   */
  public void rimuovi(Aggregato altro) throws AggregatoInsufficienteException {
    Objects.requireNonNull(altro, "L'aggregato da rimuovere non può essere null");

    // Prima verifica se il valore totale è sufficiente
    if (this.valoreTotale().minoreDi(altro.valoreTotale())) {
      throw new AggregatoInsufficienteException(AggregatoInsufficienteException.Motivo.VALUE);
    }

    // Poi verifica se ci sono abbastanza monete di ogni tipo
    for (Map.Entry<Moneta, Integer> entry : altro.monete.entrySet()) {
      Moneta m = entry.getKey();
      int richiesta = entry.getValue();
      int disponibile = this.quantita(m);
      if (disponibile < richiesta) {
        throw new AggregatoInsufficienteException(AggregatoInsufficienteException.Motivo.COINS);
      }
    }

    // Esegue la rimozione
    for (Map.Entry<Moneta, Integer> entry : altro.monete.entrySet()) {
      Moneta m = entry.getKey();
      int richiesta = entry.getValue();
      int nuovaQuantita = monete.get(m) - richiesta;
      if (nuovaQuantita == 0) {
        monete.remove(m);
      } else {
        monete.put(m, nuovaQuantita);
      }
    }
  }

  /**
   * Verifica se questo aggregato contiene almeno le monete dell'altro.
   *
   * @param altro l'aggregato da verificare, non deve essere null
   * @return true se questo aggregato contiene almeno le monete dell'altro
   * @throws NullPointerException se altro è null
   */
  public boolean contiene(Aggregato altro) {
    Objects.requireNonNull(altro, "L'aggregato da verificare non può essere null");
    for (Map.Entry<Moneta, Integer> entry : altro.monete.entrySet()) {
      if (this.quantita(entry.getKey()) < entry.getValue()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Restituisce un iteratore sulle coppie (moneta, quantità) ordinate per valore della moneta.
   *
   * <p>L'iteratore è su una copia dei dati per sicurezza.
   *
   * @return un iteratore sulle entry dell'aggregato
   */
  @Override
  public Iterator<Map.Entry<Moneta, Integer>> iterator() {
    // Ritorna un iteratore su una copia per evitare modifiche concorrenti
    return new TreeMap<>(monete).entrySet().iterator();
  }

  /**
   * Crea un aggregato a partire da una stringa nel formato "n x valore, m x valore, ...".
   *
   * <p>Esempi: "10 x .20, 5 x .50, 20 x 1"
   *
   * @param s la stringa da convertire, non deve essere null
   * @return l'aggregato corrispondente
   * @throws NullPointerException se s è null
   * @throws IllegalArgumentException se s non è nel formato corretto
   */
  public static Aggregato parse(String s) {
    if (s == null) {
      throw new NullPointerException("La stringa non può essere null");
    }

    Aggregato result = new Aggregato();
    String trimmed = s.trim();
    if (trimmed.isEmpty()) {
      return result;
    }

    String[] parti = trimmed.split(",");
    for (String parte : parti) {
      String p = parte.trim();
      if (p.isEmpty()) continue;

      // Formato: "n x valore"
      String[] tokens = p.split("\\s*x\\s*", 2);
      if (tokens.length != 2) {
        throw new IllegalArgumentException("Formato non valido, atteso 'n x valore': " + p);
      }

      int quantita;
      try {
        quantita = Integer.parseInt(tokens[0].trim());
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Quantità non valida: " + tokens[0], e);
      }

      if (quantita <= 0) {
        throw new IllegalArgumentException("La quantità deve essere positiva: " + quantita);
      }

      Moneta moneta = Moneta.parse(tokens[1].trim());
      if (moneta == null) {
        throw new IllegalArgumentException("Moneta non valida: " + tokens[1]);
      }

      result.aggiungi(moneta, quantita);
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

  /**
   * Restituisce la rappresentazione testuale dell'aggregato.
   *
   * <p>Il formato è: "&lt;n x valore, m x valore, ...&gt;" ordinato per valore crescente della
   * moneta.
   *
   * @return la rappresentazione testuale
   */
  @Override
  public String toString() {
    if (monete.isEmpty()) {
      return "<>";
    }

    StringBuilder sb = new StringBuilder("<");
    boolean first = true;
    for (Map.Entry<Moneta, Integer> entry : monete.entrySet()) {
      if (!first) {
        sb.append(", ");
      }
      first = false;
      sb.append(entry.getValue()).append(" x ").append(entry.getKey());
    }
    sb.append(">");
    return sb.toString();
  }
}
