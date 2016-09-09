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

package com.pokegoapi.main;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.listener.HeartbeatListener;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.api.settings.MapSettings;
import lombok.Getter;

import java.util.List;

public class Heartbeat {
	@Getter
	private PokemonGo api;

	private long nextMapUpdate = Long.MIN_VALUE;
	private long minMapRefresh;
	private long maxMapRefresh;
	private boolean updatingMap;

	private boolean active;

	private final Object lock = new Object();

	/**
	 * Create a new Heartbeat object for the given API
	 *
	 * @param api the api for this heartbeat
	 */
	public Heartbeat(PokemonGo api) {
		this.api = api;
	}

	/**
	 * Begins this heartbeat
	 */
	public void start() {
		if (!active) {
			active = true;
			beat();
			Thread heartbeatThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (active) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							break;
						}
						beat();
					}
				}
			});
			heartbeatThread.setDaemon(true);
			heartbeatThread.setName("Heartbeat thread");
			heartbeatThread.start();
		}
	}

	/**
	 * Performs a single heartbeat
	 */
	public void beat() {
		MapSettings mapSettings = api.settings.mapSettings;
		minMapRefresh = (long) mapSettings.minRefresh;
		maxMapRefresh = (long) mapSettings.maxRefresh;

		List<HeartbeatListener> listeners = api.getListeners(HeartbeatListener.class);
		long time = api.currentTimeMillis();
		boolean updatingMap;
		synchronized (lock) {
			updatingMap = this.updatingMap;
		}
		if (time >= nextMapUpdate && !updatingMap) {
			synchronized (lock) {
				this.updatingMap = true;
			}
			Map map = api.getMap();
			try {
				if (map.update()) {
					nextMapUpdate = time + minMapRefresh;
				}
				for (HeartbeatListener listener : listeners) {
					listener.onMapUpdate(api, map.mapObjects);
				}
			} catch (Exception exception) {
				for (HeartbeatListener listener : listeners) {
					listener.onMapUpdateException(api, exception);
				}
			}
			synchronized (lock) {
				this.updatingMap = false;
			}
		}
	}

	/**
	 * @return if the heartbeat is currently active
	 */
	public boolean active() {
		return active;
	}

	/**
	 * Exits this heartbeat
	 */
	public void exit() {
		active = false;
	}
}
