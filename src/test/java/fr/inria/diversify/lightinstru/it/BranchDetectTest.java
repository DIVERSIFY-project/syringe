package fr.inria.diversify.lightinstru.it;

import fr.inria.diversify.lightinstru.Configuration;
import fr.inria.diversify.lightinstru.LightInstru;
import fr.inria.diversify.lightinstru.TestLogger;
import fr.inria.diversify.lightinstru.detectors.CaseDetect;
import fr.inria.diversify.lightinstru.detectors.IfDetect;
import fr.inria.diversify.lightinstru.detectors.LoopDetect;
import fr.inria.diversify.lightinstru.detectors.MethodDetect;
import fr.inria.diversify.lightinstru.injectors.GenericInjector;
import org.junit.Test;

/**
 * Created by marodrig on 08/12/2014.
 */
public class BranchDetectTest extends InstrumentationTest {

    private static final String OUT_FOLDER = "out";

    public static class InstrumentCounter {

    }

    @Test
    public void branchTest() throws Exception {

        TestLogger.totalCount = 0;

        //Configure an instrumentation. We can instrument just a segment of the project using a
        //different configuration instance each time. We may create another configuration in order to
        //instrument with different parameters other location within the project
        Configuration conf = new Configuration(SRC_DIR + "/java");//Give the RELATIVE path of the dir to instrument
        //What we are going to detect
        conf.addDetector(new MethodDetect());
        conf.addDetector(new LoopDetect());
        conf.addDetector(new IfDetect());
        conf.addDetector(new CaseDetect());
        conf.addLogger(TestLogger.class); //Copies a logger class assuming the class can be found in the sources of this project
        //What we are going to inject
        String injection = TestLogger.class.getCanonicalName() + ".inc();";
        conf.addInjector(MethodDetect.BEGIN_KEY, new GenericInjector(injection));
        conf.addInjector(LoopDetect.BEGIN_KEY, new GenericInjector(injection));
        conf.addInjector(IfDetect.BEGIN_KEY, new GenericInjector(injection));
        conf.addInjector(IfDetect.ELSE_BEGIN_KEY, new GenericInjector(injection));

        //Instrument
        LightInstru l = new LightInstru(getResPath(PROJECT_DIR), SRC_DIR, getResPath("") + OUT_FOLDER);
        l.instrument(conf);
        //Run tests in order to make the magic happens!
        l.runTests();
    }

}
