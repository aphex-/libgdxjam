package com.nukethemoon.libgdxjam.game.attributes;


public class Inertia extends Attribute {
	public Inertia(float val) {
		setCurrentValue(val);
	}

	@Override
	public String name() {
		return "INERTIA";
	}
}
