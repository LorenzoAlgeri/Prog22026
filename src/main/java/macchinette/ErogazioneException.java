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
 * Eccezione per erogazione impossibile.
 *
 * <p>Motivi: SLOT (indice inesistente), EMPTY (binario vuoto),
 * VALUE (pagamento insufficiente), CHANGE (resto non calcolabile).
 */
public class ErogazioneException extends Exception {

  private static final long serialVersionUID = 1L;

  /** Motivi dell'errore. */
  public enum Motivo {
    /** Indice binario non valido. */
    SLOT,
    /** Binario vuoto. */
    EMPTY,
    /** Pagamento insufficiente. */
    VALUE,
    /** Impossibile calcolare il resto. */
    CHANGE
  }

  private final Motivo motivo;

  /**
   * Crea l'eccezione con il motivo specificato.
   *
   * @param motivo causa dell'errore
   */
  public ErogazioneException(Motivo motivo) {
    super(motivoToString(motivo));
    this.motivo = motivo;
  }

  private static String motivoToString(Motivo m) {
    if (m == Motivo.SLOT) return "slot";
    else if (m == Motivo.EMPTY) return "empty";
    else if (m == Motivo.VALUE) return "value";
    else return "change";
  }

  /** Restituisce il motivo. */
  public Motivo motivo() {
    return motivo;
  }
}
