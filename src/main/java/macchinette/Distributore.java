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
import java.util.Objects;
import java.util.Optional;

/**
 * Distributore automatico con binari, fondo cassa e strategia resto.
 *
 * <p><strong>RI:</strong> binari != null, non vuoto, elementi != null, fondoCassa != null,
 * strategia != null
 *
 * <p><strong>AF:</strong> AF(binari, fondoCassa, strategia) = distributore con binari [0..n-1],
 * quel fondo cassa e quella strategia
 */
public class Distributore {

  private final List<Binario> binari;
  private final Aggregato fondoCassa;
  private final StrategiaResto strategia;

  /**
   * Crea un distributore.
   *
   * @param binari lista binari (non vuota)
   * @param fondoCassa fondo cassa iniziale
   * @param strategia strategia per il resto
   */
  public Distributore(List<Binario> binari, Aggregato fondoCassa, StrategiaResto strategia) {
    Objects.requireNonNull(binari);
    Objects.requireNonNull(fondoCassa);
    Objects.requireNonNull(strategia);
    if (binari.isEmpty()) throw new IllegalArgumentException("binari vuoti");
    for (int i = 0; i < binari.size(); i++)
      if (binari.get(i) == null) throw new NullPointerException("binario " + i + " null");

    this.binari = new ArrayList<>(binari);
    this.fondoCassa = new Aggregato(fondoCassa);
    this.strategia = strategia;
  }

  /** Numero di binari. */
  public int numeroBinari() {
    return binari.size();
  }

  /** Valore totale del fondo cassa. */
  public Importo valoreFondoCassa() {
    return fondoCassa.valoreTotale();
  }

  /** Aggiunge monete al fondo cassa. */
  public void aggiungiAlFondoCassa(Aggregato monete) {
    Objects.requireNonNull(monete);
    fondoCassa.aggiungi(monete);
  }

  /** Svuota il fondo cassa e restituisce le monete. */
  public Aggregato svuotaFondoCassa() {
    Aggregato copia = new Aggregato(fondoCassa);
    for (Moneta m : Moneta.values()) {
      int q = fondoCassa.quantita(m);
      if (q > 0) {
        Aggregato temp = new Aggregato();
        temp.aggiungi(m, q);
        try {
          fondoCassa.rimuovi(temp);
        } catch (AggregatoInsufficienteException e) {
          throw new AssertionError(e);
        }
      }
    }
    return copia;
  }

  /**
   * Carica prodotti nei binari (in ordine).
   *
   * @return numero di prodotti non caricati
   */
  public int carica(Prodotto prodotto, int quantita) {
    Objects.requireNonNull(prodotto);
    if (quantita <= 0) throw new IllegalArgumentException("quantità non positiva");

    int rimanenti = quantita;
    for (Binario bin : binari) {
      if (rimanenti <= 0) break;
      if (!bin.accetta(prodotto)) continue;

      int spazio = bin.spazioDisponibile();
      if (spazio <= 0) continue;

      int daCaricare = Math.min(rimanenti, spazio);
      try {
        bin.carica(prodotto, daCaricare);
        rimanenti -= daCaricare;
      } catch (BinarioException e) {
        // già verificato, continua
      }
    }
    return rimanenti;
  }

  /**
   * Eroga un prodotto dal binario specificato.
   *
   * @return aggregato del resto
   * @throws ErogazioneException se impossibile (SLOT, EMPTY, VALUE, CHANGE)
   */
  public Aggregato eroga(int indiceBinario, Aggregato pagamento) throws ErogazioneException {
    Objects.requireNonNull(pagamento);

    if (indiceBinario < 0 || indiceBinario >= binari.size())
      throw new ErogazioneException(ErogazioneException.Motivo.SLOT);

    Binario bin = binari.get(indiceBinario);
    if (bin.vuoto())
      throw new ErogazioneException(ErogazioneException.Motivo.EMPTY);

    Importo prezzo = bin.prodotto().prezzo();
    Importo pagato = pagamento.valoreTotale();

    if (pagato.minoreDi(prezzo))
      throw new ErogazioneException(ErogazioneException.Motivo.VALUE);

    Importo importoResto = pagato.sottrai(prezzo);

    Aggregato resto;
    if (importoResto.equals(Importo.ZERO)) {
      resto = new Aggregato();
    } else {
      Aggregato disponibile = new Aggregato(fondoCassa);
      disponibile.aggiungi(pagamento);

      Optional<Aggregato> restoOpt = strategia.calcola(importoResto, disponibile);
      if (restoOpt.isEmpty())
        throw new ErogazioneException(ErogazioneException.Motivo.CHANGE);
      resto = restoOpt.get();
    }

    // Esegue transazione
    fondoCassa.aggiungi(pagamento);
    if (!resto.vuoto()) {
      try {
        fondoCassa.rimuovi(resto);
      } catch (AggregatoInsufficienteException e) {
        throw new AssertionError(e);
      }
    }
    try {
      bin.dispensa();
    } catch (BinarioException e) {
      throw new AssertionError(e);
    }

    return resto;
  }

  /** Classe helper per rappresentare un binario con il suo indice. */
  public static class BinarioConIndice {
    private final int indice;
    private final Binario binario;

    public BinarioConIndice(int indice, Binario binario) {
      this.indice = indice;
      this.binario = binario;
    }

    public int indice() {
      return indice;
    }

    public Binario binario() {
      return binario;
    }
  }

  /** Iteratore sui binari non vuoti con il loro indice. */
  public Iterator<BinarioConIndice> binariNonVuoti() {
    List<BinarioConIndice> result = new ArrayList<>();
    for (int i = 0; i < binari.size(); i++) {
      if (!binari.get(i).vuoto()) {
        result.add(new BinarioConIndice(i, binari.get(i)));
      }
    }
    return result.iterator();
  }

  @Override
  public String toString() {
    return "Distributore[binari=" + binari.size() + ", fondoCassa=" + fondoCassa.valoreTotale() + "]";
  }
}
