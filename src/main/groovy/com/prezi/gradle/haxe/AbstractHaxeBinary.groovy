package com.prezi.gradle.haxe

import org.gradle.api.DomainObjectCollection
import org.gradle.api.internal.DefaultDomainObjectSet
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.base.internal.AbstractBuildableModelElement
import org.gradle.language.base.internal.BinaryInternal
import org.gradle.language.base.internal.BinaryNamingScheme

/**
 * Created by lptr on 08/02/14.
 */
abstract class AbstractHaxeBinary extends AbstractBuildableModelElement implements HaxeBinary, BinaryInternal {
	private final source = new DefaultDomainObjectSet<>(LanguageSourceSet.class)
	private final String name
	private final BinaryNamingScheme namingScheme
	private final TargetPlatform targetPlatform

	protected AbstractHaxeBinary(String parentName, TargetPlatform targetPlatform) {
		this.namingScheme = new HaxeBinaryNamingScheme(parentName)
		this.name = namingScheme.getLifecycleTaskName()
		this.targetPlatform = targetPlatform
	}

	@Override
	String getName() {
		return name
	}

	@Override
	DomainObjectCollection<LanguageSourceSet> getSource() {
		return source
	}

	@Override
	TargetPlatform getTargetPlatform() {
		return targetPlatform
	}

	@Override
	BinaryNamingScheme getNamingScheme() {
		return namingScheme
	}

	@Override
	public String toString() {
		return namingScheme.getDescription();
	}
}
