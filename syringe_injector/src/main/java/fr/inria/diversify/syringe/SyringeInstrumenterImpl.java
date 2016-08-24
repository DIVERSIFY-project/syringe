package fr.inria.diversify.syringe;

import fr.inria.diversify.buildSystem.maven.MavenBuilder;
import fr.inria.diversify.buildSystem.maven.MavenDependencyResolver;
import fr.inria.diversify.syringe.detectors.Detector;
import fr.inria.diversify.syringe.events.DetectionListener;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import spoon.Launcher;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * The Process class is the central manager in charge
 * <p/>
 * Created by marodrig on 08/12/2014.
 */
public class SyringeInstrumenterImpl implements SyringeInstrumenter {

    final static Logger logger = Logger.getLogger(SyringeInstrumenterImpl.class);

    //Indicates that we must initialize the output folder
    private boolean outPutDirReady = false;

    //Parent folder of the project
    private String projectDir;

    //Output where the instrumented code is going to be stored
    private String outputDir;

    //Directory where the production code is
    private String productionDir;

    MavenDependencyResolver resolver = null;

    //Map with all the IDs of the detected elements
    private IdMap idMap;

    //Properties to be written to the property file of the logger
    private Properties loggerProperties;
    private String loggerPropertiesFile;

    //Time out of the maven build
    private int buildTimeOut;

    private boolean useClasspath;

    /**
     * Java version
     */
    private int complianceLevel;

    private boolean fileByFile;


    public SyringeInstrumenterImpl() {
        complianceLevel = 8;
        useClasspath = true;
    }

    /**
     * Creates the LightInstru object
     *
     * @param projectDir    Directory of the project being instrumented
     * @param productionDir Production dir of the project. Is a RELATIVE path to the project.
     * @param outputDir     Output directory where the instrumented code is going to be stored
     */
    public SyringeInstrumenterImpl(String projectDir, String productionDir, String outputDir) {
        this.projectDir = projectDir;
        this.productionDir = productionDir;
        this.outputDir = outputDir;
        idMap = new IdMap();
        complianceLevel = 8;
        useClasspath = true;
    }


    /**
     * Updates the logger files in the output
     *
     * @param configuration Configuration holding all logger files
     * @throws IOException
     */
    public void updateLogger(Configuration configuration) throws IOException {
        //Copy the logger files given in the configuration to the output directory
        for (Configuration.LoggerPath p : configuration.getLoggerClassFiles()) {
            p.copyTo(getOutputDir());
        }
    }

    /**
     * Instrument the code and stores a local copy of it
     */
    @Override
    public void instrument(Configuration configuration) throws Exception {

        logger.info("Instrumenting " + configuration.getDescription());
        logger.info("Project Dir " + projectDir);
        logger.info("Output Dir " + outputDir);

        //Updates the logger files in the output
        updateLogger(configuration);

        //Auto imports in case dependencies could not be properly found
        //boolean autoImports = false;
        if (resolver == null && (useClasspath || fileByFile)) {
            try {
                //Resolve dependencies in order to build as much AST as possible
                resolver = new MavenDependencyResolver();
                resolver.resolveDependencies(projectDir + "/pom.xml");
            } catch (FileNotFoundException e) {
                logger.warn("Could not found POM file. Using auto imports");
                throw e;
            }
        }

        //This is done only once.
        if (outPutDirReady == false) {
            File dir = new File(getOutputDir());
            dir.mkdirs();
            FileUtils.copyDirectory(new File(getProjectDir()), dir);
        }
        //Don't initialize twice
        outPutDirReady = true;


        //Build the factory for the part of the project being instrumented
        ArrayList<String> src = new ArrayList<>();
        src.add(projectDir + configuration.getSourceDir()); //Always add all the source code in the production dir.

        //If the configuration source dir don't lies within the production, then add it:
        //String s = projectDir + configuration.getSourceDir();
        //if (!src.get(0).startsWith(s) && !s.startsWith(src.get(0))) src.add(s);

        //Link the detectors with the injectors listening to their particular events
        for (Detector d : configuration.getDetectors()) {
            d.setIdMap(idMap);
            //Collect relevant injectors to the events this detector will detect
            for (Map.Entry<String, Collection<DetectionListener>> e : configuration.getInjectors().entrySet())
                for (DetectionListener eventListener : e.getValue())
                    d.addListener(e.getKey(), eventListener);
        }

        //Detect and listen
        if (isFileByFile()) walk(configuration, src);
        else launchInjection(configuration, src);

    }

    /**
     * Inject a given configuration and a set of sources, launchInjection them
     */
    private void launchInjection(Configuration configuration, List<String> src) {

        if (logger.isInfoEnabled()) for (String s : src) logger.info("Processing: " + s);

        final Launcher launcher = LauncherBuilder.build(src, outputDir + configuration.getSourceDir());
        launcher.getEnvironment().setComplianceLevel(getComplianceLevel());
        launcher.getEnvironment().setNoClasspath(!useClasspath);

        int fragmentsInserted = 0;
        for (Detector d : configuration.getDetectors()) launcher.addProcessor(d);

        //Try to compile using the classpath first
        try {
            if (isFileByFile()) launcher.getEnvironment().setNoClasspath(false);
            launcher.buildModel();
            launcher.process();
        } catch (AbortCompilation ex) {
            logger.info("Compilation aborted, trying without classpath");
            if (isFileByFile()) {
                for (Detector d : configuration.getDetectors()) d.reset();
                launcher.getEnvironment().setNoClasspath(true);
                launcher.process();
            }
        }

        for (Detector d : configuration.getDetectors())
            fragmentsInserted += d.getElementsDetectedCount();

        //If no new fragments where inserted no need for printing
        if (fragmentsInserted > 0) {
            launcher.prettyprint();
            logger.info("Elements detected: " + fragmentsInserted);
        } else {
            logger.info("No fragments. Sources unmodified");
        }

        for (Detector d : configuration.getDetectors()) d.reset();
    }

