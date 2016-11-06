package com.prezi.haxe.gradle;


import com.google.common.collect.Sets;
import com.prezi.haxe.gradle.incubating.BinaryContainer;
import com.prezi.haxe.gradle.incubating.BinaryInternal;
import com.prezi.haxe.gradle.incubating.DefaultBinaryContainer;
import com.prezi.haxe.gradle.incubating.DefaultProjectSourceSet;
import com.prezi.haxe.gradle.incubating.ProjectSourceSet;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.NamedDomainObjectFactory;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.internal.reflect.Instantiator;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;

import groovy.lang.Closure;
import org.gradle.util.ConfigureUtil;
import org.slf4j.LoggerFactory;

public class HaxeExtension extends DefaultHaxeCompilerParameters implements Serializable {
	private final NamedDomainObjectContainer<TargetPlatform> targetPlatforms;

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TargetPlatform.class);

	private final ProjectSourceSet sources;
	private final BinaryContainer binaries;
	private final Set<Object> compilerVersions = Sets.newLinkedHashSet();
	private File munitNodeModuleInstallDir;
	private TargetPlatformJs targetPlatformJs;
	private TargetPlatformCpp targetPlatformCpp;
	private Project project;

	public HaxeExtension(final Project project, Instantiator instantiator) {
		this.sources = instantiator.newInstance(DefaultProjectSourceSet.class, instantiator);
		this.binaries = instantiator.newInstance(DefaultBinaryContainer.class, instantiator);
		this.targetPlatforms = project.container(TargetPlatform.class, new TargetPlatformNamedDomainObjectFactory(project));
		this.project = project;

		binaries.withType(BinaryInternal.class).all(new Action<BinaryInternal>() {
			public void execute(BinaryInternal binary) {
				Task binaryLifecycleTask = project.task(binary.getNamingScheme().getLifecycleTaskName());
				binaryLifecycleTask.setGroup("build");
				binaryLifecycleTask.setDescription(String.format("Assembles %s.", binary));
				binary.setBuildTask(binaryLifecycleTask);
			}
		});
	}

	public File getMunitNodeModuleInstallDir() {
		return munitNodeModuleInstallDir;
	}

	public void setMunitNodeModuleInstallDir(File munitNodeModuleInstallDir) {
		this.munitNodeModuleInstallDir = munitNodeModuleInstallDir;
	}

	public ProjectSourceSet getSources() {
		return sources;
	}

	public void sources(Action<ProjectSourceSet> action) {
		action.execute(sources);
	}

	public BinaryContainer getBinaries() {
		return binaries;
	}

	public static <T extends TargetPlatform> T targetPlatform(HaxeExtension th, T platform, Class<T> cls, Closure closure)
	{
		T p = platform;
		if (p == null)
		{
			try {
				p = (T)cls.getConstructors()[0].newInstance(th.project);
			} catch (InstantiationException e) {
				logger.error("Fail", e);
			} catch (IllegalAccessException e) {
				logger.error("Fail", e);
			} catch (InvocationTargetException e) {
				logger.error("Fail", e);
			}
		}

		p = ConfigureUtil.configure(closure, p);

		if (platform == null)
		{
			th.targetPlatforms.add(p);
		}

		return p;
	}

	public TargetPlatformJs getJs()
	{
		return targetPlatformJs;
	}

	public TargetPlatform js(Closure closure)
	{
		targetPlatformJs = targetPlatform(this, targetPlatformJs, TargetPlatformJs.class, closure);
		return  targetPlatformJs;
	}

	public TargetPlatformCpp getCpp()
	{
		return  targetPlatformCpp;
	}

	public  TargetPlatformCpp cpp(Closure closure)
	{
		targetPlatformCpp = targetPlatform(this, targetPlatformCpp, TargetPlatformCpp.class, closure);
		return  targetPlatformCpp;
	}

	public void binaries(Action<BinaryContainer> action) {
		action.execute(binaries);
	}

	public NamedDomainObjectContainer<TargetPlatform> getTargetPlatforms() {
		return targetPlatforms;
	}

	public void targetPlatforms(Action<? super NamedDomainObjectContainer<TargetPlatform>> action) {
		action.execute(targetPlatforms);
	}

	public void setCompilerVersions(Object... versions) {
		compilerVersions.addAll(Arrays.asList(versions));
	}

	public void setCompilerVersion(Object... versions) {
		setCompilerVersions(versions);
	}

	public void compilerVersions(Object... versions) {
		setCompilerVersions(versions);
	}

	public void compilerVersion(Object... versions) {
		setCompilerVersions(versions);
	}

	public Set<Object> getCompilerVersions() {
		return compilerVersions;
	}

	private static class TargetPlatformNamedDomainObjectFactory implements NamedDomainObjectFactory<TargetPlatform>, Serializable {
		private final Project project;

		public TargetPlatformNamedDomainObjectFactory(Project project) {
			this.project = project;
		}

		@Override
		public TargetPlatform create(String platformName) {
			return new TargetPlatform(platformName, project);
		}
	}
}
