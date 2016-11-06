package com.prezi.haxe.gradle;

import groovy.lang.Closure;
import org.gradle.api.Project;

public class TargetPlatformCpp extends TargetPlatform
{
    private TargetPlatformIos targetPlatformIos;
    private TargetPlatformAndroid targetPlatformAndroid;

    protected TargetPlatformCpp(String name, Project project)
    {
        super(name, project);
    }

    public TargetPlatformCpp(Project project)
    {
        super("cpp", project);
    }

    public TargetPlatformIos getIos()
    {
        return targetPlatformIos;
    }

    public TargetPlatformAndroid getAndroid()
    {
        return targetPlatformAndroid;
    }

    public TargetPlatformIos ios(Closure closure)
    {
        final HaxeExtension extension = (HaxeExtension)project.getExtensions().findByName("haxe");
        targetPlatformIos = HaxeExtension.targetPlatform(extension, targetPlatformIos, TargetPlatformIos.class, closure);
        targetPlatformIos.setParent(this);
        return targetPlatformIos;
    }

    public TargetPlatformAndroid android(Closure closure)
    {
        final HaxeExtension extension = (HaxeExtension)project.getExtensions().findByName("haxe");
        targetPlatformAndroid = HaxeExtension.targetPlatform(extension, targetPlatformAndroid, TargetPlatformAndroid.class, closure);
        targetPlatformAndroid.setParent(this);
        return targetPlatformAndroid;
    }
}
