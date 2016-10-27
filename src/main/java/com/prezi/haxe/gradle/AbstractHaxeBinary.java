package com.prezi.haxe.gradle;

import com.prezi.haxe.gradle.incubating.AbstractBuildableModelElement;
import com.prezi.haxe.gradle.incubating.BinaryInternal;
import com.prezi.haxe.gradle.incubating.BinaryNamingScheme;
import com.prezi.haxe.gradle.incubating.LanguageSourceSet;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.internal.DefaultDomainObjectSet;

public abstract class AbstractHaxeBinary<T extends HaxeCompile> extends AbstractBuildableModelElement implements BinaryInternal {
	private final DomainObjectSet<LanguageSourceSet> source = new DefaultDomainObjectSet<LanguageSourceSet>(LanguageSourceSet.class);
	private final String name;
	private final Configuration configuration;
	private final BinaryNamingScheme namingScheme;
	private final TargetPlatform targetPlatform;
	private final Flavor flavor;
	private T compileTask;
	private Har sourceHarTask;

	protected AbstractHaxeBinary(String parentName, Configuration configuration, TargetPlatform targetPlatform, Flavor flavor) {
		this.namingScheme = new HaxeBinaryNamingScheme(parentName);
		this.name = namingScheme.getLifecycleTaskName();
		this.configuration = configuration;
		this.targetPlatform = targetPlatform;
		this.flavor = flavor;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDisplayName() {
		return namingScheme.getDescription();
	}

	@Override
	public BinaryNamingScheme getNamingScheme() {
		return namingScheme;
	}

	@Override
	public String toString() {
		return namingScheme.getDescription();
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public DomainObjectSet<LanguageSourceSet> getSource() {
		return source;
	}

	public TargetPlatform getTargetPlatform() {
		return targetPlatform;
	}

	public Flavor getFlavor() {
		return flavor;
	}

	public T getCompileTask() {
		return compileTask;
	}

	public void setCompileTask(T compileTask) {
		this.compileTask = compileTask;
	}

	public Har getSourceHarTask() {
		return sourceHarTask;
	}

	public void setSourceHarTask(Har sourceHarTask) {
		this.sourceHarTask = sourceHarTask;
	}

}
