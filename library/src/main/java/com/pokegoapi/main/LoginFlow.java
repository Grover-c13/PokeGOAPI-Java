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

import POGOProtos.Enums.TutorialStateOuterClass.TutorialState;
import POGOProtos.Networking.Requests.Messages.DownloadRemoteConfigVersionMessageOuterClass.DownloadRemoteConfigVersionMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Inventories;
import com.pokegoapi.api.listener.LoginListener;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.api.settings.Settings;

import java.util.List;

public class LoginFlow {
	public static void beginLogin(final PokemonGo api, final PokemonCallback callback) {
		api.setPlayerProfile(new PlayerProfile(api, new PokemonCallback() {
			@Override
			public void onCompleted(Exception e) {
				if (callbackIfException(e, callback)) {
					return;
				}

				api.setSettings(new Settings(api));
				api.setInventories(new Inventories(api));
				downloadConfigVersion(api, callback);
			}
		}));
	}

	private static void downloadConfigVersion(final PokemonGo api, final PokemonCallback callback) {
		DownloadRemoteConfigVersionMessage message = CommonRequests.getDownloadRemoteConfigVersionMessageRequest();
		PokemonRequest request = new PokemonRequest(RequestType.DOWNLOAD_REMOTE_CONFIG_VERSION, message);
		api.fireRequestBlock(request, new PokemonCallback() {
			@Override
			public void onCompleted(Exception e) {
				if (callbackIfException(e, callback)) {
					return;
				}

				downloadAssetDigest(api, callback);
			}
		});
	}

	private static void downloadAssetDigest(final PokemonGo api, final PokemonCallback callback) {
		api.fireAssetRequestBlock(new PokemonCallback() {
			@Override
			public void onCompleted(Exception e) {
				if (callbackIfException(e, callback)) {
					return;
				}

				finishLogin(api, callback);
			}
		});
	}

	private static void finishLogin(final PokemonGo api, final PokemonCallback callback) {
		List<LoginListener> loginListeners = api.getListeners(LoginListener.class);

		for (LoginListener listener : loginListeners) {
			listener.onLogin(api);
		}

		handleTutorials(api.getPlayerProfile(), callback);
	}

	private static void handleTutorials(final PlayerProfile profile,
										final PokemonCallback callback) {
		List<TutorialState> states = profile.getTutorialState().getTutorialStates();

		activateAccount(profile, states, callback);
	}

	private static void activateAccount(final PlayerProfile profile, final List<TutorialState> states,
										final PokemonCallback callback) {
		if (states.isEmpty()) {
			profile.activateAccount(new PokemonCallback() {
				@Override
				public void onCompleted(Exception e) {
					if (callbackIfException(e, callback)) {
						return;
					}

					setupAvatar(profile, states, callback);
				}
			});
		} else {
			completeLogin(callback);
		}
	}

	private static void setupAvatar(final PlayerProfile profile, final List<TutorialState> states,
									final PokemonCallback callback) {
		if (!states.contains(TutorialState.AVATAR_SELECTION)) {
			profile.setupAvatar(new PokemonCallback() {
				@Override
				public void onCompleted(Exception e) {
					if (callbackIfException(e, callback)) {
						return;
					}

					encounter(profile, states, callback);
				}
			});
		}
	}

	private static void encounter(final PlayerProfile profile, final List<TutorialState> states,
									final PokemonCallback callback) {
		if (!states.contains(TutorialState.POKEMON_CAPTURE)) {
			profile.setupAvatar(new PokemonCallback() {
				@Override
				public void onCompleted(Exception e) {
					if (callbackIfException(e, callback)) {
						return;
					}

					claimName(profile, states, callback);
				}
			});
		}
	}

	private static void claimName(final PlayerProfile profile, final List<TutorialState> states,
								  final PokemonCallback callback) {
		if (!states.contains(TutorialState.NAME_SELECTION)) {
			profile.claimCodeName(new PokemonCallback() {
				@Override
				public void onCompleted(Exception e) {
					if (callbackIfException(e, callback)) {
						return;
					}

					completeTutorial(profile, states, callback);
				}
			});
		}
	}

	private static void completeTutorial(final PlayerProfile profile, final List<TutorialState> states,
								  final PokemonCallback callback) {
		if (!states.contains(TutorialState.FIRST_TIME_EXPERIENCE_COMPLETE)) {
			profile.firstTimeExperienceComplete(new PokemonCallback() {
				@Override
				public void onCompleted(Exception e) {
					if (callbackIfException(e, callback)) {
						return;
					}

					completeLogin(callback);
				}
			});
		}
	}

	private static void completeLogin(final PokemonCallback callback) {
		callback.onCompleted(null);
	}

	private static boolean callbackIfException(Exception e, PokemonCallback callback) {
		if (e != null) {
			callback.onCompleted(e);
			return true;
		}
		return false;
	}
}
