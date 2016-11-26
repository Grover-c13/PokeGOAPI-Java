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

import POGOProtos.Networking.Requests.Messages.CheckChallenge.CheckChallengeMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckChallengeResponseOuterClass.CheckChallengeResponse;
import com.google.protobuf.ByteString;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.util.AsyncHelper;
import com.pokegoapi.util.Log;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import okhttp3.OkHttpClient;

import javax.swing.JFrame;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.regex.Pattern;

public class SolveCaptchaExample {
	public static final String USER_AGENT =
			"Mozilla/5.0 (Windows NT 10.0; WOW64)" +
			" AppleWebKit/537.36 (KHTML, like Gecko) " +
			"Chrome/54.0.2840.99 Safari/537.36";

	/**
	 * Opens a window for captcha solving if needed
	 *
	 * @param args args
	 */
	public static void main(String[] args) {
		/*
			Registers handler for the custom protocol used to send the token to the Pokemon app.

			If not set, the change listener registered in completeCaptcha would not fire when the token
			is sent.
		 */
		URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
			@Override
			public URLStreamHandler createURLStreamHandler(String protocol) {
				if (protocol.equals("unity")) {
					return new URLStreamHandler() {
						@Override
						protected URLConnection openConnection(URL url) throws IOException {
							return new URLConnection(url) {
								@Override
								public void connect() throws IOException {
									System.out.println("Received token: " + url.toString()
											.split(Pattern.quote(":"))[1]);
								}
							};
						}
					};
				}
				return null;
			}
		});

		OkHttpClient http = new OkHttpClient();
		PokemonGo api = new PokemonGo(http);
		try {
			api.login(new PtcCredentialProvider(http, ExampleLoginDetails.LOGIN, ExampleLoginDetails.PASSWORD));
			api.setLocation(-32.058087, 115.744325, 0);

			//Wait until challenge is requested
			while (!api.hasChallenge()) {
				Thread.sleep(100);
			}

			String challengeURL = api.getChallengeURL();

			completeCaptcha(api, challengeURL);
		} catch (Exception e) {
			Log.e("Main", "Failed to login! ", e);
		}
	}

	private static void completeCaptcha(final PokemonGo api, final String challengeURL) {
		JFXPanel panel = new JFXPanel();
		//Create WebView and WebEngine to display the captcha webpage
		WebView view = new WebView();
		WebEngine engine = view.getEngine();
		//Set UserAgent so captcha shows in the WebView
		engine.setUserAgent(USER_AGENT);
		engine.load(challengeURL);
		final JFrame frame = new JFrame("Solve Captcha");
		engine.locationProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.startsWith("unity:")) {
					String token = newValue.split(Pattern.quote(":"))[1];
					try {
						//Close this window, not valid anymore
						frame.setVisible(false);
						if (api.verifyChallenge(token)) {
							System.out.println("Captcha was correctly solved!");
						} else {
							System.out.println("Captcha was incorrectly solved! Retry.");
							//Removes the current challenge to allow the CheckChallengeMessage to send
							api.updateChallenge(null, false);
							CheckChallengeMessage message = CheckChallengeMessage.newBuilder().build();
							AsyncServerRequest request = new AsyncServerRequest(RequestType.CHECK_CHALLENGE, message);
							ByteString responseData =
									AsyncHelper.toBlocking(api.getRequestHandler().sendAsyncServerRequests(request));
							CheckChallengeResponse response = CheckChallengeResponse.parseFrom(responseData);
							String newChallenge = response.getChallengeUrl();
							if (newChallenge != null && newChallenge.length() > 0) {
								//New challenge URL, open a new window for that
								api.updateChallenge(newChallenge, true);
								completeCaptcha(api, newChallenge);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		//Applies the WebView to this panel
		panel.setScene(new Scene(view));
		frame.getContentPane().add(panel);
		frame.setSize(500, 500);
		frame.setVisible(true);
	}
}
