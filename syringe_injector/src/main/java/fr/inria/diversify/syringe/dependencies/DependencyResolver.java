package fr.inria.diversify.syringe.dependencies;

import fr.inria.diversify.syringe.Configuration;
import org.apache.log4j.Logger;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.internal.impl.DefaultRepositorySystem;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.filter.DependencyFilterUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Dependency resolver using Aether resolver... hope this one works... for good!
 * <p>
 * Created by elmarce on 26/08/16.
 */
public class DependencyResolver {

    final static Logger logger = Logger.getLogger(DependencyResolver.class);


    private MavenProject loadProject(File pomFile) throws IOException, XmlPullParserException {
        MavenXpp3Reader mavenReader = new MavenXpp3Reader();
        FileReader reader = new FileReader(pomFile);
        Model model = mavenReader.read(reader);
        model.setPomFile(pomFile);
        MavenProject ret = new MavenProject(model);
        reader.close();
        return ret;
    }

    private List<URL> doResolve(Configuration configuration, String pomPath) throws DependencyResolutionException, IOException, XmlPullParserException {
        RepositorySystem system = Booter.newRepositorySystem();

        RepositorySystemSession session = Booter.newRepositorySystemSession(system);

        MavenProject project = loadProject(new File(pomPath));

        StringBuilder sb = new StringBuilder(project.getGroupId()).append(":").
                append(project.getArtifactId()).append(":").append(project.getVersion());
        Artifact artifact = new DefaultArtifact(sb.toString());


        DependencyFilter classpathFilter = DependencyFilterUtils.classpathFilter(configuration.getScopes());
        //DependencyFilter classpathFilter = DependencyFilterUtils.classpathFilter(JavaScopes.TEST);

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
        //for ( String s : configuration.getScopes())
        //    collectRequest.addDependency(new Dependency(artifact, s));
        collectRequest.setRepositories(Booter.newRepositories(system, session));

        DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, classpathFilter);

        List<ArtifactResult> artifactResults =
                system.resolveDependencies(session, dependencyRequest).getArtifactResults();
        //Add them to the classpath
        List<URL> jarURL = new ArrayList<>();
        for (ArtifactResult r : artifactResults) {
            URL url = r.getArtifact().getFile().toURI().toURL();
            jarURL.add(url);

        }

        return jarURL;

        //for ( URL url : jarURL) System.out.println("Found: " + url.toString());


    }


    public void resolve(Configuration configuration, String pomPath, List<String> manualClassPath) {

        try {
            logger.info("Resolving dependencies of: " + pomPath);
            List<URL> jarURL = new ArrayList<>();
            jarURL.addAll(doResolve(configuration, pomPath));
            if ( manualClassPath != null )
                for (String s : manualClassPath) {
                    jarURL.add(Paths.get(s).toUri().toURL());
                    //jarURL.addAll(doResolve(configuration, s));
                }
            URLClassLoader child = new URLClassLoader(jarURL.toArray(new URL[jarURL.size()]),
                    Thread.currentThread().getContextClassLoader());
            Thread.currentThread().setContextClassLoader(child);

            logger.info("Dependencies resolved " + pomPath);
        } catch (Exception ex) {
            logger.error("Unable to resolve dependencies");
            throw new RuntimeException(ex);
        }
    }

}
