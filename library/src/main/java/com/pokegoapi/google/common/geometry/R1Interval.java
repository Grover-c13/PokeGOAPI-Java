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
 * An R1Interval represents a closed, bounded interval on the real line. It is
 * capable of representing the empty interval (containing no points) and
 * zero-length intervals (containing a single point).
 *
 */

public final strictfp class R1Interval {

  private final double lo;
  private final double hi;

  /**
   * Interval constructor. If lo &gt; hi, the interval is empty.
   * @param lo lo value
   * @param hi hi value
   */
  public R1Interval(double lo, double hi) {
    this.lo = lo;
    this.hi = hi;
  }

  /**
   * @return an empty interval. (Any interval where lo &gt; hi is considered
   * empty.)
   */
  public static R1Interval empty() {
    return new R1Interval(1, 0);
  }

  /**
   * Convenience method to construct an interval containing a single point.
   * @param p point to get interval for
   * @return a new R1Interval from the point.
   */
  public static R1Interval fromPoint(double p) {
    return new R1Interval(p, p);
  }

  /**
   * Convenience method to construct the minimal interval containing the two
   * given points. This is equivalent to starting with an empty interval and
   * calling AddPoint() twice, but it is more efficient.
   * @param p1 first point
   * @param p2 second point
   * @return new R1Interval
   */
  public static R1Interval fromPointPair(double p1, double p2) {
    if (p1 <= p2) {
      return new R1Interval(p1, p2);
    } else {
      return new R1Interval(p2, p1);
    }
  }

  public double lo() {
    return lo;
  }

  public double hi() {
    return hi;
  }

  /**
   * @return true if the interval is empty, i.e. it contains no points.
   */
  public boolean isEmpty() {
    return lo() > hi();
  }

  /**
   * @return the center of the interval. For empty intervals, the result is
   * arbitrary.
   */
  public double getCenter() {
    return 0.5 * (lo() + hi());
  }

  /**
   * @return the length of the interval. The length of an empty interval is
   * negative.
   */
  public double getLength() {
    return hi() - lo();
  }

    /**
     *
     * @param p point
     * @return true if p is between lo and hi
     */
  public boolean contains(double p) {
    return p >= lo() && p <= hi();
  }

    /**
     *
     * @param p point
     * @return true if p is inside lo and hi
     */
  public boolean interiorContains(double p) {
    return p > lo() && p < hi();
  }

  /**
   * @param y interval
   * @return true if this interval contains the interval 'y'.
   */
  public boolean contains(R1Interval y) {
    if (y.isEmpty()) {
      return true;
    }
    return y.lo() >= lo() && y.hi() <= hi();
  }

  /**
   * @param y interval
   * @return true if the interior of this interval contains the entire interval
   * 'y' (including its boundary).
   */
  public boolean interiorContains(R1Interval y) {
    if (y.isEmpty()) {
      return true;
    }
    return y.lo() > lo() && y.hi() < hi();
  }

  /**
   * @param y interval
   * @return true if this interval intersects the given interval, i.e. if they
   * have any points in common.
   */
  public boolean intersects(R1Interval y) {
    if (lo() <= y.lo()) {
      return y.lo() <= hi() && y.lo() <= y.hi();
    } else {
      return lo() <= y.hi() && lo() <= hi();
    }
  }

  /**
   * @param y interval
   * @return true if the interior of this interval intersects any point of the
   * given interval (including its boundary).
   */
  public boolean interiorIntersects(R1Interval y) {
    return y.lo() < hi() && lo() < y.hi() && lo() < hi() && y.lo() <= y.hi();
  }

  /**
   * Expand the interval so that it contains the given point "p".
   * @param p p
   * @return new R1Interval
   */
  public R1Interval addPoint(double p) {
    if (isEmpty()) {
      return R1Interval.fromPoint(p);
    } else if (p < lo()) {
      return new R1Interval(p, hi());
    } else if (p > hi()) {
      return new R1Interval(lo(), p);
    } else {
      return new R1Interval(lo(), hi());
    }
  }

  /**
   * @param radius radius
   * @return an interval that contains all points with a distance "radius" of a
   * point in this interval. Note that the expansion of an empty interval is
   * always empty.
   */
  public R1Interval expanded(double radius) {
    // assert (radius >= 0);
    if (isEmpty()) {
      return this;
    }
    return new R1Interval(lo() - radius, hi() + radius);
  }

  /**
   * @param y interval
   * @return the smallest interval that contains this interval and the given
   * interval "y".
   */
  public R1Interval union(R1Interval y) {
    if (isEmpty()) {
      return y;
    }
    if (y.isEmpty()) {
      return this;
    }
    return new R1Interval(Math.min(lo(), y.lo()), Math.max(hi(), y.hi()));
  }

  /**
   * @param y interval
   * @return the intersection of this interval with the given interval. Empty
   * intervals do not need to be special-cased.
   */
  public R1Interval intersection(R1Interval y) {
    return new R1Interval(Math.max(lo(), y.lo()), Math.min(hi(), y.hi()));
  }

  @Override
  public boolean equals(Object that) {
    if (that instanceof R1Interval) {
      R1Interval y = (R1Interval) that;
      // Return true if two intervals contain the same set of points.
      return (lo() == y.lo() && hi() == y.hi()) || (isEmpty() && y.isEmpty());

    }
    return false;
  }

  @Override
  public int hashCode() {
    if (isEmpty()) {
      return 17;
    }

    long value = 17;
    value = 37 * value + Double.doubleToLongBits(lo);
    value = 37 * value + Double.doubleToLongBits(hi);
    return (int) (value ^ (value >>> 32));
  }

  public boolean approxEquals(R1Interval y) {
    return approxEquals(y, 1e-15);
  }

  /**
   * Return true if length of the symmetric difference between the two intervals
   * is at most the given tolerance.
   * @param y y
   * @param maxError maxError
   */
  public boolean approxEquals(R1Interval y, double maxError) {
    if (isEmpty()) {
      return y.getLength() <= maxError;
    }
    if (y.isEmpty()) {
      return getLength() <= maxError;
    }
    return Math.abs(y.lo() - lo()) + Math.abs(y.hi() - hi()) <= maxError;
  }

  @Override
  public String toString() {
    return "[" + lo() + ", " + hi() + "]";
  }
}
