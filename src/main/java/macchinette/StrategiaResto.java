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

import java.util.Optional;

/**
 * Interfaccia funzionale per le strategie di calcolo del resto.
 *
 * <p>Una strategia determina come selezionare le monete da un aggregato disponibile per comporre
 * esattamente un certo importo di resto.
 */
@FunctionalInterface
public interface StrategiaResto {

  /**
   * Calcola il resto dall'aggregato disponibile.
   *
   * @param resto l'importo del resto da calcolare, non deve essere null
   * @param disponibile l'aggregato di monete disponibili, non deve essere null
   * @return un Optional contenente l'aggregato che compone esattamente il resto, o Optional.empty()
   *     se questa strategia non riesce a calcolare il resto
   * @throws NullPointerException se resto o disponibile sono null
   * @throws IllegalArgumentException se resto rappresenta un valore negativo
   */
  Optional<Aggregato> calcola(Importo resto, Aggregato disponibile);
}
