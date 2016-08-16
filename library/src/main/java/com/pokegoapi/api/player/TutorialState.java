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

import POGOProtos.Enums.TutorialStateOuterClass;

import java.util.ArrayList;
import java.util.List;

public class TutorialState {
	private final ArrayList<TutorialStateOuterClass.TutorialState> tutorialStateList = new ArrayList<>();

	public TutorialState(List<TutorialStateOuterClass.TutorialState> tutorialStateList) {
		this.tutorialStateList.addAll(tutorialStateList);
	}

	public ArrayList<TutorialStateOuterClass.TutorialState> getTutorialStates() {
		return tutorialStateList;
	}

	public void addTutorialState(TutorialStateOuterClass.TutorialState state) {
		tutorialStateList.add(state);
	}

	public void addTutorialStates(List<TutorialStateOuterClass.TutorialState> states) {
		tutorialStateList.addAll(states);
	}
}