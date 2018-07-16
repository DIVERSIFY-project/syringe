package fr.inria.diversify.syringe;

import fr.inria.diversify.syringe.detectors.Detector;
import fr.inria.diversify.syringe.events.DetectionListener;
import org.apache.commons.io.FileUtils;
import org.eclipse.aether.util.artifact.JavaScopes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * A class containing all paramethers to configure the process
 * <p>
 * Created by marodrig on 08/12/2014.
 */
public class Configuration {

    private HashSet<String> scopes;

    private String description = "";

    public HashSet<String> getScopes() {
        return scopes;
    }

    /**
     * Hack: In case the logger classes are not in System.getProperty(user.dir)
     */
    public String getLoggerRoot() {
        return loggerRoot;
    }

    public void setLoggerRoot(String loggerRoot) {
        this.loggerRoot = loggerRoot;
    }

    /**
     * Because things should have descriptions
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public class LoggerPath {
        private String basePath;
        private String relativePath;
        private String classFileName;

        public LoggerPath(String base, String relative, String className) {
            basePath = base;
            relativePath = relative;
            classFileName = className;
        }

        /**
         * Copy the logger to the output destination
         *
         * @param outputDir
         * @throws IOException
         */
        public void copyTo(String outputDir) throws IOException {
            File f = new File(outputDir + "/" + relativePath);
            if (!f.exists()) f.mkdirs();
            String s = basePath + relativePath + classFileName + ".java";
            FileUtils.copyFile(new File(s), new File(outputDir + relativePath + classFileName + ".java"));
        }
    }

    /**
     * All logger class files that are going to be copied to the instrumented programm
     */
    private Collection<LoggerPath> loggerClassFiles;

    //All detectors in the instrumentation process
    private Collection<Detector> detectors;

    //All injectors in the instrumentation process, binded to an specific location event
    private AbstractMap<String, Collection<DetectionListener>> injectors;

    // Folder where the instrumented code is going to be stored.
    private String sourceDir;

    private String loggerRoot = null;

    /**
     * Creates a configuration instance for the
     *
     * @param source Source to instrument. Is a path RELATIVE to the project path.
     */
    public Configuration(String source) {
        description = source;
        sourceDir = source;
        scopes = new HashSet<>();
        scopes.add(JavaScopes.COMPILE);
    }

    /**
     * Folder where the instrumented code is going to be stored.
     * <p>
     * Note: Is a path relative to the Project path.
     */
    public void setSourceDir(String instrumentationFolder) {
        this.sourceDir = instrumentationFolder;
    }


    public String getSourceDir() {
        return sourceDir;
    }


    /**
     * Get all detectors
     *
     * @return
     */
    public Collection<Detector> getDetectors() {
        if (detectors == null) detectors = new ArrayList<>();
        return detectors;
    }

    /**
     * Sets all detectors
     *
     * @param detectors Detector to detect events
     */
    public void setDetectors(Collection<Detector> detectors) {
        this.detectors = detectors;
    }

    /**
     * Adds a new injector to listen when an location event is found
     *
     * @param eventName     Location event to listen (e.g. method begin, method end, test begin..
     * @param eventListener Injector to listen
     */
    public void addInjector(String eventName, DetectionListener eventListener) {
        if (getInjectors().containsKey(eventName)) {
            getInjectors().get(eventName).add(eventListener);
        } else {
            Collection<DetectionListener> injs = new ArrayList<>();
            injs.add(eventListener);
            getInjectors().put(eventName, injs);
        }
    }

    /**
     * Adds a detector to detect location events
     *
     * @param detector Detector to detect events
     */
    public void addDetector(Detector detector) {
        getDetectors().add(detector);
    }

    public void addDetectors(Detector... detectors) {
        for (Detector d : detectors) getDetectors().add(d);
    }

    /**
     * Gets the injectors
     *
     * @return
     */
    public AbstractMap<String, Collection<DetectionListener>> getInjectors() {
        if (injectors == null) injectors = new HashMap<>();
        return injectors;
    }

    /**
     * Another way of adding classes to the instrumented program. Adding the file itself
     *
     * @param base     Base path to find the file
     * @param relative Relative path to find the file
     * @throws FileNotFoundException
     */
    public void addLoggerClassFile(String base, String relative, String className) throws FileNotFoundException {
        File f = new File(base + "/" + relative + "/" + className + ".java");
        if (f.exists()) getLoggerClassFiles().add(new LoggerPath(base, relative, className));
        else throw new FileNotFoundException("Cannot find: " + f.getAbsolutePath());
    }

    /**
     * Class files collected
     */
    public Collection<LoggerPath> getLoggerClassFiles() {
        if (loggerClassFiles == null) loggerClassFiles = new ArrayList<>();
        return loggerClassFiles;
    }

    public void setLoggerClassFiles(Collection<LoggerPath> loggerClassFiles) {
        this.loggerClassFiles = loggerClassFiles;
    }


    public void addLoggers(Class<?>... testLoggerClasses) throws FileNotFoundException {
        for (Class<?> c : testLoggerClasses) addLogger(c);
    }
    /**
     * Adds a logger file to the instrumentation path. The method assume the class is located in src/main/java or in
     * test/java of the current project
     *
     * @param testLoggerClass
     */
    public void addLogger(Class<?> testLoggerClass) throws FileNotFoundException {
        String s = testLoggerClass.getCanonicalName().replace(".", "/");
        String b = loggerRoot == null ? System.getProperty("user.dir") : loggerRoot;

        int index = s.lastIndexOf("/");
        String r = "/src/main/java/" + (index == -1 ? "" : s.substring(0, index));
        String c = index == -1 ? s : s.substring(index);
        File f = new File(b + r + c + ".java");

        if (f.exists()) getLoggerClassFiles().add(new LoggerPath(b, r, c));
        else {
            r = "/src/test/java/" + (index == -1 ? "" : s.substring(0, index));
            f = new File(b + r + s + ".java");
            if (f.exists()) getLoggerClassFiles().add(new LoggerPath(b, r, c));
            else throw new FileNotFoundException("Cannot find " + s);
        }
    }
}
