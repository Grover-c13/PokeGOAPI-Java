
# PokeGOAPI-Java
Pokemon GO Java API

[![Build Status](https://travis-ci.org/Grover-c13/PokeGOAPI-Java.svg?branch=master)](https://travis-ci.org/Grover-c13/PokeGOAPI-Java)
[![](https://jitpack.io/v/Grover-c13/PokeGOAPI-Java.svg)](https://jitpack.io/#Grover-c13/PokeGOAPI-Java)

Javadocs : [CLICK ME](https://jitpack.io/com/github/Grover-c13/PokeGOAPI-Java/a2828da60d/javadoc/) 

See this guide for adding functionality:
   https://docs.google.com/document/d/1BE8O6Z19sQ54T5T7QauXgA11GbL6D9vx9AAMCM5KlRA

See this spreadsheet for RPC endpoints and progress :
   https://docs.google.com/spreadsheets/d/1Xv0Gw5PzIRaVou2xrl6r7qySrcmOKjQWLBjJA73YnJM

___
:exclamation: :exclamation: :exclamation:

This API may have issues when the PokemonGO servers are under high load or down, in this case please wait for the official to get back up. You can check the official servers status on [IsPokemonGoDownOrNot.com](http://ispokemongodownornot.com) or [MMOServerStatus.com](http://www.mmoserverstatus.com/pokemon_go).

This API doesnt fake the official client perfectly, niantic may know that you arent using the official app, we encourage you to use a alternate account to play with this API.

If you are using this lib to catch pokemon and loot pokestop, take care that you arent teleporting, the servers may issue a softban against your client (its temporary, between 10 and 30 minutes in general).

:exclamation: :exclamation: :exclamation:
___

# How to import

  ```groovy
  allprojects {
    repositories {
        jcenter()
    }
  }

  dependencies {
    compile 'com.pokegoapi:PokeGOAPI-library:0.3.0'
  }
  ```

OR

  Import JAR in Eclipse
    - Complete `Build from source` below
    - Right click on the project
    - Select Build path > Java Build Path
    - Select Libraries tab
    - Select Add External JARs…
    - Select `PokeGOAPI-Java/library/build/libs/PokeGOAPI-library-all-0.3.0.jar`
    - Finish

# Build from source
  - Clone the repo and cd into the folder
  - `` git submodule update --init ``
  - `` ./gradlew :library:build ``
  - you should have the api jar in ``library/build/libs/PokeGOAPI-library-all-0.3.0.jar``

PS : for users who want to import the api into Eclipse IDE, you'll need to :
  - build once : `` ./gradlew :library:build ``
  - Right click on the project
  - Select Build path > Configure Build Path > Source > Add Folder
  - Select `library/build/generated/source/proto/main/java`
  - Finish

# Usage exemple (mostly how to login) :
```java
OkHttpClient httpClient = new OkHttpClient();

/** 
* Google: 
* You will need to redirect your user to GoogleUserCredentialProvider.LOGIN_URL
* Afer this, the user must signin on google and get the token that will be show to him.
* This token will need to be put as argument to login.
*/
GoogleUserCredentialProvider provider = new GoogleUserCredentialProvider(http);

// in this url, you will get a code for the google account that is logged
System.out.println("Please go to " + GoogleUserCredentialProvider.LOGIN_URL);
System.out.println("Enter authorisation code:");
			
// Ask the user to enter it in the standart input
Scanner sc = new Scanner(System.in);
String access = sc.nextLine();
			
// we should be able to login with this token
provider.login(access);
PokemonGo go = new PokemonGo(provider, httpClient);

/**
* After this, if you do not want to re-authorize the google account every time, 
* you will need to store the refresh_token that you can get the first time with provider.getRefreshToken()
* ! The API does not store the refresh token for you !
* log in using the refresh token like this :
*/
PokemonGo go = new PokemonGo(new GoogleUserCredentialProvider(httpClient, refreshToken), httpClient);

/**
* PTC is much simpler, but less secure.
* You will need the username and password for each user log in
* This account does not currently support a refresh_token. 
* Example log in :
*/
PokemonGo go = new PokemonGo(new PtcCredentialProvider(httpClient,username,password),httpClient);

// After this you can access the api from the PokemonGo instance :
go.getPlayerProfile(); // to get the user profile
go.getInventories(); // to get all his inventories (Pokemon, backpack, egg, incubator)
go.setLocation(lat, long, alt); // set your position to get stuff around (altitude is not needed, you can use 1 for example)
go.getMap().getCatchablePokemon(); // get all currently Catchable Pokemon around you

// If you want to go deeper, you can directly send your request with our RequestHandler
// For example, here we are sending a request to get the award for our level
// This applies to any method defined in the protos file as Request/Response)

LevelUpRewardsMessage msg = LevelUpRewardsMessage.newBuilder().setLevel(yourLVL).build(); 
ServerRequest serverRequest = new ServerRequest(RequestType.LEVEL_UP_REWARDS, msg);
go.getRequestHandler().sendServerRequests(serverRequest);

// and get the response like this :

LevelUpRewardsResponse response = null;
try {
	response = LevelUpRewardsResponse.parseFrom(serverRequest.getData());
} catch (InvalidProtocolBufferException e) {
	// its possible that the parsing fail when servers are in high load for example.
	throw new RemoteServerException(e);
}
```

##Android Dev FAQ

  - I can't use the sample code! It just throws a login exception!

You're running the sample code on the UI thread. Strict mode policy will throw an exception in that case and its being caught by the network client and treating it as a login failed exception. Run the sample code on a background thread in AsyncTask or RXJava and it will work.

  - I want to submit a refactor so that this library will use Google Volley

This library is meant to be a Java implementation of the API. Google Volley is specific to Android and should not be introduced in this library. However, if you still want to refactor it, you should create it as a separate project.

   - How can I use Android's native Google sign in with this library?

You can't. The Google Identity Platform uses the SHA1 fingerprint and package name to authenticate the caller of all sign in requests. This means that Niantic would need to add your app's SHA1 fingerprint and package name to their Google API Console. If you ever requested a Google Maps API key, you went through the same process. An alternative would be using a WebView to access the web based OAuth flow. This will work with the client ID and secret provided by this library.


## Contributing
  - Fork it!
  - Create your feature branch: `git checkout -b my-new-feature`
  - Commit your changes: `git commit -am 'Useful information about your new features'`
  - Push to the branch: `git push origin my-new-feature`
  - Submit a pull request on the `Development` branch :D

## Contributors
  - @Grover-c13
  - @jabbink
  - @Aphoh
  - @mjmfighter
  - @vmarchaud
  - @langerhans

You can join us in the slack channel #javaapi on the pkre.slack.com ([you can get invited here](https://shielded-earth-81203.herokuapp.com/))
