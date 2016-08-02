/*
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.api.pokemon;

import POGOProtos.Enums.PokemonMoveOuterClass.PokemonMove;
import lombok.Getter;
import lombok.Setter;

public class PokemonMoveMeta {

	@Getter
	@Setter
	private PokemonMove move;
	@Getter
	@Setter
	private PokemonType type;
	@Getter
	@Setter
	private int power;
	@Getter
	@Setter
	private int accuracy;
	@Getter
	@Setter
	private double critChance;
	@Getter
	@Setter
	private int time;
	@Getter
	@Setter
	private int energy;

}
