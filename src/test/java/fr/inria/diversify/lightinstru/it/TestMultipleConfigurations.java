package fr.inria.diversify.lightinstru.it;

import fr.inria.diversify.lightinstru.Configuration;
import fr.inria.diversify.lightinstru.LightInstru;
import fr.inria.diversify.lightinstru.TestLogger;
import fr.inria.diversify.lightinstru.detectors.MethodDetect;
import fr.inria.diversify.lightinstru.detectors.TestDetect;
import fr.inria.diversify.lightinstru.injectors.GenericInjector;
import org.junit.Test;

/**
 * Created by marodrig on 08/12/2014.
 */
public class TestMultipleConfigurations extends InstrumentationTest {

    private static final String OUT_FOLDER = "out";

    public static class InstrumentCounter {

    }

    @Test
    public void multiConfigurationTests() throws Exception {

        TestLogger.totalCount = 0;

        //Configuration of the source
        Configuration confSrc = new Configuration(SRC_DIR + "/java");
        confSrc.addDetector(new MethodDetect());
        confSrc.addLogger(TestLogger.class);
        String injection = TestLogger.class.getCanonicalName() + ".method();";
        confSrc.addInjector(MethodDetect.getEventName(), new GenericInjector(injection));

        //Configuration of the Test
        Configuration confTest = new Configuration(TEST_DIR + "/java");
        confTest.addDetector(new TestDetect());
        confTest.addLogger(TestLogger.class);
        injection = TestLogger.class.getCanonicalName() + ".test();";
        confTest.addInjector(TestDetect.BEGIN_KEY, new GenericInjector(injection));

        //Instrument
        LightInstru l = new LightInstru(getResPath(PROJECT_DIR), SRC_DIR, getResPath("") + OUT_FOLDER);
        l.instrument(confSrc);
        l.instrument(confTest);
        //Run tests in order to make the magic happens!
        l.runTests();
    }

}
