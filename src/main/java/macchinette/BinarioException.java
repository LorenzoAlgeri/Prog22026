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
 * Eccezione sollevata quando un'operazione su un binario non può essere eseguita.
 *
 * <p>I possibili motivi sono:
 *
 * <ul>
 *   <li>{@link Motivo#SIZE}: la taglia del prodotto eccede quella del binario
 *   <li>{@link Motivo#CAPACITY}: la quantità eccede la capacità del binario
 *   <li>{@link Motivo#ITEM}: il prodotto è diverso da quello già contenuto nel binario
 *   <li>{@link Motivo#EMPTY}: il binario è vuoto (per operazioni che richiedono contenuto)
 * </ul>
 */
public class BinarioException extends Exception {

  private static final long serialVersionUID = 1L;

  /** I possibili motivi dell'errore. */
  public enum Motivo {
    /** La taglia del prodotto eccede quella del binario. */
    SIZE,
    /** La quantità eccede la capacità del binario. */
    CAPACITY,
    /** Il prodotto è diverso da quello già contenuto. */
    ITEM,
    /** Il binario è vuoto. */
    EMPTY
  }

  /** Il motivo dell'errore. */
  private final Motivo motivo;

  /**
   * Costruisce l'eccezione con il motivo specificato.
   *
   * @param motivo il motivo dell'errore, non deve essere null
   * @throws NullPointerException se motivo è null
   */
  public BinarioException(Motivo motivo) {
    super(
        switch (motivo) {
          case SIZE -> "size";
          case CAPACITY -> "capacity";
          case ITEM -> "item";
          case EMPTY -> "empty";
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
