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
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class Heartbeat {
	@Getter
	private PokemonGo api;

	private long nextMapUpdate = Long.MIN_VALUE;
	private long minMapRefresh;
	private long maxMapRefresh;

	private boolean active;

	private Queue<Runnable> tasks = new LinkedBlockingDeque<>();

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
		active = true;
		MapSettings mapSettings = api.getSettings().getMapSettings();
		minMapRefresh = (long) mapSettings.getMinRefresh();
		maxMapRefresh = (long) mapSettings.getMaxRefresh();
		beat();
		Thread heartbeatThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
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
		heartbeatThread.setName("Pokemon GO Heartbeat");
		heartbeatThread.start();
	}

	/**
	 * Performs a single heartbeat
	 */
	public void beat() {
		if (!api.hasChallenge()) {
			List<HeartbeatListener> listeners = api.getListeners(HeartbeatListener.class);
			long time = api.currentTimeMillis();
			if (time >= nextMapUpdate) {
				nextMapUpdate = time + minMapRefresh;
				Map map = api.getMap();
				try {
					map.update();
					for (HeartbeatListener listener : listeners) {
						listener.onMapUpdate(api, map.getMapObjects());
					}
				} catch (Exception exception) {
					for (HeartbeatListener listener : listeners) {
						listener.onMapUpdateException(api, exception);
					}
				}
			}
		}
		long startTime = api.currentTimeMillis();
		while (!tasks.isEmpty()) {
			Runnable task = tasks.poll();
			task.run();
			if (api.currentTimeMillis() - startTime > 1000) {
				break;
			}
		}
	}

	/**
	 * @return if the heartbeat is currently active
	 */
	public boolean active() {
		return this.active;
	}

	/**
	 * Enqueues the given task
	 *
	 * @param task the task to enqueue
	 */
	public void enqueueTask(Runnable task) {
		tasks.add(task);
	}
}
