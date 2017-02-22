package com.pokegoapi.go.gym.spec;

import com.pokegoapi.go.gym.Gym;
import com.pokegoapi.network.LoginFailedException;
import com.pokegoapi.network.RemoteServerException;

/**
 * Created by chris on 1/23/2017.
 */
public interface Battle {

//    void start(Pokemon[] attackingTeam, BattleListenerSpec spec) throws CaptchaActiveException, LoginFailedException,
//            RemoteServerException;

    /**
     * Performs an attack action
     *
     * @return the duration of this attack
     */
    int attack();

    /**
     * Performs a special attack action
     *
     * @return the duration of this attack
     */
    int attackSpecial();

    /**
     * Performs a dodge action
     *
     * @return the duration of this action
     */
    int dodge();

    /**
     * Swaps your current attacking Pokemon
     *
     * @param pokemon the pokemon to swap to
     * @return the duration of this action
     */
//    int swap(Pokemon pokemon);

    /**
     * @return the time left for this battle before it times out
     */
    long getTimeLeft();

    /**
     * @return the gym that this battle is taking place at
     */
    Gym getGym();
}
