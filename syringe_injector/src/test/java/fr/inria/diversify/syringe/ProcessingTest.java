package fr.inria.diversify.syringe;

import fr.inria.diversify.syringe.detectors.Detector;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

/**
 * Created by marodrig on 15/12/2015.
 */
public class ProcessingTest {
    public void process(Detector e, String folder) throws Exception {
        final Launcher launcher = new Launcher();
        launcher.getEnvironment().setNoClasspath(true);
        launcher.addInputResource(folder);
        launcher.setSourceOutputDirectory("./target/trash");
        launcher.addProcessor(e);
        launcher.buildModel();
        launcher.process();
    }
}
