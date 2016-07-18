package com.pokegoapi.api;

import POGOProtos.Enums.GenderOuterClass;
import lombok.Data;

@Data
public class PlayerAvatar 
{
	private GenderOuterClass.Gender gender;
	private int skin;
	private int hair;
	private int shirt;
	private int pants;
	private int hat;
	private int shoes;
	private int eyes;
	private int backpack;
	
}
