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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class HaxelibCache {
    private static final Logger logger = LoggerFactory.getLogger(HaxelibCache.class);

    public static void cacheHaxelibs(final Project project)
    {
        logger.debug("cacheHaxelibs2()");
        project.getRepositories().ivy(new Action<IvyArtifactRepository>() {
            @Override
            public void execute(IvyArtifactRepository ivyArtifactRepository) {
                ivyArtifactRepository.setUrl("${System.properties['user.home']}/.duell/haxelib-ivy");
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
                                if (requested instanceof ModuleComponentSelector)
                                {
                                    ModuleComponentSelector mod = (ModuleComponentSelector) requested;
                                    logger.debug("Checking dep: {} {} {}", mod.getGroup(), mod.getModule(), mod.getVersion());
                                    if (mod.getGroup().equals("org.haxe.lib"))
                                    {
                                        Path path = getIvyPath(mod);
                                        logger.debug("Ivy path: {}", path);
                                        if (!isCached(project, mod))
                                        {
                                            cacheDep(project, mod);
                                        }
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    static private void cacheDep(Project project, ModuleComponentSelector mod)
    {
        logger.info("Caching haxelib dependency {} {}", mod.getModule(), mod.getVersion());
        //https://lib.haxe.org/p/actuate/1.8.7/download/

        try
        {
            Path ivyPath = getIvyPath(mod);
            Files.createDirectories(ivyPath);
            Path libPath = getLibPath(ivyPath, mod);
            String urlStr = "https://lib.haxe.org/p/" + mod.getModule() + "/" + mod.getVersion() + "/download";
            logger.info("Requesting haxelib from {}", urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            logger.debug("Got {} {} {}", connection.getURL(), connection.getContentType(), connection.getResponseCode());
            Files.copy(connection.getInputStream(), libPath, StandardCopyOption.REPLACE_EXISTING);
            writeManifest(ivyPath, mod);
        }
        catch (MalformedURLException e)
        {
            logger.error("Wrong haxelib url", e);
        }
        catch (IOException e)
        {
            logger.error("Error retriving haxelib", e);
        }
    }

    private static boolean isCached(Project project, ModuleComponentSelector haxelibDep)
    {
        Path ivyPath = getIvyPath(haxelibDep);
        Path manifestPath = getManifestPath(ivyPath, haxelibDep);
        return Files.exists(manifestPath);
    }

    private static Path getManifestPath(Path ivyPath, ModuleComponentSelector mod)
    {
        return ivyPath.resolve("ivy-" + mod.getVersion() + ".xml");
    }

    private static void writeManifest(Path ivyPath, ModuleComponentSelector mod)
    {
        Path manifestPath = getManifestPath(ivyPath, mod);
    }

    private static Path getLibPath(Path ivyPath, ModuleComponentSelector mod)
    {
        return ivyPath.resolve(mod.getModule() + "-" + mod.getVersion() + "-src.zip");
    }

    private static Path getIvyPath(ModuleComponentSelector dep)
    {
        String home = System.getProperty("user.home");
        return Paths.get(home, ".duell", "haxelib-ivy", "org.haxe.lib", dep.getModule(), dep.getVersion());
    }
}
