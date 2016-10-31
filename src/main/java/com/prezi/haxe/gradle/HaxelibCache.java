package com.prezi.haxe.gradle;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.DependencySubstitution;
import org.gradle.api.artifacts.DependencySubstitutions;
import org.gradle.api.artifacts.component.ComponentSelector;
import org.gradle.api.artifacts.component.ModuleComponentSelector;
import org.gradle.api.artifacts.repositories.IvyArtifactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HaxelibCache {
    private static final Logger logger = LoggerFactory.getLogger(HaxelibCache.class);

    public static void cacheHaxelibs(Project project)
    {
        logger.debug("cacheHaxelibs()");
        project.getRepositories().ivy(new Action<IvyArtifactRepository>() {
            @Override
            public void execute(IvyArtifactRepository ivyArtifactRepository) {
                //ivyArtifactRepository.setUrl("${System.properties['user.home']}/.duell/ivy");
            }
        });

        project.getConfigurations().all(new Action<Configuration>() {
            @Override
            public void execute(Configuration conf) {
                conf.getResolutionStrategy().dependencySubstitution(new Action<DependencySubstitutions>() {
                    @Override
                    public void execute(DependencySubstitutions dependencySubstitutions) {
                        dependencySubstitutions.all(new Action<DependencySubstitution>() {
                            @Override
                            public void execute(DependencySubstitution dependencySubstitution) {
                                ComponentSelector requested = dependencySubstitution.getRequested();
                                if (requested instanceof ModuleComponentSelector) {
                                    ModuleComponentSelector mod = (ModuleComponentSelector) requested;
                                    logger.debug("Checking dep: {} {} {}", mod.getGroup(), mod.getModule(), mod.getVersion());
                                }
                            }
                        });
                    }
                });
            }
        });
    }


    private static boolean isCached(Project project, ModuleComponentSelector haxelibDep)
    {
        return false;
    }

    private static Path getIvyPath(ModuleComponentSelector dep)
    {
        String home = System.getProperty("user.home");
        return Paths.get(home, "duell", "ivy", "org.haxe.haxelib", dep.getModule(), dep.getVersion());
    }
}
