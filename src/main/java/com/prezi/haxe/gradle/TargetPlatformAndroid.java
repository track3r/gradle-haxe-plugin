package com.prezi.haxe.gradle;

import org.gradle.api.Project;

public class TargetPlatformAndroid extends  TargetPlatformCpp
{
    private String androidTest;

    public TargetPlatformAndroid(Project project)
    {
        super("android", project);
    }

    String getAndroidTest(){return androidTest;}
    void setAndroidTestTest(String t) {androidTest = t;}
}
