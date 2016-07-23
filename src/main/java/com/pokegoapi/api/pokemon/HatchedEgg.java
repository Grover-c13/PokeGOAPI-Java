package com.pokegoapi.api.pokemon;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class HatchedEgg {
	
	private Long id;
	private int	experience;
	private int	candy;
	private int stardust;
}
