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
 * An abstract directed edge from one S2Point to another S2Point.
 *
 * @author kirilll@google.com (Kirill Levin)
 */
public final class S2Edge {

  private final S2Point start;
  private final S2Point end;

  public S2Edge(S2Point start, S2Point end) {
    this.start = start;
    this.end = end;
  }

  public S2Point getStart() {
    return start;
  }

  public S2Point getEnd() {
    return end;
  }

  @Override
  public String toString() {
    return String.format("Edge: (%s -> %s)\n   or [%s -> %s]",
        start.toDegreesString(), end.toDegreesString(), start, end);
  }

  @Override
  public int hashCode() {
    return getStart().hashCode() - getEnd().hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || !(o instanceof S2Edge)) {
      return false;
    }
    S2Edge other = (S2Edge) o;
    return getStart().equals(other.getStart()) && getEnd().equals(other.getEnd());
  }
}
