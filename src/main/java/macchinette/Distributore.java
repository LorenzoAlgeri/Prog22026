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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Rappresenta un distributore automatico di prodotti.
 *
 * <p>Un distributore è composto da un elenco di binari, un fondo cassa e una strategia per il
 * calcolo del resto.
 *
 * <p><strong>Invariante di rappresentazione:</strong> binari != null &amp;&amp; !binari.isEmpty()
 * &amp;&amp; tutti gli elementi di binari sono != null &amp;&amp; fondoCassa != null &amp;&amp;
 * strategia != null
 *
 * <p><strong>Funzione di astrazione:</strong> AF(binari, fondoCassa, strategia) rappresenta un
 * distributore con i binari indicizzati da 0 a binari.size()-1, il fondo cassa e la strategia di
 * resto specificati.
 */
public class Distributore {

  /** L'elenco dei binari. */
  private final List<Binario> binari;

  /** Il fondo cassa. */
  private final Aggregato fondoCassa;

  /** La strategia per il calcolo del resto. */
  private final StrategiaResto strategia;

  /**
   * Costruisce un distributore con i binari, il fondo cassa e la strategia specificati.
   *
   * @param binari l'elenco dei binari, non deve essere null né vuoto
   * @param fondoCassa il fondo cassa iniziale, non deve essere null
   * @param strategia la strategia di calcolo del resto, non deve essere null
   * @throws NullPointerException se uno dei parametri è null
   * @throws IllegalArgumentException se binari è vuoto
   */
  public Distributore(List<Binario> binari, Aggregato fondoCassa, StrategiaResto strategia) {
    Objects.requireNonNull(binari, "L'elenco dei binari non può essere null");
    Objects.requireNonNull(fondoCassa, "Il fondo cassa non può essere null");
    Objects.requireNonNull(strategia, "La strategia non può essere null");

    if (binari.isEmpty()) {
      throw new IllegalArgumentException("L'elenco dei binari non può essere vuoto");
    }

    for (int i = 0; i < binari.size(); i++) {
      if (binari.get(i) == null) {
        throw new NullPointerException("Il binario all'indice " + i + " è null");
      }
    }

    // Copia difensiva
    this.binari = new ArrayList<>(binari);
    this.fondoCassa = new Aggregato(fondoCassa);
    this.strategia = strategia;
  }

  /**
   * Restituisce il numero di binari del distributore.
   *
   * @return il numero di binari
   */
  public int numeroBinari() {
    return binari.size();
  }

  /**
   * Restituisce il valore totale del fondo cassa.
   *
   * @return l'importo totale nel fondo cassa
   */
  public Importo valoreFondoCassa() {
    return fondoCassa.valoreTotale();
  }

  /**
   * Aggiunge monete al fondo cassa.
   *
   * @param monete l'aggregato di monete da aggiungere, non deve essere null
   * @throws NullPointerException se monete è null
   */
  public void aggiungiAlFondoCassa(Aggregato monete) {
    Objects.requireNonNull(monete, "L'aggregato di monete non può essere null");
    fondoCassa.aggiungi(monete);
  }

  /**
   * Svuota il fondo cassa e restituisce le monete.
   *
   * @return un aggregato contenente tutte le monete del fondo cassa
   */
  public Aggregato svuotaFondoCassa() {
    Aggregato copia = new Aggregato(fondoCassa);
    // Svuota rimuovendo tutto
    for (Moneta m : Moneta.values()) {
      int q = fondoCassa.quantita(m);
      if (q > 0) {
        Aggregato daRimuovere = new Aggregato();
        daRimuovere.aggiungi(m, q);
        try {
          fondoCassa.rimuovi(daRimuovere);
        } catch (AggregatoInsufficienteException e) {
          // Non dovrebbe mai accadere
          throw new AssertionError(e);
        }
      }
    }
    return copia;
  }

