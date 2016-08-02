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

package com.pokegoapi.util;

<<<<<<< HEAD:library/src/main/java/com/pokegoapi/util/SystemTimeImpl.java
public class SystemTimeImpl implements Time {
	@Override
	public long currentTimeMillis() {
		return System.currentTimeMillis();
=======
import POGOProtos.Data.Player.ContactSettingsOuterClass;
import lombok.Data;

@Data
public class ContactSettings {
	private ContactSettingsOuterClass.ContactSettings proto;

	public ContactSettings(ContactSettingsOuterClass.ContactSettings proto) {
		this.proto = proto;
	}

	public boolean getSendMarketingEmails() {
		return proto.getSendMarketingEmails();
	}

	public boolean getSendPushNotifications() {
		return proto.getSendPushNotifications();
>>>>>>> d1cd3f98c5c1ecb22cb0f363a0b430d84a66919e:src/main/java/com/pokegoapi/api/player/ContactSettings.java
	}
}
