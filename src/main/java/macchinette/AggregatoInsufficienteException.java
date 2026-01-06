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
 * Eccezione sollevata quando non è possibile rimuovere un aggregato da un altro.
 *
 * <p>Ci sono due possibili motivi:
 *
 * <ul>
 *   <li>{@link Motivo#VALUE}: il valore totale dell'aggregato da cui rimuovere è insufficiente
 *   <li>{@link Motivo#COINS}: il valore totale è sufficiente, ma mancano monete di un certo taglio
 * </ul>
 */
public class AggregatoInsufficienteException extends Exception {

  /** I possibili motivi dell'insufficienza. */
  public enum Motivo {
    /** Il valore totale è insufficiente. */
    VALUE,
    /** Mancano monete di un certo taglio. */
    COINS
  }

  /** Il motivo dell'insufficienza. */
  private final Motivo motivo;

  /**
   * Costruisce l'eccezione con il motivo specificato.
   *
   * @param motivo il motivo dell'insufficienza, non deve essere null
   * @throws NullPointerException se motivo è null
   */
  public AggregatoInsufficienteException(Motivo motivo) {
    super(motivo == Motivo.VALUE ? "value" : "coins");
    if (motivo == null) {
      throw new NullPointerException("Il motivo non può essere null");
    }
    this.motivo = motivo;
  }

  /**
   * Restituisce il motivo dell'insufficienza.
   *
   * @return il motivo
   */
  public Motivo motivo() {
    return motivo;
  }
}
