package com.prezi.haxe.gradle;

import org.gradle.api.Project;

/**
 * Created by root1 on 06/11/16.
 */
public class TargetPlatformIos extends TargetPlatformCpp
{
    public TargetPlatformIos(Project project)
    {
        super("ios", project);
    }
}
