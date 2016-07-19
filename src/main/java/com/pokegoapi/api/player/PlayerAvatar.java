package com.pokegoapi.api.player;

import POGOProtos.Enums.GenderOuterClass;

public class PlayerAvatar {
	private GenderOuterClass.Gender gender;
	private int skin;
	private int hair;
	private int shirt;
	private int pants;
	private int hat;
	private int shoes;
	private int eyes;
	private int backpack;

	public GenderOuterClass.Gender getGender() {
		return gender;
	}

	public void setGender(GenderOuterClass.Gender gender) {
		this.gender = gender;
	}

	public int getSkin() {
		return skin;
	}

	public void setSkin(int skin) {
		this.skin = skin;
	}

	public int getHair() {
		return hair;
	}

	public void setHair(int hair) {
		this.hair = hair;
	}

	public int getShirt() {
		return shirt;
	}

	public void setShirt(int shirt) {
		this.shirt = shirt;
	}

	public int getPants() {
		return pants;
	}

	public void setPants(int pants) {
		this.pants = pants;
	}

	public int getHat() {
		return hat;
	}

	public void setHat(int hat) {
		this.hat = hat;
	}

	public int getShoes() {
		return shoes;
	}

	public void setShoes(int shoes) {
		this.shoes = shoes;
	}

	public int getEyes() {
		return eyes;
	}

	public void setEyes(int eyes) {
		this.eyes = eyes;
	}

	public int getBackpack() {
		return backpack;
	}

	public void setBackpack(int backpack) {
		this.backpack = backpack;
	}
}
