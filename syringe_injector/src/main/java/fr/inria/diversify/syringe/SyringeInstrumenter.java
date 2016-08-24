package fr.inria.diversify.syringe;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by marodrig on 19/02/2015.
 */
public interface SyringeInstrumenter {
    void instrument(Configuration configuration) throws Exception;

    void writeIdFile(String idName) throws IOException;

    void clean();

    void runTests(boolean verbose, String[] phases) throws Exception;

    void runTests(boolean verbose) throws Exception;

    void setProjectDir(String projectDir);

    String getProjectDir();

    void setOutputDir(String outputDir);

    String getOutputDir();

    String getProductionDir();

    void setProductionDir(String productionDir);

    boolean isOnlyCopyLogger();

    void setOnlyCopyLogger(boolean onlyCopyLogger);

    void setBuildTimeOut(int seconds);

    void writeLoggerProperties(String fileName, Properties properties) throws IOException;

    void setUseClassPath(boolean use);

    public int getComplianceLevel() ;

    public void setComplianceLevel(int complianceLevel) ;
}
