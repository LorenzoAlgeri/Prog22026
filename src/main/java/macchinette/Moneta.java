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
import java.util.Optional;

/**
 * Monete disponibili: 1, 2, 5, 10, 20, 50 centesimi e 1, 2 euro.
 *
 * <p><strong>RI:</strong> garantito dall'enum; il valore è sempre valido e corrisponde
 * a una delle otto denominazioni previste.
 *
 * <p><strong>AF:</strong> ogni costante dell'enum rappresenta una moneta fisica con il valore
 * nominale specificato; ad esempio EURO_1 rappresenta la moneta da 1 euro.
 */
public enum Moneta {
  CENT_1(1),
  CENT_2(2),
  CENT_5(5),
  CENT_10(10),
  CENT_20(20),
  CENT_50(50),
  EURO_1(100),
  EURO_2(200);

  private final Importo valore;

  Moneta(int centesimi) {
    this.valore = new Importo(centesimi / 100, centesimi % 100);
  }

  /** Restituisce il valore della moneta. */
  public Importo valore() {
    return valore;
  }

  /**
   * Restituisce la moneta corrispondente all'importo specificato.
   *
   * @param importo l'importo da convertire in moneta
   * @return Optional contenente la moneta se esiste, Optional.empty() altrimenti
   */
  public static Optional<Moneta> fromImporto(Importo importo) {
    Objects.requireNonNull(importo);
    for (Moneta m : values()) {
      if (m.valore.equals(importo)) return Optional.of(m);
    }
    return Optional.empty();
  }

  /**
   * Parsing di un importo come stringa.
   *
   * @return Optional contenente la moneta se corrisponde a una moneta valida, Optional.empty() altrimenti
   */
  public static Optional<Moneta> parse(String s) {
    if (s == null) throw new NullPointerException();
    return fromImporto(Importo.parse(s));
  }

  @Override
  public String toString() {
    return valore.toString();
  }
}
