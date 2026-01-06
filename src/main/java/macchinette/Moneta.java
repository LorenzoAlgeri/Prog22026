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

import java.util.Objects;

/**
 * Rappresenta una moneta con un valore predefinito.
 *
 * <p>Le monete disponibili sono: 1, 2, 5, 10, 20, 50 centesimi e 1, 2 euro. L'ordinamento naturale
 * delle monete è dato dal loro valore.
 *
 * <p><strong>Invariante di rappresentazione:</strong> essendo un enum, l'invariante è garantito
 * dalla definizione stessa dei valori. Ogni valore ha un importo non null e corrispondente al
 * valore nominale della moneta.
 *
 * <p><strong>Funzione di astrazione:</strong> ogni valore dell'enum rappresenta una moneta fisica
 * con il corrispondente valore nominale.
 */
public enum Moneta {
  /** Moneta da 1 centesimo. */
  CENT_1(1),
  /** Moneta da 2 centesimi. */
  CENT_2(2),
  /** Moneta da 5 centesimi. */
  CENT_5(5),
  /** Moneta da 10 centesimi. */
  CENT_10(10),
  /** Moneta da 20 centesimi. */
  CENT_20(20),
  /** Moneta da 50 centesimi. */
  CENT_50(50),
  /** Moneta da 1 euro. */
  EURO_1(100),
  /** Moneta da 2 euro. */
  EURO_2(200);

  /** Il valore della moneta come importo. */
  private final Importo valore;

  /**
   * Costruisce una moneta con il valore specificato in centesimi.
   *
   * @param centesimi il valore in centesimi
   */
  Moneta(int centesimi) {
    this.valore = new Importo(centesimi / 100, centesimi % 100);
  }

  /**
   * Restituisce il valore di questa moneta come importo.
   *
   * @return l'importo corrispondente al valore della moneta
   */
  public Importo valore() {
    return valore;
  }

  /**
   * Restituisce la moneta corrispondente all'importo specificato.
   *
   * @param importo l'importo da convertire in moneta, non deve essere null
   * @return la moneta corrispondente, o null se l'importo non corrisponde a nessuna moneta valida
   * @throws NullPointerException se importo è null
   */
  public static Moneta fromImporto(Importo importo) {
    Objects.requireNonNull(importo, "L'importo non può essere null");
    for (Moneta m : values()) {
      if (m.valore.equals(importo)) {
        return m;
      }
    }
    return null;
  }

  /**
   * Crea una moneta a partire da una stringa che rappresenta un importo.
   *
   * @param s la stringa da convertire, non deve essere null
   * @return la moneta corrispondente, o null se l'importo non corrisponde a nessuna moneta valida
   * @throws NullPointerException se s è null
   * @throws IllegalArgumentException se s non è nel formato corretto
   */
  public static Moneta parse(String s) {
    if (s == null) {
      throw new NullPointerException("La stringa non può essere null");
    }
    Importo importo = Importo.parse(s);
    return fromImporto(importo);
  }

  /**
   * Restituisce la rappresentazione testuale della moneta.
   *
   * <p>Il formato è lo stesso dell'importo corrispondente.
   *
   * @return la rappresentazione testuale del valore della moneta
   */
  @Override
  public String toString() {
    return valore.toString();
  }
}
