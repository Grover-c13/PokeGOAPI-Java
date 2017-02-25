package com.pokegoapi.go.gym.spec;

import com.github.aeonlucid.pogoprotos.data.Battle.BattleParticipant;

public interface BattleListener {

    /**
     * Called when this battle end, and you won
     *
     * @param battle the current battle
     * @param deltaPoints the amount of points (prestige) added or removed after completing this battle
     * @param newPoints the new amount of points on this gym
     */
    void onVictory(Battle battle, int deltaPoints, long newPoints);

    /**
     * Called when this battle ends, and you were defeated
     *
     * @param battle the current battle
     */
    void onDefeated(Battle battle);

    /**
     * Called when this battle times out
     *
     * @param battle the current battle
     */
    void onTimedOut(Battle battle);

    /**
     * Called when a player joins this battle
     *
     * @param battle the current battle
     * @param joined the player that joined
     */
    void onPlayerJoin(Battle battle, BattleParticipant joined);

    /**
     * Called when a player leaves this battle
     *
     * @param battle the current battle
     * @param left player that left
     */
    void onPlayerLeave(Battle battle, BattleParticipant left);

    /**
     * Called when a Pokemon is attacked in this battle
     *
     * @param battle the current battle
     * @param attacked the attacked pokemon
     * @param attacker the pokemon attacking the attacked pokemon
     * @param duration the duration of the attack
     * @param damageWindowStart the start of the damage window
     * @param damageWindowEnd the end of the damage window
     */
    void onAttacked(Battle battle, BattlePokemon attacked, BattlePokemon attacker,
                    int duration, long damageWindowStart, long damageWindowEnd);

    /**
     * Called when a Pokemon is attacked with the special move in this battle
     *
     * @param battle the current battle
     * @param attacked the attacked pokemon
     * @param attacker the pokemon attacking the attacked pokemon
     * @param duration the duration of the attack
     * @param damageWindowStart the start of the damage window
     * @param damageWindowEnd the end of the damage window
     * @param energyDelta the change in energy for the attacking pokemon
    */
    void onAttackedSpecial(Battle battle, BattlePokemon attacked, BattlePokemon attacker,
                           int duration, long damageWindowStart, long damageWindowEnd, int energyDelta);

    /**
     * Called when the attacker's health is updated
     *
     * @param battle the current battle
     * @param lastHealth the attacker's last health
     * @param health the attacker's new health
     * @param maxHealth the maximum health for the attacker
     * @param energyDelta the change in energy for the attacker
     */
    void onAttackerHealthUpdate(Battle battle, int lastHealth, int health, int maxHealth, int energyDelta);

    /**
     * Called when the defender's health is updated
     *
     * @param battle the current battle
     * @param lastHealth the defender's last health
     * @param health the defender's new health
     * @param maxHealth the maximum health for the defender
     * @param energyDelta the change in energy for the defender
     */
    void onDefenderHealthUpdate(Battle battle, int lastHealth, int health, int maxHealth, int energyDelta);

    /**
     * Called when the attacker Pokemon changes
     *
     * @param battle the current battle
     * @param newAttacker the new attacker pokemon
     */
    void onAttackerSwap(Battle battle, BattlePokemon newAttacker);

    /**
     * Called when the defender Pokemon changes
     *
     * @param battle the current battle
     * @param newDefender the new defender pokemon
     */
    void onDefenderSwap(Battle battle, BattlePokemon newDefender);

    /**
     * Called when the given Pokemon faints.
     *
     * @param battle the current battle
     * @param pokemon the fainted pokemon
     * @param duration the duration of this action
     */
    void onFaint(Battle battle, BattlePokemon pokemon, int duration);

    /**
     * Called when the given Pokemon dodges.
     *
     * @param battle the current battle
     * @param pokemon the dodging pokemon
     * @param duration the duration of this action
     */
    void onDodge(Battle battle, BattlePokemon pokemon, int duration);

    /**
     * Called when an exception occurs during this battle
     *
     * @param battle the current battle
     * @param throwable the exception that occurred
     */
    void onException(Battle battle, Throwable throwable);

    /**
     * Called when invalid actions are sent to the server
     *
     * @param battle the current battle
     */
    void onInvalidActions(Battle battle);
}