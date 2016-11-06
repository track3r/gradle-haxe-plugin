package com.prezi.haxe.gradle;

import org.gradle.api.Project;

public class TargetPlatformJs extends TargetPlatform
{
    public TargetPlatformJs(Project project)
    {
        super("js", project);
    }

}
