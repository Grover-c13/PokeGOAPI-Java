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

package com.pokegoapi.examples;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.listener.LoginListener;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.main.AsyncReturn;
import com.pokegoapi.main.BlockingCallback;
import com.pokegoapi.main.SyncedReturn;
import com.pokegoapi.util.CaptchaSolveHelper;
import com.pokegoapi.util.Log;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import okhttp3.OkHttpClient;

import javax.swing.JFrame;

public class SolveCaptchaExample {
	/**
	 * Opens a window for captcha solving if needed
	 *
	 * @param args args
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		PokemonGo api = new PokemonGo(http);
		try {
			//Add listener to listen for the captcha URL
			api.addListener(new LoginListener() {
				@Override
				public void onLogin(PokemonGo api) {
					System.out.println("Successfully logged in with SolveCaptchaExample!");
				}

				@Override
				public void onChallenge(PokemonGo api, String challengeURL) {
					System.out.println("Captcha received! URL: " + challengeURL);
					completeCaptcha(api, challengeURL);
				}
			});

			BlockingCallback callback = new BlockingCallback();
			api.login(new PtcCredentialProvider(http, ExampleLoginDetails.LOGIN, ExampleLoginDetails.PASSWORD),
					callback);
			//Block thread until login is complete
			callback.block();
			api.setLocation(-32.058087, 115.744325, 0);
		} catch (Exception e) {
			Log.e("Main", "Failed to run captcha example! ", e);
		}
	}

	private static void completeCaptcha(final PokemonGo api, final String challengeURL) {
		JFXPanel panel = new JFXPanel();
		//Create a WebView and WebEngine to display the captcha from challengeURL.
		WebView view = new WebView();
		WebEngine engine = view.getEngine();
		//Set UserAgent so the captcha shows correctly in the WebView.
		engine.setUserAgent(CaptchaSolveHelper.USER_AGENT);
		engine.load(challengeURL);
		final JFrame frame = new JFrame("Solve Captcha");
		//Register listener to receive the token when the captcha has been solved from inside the WebView.
		CaptchaSolveHelper.Listener listener = new CaptchaSolveHelper.Listener() {
			@Override
			public void onTokenReceived(String token) {
				System.out.println("Token received: " + token + "!");
				//Remove this listener as we no longer need to listen for tokens, the captcha has been solved.
				CaptchaSolveHelper.removeListener(this);
				try {
					//Close this window, it not valid anymore.
					frame.setVisible(false);
					SyncedReturn<Boolean> verified = new SyncedReturn<>();
					api.verifyChallenge(token, verified);
					if (verified.get()) {
						System.out.println("Captcha was correctly solved!");
					} else {
						System.out.println("Captcha was incorrectly solved! Please try again.");

						/*
							Ask for a new challenge url.
						*/
						api.checkChallenge(new AsyncReturn<String>() {
							@Override
							public void onReceive(String url, Exception e) {
								System.out.println("New challenge URL received!");
							}
						});
					}
				} catch (Exception e) {
					Log.e("Main", "Error while solving captcha!", e);
				}
			}
		};
		CaptchaSolveHelper.registerListener(listener);

		//Applies the WebView to this panel
		panel.setScene(new Scene(view));
		frame.getContentPane().add(panel);
		frame.setSize(500, 500);
		frame.setVisible(true);
	}
}
