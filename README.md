
# PokeGOAPI-Java
Pokemon GO Java API

[![Build Status](https://travis-ci.org/Grover-c13/PokeGOAPI-Java.svg?branch=master)](https://travis-ci.org/Grover-c13/PokeGOAPI-Java)

See this guide for adding functionality:
   https://docs.google.com/document/d/1BE8O6Z19sQ54T5T7QauXgA11GbL6D9vx9AAMCM5KlRA

See this spreadsheet for RPC endpoints and progress :
   https://docs.google.com/spreadsheets/d/1Xv0Gw5PzIRaVou2xrl6r7qySrcmOKjQWLBjJA73YnJM

___
:exclamation: This api is unstable as the backend pogo server are, so if you just want to use it, wait some days that we get stuff working great before complaining about the api isnt working ;) :exclamation:
___

# Build
  - Clone the repo and cd into the folder
  - `` git submodule init ``
  - `` git submodule update ``
  - verify that you have the jdk 8 in your path
  - `` mvn install ``
  - you should have the api bundled in ``target/pokemongo_api-jar-with-dependencies.jar``

# Usage
Mostly everything is accessed through the PokemonGo class in the api package.
The constructor of PokemonGo class requires a AuthInfo object which can be obtained from GoogleLogin().login or PTCLogin().login.

EG:
```java
AuthInfo auth = new GoogleLogin().login("token");           
PokemonGo go = new PokemonGo(auth);
System.out.println(go.getPlayerProfile());
```

## Contributing
  - Fork it!
  - Create your feature branch: `git checkout -b my-new-feature`
  - Commit your changes: `git commit -am 'Usefull information about your new features'`
  - Push to the branch: `git push origin my-new-feature`
  - Submit a pull request :D

## Contributors
  - Grover-c13
  - jabbink
  - zeladada
  - darakath
  - vmarchaud

You can join us in the slack channel #javaapi on the pkre.slack.com (you should get an invite by a bot posted somewhere in the subreddit /r/pokemongodev)
