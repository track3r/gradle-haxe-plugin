package com.prezi.haxe.gradle;

import org.gradle.api.Named;

import java.io.Serializable;

public class Flavor extends DefaultHaxeCompilerParameters implements Named, Serializable {
	private final String name;

	public Flavor(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "flavor: " + name;
	}
}