    /**
     * Walks file by file in in a given sources, injecting them
     */
    private void walk(final Configuration configuration, ArrayList<String> src) throws IOException {
        for (String s : src)
            Files.walkFileTree(Paths.get(s), new FileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        launchInjection(configuration, Arrays.asList(file.toAbsolutePath().toString()));
                    } catch (Exception ex) {
                        logger.warn("Error: " + ex.getMessage() + " at " + file.toString());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.TERMINATE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
    }

    /**
     * Writes the Id map to file
     */
    @Override
    public void writeIdFile(String idName) throws IOException {
        File file = new File(getOutputDir() + "/log");
        file.mkdirs();
        FileWriter fw = new FileWriter(getOutputDir() + "/log/" + idName);
        for (String s : idMap.keySet()) fw.write(idMap.get(s) + " " + s + "\n");
        fw.close();
    }

    /**
     * Return all names of files containing a java class
     *
     * @param dir Dir to find for java files
     * @return A list of java files
     */
    protected List<String> allClassesName(File dir) {
        List<String> list = new ArrayList<>();

        for (File file : dir.listFiles())
            if (file.isDirectory())
                list.addAll(allClassesName(file));
            else {
                String name = file.getName();
                if (name.endsWith(".java")) {
                    String[] tmp = name.substring(0, name.length() - 5).split("/");
                    list.add(tmp[tmp.length - 1]);
                }
            }
        return list;
    }

    /**
     * Applies an spoon processor
     *
     * @param factory   Factory to apply the processor
     * @param processor Abstract processor

    protected void applyProcessor(Factory factory, Processor processor) {
    ProcessingManager pm = new QueueProcessingManager(factory);
    pm.addProcessor(processor);
    pm.process(processor);
    }*/

    /**
     * Indicates that we may initialize the output dir
     */
    @Override
    public void clean() {
        outPutDirReady = false;
    }

    public void runTests(boolean verbose, String[] phases) throws Exception {
        MavenBuilder rb = new MavenBuilder(getOutputDir(), getProductionDir());
        rb.setTimeOut(buildTimeOut);
        rb.setVerbose(verbose);
        rb.setPhase(phases);
        rb.setTimeOut(0);
//        rb.initPom(getOutputDir() + "/pom.xml");
        rb.runBuilder();

        if (rb.getStatus() == -2) {
            throw new RuntimeException("The instrumented source failed to compile");
        }

        if (rb.getStatus() == -1) {
            throw new RuntimeException("The instrumented source failed to pass all tests");
        }
    }

    @Override
    public void runTests(boolean verbose) throws Exception {
        runTests(verbose, new String[]{"clean", "test"});
    }

    /**
     * Sets the parent directory of the project being instrumented
     *
     * @param projectDir
     */
    @Override
    public void setProjectDir(String projectDir) {
        this.projectDir = projectDir;
    }

    @Override
    public String getProjectDir() {
        return projectDir;
    }

    /**
     * Sets the parent directory where the instrumented code is going to be stored
     *
     * @param outputDir
     */
    @Override
    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    @Override
    public String getOutputDir() {
        return outputDir;
    }


    @Override
    public String getProductionDir() {
        return productionDir;
    }

    @Override
    public void setProductionDir(String productionDir) {
        this.productionDir = productionDir;
    }

    @Override
    @Deprecated
    public boolean isOnlyCopyLogger() {
        return false;
    }

    @Override
    @Deprecated
    public void setOnlyCopyLogger(boolean onlyCopyLogger) {
        //this.onlyCopyLogger = onlyCopyLogger;
    }

    @Override
    public void setBuildTimeOut(int seconds) {
        this.buildTimeOut = seconds;
    }

    @Override
    public void writeLoggerProperties(String fileName, Properties properties) throws IOException {
        loggerPropertiesFile = fileName;
        loggerProperties = properties;
        //finally copy the property file of the logger if any
        if (loggerPropertiesFile != null && !loggerPropertiesFile.isEmpty()) {
            File logFile = new File(getOutputDir() + "/log/");
            if (!logFile.exists()) logFile.mkdir();
            loggerProperties.store(new BufferedWriter(
                    new FileWriter(logFile.getAbsolutePath() + "/" + loggerPropertiesFile)), "");
        }
    }

    @Override
    public void setUseClassPath(boolean use) {
        useClasspath = use;
    }

    public int getComplianceLevel() {
        return complianceLevel;
    }

    public void setComplianceLevel(int complianceLevel) {
        this.complianceLevel = complianceLevel;
    }

    /**
     * Compiles, detect and launchInjection file by file in a project. Much more slower, use only when injecting the whole
     * project don't works
     */
    public boolean isFileByFile() {
        return fileByFile;
    }

    public void setFileByFile(boolean fileByFile) {
        this.fileByFile = fileByFile;
    }
}
