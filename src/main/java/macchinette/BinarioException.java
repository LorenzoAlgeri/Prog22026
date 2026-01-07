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
 * Eccezione per operazioni non valide su un binario.
 *
 * <p>Motivi: SIZE (taglia eccessiva), CAPACITY (capacità superata),
 * ITEM (prodotto diverso), EMPTY (binario vuoto).
 */
public class BinarioException extends Exception {

  private static final long serialVersionUID = 1L;

  /** Motivi dell'errore. */
  public enum Motivo {
    /** Taglia prodotto eccede quella del binario. */
    SIZE,
    /** Quantità eccede la capacità. */
    CAPACITY,
    /** Prodotto diverso da quello contenuto. */
    ITEM,
    /** Binario vuoto. */
    EMPTY
  }

  private final Motivo motivo;

  /**
   * Crea l'eccezione con il motivo specificato.
   *
   * @param motivo causa dell'errore
   */
  public BinarioException(Motivo motivo) {
    super(toMessage(motivo));
    this.motivo = motivo;
  }

  // converte motivo in stringa per getMessage()
  private static String toMessage(Motivo m) {
    if (m == Motivo.SIZE) return "size";
    if (m == Motivo.CAPACITY) return "capacity";
    if (m == Motivo.ITEM) return "item";
    return "empty";
  }

  /** Restituisce il motivo. */
  public Motivo motivo() {
    return motivo;
  }
}
