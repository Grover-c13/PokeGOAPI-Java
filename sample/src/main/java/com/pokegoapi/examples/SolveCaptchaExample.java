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
import com.pokegoapi.util.CaptchaSolveHelper;
import com.pokegoapi.util.Log;
import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import okhttp3.OkHttpClient;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

			api.login(new PtcCredentialProvider(http, ExampleConstants.LOGIN, ExampleConstants.PASSWORD));
			api.setLocation(ExampleConstants.LATITUDE, ExampleConstants.LONGITUDE, ExampleConstants.ALTITUDE);

			while (!api.hasChallenge()) {
			}
		} catch (Exception e) {
			Log.e("Main", "Failed to run captcha example! ", e);
		}
	}

	private static void completeCaptcha(final PokemonGo api, final String challengeURL) {
		//Run this on the swing thread
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				//Startup JFX
				PlatformImpl.startup(new Runnable() {
					@Override
					public void run() {
					}
				});

				//Run on JFX Thread
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
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
									frame.dispose();

									if (api.verifyChallenge(token)) {
										System.out.println("Captcha was correctly solved!");
									} else {
										System.out.println("Captcha was incorrectly solved! Please try again.");

						/*
							Ask for a new challenge url, don't need to check the result,
							because the LoginListener will be called when this completed.
						*/
										api.checkChallenge();
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
						//Don't allow this window to be closed
						frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
						frame.addWindowListener(new WindowAdapter() {
							@Override
							public void windowClosing(WindowEvent e) {
								System.out.println("Please solve the captcha before closing the window!");
							}
						});
					}
				});
			}
		});
	}
}
