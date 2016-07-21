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
 * The area of an interior, i.e. the region on the left side of an odd
 * number of loops and optionally a centroid.
 * The area is between 0 and 4*Pi. If it has a centroid, it is
 * the true centroid of the interiord multiplied by the area of the shape.
 * Note that the centroid may not be contained by the shape.
 *
 * @author dbentley@google.com (Daniel Bentley)
 */
public final class S2AreaCentroid {

  private final double area;
  private final S2Point centroid;

  public S2AreaCentroid(double area, S2Point centroid) {
    this.area = area;
    this.centroid = centroid;
  }

  public double getArea() {
    return area;
  }

  public S2Point getCentroid() {
    return centroid;
  }
}
