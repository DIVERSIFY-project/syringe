package fr.inria.diversify.syringe;

import fr.inria.diversify.syringe.detectors.Detector;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

/**
 * Created by marodrig on 15/12/2015.
 */
public class ProcessingTest {
    public void process(Detector e, String folder) throws Exception {
        Factory factory = new SpoonMetaFactory().buildNewFactory(folder, 5);
        ProcessingManager pm = new QueueProcessingManager(factory);
        pm.addProcessor(e);
        pm.process();
    }
}
