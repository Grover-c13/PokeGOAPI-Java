
# PokeGOAPI-Java
Pokemon GO Java API

The original https://github.com/Grover-c13/PokeGOAPI-Java is more aimed at Android where this is aimed at writing a bot. 
This means different design choices are made. I will try to keep it up to date with the original repo as much as possible. 

[![Build Status](https://travis-ci.org/Grover-c13/PokeGOAPI-Java.svg?branch=master)](https://travis-ci.org/Grover-c13/PokeGOAPI-Java)

See this guide for adding functionality:
   https://docs.google.com/document/d/1BE8O6Z19sQ54T5T7QauXgA11GbL6D9vx9AAMCM5KlRA

See this spreadsheet for RPC endpoints and progress :
   https://docs.google.com/spreadsheets/d/1Xv0Gw5PzIRaVou2xrl6r7qySrcmOKjQWLBjJA73YnJM

___
:exclamation:

This API may seem unstable. This is because the backend Pokemon GO servers are unstable.

In case stuff is not working as expected, wait a moment to see if the problem resolves itself automatically.

You may also check the status of the servers on [IsPokemonGoDownOrNot.com](http://ispokemongodownornot.com) or [MMOServerStatus.com](http://www.mmoserverstatus.com/pokemon_go).

If you just want to use it, wait some days until the server issues are resolved (or if there is a problem with this library, you may fix it and send a PR this way).

:exclamation:
___

# Build
  - Clone the repo and cd into the folder
  - `` git submodule update --init ``
  - compile and package
  - `` ./gradlew build bundle ``
  - you should have the api bundled in ``build/libs/PokeGOAPI-Java_bundle-0.0.1-SNAPSHOT.jar``

  PS : To Eclipse user, you must build once : `` ./gradlew build `` and add the generated java class for proto into eclipse source path : Right click on the project > Build path > Configure Build Path > Source > Add Folder > Select `build/generated/source/proto/main/java` > Finish

# Usage
Include the API as jar from your own build, or use Maven/Gradle/SBT/Leiningen: https://jitpack.io/#Grover-c13/PokeGOAPI-Java/master-SNAPSHOT

Mostly everything is accessed through the PokemonGo class in the API package.

The constructor of PokemonGo class requires a AuthInfo object which can be obtained from GoogleLogin().login or PtcLogin().login, and a OkHttpClient object.

EG:
```java
OkHttpClient httpClient = new OkHttpClient();
AuthInfo auth = new GoogleLogin(httpClient).login("token");           
PokemonGo go = new PokemonGo(auth,httpClient);
Log.v(go.getPlayerProfile());
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
  - Grover-c13
  - jabbink
  - zeladada
  - darakath
  - vmarchaud

You can join us in the slack channel #javaapi on the pkre.slack.com (you should get an invite by a bot posted somewhere in the subreddit /r/pokemongodev)
