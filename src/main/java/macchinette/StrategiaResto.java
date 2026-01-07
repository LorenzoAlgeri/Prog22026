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
 * Strategia per il calcolo del resto.
 *
 * <p>Determina come selezionare monete per comporre esattamente un importo.
 */
@FunctionalInterface
public interface StrategiaResto {

  /**
   * Calcola il resto dall'aggregato disponibile.
   *
   * @param resto importo da comporre
   * @param disponibile monete disponibili
   * @return Optional con le monete per il resto, o empty se impossibile
   */
  Optional<Aggregato> calcola(Importo resto, Aggregato disponibile);
}
