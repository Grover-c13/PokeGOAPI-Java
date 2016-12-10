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

package com.pokegoapi.api;

import POGOProtos.Enums.TutorialStateOuterClass.TutorialState;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import POGOProtos.Networking.Envelopes.SignatureOuterClass;
import POGOProtos.Networking.Requests.Messages.CheckChallenge.CheckChallengeMessage;
import POGOProtos.Networking.Requests.Messages.VerifyChallenge.VerifyChallengeMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckChallengeResponseOuterClass.CheckChallengeResponse;
import POGOProtos.Networking.Responses.VerifyChallengeResponseOuterClass.VerifyChallengeResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.device.ActivityStatus;
import com.pokegoapi.api.device.DeviceInfo;
import com.pokegoapi.api.device.LocationFixes;
import com.pokegoapi.api.device.SensorInfo;
import com.pokegoapi.api.inventory.Inventories;
import com.pokegoapi.api.listener.Listener;
import com.pokegoapi.api.listener.LoginListener;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.api.map.Point;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.api.settings.Settings;
import com.pokegoapi.auth.CredentialProvider;
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.main.CommonRequests;
import com.pokegoapi.main.RequestHandler;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.AsyncHelper;
import com.pokegoapi.util.ClientInterceptor;
import com.pokegoapi.util.SystemTimeImpl;
import com.pokegoapi.util.Time;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class PokemonGo {

	private static final java.lang.String TAG = PokemonGo.class.getSimpleName();
	private final Time time;
	@Getter
	private long startTime;
	@Getter
	private final byte[] sessionHash;
	@Getter
	RequestHandler requestHandler;
	@Getter
	private PlayerProfile playerProfile;
	@Getter
	private Inventories inventories;
	@Getter
	private double latitude;
	@Getter
	private double longitude;
	@Getter
	@Setter
	private double altitude;
	@Getter
	@Setter
	private double accuracy = 1;
	private CredentialProvider credentialProvider;
	@Getter
	private Settings settings;
	private Map map;
	@Setter
	private DeviceInfo deviceInfo;
	@Getter
	@Setter
	public SensorInfo sensorInfo;
	@Getter
	@Setter
	public ActivityStatus activityStatus;
	@Setter
	@Getter
	private long seed;
	@Getter
	@Setter
	public LocationFixes locationFixes;

	@Setter
	private boolean hasChallenge;
	@Getter
	private String challengeURL;

	@Getter
	private List<Listener> listeners = Collections.synchronizedList(new ArrayList<Listener>());

	private final Object lock = new Object();

	@Getter
	private boolean loggingIn;

	/**
	 * Instantiates a new Pokemon go.
	 *
	 * @param client the http client
	 * @param time a time implementation
	 * @param seed the seed to generate same device
	 */
	public PokemonGo(OkHttpClient client, Time time, long seed) {
		this.time = time;
		this.seed = seed;
		sessionHash = new byte[32];
		new Random().nextBytes(sessionHash);
		client = client.newBuilder()
				.addNetworkInterceptor(new ClientInterceptor())
				.build();
		requestHandler = new RequestHandler(this, client);
		map = new Map(this);
		longitude = Double.NaN;
		latitude = Double.NaN;
		altitude = Double.NaN;
	}

	/**
	 * Instantiates a new Pokemon go.
	 * Deprecated: specify a time implementation
	 *
	 * @param client the http client
	 * @param seed the seed to generate same device
	 */
	public PokemonGo(OkHttpClient client, long seed) {
		this(client, new SystemTimeImpl(), seed);
	}

	/**
	 * Instantiates a new Pokemon go.
	 * Deprecated: specify a time implementation
	 *
	 * @param client the http client
	 * @param time a time implementation
	 */
	public PokemonGo(OkHttpClient client, Time time) {
		this(client, time, hash(UUID.randomUUID().toString()));
	}

	/**
	 * Instantiates a new Pokemon go.
	 * Deprecated: specify a time implementation
	 *
	 * @param client the http client
	 */
	public PokemonGo(OkHttpClient client) {
		this(client, new SystemTimeImpl(), hash(UUID.randomUUID().toString()));
	}

	/**
	 * Login user with the provided provider
	 *
	 * @param credentialProvider the credential provider
	 * @throws LoginFailedException When login fails
	 * @throws RemoteServerException When server fails
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public void login(CredentialProvider credentialProvider)
			throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		this.loggingIn = true;
		if (credentialProvider == null) {
			throw new NullPointerException("Credential Provider is null");
		}
		this.credentialProvider = credentialProvider;
		startTime = currentTimeMillis();
		inventories = new Inventories(this);
		settings = new Settings(this);
		playerProfile = new PlayerProfile(this);
		playerProfile.updateProfile();

		initialize();

		this.loggingIn = false;
	}

	private void initialize() throws RemoteServerException, CaptchaActiveException, LoginFailedException {
		fireRequestBlock(new ServerRequest(RequestType.DOWNLOAD_REMOTE_CONFIG_VERSION,
				CommonRequests.getDownloadRemoteConfigVersionMessageRequest()));

		fireRequestBlockTwo();

		List<LoginListener> loginListeners = getListeners(LoginListener.class);

		for (LoginListener listener : loginListeners) {
			listener.onLogin(this);
		}

		// From now one we will start to check our accounts is ready to fire requests.
		// Actually, we can receive valid responses even with this first check,
		// that mark the tutorial state into LEGAL_SCREEN.
		// Following, we are going to check if the account binded to this session
		// have an avatar, a nickname, and all the other things that are usually filled
		// on the official client BEFORE sending any requests such as the getMapObject etc.
		ArrayList<TutorialState> tutorialStates = playerProfile.getTutorialState().getTutorialStates();
		if (tutorialStates.isEmpty()) {
			playerProfile.activateAccount();
		}

		if (!tutorialStates.contains(TutorialState.AVATAR_SELECTION)) {
			playerProfile.setupAvatar();
		}

		if (!tutorialStates.contains(TutorialState.POKEMON_CAPTURE)) {
			playerProfile.encounterTutorialComplete();
		}

		if (!tutorialStates.contains(TutorialState.NAME_SELECTION)) {
			playerProfile.claimCodeName();
		}

		if (!tutorialStates.contains(TutorialState.FIRST_TIME_EXPERIENCE_COMPLETE)) {
			playerProfile.firstTimeExperienceComplete();
		}
	}

	/**
	 * Fire requests block.
	 *
	 * @param request server request
	 * @throws LoginFailedException When login fails
	 * @throws RemoteServerException When server fails
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	private void fireRequestBlock(ServerRequest request)
			throws RemoteServerException, CaptchaActiveException, LoginFailedException {
		getRequestHandler().sendServerRequests(request.withCommons());
	}

	/**
	 * Second requests block. Public since it could be re-fired at any time
	 *
	 * @throws LoginFailedException When login fails
	 * @throws RemoteServerException When server fails
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public void fireRequestBlockTwo() throws RemoteServerException, CaptchaActiveException, LoginFailedException {
		fireRequestBlock(new ServerRequest(RequestTypeOuterClass.RequestType.GET_ASSET_DIGEST,
				CommonRequests.getGetAssetDigestMessageRequest()));
	}

	/**
	 * Hash the given string
	 *
	 * @param string string to hash
	 * @return the hashed long
	 */
	private static long hash(String string) {
		long upper = ((long) string.hashCode()) << 32;
		int len = string.length();
		StringBuilder dest = new StringBuilder(len);

		for (int index = (len - 1); index >= 0; index--) {
			dest.append(string.charAt(index));
		}
		long lower = ((long) dest.toString().hashCode()) - ((long) Integer.MIN_VALUE);
		return upper + lower;
	}

	/**
	 * Fetches valid AuthInfo
	 *
	 * @return AuthInfo object
	 * @throws LoginFailedException when login fails
	 * @throws RemoteServerException When server fails
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public AuthInfo getAuthInfo()
			throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		return credentialProvider.getAuthInfo();
	}

	/**
	 * Sets location.
	 *
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param altitude the altitude
	 */
	public void setLocation(double latitude, double longitude, double altitude) {
		setLocation(latitude, longitude, altitude, accuracy);
	}

	/**
	 * Sets location with accuracy.
	 *
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param altitude the altitude
	 * @param accuracy the accuracy of this location
	 */
	public void setLocation(double latitude, double longitude, double altitude, double accuracy) {
		if (latitude != this.latitude || longitude != this.longitude) {
			getMap().clearCache();
		}
		setLatitude(latitude);
		setLongitude(longitude);
		setAltitude(altitude);
		setAccuracy(accuracy);
	}

	public long currentTimeMillis() {
		return time.currentTimeMillis();
	}

	/**
	 * Validates and sets a given latitude value
	 *
	 * @param value the latitude
	 * @throws IllegalArgumentException if value exceeds +-90
	 */
	public void setLatitude(double value) {
		if (value > 90 || value < -90) {
			throw new IllegalArgumentException("latittude can not exceed +/- 90");
		}
		latitude = value;
	}

	/**
	 * Validates and sets a given longitude value
	 *
	 * @param value the longitude
	 * @throws IllegalArgumentException if value exceeds +-180
	 */
	public void setLongitude(double value) {
		if (value > 180 || value < -180) {
			throw new IllegalArgumentException("longitude can not exceed +/- 180");
		}
		longitude = value;
	}

	/**
	 * Gets the map API
	 *
	 * @return the map
	 * @throws IllegalStateException if location has not been set
	 */
	public Map getMap() {
		if (this.latitude == Double.NaN || this.longitude == Double.NaN) {
			throw new IllegalStateException("Attempt to get map without setting location first");
		}
		return map;
	}

	/**
	 * Gets the device info
	 *
	 * @return the device info
	 */
	public SignatureOuterClass.Signature.DeviceInfo getDeviceInfo() {
		if (deviceInfo == null) {
			deviceInfo = DeviceInfo.getDefault(this);
		}
		return deviceInfo.getDeviceInfo();
	}

	/**
	 * Gets the sensor info
	 *
	 * @param currentTime the current time
	 * @param random the random object
	 * @return the sensor info
	 */
	public SignatureOuterClass.Signature.SensorInfo getSensorSignature(long currentTime, Random random) {
		if (this.sensorInfo == null || sensorInfo.getTimestampCreate() != 0L) {
			return SensorInfo.getDefault(this, currentTime, random);
		}
		return sensorInfo.getSensorInfo();
	}

	/**
	 * Gets the activity status
	 *
	 * @param random the random object
	 * @return the activity status
	 */
	public SignatureOuterClass.Signature.ActivityStatus getActivitySignature(Random random) {
		if (this.activityStatus == null) {
			return ActivityStatus.getDefault(this, random);
		}
		return activityStatus.getActivityStatus();
	}

	/**
	 * Updates the current challenge
	 *
	 * @param url the challenge url, if any
	 * @param hasChallenge whether the challenge solve is required
	 */
	public void updateChallenge(String url, boolean hasChallenge) {
		this.hasChallenge = hasChallenge;
		this.challengeURL = url;
		if (hasChallenge) {
			List<LoginListener> listeners = getListeners(LoginListener.class);
			for (LoginListener listener : listeners) {
				listener.onChallenge(this, url);
			}
		}
	}

	/**
	 * Registers the given listener to this api.
	 *
	 * @param listener the listener to register
	 */
	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes the given listener from this api.
	 *
	 * @param listener the listener to remove
	 */
	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}

	/**
	 * Returns all listeners for the given type.
	 *
	 * @param listenerType the type of listeners to return
	 * @return all listeners for the given type
	 */
	public <T extends Listener> List<T> getListeners(Class<T> listenerType) {
		List<T> listeners = new ArrayList<T>();
		synchronized (this.lock) {
			for (Listener listener : this.listeners) {
				if (listenerType.isAssignableFrom(listener.getClass())) {
					listeners.add((T) listener);
				}
			}
		}
		return listeners;
	}

	/**
	 * Invokes a method in all listeners of the given type
	 *
	 * @param listenerType the listener to call to
	 * @param name the method name to call
	 * @param parameters the parameters to pass to the method
	 * @param <T> the listener type
	 * @throws ReflectiveOperationException if an exception occurred while invoking the listener
	 */
	public <T extends Listener> void callListener(Class<T> listenerType, String name, Object... parameters)
			throws ReflectiveOperationException {
		Class[] parameterTypes = new Class[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			Object parameter = parameters[i];
			parameterTypes[i] = parameter.getClass();
		}
		Method method = listenerType.getMethod(name, parameterTypes);
		if (method != null) {
			List<T> listeners = getListeners(listenerType);
			for (T listener : listeners) {
				method.invoke(listener, parameters);
			}
		} else {
			throw new NoSuchMethodException("Method \"" + name + "\" does not exist");
		}
	}

	/**
	 * @return if there is an active challenge required. Challenge accessible via getChallengeURL.
	 */
	public boolean hasChallenge() {
		return this.hasChallenge;
	}

	/**
	 * Verifies the current challenge with the given token.
	 *
	 * @param token the challenge response token
	 * @return if the token was valid or not
	 * @throws LoginFailedException when login fails
	 * @throws RemoteServerException when server fails
	 * @throws InvalidProtocolBufferException when the client receives an invalid message from the server
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public boolean verifyChallenge(String token)
			throws RemoteServerException, CaptchaActiveException, LoginFailedException, InvalidProtocolBufferException {
		hasChallenge = false;
		VerifyChallengeMessage message = VerifyChallengeMessage.newBuilder().setToken(token).build();
		AsyncServerRequest request = new AsyncServerRequest(RequestType.VERIFY_CHALLENGE, message);
		ByteString responseData = AsyncHelper.toBlocking(getRequestHandler().sendAsyncServerRequests(request));
		VerifyChallengeResponse response = VerifyChallengeResponse.parseFrom(responseData);
		hasChallenge = !response.getSuccess();
		if (!hasChallenge) {
			challengeURL = null;
		}
		return response.getSuccess();
	}

	/**
	 * Checks for a challenge / captcha
	 *
	 * @return the new challenge URL, if any
	 * @throws LoginFailedException when login fails
	 * @throws RemoteServerException when server fails
	 * @throws InvalidProtocolBufferException when the client receives an invalid message from the server
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public String checkChallenge()
			throws RemoteServerException, CaptchaActiveException, LoginFailedException, InvalidProtocolBufferException {
		CheckChallengeMessage message = CheckChallengeMessage.newBuilder().build();
		AsyncServerRequest request = new AsyncServerRequest(RequestType.CHECK_CHALLENGE, message);
		ByteString responseData =
				AsyncHelper.toBlocking(getRequestHandler().sendAsyncServerRequests(request));
		CheckChallengeResponse response = CheckChallengeResponse.parseFrom(responseData);
		String newChallenge = response.getChallengeUrl();
		if (response.getShowChallenge() && newChallenge != null && newChallenge.length() > 0) {
			updateChallenge(newChallenge, true);
			return newChallenge;
		}
		return null;
	}

	/**
	 * @return the current player position in Point form
	 */
	public Point getPoint() {
		return new Point(this.getLatitude(), this.getLongitude());
	}
}
