package com.prezi.haxe.gradle;

import com.prezi.haxe.gradle.incubating.AbstractLanguageSourceSet;
import com.prezi.haxe.gradle.incubating.FunctionalSourceSet;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.internal.file.DefaultSourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.file.collections.DefaultDirectoryFileTreeFactory;

public class HaxeSourceSet extends AbstractLanguageSourceSet  {

	private final Configuration compileClassPath;

	public HaxeSourceSet(String name, FunctionalSourceSet parent, Configuration compileClassPath, FileResolver fileResolver) {
		super(name, parent, "Haxe source", new DefaultSourceDirectorySet("source", fileResolver, new DefaultDirectoryFileTreeFactory()));
		this.compileClassPath = compileClassPath;
	}

	public Configuration getCompileClassPath() {
		return compileClassPath;
	}
}
