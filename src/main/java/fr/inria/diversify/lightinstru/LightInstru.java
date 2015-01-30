package fr.inria.diversify.lightinstru;

import fr.inria.diversify.buildSystem.maven.MavenBuilder;
import fr.inria.diversify.buildSystem.maven.MavenDependencyResolver;
import fr.inria.diversify.lightinstru.detectors.Detector;
import org.apache.commons.io.FileUtils;
import spoon.compiler.Environment;
import spoon.processing.AbstractProcessor;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.FragmentDrivenJavaPrettyPrinter;
import spoon.support.QueueProcessingManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * The Process class is the central manager in charge
 * <p>
 * Created by marodrig on 08/12/2014.
 */
public class LightInstru {

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

    //Flag indicating if only the logger files are going to be copied.
    //This is used when only the logging logic is changed, not de detection/instrumentation logic.
    private boolean onlyCopyLogger;

    /**
     * Creates the LightInstru object
     *
     * @param projectDir    Directory of the project being instrumented
     * @param productionDir Production dir of the project. Is a RELATIVE path to the project.
     * @param outputDir     Output directory where the instrumented code is going to be stored
     */
    public LightInstru(String projectDir, String productionDir, String outputDir) {
        this.projectDir = projectDir;
        this.productionDir = productionDir;
        this.outputDir = outputDir;
        idMap = new IdMap();
    }

    /**
     * Instrument the code and stores a local copy of it
     */
    public void instrument(Configuration configuration) throws Exception {

        //Copy the logger files given in the configuration
        for (Configuration.LoggerPath p : configuration.getLoggerClassFiles()) {
            p.copyTo(getOutputDir());
        }
        if (onlyCopyLogger) return;

        if (resolver == null) {
            //Resolve dependencies in order to build as much AST as possible
            resolver = new MavenDependencyResolver();
            resolver.DependencyResolver(projectDir + "/pom.xml");
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
        src.add(projectDir + productionDir); //Always add all the source code in the production dir.

        //If the configuration source dir don't lies within the production, then add it:
        String s = projectDir + configuration.getSourceDir();
        if (!src.get(0).startsWith(s) && !s.startsWith(src.get(0))) src.add(s);

        //Build the factory
        Factory factory = new SpoonMetaFactory().buildNewFactory(src, 7);

        //Instrument, 1. Detect, 2. Inject code.
        for (Detector d : configuration.getDetectors()) {
            d.setIdMap(idMap);
            //Collect relevant injectors to the events this detector will detect
            d.collectInjectors(configuration.getInjectors());
            //Detect and inject
            applyProcessor(factory, d);
        }

        //Print the instrumented files
        Environment env = factory.getEnvironment();
        env.useSourceCodeFragments(true);
        applyProcessor(factory,
                new JavaOutputProcessorWithFilter(new File(getOutputDir() + configuration.getSourceDir()),
                        new FragmentDrivenJavaPrettyPrinter(env),
                        allClassesName(new File(projectDir + configuration.getSourceDir()))));
    }

    /**
     * Writes the Id map to file
     */
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
     */
    protected void applyProcessor(Factory factory, AbstractProcessor processor) {
        ProcessingManager pm = new QueueProcessingManager(factory);
        pm.addProcessor(processor);
        pm.process();
    }

    /**
     * Indicates that we may initialize the output dir
     */
    public void clean() {
        outPutDirReady = false;
    }

    public void runTests() throws Exception {
        MavenBuilder rb = new MavenBuilder(getOutputDir(), getProductionDir());
        rb.setPhase(new String[]{"clean", "test"});
        rb.setTimeOut(0);
//        rb.initPom(getOutputDir() + "/pom.xml");
        rb.runBuilder();

        if ( rb.getStatus() == -2 ) {
            throw new RuntimeException("The instrumented source failed to compile");
        }

        if ( rb.getStatus() == -1 ) {
            throw new RuntimeException("The instrumented source failed to pass al tests");
        }
    }

    /**
     * Sets the parent directory of the project being instrumented
     *
     * @param projectDir
     */
    public void setProjectDir(String projectDir) {
        this.projectDir = projectDir;
    }

    public String getProjectDir() {
        return projectDir;
    }

    /**
     * Sets the parent directory where the instrumented code is going to be stored
     *
     * @param outputDir
     */
    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getOutputDir() {
        return outputDir;
    }


    public String getProductionDir() {
        return productionDir;
    }

    public void setProductionDir(String productionDir) {
        this.productionDir = productionDir;
    }

    public boolean isOnlyCopyLogger() {
        return onlyCopyLogger;
    }

    public void setOnlyCopyLogger(boolean onlyCopyLogger) {
        this.onlyCopyLogger = onlyCopyLogger;
    }
}
