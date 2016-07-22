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
 * An S2Region represents a two-dimensional region over the unit sphere. It is
 * an abstract interface with various concrete subtypes.
 *
 *  The main purpose of this interface is to allow complex regions to be
 * approximated as simpler regions. So rather than having a wide variety of
 * virtual methods that are implemented by all subtypes, the interface is
 * restricted to methods that are useful for computing approximations.
 *
 *
 */
public interface S2Region {

  /** Return a bounding spherical cap. */
  public abstract S2Cap getCapBound();


  /** Return a bounding latitude-longitude rectangle. */
  public abstract S2LatLngRect getRectBound();

  /**
   * If this method returns true, the region completely contains the given cell.
   * Otherwise, either the region does not contain the cell or the containment
   * relationship could not be determined.
   */
  public abstract boolean contains(S2Cell cell);

  /**
   * If this method returns false, the region does not intersect the given cell.
   * Otherwise, either region intersects the cell, or the intersection
   * relationship could not be determined.
   */
  public abstract boolean mayIntersect(S2Cell cell);
}