  /**
   * Carica una quantità di prodotti nel distributore.
   *
   * <p>I prodotti vengono caricati nei binari in ordine di indice, riempiendo ogni binario il più
   * possibile prima di passare al successivo.
   *
   * @param prodotto il prodotto da caricare, non deve essere null
   * @param quantita la quantità da caricare, deve essere &gt; 0
   * @return il numero di prodotti non caricati per mancanza di spazio
   * @throws NullPointerException se prodotto è null
   * @throws IllegalArgumentException se quantita &lt;= 0
   */
  public int carica(Prodotto prodotto, int quantita) {
    Objects.requireNonNull(prodotto, "Il prodotto non può essere null");
    if (quantita <= 0) {
      throw new IllegalArgumentException("La quantità deve essere positiva: " + quantita);
    }

    int rimanenti = quantita;

    for (Binario binario : binari) {
      if (rimanenti <= 0) break;

      // Verifica se il binario può accettare questo prodotto
      if (!binario.accetta(prodotto)) {
        continue;
      }

      // Quanti ne possiamo caricare in questo binario?
      int spazio = binario.spazioDisponibile();
      if (spazio <= 0) {
        continue;
      }

      int daCaricare = Math.min(rimanenti, spazio);

      try {
        binario.carica(prodotto, daCaricare);
        rimanenti -= daCaricare;
      } catch (BinarioException e) {
        // Non dovrebbe accadere dato che abbiamo già verificato
        continue;
      }
    }

    return rimanenti;
  }

  /**
   * Eroga un prodotto dal binario specificato.
   *
   * @param indiceBinario l'indice del binario
   * @param pagamento l'aggregato di monete usato per il pagamento, non deve essere null
   * @return l'aggregato del resto
   * @throws NullPointerException se pagamento è null
   * @throws ErogazioneException se l'erogazione non è possibile
   */
  public Aggregato eroga(int indiceBinario, Aggregato pagamento) throws ErogazioneException {
    Objects.requireNonNull(pagamento, "Il pagamento non può essere null");

    // Verifica indice
    if (indiceBinario < 0 || indiceBinario >= binari.size()) {
      throw new ErogazioneException(ErogazioneException.Motivo.SLOT);
    }

    Binario binario = binari.get(indiceBinario);

    // Verifica se vuoto
    if (binario.vuoto()) {
      throw new ErogazioneException(ErogazioneException.Motivo.EMPTY);
    }

    Prodotto prodotto = binario.prodotto();
    Importo prezzo = prodotto.prezzo();
    Importo pagato = pagamento.valoreTotale();

    // Verifica pagamento sufficiente
    if (pagato.minoreDi(prezzo)) {
      throw new ErogazioneException(ErogazioneException.Motivo.VALUE);
    }

    // Calcola il resto
    Importo importoResto = pagato.sottrai(prezzo);

    // Se il resto è zero, nessun problema
    Aggregato resto;
    if (importoResto.equals(Importo.ZERO)) {
      resto = new Aggregato();
    } else {
      // Unisce il pagamento al fondo cassa temporaneamente per calcolare il resto
      Aggregato disponibile = new Aggregato(fondoCassa);
      disponibile.aggiungi(pagamento);

      Optional<Aggregato> restoOpt = strategia.calcola(importoResto, disponibile);
      if (restoOpt.isEmpty()) {
        throw new ErogazioneException(ErogazioneException.Motivo.CHANGE);
      }
      resto = restoOpt.get();
    }

    // A questo punto l'erogazione può procedere
    // 1. Aggiungi il pagamento al fondo cassa
    fondoCassa.aggiungi(pagamento);

    // 2. Rimuovi il resto dal fondo cassa
    if (!resto.vuoto()) {
      try {
        fondoCassa.rimuovi(resto);
      } catch (AggregatoInsufficienteException e) {
        // Non dovrebbe accadere perché abbiamo già verificato
        throw new AssertionError(e);
      }
    }

    // 3. Dispensa il prodotto
    try {
      binario.dispensa();
    } catch (BinarioException e) {
      // Non dovrebbe accadere perché abbiamo già verificato
      throw new AssertionError(e);
    }

    return resto;
  }

  /**
   * Restituisce un iteratore sui binari non vuoti con il loro indice.
   *
   * <p>Ogni elemento è una coppia (indice, binario).
   *
   * @return un iteratore sulle entry (indice, binario) per i binari non vuoti
   */
  public Iterator<Map.Entry<Integer, Binario>> binariNonVuoti() {
    List<Map.Entry<Integer, Binario>> nonVuoti = new ArrayList<>();
    for (int i = 0; i < binari.size(); i++) {
      if (!binari.get(i).vuoto()) {
        final int indice = i;
        final Binario b = binari.get(i);
        nonVuoti.add(
            new Map.Entry<Integer, Binario>() {
              @Override
              public Integer getKey() {
                return indice;
              }

              @Override
              public Binario getValue() {
                return b;
              }

              @Override
              public Binario setValue(Binario value) {
                throw new UnsupportedOperationException();
              }
            });
      }
    }
    return nonVuoti.iterator();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Distributore[binari=").append(binari.size());
    sb.append(", fondoCassa=").append(fondoCassa.valoreTotale());
    sb.append("]");
    return sb.toString();
  }
}
