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

/**
 * Eccezione sollevata quando l'erogazione di un prodotto non può essere eseguita.
 *
 * <p>I possibili motivi sono:
 *
 * <ul>
 *   <li>{@link Motivo#SLOT}: l'indice del binario non esiste
 *   <li>{@link Motivo#EMPTY}: il binario è vuoto
 *   <li>{@link Motivo#VALUE}: il valore del pagamento è insufficiente
 *   <li>{@link Motivo#CHANGE}: non è possibile calcolare il resto
 * </ul>
 */
public class ErogazioneException extends Exception {

  /** I possibili motivi dell'errore. */
  public enum Motivo {
    /** L'indice del binario non esiste. */
    SLOT,
    /** Il binario è vuoto. */
    EMPTY,
    /** Il valore del pagamento è insufficiente. */
    VALUE,
    /** Non è possibile calcolare il resto. */
    CHANGE
  }

  /** Il motivo dell'errore. */
  private final Motivo motivo;

  /**
   * Costruisce l'eccezione con il motivo specificato.
   *
   * @param motivo il motivo dell'errore, non deve essere null
   * @throws NullPointerException se motivo è null
   */
  public ErogazioneException(Motivo motivo) {
    super(
        switch (motivo) {
          case SLOT -> "slot";
          case EMPTY -> "empty";
          case VALUE -> "value";
          case CHANGE -> "change";
        });
    if (motivo == null) {
      throw new NullPointerException("Il motivo non può essere null");
    }
    this.motivo = motivo;
  }

  /**
   * Restituisce il motivo dell'errore.
   *
   * @return il motivo
   */
  public Motivo motivo() {
    return motivo;
  }
}
