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
 * R2Vector represents a vector in the two-dimensional space. It defines the
 * basic geometrical operations for 2D vectors, e.g. cross product, addition,
 * norm, comparison etc.
 *
 */
public final strictfp class R2Vector {
  private final double x;
  private final double y;

  public R2Vector() {
    this(0, 0);
  }

  public R2Vector(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public R2Vector(double[] coord) {
    if (coord.length != 2) {
      throw new IllegalStateException("Points must have exactly 2 coordinates");
    }
    x = coord[0];
    y = coord[1];
  }

  public double x() {
    return x;
  }

  public double y() {
    return y;
  }

  public double get(int index) {
    if (index > 1) {
      throw new ArrayIndexOutOfBoundsException(index);
    }
    return index == 0 ? this.x : this.y;
  }

  public static R2Vector add(final R2Vector p1, final R2Vector p2) {
    return new R2Vector(p1.x + p2.x, p1.y + p2.y);
  }

  public static R2Vector mul(final R2Vector p, double m) {
    return new R2Vector(m * p.x, m * p.y);
  }

  public double norm2() {
    return (x * x) + (y * y);
  }

  public static double dotProd(final R2Vector p1, final R2Vector p2) {
    return (p1.x * p2.x) + (p1.y * p2.y);
  }

  public double dotProd(R2Vector that) {
    return dotProd(this, that);
  }

  public double crossProd(final R2Vector that) {
    return this.x * that.y - this.y * that.x;
  }

  public boolean lessThan(R2Vector vb) {
    if (x < vb.x) {
      return true;
    }
    if (vb.x < x) {
      return false;
    }
    if (y < vb.y) {
      return true;
    }
    return false;
  }

  @Override
  public boolean equals(Object that) {
    if (!(that instanceof R2Vector)) {
      return false;
    }
    R2Vector thatPoint = (R2Vector) that;
    return this.x == thatPoint.x && this.y == thatPoint.y;
  }

  /**
   * Calcualates hashcode based on stored coordinates. Since we want +0.0 and
   * -0.0 to be treated the same, we ignore the sign of the coordinates.
   */
  @Override
  public int hashCode() {
    long value = 17;
    value += 37 * value + Double.doubleToLongBits(Math.abs(x));
    value += 37 * value + Double.doubleToLongBits(Math.abs(y));
    return (int) (value ^ (value >>> 32));
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }
}
