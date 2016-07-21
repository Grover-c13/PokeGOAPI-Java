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
package com.pokegoapi.google.common.geometry;

/**
 * Like an Integer, but mutable :)
 *
 *  Sometimes it is just really convenient to be able to pass a MutableInteger
 * as a parameter to a function, or for synchronization purposes (so that you
 * can guard access to an int value without creating a separate Object just to
 * synchronize on).
 *
 * NOT thread-safe
 *
 */
public class MutableInteger {

  private int value;
  private Integer cachedIntegerValue = null;

  public MutableInteger(final int i) {
    value = i;
  }

  public int intValue() {
    return value;
  }

  public Integer integerValue() {
    if (cachedIntegerValue == null) {
      cachedIntegerValue = intValue();
    }
    return cachedIntegerValue;
  }

  @Override
  public boolean equals(final Object o) {
    return o instanceof MutableInteger && ((MutableInteger) o).value == this.value;
  }

  @Override
  public int hashCode() {
    return integerValue().hashCode();
  }

  public void setValue(final int value) {
    this.value = value;
    cachedIntegerValue = null;
  }

  public void increment() {
    add(1);
  }

  public void add(final int amount) {
    setValue(value + amount);
  }

  public void decrement() {
    subtract(1);
  }

  public void subtract(final int amount) {
    add(amount * -1);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
