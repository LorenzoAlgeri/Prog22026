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
 * Eccezione per rimozione impossibile da un aggregato.
 *
 * <p>Motivi: {@link Motivo#VALUE} se valore insufficiente,
 * {@link Motivo#COINS} se mancano monete del taglio richiesto.
 */
public class AggregatoInsufficienteException extends Exception {

  private static final long serialVersionUID = 1L;

  /** Motivi dell'insufficienza. */
  public enum Motivo {
    /** Valore totale insufficiente. */
    VALUE,
    /** Monete del taglio richiesto insufficienti. */
    COINS
  }

  private final Motivo motivo;

  /**
   * Crea l'eccezione con il motivo specificato.
   *
   * @param motivo causa dell'errore
   */
  public AggregatoInsufficienteException(Motivo motivo) {
    super(motivo == Motivo.VALUE ? "value" : "coins");
    this.motivo = motivo;
  }

  /** Restituisce il motivo. */
  public Motivo motivo() {
    return motivo;
  }
}
