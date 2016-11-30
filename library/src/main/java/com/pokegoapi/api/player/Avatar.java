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

package com.pokegoapi.api.player;

public interface Avatar {
	int id();

	enum Skin implements Avatar {
		LIGHT,
		YELLOW,
		BROWN,
		DARK_BROWN;

		@Override
		public int id() {
			return ordinal();
		}
	}

	enum Hair implements Avatar {
		BROWN,
		BLONDE,
		BLACK,
		RED,
		BLUE,
		PURPLE;

		@Override
		public int id() {
			return ordinal();
		}
	}

	enum Eye implements Avatar {
		BLUE,
		GREEN,
		BROWN,
		BLACK,
		LIGHT_BLUE;

		@Override
		public int id() {
			return ordinal();
		}
	}

	enum MaleHat implements Avatar {
		PLAIN_BLACK,
		BLACK_POKEBALL,
		BLACK_YELLOW_POKEBALL,
		BLACK_RED_POKEBALL,
		BLACK_BLUE_POKEBALL;

		@Override
		public int id() {
			return ordinal();
		}
	}

	enum FemaleHat implements Avatar {
		RED_WHITE_FRONT_ORANGE_POKEBALL,
		BLUE_WHITE_FRONT_YELLOW_POKEBALL,
		BLACK_YELLOW_POKEBALL,
		RED_WHITE_FRONT_BLACK_POKEBALL,
		YELLOW_WHITE_FRONT_BLACK_POKEBALL;

		@Override
		public int id() {
			return ordinal();
		}
	}

	enum MaleShirt implements Avatar {
		RED,
		ORANGE,
		YELLOW,
		BLUE;

		@Override
		public int id() {
			return ordinal();
		}
	}

	enum FemaleShirt implements Avatar {
		MAROON,
		WHITE_BLUE_LINES,
		YELLOW_WHITE,
		BLUE,
		RED,
		WHITE_RED_LINES,
		WHITE_YELLOW_LINES,
		YELLOW,
		ORANGE_WHITE;

		@Override
		public int id() {
			return ordinal();
		}
	}

	enum MalePants implements Avatar {
		BLACK_RED_STRIPE,
		BLACK_ORANGE_DOUBLE_STRIPE,
		BLACK_ORANGE_STRIPE;

		@Override
		public int id() {
			return ordinal();
		}
	}

	enum FemalePants implements Avatar {
		MAROON_BLACK,
		BLUE_BLACK_BLUE_STRIPE,
		BLACK_PURPLE_STRIPE,
		BLUE_BLACK,
		RED_BLACK,
		YELLOW_BLACK;

		@Override
		public int id() {
			return ordinal();
		}
	}

	enum MaleShoes implements Avatar {
		BLACK_GREEN_STRIPE,
		BLACK_WHITE_STRIPE,
		BLAC_YELLOW_STRIPE,
		BLUE_WHITE_STRIPE,
		GREEN_WHITE_STRIPE,
		RED_WHITE_STRIPE,
		YELLOW_WHITE_STRIPE;

		@Override
		public int id() {
			return ordinal();
		}
	}

	enum FemaleShoes implements Avatar {
		BLACK_ORANGE,
		BLACK_RED_STRIPE,
		BLACK_YELLOW_STRIPE,
		BLUE_WHITE_STRIPE,
		ORANGE_WHITE_STRIPE,
		RED_RED_STRIPE,
		YELLOW_YELLOW_STRIPE;

		@Override
		public int id() {
			return ordinal();
		}
	}

	enum MaleBackpack implements Avatar {
		GRAY_RED_POKEBALL,
		GRAY_GRAY_POKEBALL,
		GRAY_YELLOW_POKEBALL,
		BLACK_BLUE_BOTTOM_BLUE_POKEBALL,
		BLACK_RED_STRIPE_WHITE_POKEBALL,
		BLACK_GREEN_STRIPE_GREEN_POKEBALL;

		@Override
		public int id() {
			return ordinal();
		}
	}

	enum FemaleBackpack implements Avatar {
		WHITE_GRAY_RED_POKEBALL,
		GRAY_BLACK_YELLOW_POKEBALL,
		WHITE_BLACK_PURPLE_POKEBALL;

		@Override
		public int id() {
			return ordinal();
		}
	}
}