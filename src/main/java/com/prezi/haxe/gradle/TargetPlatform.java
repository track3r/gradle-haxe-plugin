package com.prezi.haxe.gradle;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import groovy.lang.Closure;
import org.gradle.api.*;
import org.gradle.util.ConfigureUtil;

import java.io.Serializable;

public class TargetPlatform extends DefaultHaxeCompilerParameters implements Named, Serializable {
	private final String name;
	private final NamedDomainObjectContainer<Flavor> flavors;

	public TargetPlatform(String name, Project project) {
		this.name = name;
		this.flavors = project.container(Flavor.class, new FlavorNamedDomainObjectFactory());
	}

	@Override
	public String getName() {
		return name;
	}

	public NamedDomainObjectContainer<Flavor> getFlavors() {
		return flavors;
	}

	public void flavors(Action<? super NamedDomainObjectContainer<Flavor>> action) {
		action.execute(getFlavors());
	}

	public void flavors(Closure closure) {
		ConfigureUtil.configure(closure, getFlavors());
	}

	@Override
	public String toString() {
		return "platform: " + name + " " + Collections2.transform(flavors, new Function<Flavor, String>() {
			@Override
			public String apply(Flavor flavor) {
				return flavor.getName();
			}
		});
	}

	private static class FlavorNamedDomainObjectFactory implements NamedDomainObjectFactory<Flavor>, Serializable {
		@Override
		public Flavor create(String name) {
			return new Flavor(name);
		}
	}
}
